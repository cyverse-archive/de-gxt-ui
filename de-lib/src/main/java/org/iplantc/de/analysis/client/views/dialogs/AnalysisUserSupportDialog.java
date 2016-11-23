package org.iplantc.de.analysis.client.views.dialogs;


import org.iplantc.de.analysis.client.views.status.HelpRendererTemplates;
import org.iplantc.de.client.models.analysis.Analysis;
import org.iplantc.de.client.models.analysis.AnalysisExecutionStatus;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;

import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.Radio;


/**
 *
 */
public class AnalysisUserSupportDialog extends IPlantDialog {

    public interface AnalysisUserSupportAppearance {

        String outputUnexpected();

        String noOutput();

        String backgroudColor();

        String selectCondition();
    }


    private final Analysis selectedAnalysis;
    private HelpRendererTemplates renderer = GWT.create(HelpRendererTemplates.class);
    private AnalysisUserSupportAppearance appearance = GWT.create(AnalysisUserSupportAppearance.class);
    private VerticalLayoutContainer vlc;

    public AnalysisUserSupportDialog(Analysis selectedAnalysis) {
        setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
        TextButton tb = getButton (PredefinedButton.CANCEL);
        tb.setText("I need more help!");
        this.selectedAnalysis = selectedAnalysis;
        vlc = new VerticalLayoutContainer();
        vlc.getElement().getStyle().setBackgroundColor(appearance.backgroudColor());
        vlc.setScrollMode(ScrollSupport.ScrollMode.AUTO);
        renderHelp();
    }

    private void renderHelp() {
        HTML text = null;
        GWT.log("system id--> " + selectedAnalysis.getSystemId());
        switch (AnalysisExecutionStatus.fromTypeString(selectedAnalysis.getStatus().toLowerCase())) {
            case SUBMITTED:
                if (selectedAnalysis.getSystemId().equalsIgnoreCase("DE")) {
                    text = new HTML(renderer.renderCondorSubmitted(selectedAnalysis));
                } else {
                    text = new HTML(renderer.renderAgaveSubmitted(selectedAnalysis));
                }
                vlc.add(text);
                add(vlc);
                break;
            case RUNNING:
                if (selectedAnalysis.getSystemId().equalsIgnoreCase("DE")) {
                    text = new HTML(renderer.renderCondorRunning(selectedAnalysis));
                } else {
                    text = new HTML(renderer.renderAgaveRunning(selectedAnalysis));
                }
                vlc.add(text);
                add(vlc);
                break;
            case FAILED:
                text = new HTML(renderer.renderFailed(selectedAnalysis));
                vlc.add(text);
                add(vlc);
                break;
            case COMPLETED:
                remove(vlc);
                onCompletedState();
                break;

        }

    }

    private void onCompletedState() {
        vlc.clear();

        Label status = new Label(appearance.selectCondition());
        vlc.add(status,new VerticalLayoutContainer.VerticalLayoutData(1, -1,new Margins(5)));

        final Radio noOutputOption = new Radio();
        noOutputOption.setBoxLabel(appearance.noOutput());

        final Radio unExpectedOutputOption = new Radio();
        unExpectedOutputOption.setBoxLabel(appearance.outputUnexpected());

        ToggleGroup group = new ToggleGroup();
        group.add(noOutputOption);
        group.add(unExpectedOutputOption);

        vlc.add(noOutputOption, new VerticalLayoutContainer.VerticalLayoutData(1, -1,new Margins(5)));
        vlc.add(unExpectedOutputOption, new VerticalLayoutContainer.VerticalLayoutData(1, -1,new Margins(5)));

        unExpectedOutputOption.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (unExpectedOutputOption.getValue()) {
                    vlc.clear();
                    GWT.log("unexpected output option selected" + unExpectedOutputOption.getValue());
                    vlc.add(new HTML(renderer.renderCompletedUnExpected(selectedAnalysis)));
                }
            }
        });

        noOutputOption.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (noOutputOption.getValue()) {
                    vlc.clear();
                    GWT.log("no output option selected" + noOutputOption.getValue());
                    vlc.add(new HTML(renderer.renderCompletedNoOutput(selectedAnalysis)));
                }
            }
        });

        add(vlc);


    }

}
