/**
 * 
 */
package org.iplantc.de.collaborators.client.util;

import org.iplantc.de.client.gin.ServicesInjector;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.services.CollaboratorsServiceFacade;
import org.iplantc.de.collaborators.client.util.UserSearchField.UsersLoadConfig;
import org.iplantc.de.commons.client.ErrorHandler;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

import java.util.List;

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
                callback.onSuccess(new PagingLoadResultBean<>(result, result.size(), 0));
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                callback.onFailure(caught);
            }
        });

    }

}
