package org.iplantc.de.analysis.client.views.dialogs;

import org.iplantc.de.analysis.client.AnalysesView;
import org.iplantc.de.analysis.client.views.AnalysisStepsView;
import org.iplantc.de.client.models.analysis.AnalysisStepsInfo;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;

import com.google.inject.Inject;

public class AnalysisStepsInfoDialog extends IPlantDialog {

    private AnalysisStepsView view;

    @Inject
    public AnalysisStepsInfoDialog(AnalysisStepsView view,
                                   AnalysesView.Appearance appearance) {
        this.view = view;
        setHeading(appearance.stepInfoDialogHeader());
        setSize(appearance.stepInfoDialogWidth(), appearance.stepInfoDialogHeight());
        add(view);
        setPredefinedButtons(PredefinedButton.OK);
    }

    public void show(AnalysisStepsInfo stepsInfo) {
        view.clearData();
        view.setData(stepsInfo.getSteps());
        super.show();
    }

    @Override
    public void show() throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "This method is not supported. Use show(AnalysisStepsInfo) method instead.");
    }

}
