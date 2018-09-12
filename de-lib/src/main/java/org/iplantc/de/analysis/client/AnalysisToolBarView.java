package org.iplantc.de.analysis.client;

import org.iplantc.de.analysis.client.events.AnalysisCommentUpdate;
import org.iplantc.de.analysis.client.events.AnalysisFilterChanged;
import org.iplantc.de.analysis.client.events.selection.AnalysisJobInfoSelected;
import org.iplantc.de.analysis.client.events.selection.CancelAnalysisSelected;
import org.iplantc.de.analysis.client.events.selection.CompleteAnalysisSelected;
import org.iplantc.de.analysis.client.events.selection.DeleteAnalysisSelected;
import org.iplantc.de.analysis.client.events.selection.GoToAnalysisFolderSelected;
import org.iplantc.de.analysis.client.events.selection.RefreshAnalysesSelected;
import org.iplantc.de.analysis.client.events.selection.RelaunchAnalysisSelected;
import org.iplantc.de.analysis.client.events.selection.RenameAnalysisSelected;
import org.iplantc.de.analysis.client.events.selection.ShareAnalysisSelected;
import org.iplantc.de.analysis.client.events.selection.ViewAnalysisParamsSelected;
import org.iplantc.de.analysis.client.views.widget.AnalysisSearchField;
import org.iplantc.de.client.models.AppTypeFilter;
import org.iplantc.de.client.models.analysis.Analysis;
import org.iplantc.de.client.models.analysis.AnalysisPermissionFilter;

import com.google.gwt.user.client.ui.IsWidget;

import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;

/**
 * @author jstroot
 */
public interface AnalysisToolBarView extends IsWidget,
                                             SelectionChangedEvent.SelectionChangedHandler<Analysis>,
                                             AnalysisJobInfoSelected.HasAnalysisJobInfoSelectedHandlers,
                                             AnalysisCommentUpdate.HasAnalysisCommentUpdateHandlers,
                                             ShareAnalysisSelected.HasShareAnalysisSelectedHandlers,
                                             AnalysisFilterChanged.HasAnalysisFilterChangedHandlers,
                                             RefreshAnalysesSelected.HasRefreshAnalysesSelectedHandlers,
                                             RenameAnalysisSelected.HasRenameAnalysisSelectedHandlers,
                                             RelaunchAnalysisSelected.HasRelaunchAnalysisSelectedHandlers,
                                             GoToAnalysisFolderSelected.HasGoToAnalysisFolderSelectedHandlers,
                                             DeleteAnalysisSelected.HasDeleteAnalysisSelectedHandlers,
                                             CancelAnalysisSelected.HasCancelAnalysisSelectedHandlers,
                                             CompleteAnalysisSelected.HasCompleteAnalysisSelectedHandlers,
                                             ViewAnalysisParamsSelected.HasViewAnalysisParamsSelectedHandlers {

    void filterByAnalysisId(String analysisId, String name);

    void filterByParentAnalysisId(String id);

    void setFilterInView(AnalysisPermissionFilter permFilter, AppTypeFilter typeFilter);

    AnalysisSearchField getSearchField();
}
