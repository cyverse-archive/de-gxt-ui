package org.iplantc.de.analysis.client.views.dialogs;


import org.iplantc.de.analysis.client.views.status.HelpRendererTemplates;
import org.iplantc.de.client.models.analysis.Analysis;
import org.iplantc.de.client.models.analysis.AnalysisExecutionStatus;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;

import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

/**
 *
 */
public class AnalysisUserSupportDialog extends IPlantDialog {
    private final Analysis selectedAnalysis;
    private HelpRendererTemplates renderer = GWT.create(HelpRendererTemplates.class);

    public AnalysisUserSupportDialog(Analysis selectedAnalysis) {
        this.selectedAnalysis = selectedAnalysis;
        renderHelp();
    }

    private void renderHelp() {
        HTML text = null;
        switch (AnalysisExecutionStatus.fromTypeString(selectedAnalysis.getStatus().toLowerCase())) {
           case SUBMITTED:
                if(selectedAnalysis.getSystemId().equalsIgnoreCase("DE")) {
                  text = new HTML(renderer.renderCondorSubmitted(selectedAnalysis));
                } else {
                  text = new HTML(renderer.renderAgaveSubmitted(selectedAnalysis));
                }
                break;
            case  RUNNING:
                if(selectedAnalysis.getSystemId().equalsIgnoreCase("DE")) {
                  text = new HTML(renderer.renderCondorRunning(selectedAnalysis));
                } else {
                  text = new HTML(renderer.renderAgaveRunning(selectedAnalysis));
                }
                break;
            case FAILED:
                text = new HTML(renderer.renderFailed(selectedAnalysis));
                break;
            case COMPLETED:
                text = new HTML(renderer.renderCompletedUnExpected(selectedAnalysis));
                break;

        }
        VerticalLayoutContainer vlc = new VerticalLayoutContainer();
        vlc.getElement().getStyle().setBackgroundColor("#FFFFFF");
        vlc.setScrollMode(ScrollSupport.ScrollMode.AUTO);
        vlc.add(text);
        add(vlc);
    }

}
