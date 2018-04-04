package org.iplantc.de.diskResource.client.views.search;

import org.iplantc.de.commons.client.util.CyVerseReactComponents;
import org.iplantc.de.diskResource.client.SearchView;
import org.iplantc.de.diskResource.client.events.selection.QueryDSLSearchBtnSelected;
import org.iplantc.de.diskResource.client.presenters.search.DateIntervalProvider;
import org.iplantc.de.diskResource.share.DiskResourceModule;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.BaseEventPreview;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent;

/**
 * A form the user can fill out to perform advanced searches in the Data window which utilize the search service
 */
public class SearchViewImpl extends Composite implements SearchView {

    private VerticalLayoutContainer con;
    private SearchViewAppearance appearance;
    private DateIntervalProvider dateIntervalProvider;
    private final BaseEventPreview eventPreview;
    private boolean showing = false;

    @Inject
    public SearchViewImpl(SearchViewAppearance appearance,
                          DateIntervalProvider dateIntervalProvider) {
        this.appearance = appearance;
        this.dateIntervalProvider = dateIntervalProvider;
        this.con = new VerticalLayoutContainer();
        initWidget(con);
        con.getElement().getStyle().setBackgroundColor("#fff");
        con.setWidth("500px");

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

    @Override
    public void onSearchBtnClicked(Splittable query) {
        if (query != null) {
            GWT.log(query.getPayload());
            fireEvent(new QueryDSLSearchBtnSelected(query));
        }
    }

    @Override
    public void show(Element parent, Style.AnchorAlignment anchorAlignment) {
        getElement().makePositionable(true);

        ReactSearchForm.SearchFormProps props = new ReactSearchForm.SearchFormProps();
        props.presenter = SearchViewImpl.this;
        props.appearance = appearance;
        props.id = DiskResourceModule.Ids.SEARCH_FORM;
        props.dateIntervals = dateIntervalProvider.get();

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
