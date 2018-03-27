package org.iplantc.de.diskResource.client.views.search;

import org.iplantc.de.client.models.querydsl.QueryDSLTemplate;
import org.iplantc.de.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.de.client.models.search.SearchAutoBeanFactory;
import org.iplantc.de.commons.client.events.SubmitTextSearchEvent;
import org.iplantc.de.commons.client.events.SubmitTextSearchEvent.SubmitTextSearchEventHandler;
import org.iplantc.de.commons.client.widgets.search.SearchFieldDecorator;
import org.iplantc.de.diskResource.client.events.search.FetchTagSuggestions;
import org.iplantc.de.diskResource.client.events.search.SavedSearchDeletedEvent;
import org.iplantc.de.diskResource.client.events.search.SubmitDiskResourceQueryEvent;
import org.iplantc.de.diskResource.client.events.selection.QueryDSLSearchBtnSelected;
import org.iplantc.de.diskResource.client.views.search.cells.DiskResourceSearchCellv2;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.event.CollapseEvent.CollapseHandler;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.HasCollapseHandlers;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.ExpandHandler;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.HasExpandHandlers;
import com.sencha.gxt.widget.core.client.event.ParseErrorEvent;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.PropertyEditor;
import com.sencha.gxt.widget.core.client.form.TriggerField;

import java.text.ParseException;

/**
 * This class is a clone-and-own of {@link DateField}.
 *
 * This is a search widget for the Data window that contains a "trigger" for opening the SearchViewImpl
 * 
 * @author aramsey
 * 
 */
public class DiskResourceSearchFieldv2 extends TriggerField<String> implements HasExpandHandlers,
                                                                               HasCollapseHandlers,
                                                                               QueryDSLSearchBtnSelected.HasQueryDSLSearchBtnSelectedHandlers,
                                                                               SubmitTextSearchEventHandler,
                                                                               QueryDSLSearchBtnSelected.QueryDSLSearchBtnSelectedHandler,
                                                                               SavedSearchDeletedEvent.SavedSearchDeletedEventHandler,
                                                                               FetchTagSuggestions.HasFetchTagSuggestionsHandlers {

    public final class QueryStringPropertyEditor extends PropertyEditor<String> {
        private final SearchAutoBeanFactory factory = GWT.create(SearchAutoBeanFactory.class);
        @Override
        public String parse(CharSequence text) throws ParseException {
            clearInvalid();

            if (text.length() < 3) {
                return text.toString();
            }

            DiskResourceQueryTemplate qt = factory.dataSearchFilter().as();
            qt.setFileQuery(text.toString());
            getCell().getSearchForm().fireEvent(new SubmitDiskResourceQueryEvent(qt));
            return text.toString();
        }

        @Override
        public String render(String object) {
            return object;
        }
    }

    /**
     * Creates a new iPlant Search field.
     */
    @Inject
    public DiskResourceSearchFieldv2(final DiskResourceSearchCellv2 searchCell) {
        super(searchCell);

        setPropertyEditor(new QueryStringPropertyEditor());
        getCell().addQueryDSLSearchBtnSelectedHandler(this);

        // Add search field decorator to enable "auto-search"
        new SearchFieldDecorator<TriggerField<String>>(this).addSubmitTextSearchEventHandler(this);
    }

    @Override
    public HandlerRegistration addCollapseHandler(CollapseHandler handler) {
        return getCell().addCollapseHandler(handler);
    }

    @Override
    public HandlerRegistration addExpandHandler(ExpandHandler handler) {
        return getCell().addExpandHandler(handler);
    }

    @Override
    public HandlerRegistration addQueryDSLSearchBtnSelectedHandler(QueryDSLSearchBtnSelected.QueryDSLSearchBtnSelectedHandler handler) {
        return getCell().addQueryDSLSearchBtnSelectedHandler(handler);
    }

    @Override
    public HandlerRegistration addFetchTagSuggestionsHandler(FetchTagSuggestions.FetchTagSuggestionsHandler handler) {
        return getCell().addFetchTagSuggestionsHandler(handler);
    }

    @Override
    public void onSavedSearchDeleted(SavedSearchDeletedEvent event) {
        clearSearch();
    }

    public void clearSearch() {
        // Forward clear call to searchForm
        getCell().getSearchForm().clearSearch();
        clearInvalid();
        clear();
    }

    public void edit(QueryDSLTemplate queryTemplate) {
        // Forward edit call to searchForm
//        getCell().getSearchForm().edit(queryTemplate);
        clear();
    }

    @Override
    public DiskResourceSearchCellv2 getCell() {
        return (DiskResourceSearchCellv2)super.getCell();
    }

    protected void expand() {
        getCell().expand(createContext(), getElement(), getValue(), valueUpdater);
    }

    @Override
    protected void onCellParseError(ParseErrorEvent event) {
        super.onCellParseError(event);
        String msg = "Default message";
        forceInvalid(msg);
    }

    @Override
    public void onSubmitTextSearch(SubmitTextSearchEvent event) {
        // Finish editing to fire search event.
        finishEditing();
        focus();
    }

    @Override
    public void onQueryDSLSearchBtnSelected(QueryDSLSearchBtnSelected event) {
        clear();
    }
}
