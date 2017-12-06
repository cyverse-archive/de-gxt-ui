package org.iplantc.de.diskResource.client.views.search.cells;

import org.iplantc.de.client.models.querydsl.QueryDSLTemplate;
import org.iplantc.de.client.models.sharing.PermissionValue;
import org.iplantc.de.client.util.SearchModelUtils;
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
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;

import java.util.Arrays;

/**
 * A form the user can fill out to perform advanced searches in the Data window which utilize the search service
 */
public class QueryDSLForm extends Composite implements Editor<QueryDSLTemplate>,
                                                       QueryDSLSearchBtnSelected.HasQueryDSLSearchBtnSelectedHandlers {

    interface QueryDSLFormEditor extends SimpleBeanEditorDriver<QueryDSLTemplate, QueryDSLForm> {}
    interface QueryDSLFormUiBinder extends UiBinder<Widget, QueryDSLForm> {
    }

    public interface QueryDSLFormAppearance {
        String nameHas();

        String pathPrefix();

        String exactNameMatch();

        String owner();

        String exactUserNameMatch();

        String permissionValueLabel();

        String permissionRecurse();

        String permissionUsers();

        String emptyText();

        String emptyDropDownText();

        String searchBtnText();
    }
    static QueryDSLFormUiBinder uiBinder = GWT.create(QueryDSLFormUiBinder.class);
    QueryDSLFormEditor editorDriver = GWT.create(QueryDSLFormEditor.class);

    @Ignore @UiField VerticalLayoutContainer con;
    @Path("label")
    @UiField TextField nameHas;
    @Path("labelExact")
    @UiField CheckBox exactNameMatch;
    @UiField TextField pathPrefix;
    @UiField TextField owner;
    @UiField SimpleComboBox<PermissionValue> permissionValue;
    @UiField CheckBox permissionRecurse;
    @Ignore private boolean showing;
//    @UiField TextField permissionUsers;
//    @UiField Radio exactUserNameMatch;
    @Ignore @UiField TextButton searchBtn;

    @UiField(provided = true) QueryDSLFormAppearance appearance;
    private SearchModelUtils searchModelUtils;
    protected final BaseEventPreview eventPreview;

    @Inject
    public QueryDSLForm(QueryDSLFormAppearance appearance,
                        SearchModelUtils searchModelUtils) {
        this.appearance = appearance;
        this.searchModelUtils = searchModelUtils;

        initWidget(uiBinder.createAndBindUi(this));
        con.getElement().getStyle().setBackgroundColor("#fff");

        editorDriver.initialize(this);

        eventPreview = new BaseEventPreview() {

            @Override
            protected boolean onPreview(Event.NativePreviewEvent pe) {
                QueryDSLForm.this.onPreviewEvent(pe);
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
    }

    @UiFactory
    SimpleComboBox<PermissionValue> permissionValue() {
        SimpleComboBox<PermissionValue> comboBox = new SimpleComboBox<>(new StringLabelProvider<PermissionValue>());
        comboBox.add(Arrays.asList(PermissionValue.own,
                                   PermissionValue.write,
                                   PermissionValue.read));
        return comboBox;
    }

    public void edit(QueryDSLTemplate template) {
        editorDriver.edit(template);
    }

    @UiHandler("searchBtn")
    void onSearchBtnClicked(SelectEvent event) {
        if (editorDriver.isDirty()) {
            QueryDSLTemplate template = editorDriver.flush();
            AutoBean<QueryDSLTemplate> autoBean = AutoBeanUtils.getAutoBean(template);
            String encode = AutoBeanCodex.encode(autoBean).getPayload();
            GWT.log("Bean ended up being: " + encode);
            GWT.log("Combo box was set to : " + permissionValue.getValue());
            fireEvent(new QueryDSLSearchBtnSelected(template));
        }
    }

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

    public void clearSearch() {
        editorDriver.edit(searchModelUtils.createDefaultQuery());
    }
}
