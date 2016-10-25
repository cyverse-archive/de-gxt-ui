package org.iplantc.de.client.services;

import org.iplantc.de.client.models.apps.proxy.AppListLoadResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.sencha.gxt.data.shared.SortDir;

/**
 * Created by sriram on 10/25/16.
 */
public interface AppSearchFacade {
    /**
     * Searches for all active Apps with a name or description that contains the given search term.
     * @param term  search term
     * @param dir   sort direction
     * @param field  sort field
     * @param callback  callback object to call when search is complete
     */
    void searchApp(String term, SortDir dir, String field, AsyncCallback<AppListLoadResult> callback);
}
