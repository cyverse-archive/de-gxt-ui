package org.iplantc.de.client.services;

import org.iplantc.de.analysis.client.models.FilterBeanList;
import org.iplantc.de.client.models.analysis.Analysis;
import org.iplantc.de.client.models.analysis.AnalysisParameter;
import org.iplantc.de.client.models.analysis.AnalysisStepsInfo;
import org.iplantc.de.client.models.analysis.sharing.AnalysisSharingRequestList;
import org.iplantc.de.client.models.analysis.sharing.AnalysisUnsharingRequestList;
import org.iplantc.de.shared.DECallback;

import java.util.List;
import java.util.Map;

public interface AnalysisServiceFacade {

    void getAnalyses(int limit,
                     int offSet,
                     FilterBeanList filters,
                     String sortField,
                     String sortDir,
                     DECallback<String> callback);

    /**
     * Delete an analysis execution
     * 
     * @param analysesToDelete the analyses to be deleted.
     * @param callback executed when RPC call completes.
     */
    void deleteAnalyses(List<Analysis> analysesToDelete, DECallback<String> callback);

    /**
     * Renames an analysis.
     *
     * @param id the analysis id which will be renamed
     * @param newName the new analysis name
     * @param callback executed when RPC call completes.
     */
    void renameAnalysis(String id, String newName, DECallback<Void> callback);

    /**
     * Stop a currently running analysis
     * 
     * @param analysis the analysis to be stopped.
     * @param callback executed when RPC call completes.
     */
    void stopAnalysis(Analysis analysis, DECallback<String> callback, String status);

    void getAnalysisParams(String analysis_id, DECallback<List<AnalysisParameter>> callback);

    void updateAnalysisComments(String id, String newComment, DECallback<Void> callback);

    /**
     * 
     * @sriram: the URL path for the new service will be `/analyses/:analysis-id/steps`
     * @param id analysis id
     * @param callback
     */
    void getAnalysisSteps(String id, DECallback<AnalysisStepsInfo> callback);

    void shareAnalyses(AnalysisSharingRequestList request, DECallback<String> callback);

    void unshareAnalyses(AnalysisUnsharingRequestList request, DECallback<String> callback);

    void getPermissions(List<Analysis> analyses, DECallback<String> callback);

}
