package org.iplantc.de.theme.base.client.analyses;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * Created by jstroot on 2/19/15.
 * @author jstroot
 */
public interface AnalysesMessages extends Messages {
    String analysesRetrievalFailure();

    SafeHtml analysisCommentUpdateFailed();

    SafeHtml analysisCommentUpdateSuccess();

    SafeHtml analysisRenameFailed();

    SafeHtml analysisRenameSuccess();

    String analysisStopSuccess(String name);

    String comments();

    String deleteAnalysisError();

    String stopAnalysisError(String name);

    String analysisStepInfoError();

    String userRequestingHelpSubject();

    String requestProcessing();

    String analysesExecDeleteWarning();

    String renameAnalysis();

    String supportRequestFailed();

    String supportRequestSuccess();

    String viewParameters(String name);

    String importRequestSubmit(String name);

    String htAnalysisTitle(String analysisName);
}

