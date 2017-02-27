package org.iplantc.de.theme.base.client.analyses;

import org.iplantc.de.analysis.client.views.dialogs.AnalysisUserSupportDialog;
import org.iplantc.de.client.models.analysis.Analysis;
import org.iplantc.de.client.models.bootstrap.UserProfile;
import org.iplantc.de.theme.base.client.analyses.support.HelpRendererTemplates;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;

import java.util.Date;

/**
 * Created by sriram on 11/22/16.
 */
public class AnalysisUserSupportDefaultAppearance
        implements AnalysisUserSupportDialog.AnalysisUserSupportAppearance {

    final AnalysesMessages messages = GWT.create(AnalysesMessages.class);

    final HelpRendererTemplates renderer = GWT.create(HelpRendererTemplates.class);

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

    @Override
    public SafeHtml renderCondorSubmitted(Analysis selectedAnalysis) {
        return renderer.renderCondorSubmitted(selectedAnalysis);
    }

    @Override
    public SafeHtml renderAgaveSubmitted(Analysis selectedAnalysis) {
        return renderer.renderAgaveSubmitted(selectedAnalysis);
    }

    @Override
    public SafeHtml renderCondorRunning(Analysis selectedAnalysis) {
        return renderer.renderCondorRunning(selectedAnalysis);
    }

    @Override
    public SafeHtml renderAgaveRunning(Analysis selectedAnalysis) {
        return renderer.renderAgaveRunning(selectedAnalysis);
    }

    @Override
    public SafeHtml renderFailed(Analysis selectedAnalysis) {
        return renderer.renderFailed(selectedAnalysis);
    }

    @Override
    public SafeHtml renderCompletedUnExpected(Analysis selectedAnalysis) {
        return renderer.renderCompletedUnExpected(selectedAnalysis);
    }

    @Override
    public SafeHtml renderCompletedNoOutput(Analysis selectedAnalysis) {
        return renderer.renderCompletedNoOutput(selectedAnalysis);
    }

    @Override
    public SafeHtml renderSubmitToSupport(Analysis selectedAnalysis, UserProfile userProfile) {
        return renderer.renderSubmitToSupport(selectedAnalysis,
                                              new Date(selectedAnalysis.getStartDate()),
                                              new Date(selectedAnalysis.getEndDate()),
                                              userProfile);
    }


}
