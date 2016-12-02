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

    @Override
    public String comments() {
        return messages.comments();
    }

    @Override
    public String needHelp() {
        return messages.needHelp();
    }

    @Override
    public String submit() {
        return messages.submit();
    }

    @Override
    public String agreeToShare() {
        return messages.agreeToShare();
    }

    @Override
    public String disclaimer() {
        return messages.disclaimer();
    }

    @Override
    public String termsOfSupport() {
        return messages.termsOfSupport();
    }

    @Override
    public String supportRequestFailed() {
        return messages.supportRequestFailed();
    }

    @Override
    public String supportRequestSuccess() {
        return messages.supportRequestSuccess();
    }
}
