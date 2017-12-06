package org.iplantc.de.theme.base.client.diskResource.search.cells;

import org.iplantc.de.diskResource.client.views.search.cells.QueryDSLForm;
import org.iplantc.de.theme.base.client.diskResource.search.SearchMessages;

import com.google.gwt.core.client.GWT;

public class QueryDSLFormDefaultAppearance implements QueryDSLForm.QueryDSLFormAppearance {

    private SearchMessages searchMessages;

    public QueryDSLFormDefaultAppearance() {
        this(GWT.<SearchMessages> create(SearchMessages.class));
    }

    public QueryDSLFormDefaultAppearance(SearchMessages searchMessages) {
        this.searchMessages = searchMessages;
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
    public String permissionUsers() {
        return searchMessages.permissionUsers();
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
    public String searchBtnText() {
        return searchMessages.searchBtnText();
    }
}
