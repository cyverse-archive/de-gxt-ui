package org.iplantc.de.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.HasQualifiedId;
import org.iplantc.de.client.models.apps.integration.AppTemplate;
import org.iplantc.de.client.models.apps.integration.AppTemplateAutoBeanFactory;
import org.iplantc.de.client.models.apps.integration.JobExecution;
import org.iplantc.de.client.services.impl.models.AnalysisSubmissionResponse;

public interface AppTemplateServices {

    /**
     * Retrieves a command line preview represented by the given <code>AppTemplate</code>.
     * 
     * @param at the <code>AppTemplate</code> for which the command line preview will be generated.
     * @param callback
     */
    void cmdLinePreview(AppTemplate at, AsyncCallback<String> callback);

    /**
     * Retrieves an <code>AppTemplate</code> from the database for viewing.
     * 
     * @param appId the <code>App</code> id.
     * @param callback
     */
    void getAppTemplate(HasQualifiedId appId, AsyncCallback<AppTemplate> callback);

    AppTemplateAutoBeanFactory getAppTemplateFactory();

    /**
     * Retrieves an <code>AppTemplate</code> from the database for viewing.
     * 
     * @param appId the <code>App</code> id.
     * @param callback
     */
    void getAppTemplateForEdit(HasId appId, AsyncCallback<AppTemplate> callback);

    /**
     * Retrieves a UI preview of the given <code>AppTemplate</code>.
     * 
     * @param at the <code>AppTemplate</code> for which the preview should be produced.
     * @param callback
     */
    void getAppTemplatePreview(AppTemplate at, AsyncCallback<AppTemplate> callback);

    /**
     * Launches an analysis using the given <code>AppTemplate</code> and <code>JobExecution</code>
     * objects. The service call will construct the expected JSON using the two objects.
     * 
     * @param at the object which contains the values (default or user entered) used for job submission.
     * @param je contains necessary information for job submission
     * @param callback
     */
    void launchAnalysis(AppTemplate at, JobExecution je, AsyncCallback<AnalysisSubmissionResponse> callback);

    /**
     * Retrieves an <code>AppTemplate</code> with all of the values from the given analysisId.
     *  @param analysisId the ID of the analysis for which the <code>AppTemplate</code> should be fetched.
     * @param appId
     * @param callback
     */
    void rerunAnalysis(HasId analysisId, String appId, AsyncCallback<AppTemplate> callback);

    /**
     * Performs an initial publishing of new AppTemplates to the database, or updates of existing ones
     * after they've been updated.
     * 
     * @param at the <code>AppTemplate</code> to be saved/published.
     * @param callback
     */
    void saveAndPublishAppTemplate(AppTemplate at, AsyncCallback<AppTemplate> callback);

    /**
     * Create a new App template
     * 
     * @param at
     * @param callback
     */
    void createAppTemplate(AppTemplate at, AsyncCallback<AppTemplate> callback);

    /**
     * Submits a published app template with non-functional changes.
     * 
     * @param at
     * @param callback
     */
    void updateAppLabels(AppTemplate at, AsyncCallback<AppTemplate> callback);
}
