package org.iplantc.de.client.services;

import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.HasQualifiedId;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppDoc;
import org.iplantc.de.client.models.apps.AppFeedback;
import org.iplantc.de.client.models.apps.PublishAppRequest;
import org.iplantc.de.client.models.apps.Publishable;
import org.iplantc.de.client.models.apps.integration.AppTemplate;
import org.iplantc.de.client.models.apps.sharing.AppSharingRequestList;
import org.iplantc.de.client.models.apps.sharing.AppUnSharingRequestList;
import org.iplantc.de.shared.DECallback;

import com.google.web.bindery.autobean.shared.Splittable;

import java.util.List;

/**
 * @author jstroot
 */
public interface AppUserServiceFacade extends AppServiceFacade, AppSearchFacade {

    void favoriteApp(HasQualifiedId appId, boolean fav, DECallback<Void> callback);

    /**
     * Retrieves the name and a list of inputs and outputs for the given app. The response JSON will be
     * formatted as follows:
     * 
     * <pre>
     * {
     *     "id": "app-id",
     *     "name": "analysis-name",
     *     "inputs": [{...property-details...},...],
     *     "outputs": [{...property-details...},...]
     * }
     * </pre>
     * 
     * @param app the app identifier information.
     * @param callback called when the RPC call is complete.
     */
    void getDataObjectsForApp(HasQualifiedId app, DECallback<String> callback);

    /**
     * Publishes a workflow / pipeline to user's workspace
     * 
     * @param body post body json
     * @param callback called when the RPC call is complete
     */
    void publishWorkflow(String workflowId, String body, DECallback<String> callback);

    /**
     * Retrieves a workflow from the database for editing in the client.
     *  @param workflowId unique identifier for the workflow.
     * @param callback called when the RPC call is complete.
     */
    void editWorkflow(HasId workflowId, DECallback<String> callback);

    /**
     * Retrieves a new copy of a workflow from the database for editing in the client.
     */
    void copyWorkflow(String workflowId, DECallback<String> callback);

    void copyApp(HasQualifiedId app, DECallback<AppTemplate> callback);

    void deleteAppsFromWorkspace(List<App> apps,
                                 DECallback<Void> callback);

    /**
     * Adds an app to the given public categories.
     */
    void publishToWorld(PublishAppRequest req, DECallback<Void> callback);

    /**
     * Get app details
     */
    void getAppDetails(App app, DECallback<App> callback);

    void getAppDoc(HasQualifiedId app, DECallback<Splittable> callback);

    void saveAppDoc(HasQualifiedId appId, String doc, DECallback<AppDoc> callback);

    void createWorkflows(String body, DECallback<String> callback);

    void rateApp(App app,
                 int rating,
                 DECallback<AppFeedback> callback);

    void deleteRating(App app, DECallback<AppFeedback> callback);

    void getPermissions(List<App> apps, DECallback<String> callback);

    void shareApp(AppSharingRequestList request, DECallback<String> callback);
    
    void unshareApp(AppUnSharingRequestList request, DECallback<String> callback);

    void isPublishable(String system_id, String id, DECallback<Publishable> callback);
}
