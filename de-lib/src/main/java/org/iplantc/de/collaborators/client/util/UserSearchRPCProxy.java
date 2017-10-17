/**
 * 
 */
package org.iplantc.de.collaborators.client.util;

import org.iplantc.de.client.gin.ServicesInjector;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.services.CollaboratorsServiceFacade;
import org.iplantc.de.collaborators.client.util.UserSearchField.UsersLoadConfig;
import org.iplantc.de.commons.client.ErrorHandler;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sriram
 *
 */
public class UserSearchRPCProxy extends RpcProxy<UsersLoadConfig, PagingLoadResult<Subject>> {

    CollaboratorsServiceFacade serviceFacade;
    private String lastQueryText = ""; //$NON-NLS-1$

    public UserSearchRPCProxy() {
        serviceFacade = ServicesInjector.INSTANCE.getCollaboratorsServiceFacade();
    }

    public String getLastQueryText() {
        return lastQueryText;
    }

    @Override
    public void load(UsersLoadConfig loadConfig,
            final AsyncCallback<PagingLoadResult<Subject>> callback) {

        lastQueryText = loadConfig.getQuery();

        if (lastQueryText == null || lastQueryText.isEmpty()) {
            // nothing to search
            return;
        }

        serviceFacade.searchCollaborators(lastQueryText, new AsyncCallback<List<Subject>>() {
            @Override
            public void onSuccess(List<Subject> result) {
                List<Subject> filteredResults = getFilteredResults(result);
                callback.onSuccess(new PagingLoadResultBean<>(filteredResults, filteredResults.size(), 0));
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                callback.onFailure(caught);
            }
        });

    }

    /**
     * Filter the results so that the user never sees the "default" collaborator list in their search results
     * @param result
     * @return
     */
    List<Subject> getFilteredResults(List<Subject> result) {
        return result.stream()
                     .filter(subject -> !Group.DEFAULT_GROUP.equals(subject.getName()))
                     .collect(Collectors.toList());
    }

}
