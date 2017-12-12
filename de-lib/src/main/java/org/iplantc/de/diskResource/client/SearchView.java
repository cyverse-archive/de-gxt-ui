package org.iplantc.de.diskResource.client;

import org.iplantc.de.client.models.querydsl.QueryDSLTemplate;
import org.iplantc.de.diskResource.client.events.SavedSearchesRetrievedEvent.SavedSearchesRetrievedEventHandler;
import org.iplantc.de.diskResource.client.events.search.DeleteSavedSearchClickedEvent.DeleteSavedSearchEventHandler;
import org.iplantc.de.diskResource.client.events.search.SaveDiskResourceQueryClickedEvent.SaveDiskResourceQueryClickedEventHandler;
import org.iplantc.de.diskResource.client.events.search.SavedSearchDeletedEvent.HasSavedSearchDeletedEventHandlers;
import org.iplantc.de.diskResource.client.events.search.UpdateSavedSearchesEvent.HasUpdateSavedSearchesEventHandlers;
import org.iplantc.de.diskResource.client.events.selection.QueryDSLSearchBtnSelected;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.IsWidget;

import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.event.HideEvent;

/**
 * Created by jstroot on 2/10/15.
 * @author jstroot, aramsey
 */
public interface SearchView extends IsWidget,
                                    HideEvent.HasHideHandlers,
                                    QueryDSLSearchBtnSelected.HasQueryDSLSearchBtnSelectedHandlers {

    interface SearchViewAppearance {

        String deleteSearchSuccess(String searchName);

        String saveQueryTemplateFail();

        String nameHas();

        String pathPrefix();

        String exactNameMatch();

        String owner();

        String exactUserNameMatch();

        String permissionValueLabel();

        String permissionRecurse();

        String sharedWith();

        String emptyText();

        String emptyDropDownText();

        String searchBtnText();
    }

    /**
     * An interface definition for the "search" sub-system.
     *
     * <h2><u>Terms and Concepts</u></h2>
     * <dl>
     * <dt>Query Template</dt>
     * <dd>a template which is used to generate a query to be submitted to the search endpoints.</dd>
     * <dd>acts as a "smart folder" which is accessed from the data navigation window.</dd>
     * <dt>Active Query</dt>
     * <dd>the current query template whose generated search query results are displayed in the view's center
     * panel.</dd>
     * <dd>
     * </dl>
     *
     * <h2>Presenter Responsibilities</h2>
     * <ul>
     * <li>Managing the <em>active query</em> state. This includes:
     * <ul>
     * <li>Ensuring that the view communicates to the user what the current <em>active query</em> is.</li>
     * <li>Ensuring that the view communicates to the user if there is no <em>active query</em>.</li>
     * </ul>
     * </li>
     *
     * <li>Maintaining a list of saved query templates.</li>
     * <li>Saving query templates when the user requests.<br/>
     * This includes ensuring that the user has full permissions to the query template.</li>
     * <li>Retrieving saved query templates
     * <ul>
     * <li>Displaying the saved filters as selectable root items in the Navigation panel</li>
     * </ul>
     * </li>
     * <li>Deleting saved queries</li>
     *
     * </ul>
     *
     *
     * @author jstroot
     *
     */
    interface Presenter extends SaveDiskResourceQueryClickedEventHandler,
                                DeleteSavedSearchEventHandler,
                                HasSavedSearchDeletedEventHandlers,
                                SavedSearchesRetrievedEventHandler,
                                HasUpdateSavedSearchesEventHandlers {
    }

    void edit(QueryDSLTemplate template);

    void show(Element parent, Style.AnchorAlignment anchorAlignment);

    void clearSearch();

    XElement getElement();

    void hide();

    void fireEvent(GwtEvent<?> event);
}

