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

    @Override
    public String nameHas() {
        return searchMessages.nameHas();
    }

    @Override
    public String pathPrefix() {
        return searchMessages.pathPrefix();
    }

    @Override
    public String exactNameMatch() {
        return searchMessages.exactNameMatch();
    }

    @Override
    public String owner() {
        return searchMessages.owner();
    }

    @Override
    public String exactUserNameMatch() {
        return searchMessages.exactUserNameMatch();
    }

    @Override
    public String permissionValueLabel() {
        return searchMessages.permissionValue();
    }

    @Override
    public String permissionRecurse() {
        return searchMessages.permissionRecurse();
    }

    @Override
    public String sharedWith() {
        return searchMessages.sharedWith();
    }

    @Override
    public String emptyText() {
        return searchMessages.emptyText();
    }

    @Override
    public String emptyDropDownText() {
        return searchMessages.emptyDropDownText();
    }

    @Override
    public String searchBtn() {
        return searchMessages.searchBtnText();
    }

    @Override
    public String createdWithin() {
        return searchMessages.createdWithin();
    }

    @Override
    public String nameHasNot() {
        return searchMessages.nameHasNot();
    }

    @Override
    public String modifiedWithin() {
        return searchMessages.modifiedWithin();
    }

    @Override
    public String metadataAttributeHas() {
        return searchMessages.metadataAttributeHas();
    }

    @Override
    public String ownedBy() {
        return searchMessages.ownedBy();
    }

    @Override
    public String metadataValueHas() {
        return searchMessages.metadataValueHas();
    }

    @Override
    public String fileSizeGreater() {
        return searchMessages.fileSizeGreaterThan();
    }

    @Override
    public String fileSizeLessThan() {
        return searchMessages.fileSizeLessThan();
    }

    @Override
    public String includeTrash() {
        return searchMessages.includeTrash();
    }

    @Override
    public String taggedWith() {
        return searchMessages.taggedWith();
    }

    @Override
    public String fileSizes() {
        return searchMessages.fileSizes();
    }

    @Override
    public String enterCyVerseUserName() {
        return searchMessages.emptyNameText();
    }
}
