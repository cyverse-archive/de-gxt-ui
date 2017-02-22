package org.iplantc.de.analysis.client;

import org.iplantc.de.analysis.client.events.selection.AnalysisJobInfoSelected;
import org.iplantc.de.analysis.client.models.AnalysisFilter;
import org.iplantc.de.analysis.client.views.widget.AnalysisSearchField;
import org.iplantc.de.client.models.analysis.Analysis;

import com.google.gwt.user.client.ui.IsWidget;

import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;

/**
 * @author jstroot
 */
public interface AnalysisToolBarView extends IsWidget,
                                             SelectionChangedEvent.SelectionChangedHandler<Analysis>,
                                             AnalysisJobInfoSelected.HasAnalysisJobInfoSelectedHandlers {

    void filterByAnalysisId(String analysisId, String name);

    void filterByParentAnalysisId(String id);

    void setFilterInView(AnalysisFilter filter);

    AnalysisSearchField getSearchField();
}
