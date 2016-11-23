package org.iplantc.de.theme.base.client.analyses;

import org.iplantc.de.analysis.client.views.dialogs.AnalysisUserSupportDialog;

import com.google.gwt.core.client.GWT;

/**
 * Created by sriram on 11/22/16.
 */
public class AnalysisUserSupportDefaultAppearance implements AnalysisUserSupportDialog.AnalysisUserSupportAppearance {

    final AnalysesMessages messages = GWT.create(AnalysesMessages.class);

    @Override
    public String outputUnexpected() {
        return messages.outputUnexpected();
    }

    @Override
    public String noOutput() {
        return messages.noOutput();
    }

    @Override
    public String backgroudColor() {
        return "#FFFFFF";
    }

    @Override
    public String selectCondition() {
        return messages.selectCondition();
    }
}
