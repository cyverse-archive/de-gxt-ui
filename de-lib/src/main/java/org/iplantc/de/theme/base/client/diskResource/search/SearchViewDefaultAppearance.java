package org.iplantc.de.theme.base.client.diskResource.search;

import org.iplantc.de.diskResource.client.SearchView;

import com.google.gwt.core.client.GWT;

public class SearchViewDefaultAppearance implements SearchView.SearchViewAppearance {
    private SearchMessages searchMessages;

    public SearchViewDefaultAppearance() {
        this(GWT.<SearchMessages> create(SearchMessages.class));
    }

    public SearchViewDefaultAppearance(SearchMessages searchMessages) {
        this.searchMessages = searchMessages;
    }

    @Override
    public String deleteSearchSuccess(String searchName) {
        return searchMessages.deleteSearchSuccess(searchName);
    }

    @Override
    public String saveQueryTemplateFail() {
        return searchMessages.saveQueryTemplateFail();
    }
}
