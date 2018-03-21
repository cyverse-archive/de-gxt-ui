package org.iplantc.de.diskResource.client.views.search;

import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.querydsl.QueryDSLTemplate;
import org.iplantc.de.client.models.sharing.PermissionValue;
import org.iplantc.de.client.util.SearchModelUtils;
import org.iplantc.de.collaborators.client.models.SubjectKeyProvider;
import org.iplantc.de.collaborators.client.util.UserSearchField;
import org.iplantc.de.collaborators.client.views.CollaboratorsColumnModel;
import org.iplantc.de.commons.client.util.CyVerseReactComponents;
import org.iplantc.de.diskResource.client.SearchView;
import org.iplantc.de.diskResource.client.events.selection.QueryDSLSearchBtnSelected;
import org.iplantc.de.diskResource.share.DiskResourceModule;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
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
public class SearchViewImpl extends Composite implements SearchView {

    private VerticalLayoutContainer con;
    private SearchViewAppearance appearance;
    private final BaseEventPreview eventPreview;
    private boolean showing = false;

    @Inject
    public SearchViewImpl(SearchViewAppearance appearance) {
        this.appearance = appearance;
        this.con = new VerticalLayoutContainer();
        initWidget(con);
        con.getElement().getStyle().setBackgroundColor("#fff");

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
    }

//    @UiHandler("searchBtn")
//    void onSearchBtnClicked(SelectEvent event) {
//        if (editorDriver.isDirty()) {
//            QueryDSLTemplate template = editorDriver.flush();
//            convertUsers(template);
//            template.setPermission(permission.getValue());
//            AutoBean<QueryDSLTemplate> autoBean = AutoBeanUtils.getAutoBean(template);
//            String encode = AutoBeanCodex.encode(autoBean).getPayload();
//            GWT.log("Bean ended up being: " + encode);
//            fireEvent(new QueryDSLSearchBtnSelected(template));
//        }
//    }

    @Override
    public void show(Element parent, Style.AnchorAlignment anchorAlignment) {
        getElement().makePositionable(true);

        ReactSearchForm.SearchFormProps props = new ReactSearchForm.SearchFormProps();
        props.appearance = appearance;
        props.id = DiskResourceModule.Ids.SEARCH_FORM;

        CyVerseReactComponents.render(ReactSearchForm.SearchForm, props, DivElement.as(con.getElement()));

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

                //Ignore targets who are parents of or children of elements with
                // an attribute role=menuitem (which are material-ui dropdowns)
                if (target.findParent("[role=menuitem]", 10) != null ||
                        target.child("[role=menuitem]") != null) {
                    return;
                }

                if (!getElement().isOrHasChild(target)) {
                    hide();
                }
        }
    }

    @Override
    public HandlerRegistration addQueryDSLSearchBtnSelectedHandler(QueryDSLSearchBtnSelected.QueryDSLSearchBtnSelectedHandler handler) {
        return addHandler(handler, QueryDSLSearchBtnSelected.TYPE);
    }

    @Override
    public void clearSearch() {
        GWT.log("CLEAR SEARCH...");
    }
}
