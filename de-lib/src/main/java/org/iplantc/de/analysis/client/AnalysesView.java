package org.iplantc.de.analysis.client;

import org.iplantc.de.analysis.client.events.AnalysisCommentUpdate;
import org.iplantc.de.analysis.client.events.HTAnalysisExpandEvent;
import org.iplantc.de.analysis.client.events.selection.AnalysisAppSelectedEvent;
import org.iplantc.de.analysis.client.events.selection.AnalysisJobInfoSelected;
import org.iplantc.de.analysis.client.events.selection.AnalysisNameSelectedEvent;
import org.iplantc.de.analysis.client.events.selection.AnalysisUserSupportRequestedEvent;
import org.iplantc.de.analysis.client.events.selection.CancelAnalysisSelected;
import org.iplantc.de.analysis.client.events.selection.DeleteAnalysisSelected;
import org.iplantc.de.analysis.client.events.selection.GoToAnalysisFolderSelected;
import org.iplantc.de.analysis.client.events.selection.RelaunchAnalysisSelected;
import org.iplantc.de.analysis.client.events.selection.RenameAnalysisSelected;
import org.iplantc.de.analysis.client.events.selection.ShareAnalysisSelected;
import org.iplantc.de.analysis.client.events.selection.ViewAnalysisParamsSelected;
import org.iplantc.de.client.models.analysis.AnalysisFilter;
import org.iplantc.de.analysis.client.views.widget.AnalysisSearchField;
import org.iplantc.de.client.models.analysis.Analysis;
import org.iplantc.de.theme.base.client.analyses.AnalysesViewDefaultAppearance.AnalysisInfoStyle;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

/**
 * @author sriram, jstroot
 */
public interface AnalysesView extends IsWidget,
                                      AnalysisAppSelectedEvent.HasAnalysisAppSelectedEventHandlers,
                                      AnalysisNameSelectedEvent.HasAnalysisNameSelectedEventHandlers,
                                      HTAnalysisExpandEvent.HasHTAnalysisExpandEventHandlers,
                                      AnalysisUserSupportRequestedEvent.HasAnalysisUserSupportRequestedEventHandlers,
                                      AnalysisCommentUpdate.HasAnalysisCommentUpdateHandlers,
                                      RelaunchAnalysisSelected.HasRelaunchAnalysisSelectedHandlers,
                                      ShareAnalysisSelected.HasShareAnalysisSelectedHandlers,
                                      RenameAnalysisSelected.HasRenameAnalysisSelectedHandlers,
                                      GoToAnalysisFolderSelected.HasGoToAnalysisFolderSelectedHandlers,
                                      DeleteAnalysisSelected.HasDeleteAnalysisSelectedHandlers,
                                      CancelAnalysisSelected.HasCancelAnalysisSelectedHandlers,
                                      ViewAnalysisParamsSelected.HasViewAnalysisParamsSelectedHandlers,
                                      AnalysisJobInfoSelected.HasAnalysisJobInfoSelectedHandlers {

    interface Appearance {

        String appName();

        String endDate();

        String gridEmptyText();

        String name();

        String noParameters();

        String pagingToolbarStyle();

        String paramName();

        String paramType();

        String paramValue();

        String retrieveParametersLoadingMask();

        String searchFieldEmptyText();

        String selectionCount(int count);

        String startDate();

        String status();

        String goToOutputFolder();

        ImageResource folderIcon();

        String viewParamLbl();

        ImageResource fileViewIcon();

        String relaunchAnalysis();

        ImageResource runIcon();

        String cancelAnalysis();

        ImageResource deleteIcon();

        String delete();

        ImageResource cancelIcon();

        String analysesMenuLbl();

        String editMenuLbl();

        String renameMenuItem();

        ImageResource fileRenameIcon();

        String updateComments();

        ImageResource userCommentIcon();

        String refresh();

        ImageResource refreshIcon();

        String showAll();

        ImageResource arrow_undoIcon();

        ImageResource saveIcon();

        String saveAs();

        String viewAnalysisStepInfo();

        String stepType();

        String jobId();

        AnalysisInfoStyle css();

        ImageResource shareIcon();

        String share();

        String shareCollab();

        String shareSupport();

        String shareSupportConfirm();

        String shareWithInput();

        String shareOutputOnly();

        String stepInfoDialogHeader();

        String stepInfoDialogWidth();

        String stepInfoDialogHeight();

        int dotMenuWidth();

        String windowWidth();

        String windowHeight();

        int windowMinWidth();

        int windowMinHeight();
    }

    interface Presenter {

        void onShareSupportSelected(List<Analysis> currentSelection, boolean shareInputs);

        interface Appearance {

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

            String commentsDialogWidth();

            String commentsDialogHeight();

            String warning();

            String analysesExecDeleteWarning();

            String rename();

            String renameAnalysis();
        }

        List<Analysis> getSelectedAnalyses();

        void go(final HasOneWidget container, List<Analysis> selectedAnalyses);

        void onShowAllSelected();

        void setSelectedAnalyses(List<Analysis> selectedAnalyses);

        void setViewDebugId(String baseId);

        AnalysisFilter getCurrentFilter();

        void loadAnalyses(AnalysisFilter filter);

        void setFilterInView(AnalysisFilter filter);
    }

    void filterByAnalysisId(String id, String name);

    void filterByParentAnalysisId(String id);

    List<Analysis> getSelectedAnalyses();

    void setSelectedAnalyses(List<Analysis> selectedAnalyses);

    void setFilterInView(AnalysisFilter filter);

    String getParentAnalysisId();

    AnalysisSearchField getSearchField();

    AnalysisToolBarView getToolBarView();
}
