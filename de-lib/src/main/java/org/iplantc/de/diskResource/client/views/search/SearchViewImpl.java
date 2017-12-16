package org.iplantc.de.diskResource.client.views.search;

import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.querydsl.QueryDSLTemplate;
import org.iplantc.de.client.models.sharing.PermissionValue;
import org.iplantc.de.client.util.SearchModelUtils;
import org.iplantc.de.collaborators.client.models.SubjectKeyProvider;
import org.iplantc.de.collaborators.client.util.UserSearchField;
import org.iplantc.de.collaborators.client.views.CollaboratorsColumnModel;
import org.iplantc.de.diskResource.client.SearchView;
import org.iplantc.de.diskResource.client.events.selection.QueryDSLSearchBtnSelected;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.BaseEventPreview;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.StringComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A form the user can fill out to perform advanced searches in the Data window which utilize the search service
 */
public class SearchViewImpl extends Composite implements SearchView,
                                                         Editor<QueryDSLTemplate> {

    interface SearchViewImplEditor extends SimpleBeanEditorDriver<QueryDSLTemplate, SearchViewImpl> {}
    interface SearchViewImplUiBinder extends UiBinder<Widget, SearchViewImpl> {
    }

    static SearchViewImplUiBinder uiBinder = GWT.create(SearchViewImplUiBinder.class);
    SearchViewImplEditor editorDriver = GWT.create(SearchViewImplEditor.class);

    @Ignore @UiField VerticalLayoutContainer con;
    @Path("label")
    @UiField TextField nameHas;
    @Path("labelExact")
    @UiField CheckBox exactNameMatch;
    @UiField TextField pathPrefix;
    @UiField TextField owner;
    @UiField StringComboBox permission;
    @UiField CheckBox permissionRecurse;
    @Ignore private boolean showing;
    @Ignore @UiField(provided = true) UserSearchField userSearchField;
    @UiField ListStore<Subject> permissionUsers;
    @Ignore @UiField TextButton searchBtn;

    @UiField(provided = true) SearchViewAppearance appearance;
    private SearchModelUtils searchModelUtils;
    protected final BaseEventPreview eventPreview;

    @Inject
    public SearchViewImpl(SearchViewAppearance appearance,
                          UserSearchField userSearchField,
                          SearchModelUtils searchModelUtils) {
        this.appearance = appearance;
        this.userSearchField = userSearchField;
        this.searchModelUtils = searchModelUtils;
        initWidget(uiBinder.createAndBindUi(this));
        con.getElement().getStyle().setBackgroundColor("#fff");

        editorDriver.initialize(this);

        eventPreview = new BaseEventPreview() {

            @Override
            protected boolean onPreview(Event.NativePreviewEvent pe) {
                SearchViewImpl.this.onPreviewEvent(pe);
                return super.onPreview(pe);
            }

            @Override
            protected void onPreviewKeyPress(Event.NativePreviewEvent pe) {
                super.onPreviewKeyPress(pe);
                onEscape(pe);
            }

        };
        eventPreview.getIgnoreList().add(getElement());
        eventPreview.setAutoHide(false);
        userSearchField.addUserSearchResultSelectedEventHandler(selectEvent -> permissionUsers.add(selectEvent.getSubject()));
    }

    @UiFactory
    StringComboBox permission() {
        StringComboBox comboBox = new StringComboBox();
        comboBox.add(Arrays.asList(PermissionValue.own.toString(),
                                                   PermissionValue.write.toString(),
                                                   PermissionValue.read.toString()));
        return comboBox;
    }

    @UiFactory
    ListStore<Subject> createListStore() {
        return new ListStore<>(new SubjectKeyProvider());
    }

    @UiFactory
    ColumnModel<Subject> buildColumnModel() {
        CollaboratorsColumnModel cm = new CollaboratorsColumnModel(null);
        cm.deleteColumnVisible(true);
        cm.addSubjectDeleteCellClickedHandler(event -> permissionUsers.remove(event.getSubject()));
        return cm;
    }

    @Override
    public void edit(QueryDSLTemplate template) {
        editorDriver.edit(template);
    }

    @UiHandler("searchBtn")
    void onSearchBtnClicked(SelectEvent event) {
        if (editorDriver.isDirty()) {
            QueryDSLTemplate template = editorDriver.flush();
            convertUsers(template);
            template.setPermission(permission.getValue());
            AutoBean<QueryDSLTemplate> autoBean = AutoBeanUtils.getAutoBean(template);
            String encode = AutoBeanCodex.encode(autoBean).getPayload();
            GWT.log("Bean ended up being: " + encode);
            fireEvent(new QueryDSLSearchBtnSelected(template));
        }
    }

    void convertUsers(QueryDSLTemplate template) {
        List<Subject> subjects = permissionUsers.getAll();
        if (subjects != null && !subjects.isEmpty()) {
            List<String> userNames = subjects.stream().map(HasId::getId).collect(Collectors.toList());
            template.setPermissionUsers(userNames);
        }
    }

    @Override
    public void show(Element parent, Style.AnchorAlignment anchorAlignment) {
        getElement().makePositionable(true);
        RootPanel.get().add(this);
        onShow();
        getElement().updateZIndex(0);

        showing = true;

        getElement().alignTo(parent, anchorAlignment, 0, 0);

        getElement().show();
        eventPreview.add();

        focus();
        fireEvent(new ShowEvent());
    }

    @Override
    public void hide() {
        if (showing) {
            onHide();
            RootPanel.get().remove(this);
            eventPreview.remove();
            showing = false;
            hidden = true;
            fireEvent(new HideEvent());
        }
    }

    void onEscape(Event.NativePreviewEvent pe) {
        if (pe.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            pe.getNativeEvent().preventDefault();
            pe.getNativeEvent().stopPropagation();
            hide();
        }
    }

    protected void onPreviewEvent(Event.NativePreviewEvent pe) {
        int type = pe.getTypeInt();
        switch (type) {
            case Event.ONMOUSEDOWN:
            case Event.ONMOUSEWHEEL:
            case Event.ONSCROLL:
            case Event.ONKEYPRESS:
                XElement target = pe.getNativeEvent().getEventTarget().cast();

                // ignore targets within a parent with x-ignore, such as the listview in
                // a combo
                if (target.findParent(".x-ignore", 10) != null) {
                    return;
                }

                if (!getElement().isOrHasChild(target)) {
                    hide();
                }
        }
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
    }

    @Override
    public HandlerRegistration addQueryDSLSearchBtnSelectedHandler(QueryDSLSearchBtnSelected.QueryDSLSearchBtnSelectedHandler handler) {
        return addHandler(handler, QueryDSLSearchBtnSelected.TYPE);
    }

    @Override
    public void clearSearch() {
        editorDriver.edit(searchModelUtils.createDefaultQuery());
    }
}
