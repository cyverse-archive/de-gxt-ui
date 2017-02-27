package org.iplantc.de.analysis.client.views.dialogs;


import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.analysis.AnalysesAutoBeanFactory;
import org.iplantc.de.client.models.analysis.Analysis;
import org.iplantc.de.client.models.analysis.AnalysisExecutionStatus;
import org.iplantc.de.client.models.bootstrap.UserProfile;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.inject.Inject;

import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.Radio;


/**
 * @author sriram
 */
public class AnalysisUserSupportDialog extends Window {

    private TextArea comments;
    private TextButton submitBtn;

    public interface AnalysisUserSupportAppearance {

        String outputUnexpected();

        String noOutput();

        String backgroudColor();

        String selectCondition();

        String comments();

        String needHelp();

        String submit();

        String agreeToShare();

        String disclaimer();

        String termsOfSupport();

        String supportRequestFailed();

        String supportRequestSuccess();

        SafeHtml renderCondorSubmitted(Analysis selectedAnalysis);

        SafeHtml renderAgaveSubmitted(Analysis selectedAnalysis);

        SafeHtml renderCondorRunning(Analysis selectedAnalysis);

        SafeHtml renderAgaveRunning(Analysis selectedAnalysis);

        SafeHtml renderFailed(Analysis selectedAnalysis);

        SafeHtml renderCompletedUnExpected(Analysis selectedAnalysis);

        SafeHtml renderCompletedNoOutput(Analysis selectedAnalysis);

        SafeHtml renderSubmitToSupport(Analysis selectedAnalysis, UserProfile userProfile);

    }


    private Analysis selectedAnalysis;
    private AnalysisUserSupportAppearance appearance = GWT.create(AnalysisUserSupportAppearance.class);
    private VerticalLayoutContainer vlc;
    private TextButton needHelpButton;
    private final String DE_SYSTEM_ID = "de";

    @Inject
    AnalysesAutoBeanFactory factory;
    @Inject
    UserInfo userInfo;

    @Inject
    public AnalysisUserSupportDialog() {
        needHelpButton = new TextButton(appearance.needHelp());
        needHelpButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                displayConfirmation();
            }
        });
        submitBtn = new TextButton(appearance.submit());
        submitBtn.disable();
        vlc = new VerticalLayoutContainer();
        vlc.getElement().getStyle().setBackgroundColor(appearance.backgroudColor());
        vlc.setScrollMode(ScrollSupport.ScrollMode.AUTO);
    }

    public void renderHelp(Analysis analysis) {
        this.selectedAnalysis = analysis;
        HTML text = null;
        switch (AnalysisExecutionStatus.fromTypeString(selectedAnalysis.getStatus().toLowerCase())) {
            case SUBMITTED:
                if (selectedAnalysis.getSystemId().equalsIgnoreCase(getDESystemId())) {
                    text = new HTML(appearance.renderCondorSubmitted(selectedAnalysis));
                } else {
                    text = new HTML(appearance.renderAgaveSubmitted(selectedAnalysis));
                }
                vlc.add(text);
                vlc.add(needHelpButton,
                        new VerticalLayoutContainer.VerticalLayoutData(-1,
                                                                       -1,
                                                                       new Margins(20, 0, 10, 300)));
                add(vlc);
                break;
            case RUNNING:
                if (selectedAnalysis.getSystemId().equalsIgnoreCase(getDESystemId())) {
                    text = new HTML(appearance.renderCondorRunning(selectedAnalysis));
                } else {
                    text = new HTML(appearance.renderAgaveRunning(selectedAnalysis));
                }
                vlc.add(text);
                vlc.add(needHelpButton,
                        new VerticalLayoutContainer.VerticalLayoutData(-1,
                                                                       -1,
                                                                       new Margins(20, 0, 10, 300)));
                add(vlc);
                break;
            case FAILED:
                text = new HTML(appearance.renderFailed(selectedAnalysis));
                vlc.add(text);
                add(vlc);
                vlc.add(needHelpButton,
                        new VerticalLayoutContainer.VerticalLayoutData(-1,
                                                                       -1,
                                                                       new Margins(20, 0, 10, 300)));
                break;
            case COMPLETED:
                remove(vlc);
                onCompletedState();
                break;

        }

    }

    private String getDESystemId() {
         if(userInfo.hasSystemsError() || userInfo.hasAppsInfoError())  {
             return DE_SYSTEM_ID;  //hardcoded default
         } else {
             return userInfo.getSystemIds().getDESytemId();
         }
    }

    private void onCompletedState() {
        vlc.clear();

        Label status = new Label(appearance.selectCondition());
        vlc.add(status, new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(5)));

        final Radio noOutputOption = new Radio();
        noOutputOption.setBoxLabel(appearance.noOutput());

        final Radio unExpectedOutputOption = new Radio();
        unExpectedOutputOption.setBoxLabel(appearance.outputUnexpected());

        ToggleGroup group = new ToggleGroup();
        group.add(noOutputOption);
        group.add(unExpectedOutputOption);

        vlc.add(noOutputOption, new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(5)));
        vlc.add(unExpectedOutputOption,
                new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(5)));

        unExpectedOutputOption.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (unExpectedOutputOption.getValue()) {
                    vlc.clear();
                    vlc.add(new HTML(appearance.renderCompletedUnExpected(selectedAnalysis)));
                    vlc.add(needHelpButton,
                            new VerticalLayoutContainer.VerticalLayoutData(-1,
                                                                           -1,
                                                                           new Margins(20, 0, 0, 300)));
                    vlc.forceLayout();
                }
            }
        });

        noOutputOption.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (noOutputOption.getValue()) {
                    vlc.clear();
                    vlc.add(new HTML(appearance.renderCompletedNoOutput(selectedAnalysis)));
                    vlc.add(needHelpButton,
                            new VerticalLayoutContainer.VerticalLayoutData(-1,
                                                                           -1,
                                                                           new Margins(20, 0, 0, 300)));
                    vlc.forceLayout();
                }
            }
        });

        add(vlc);
    }

    private void displayConfirmation() {
        vlc.clear();
        vlc.add(new HTML(appearance.renderSubmitToSupport(selectedAnalysis,
                                                          UserInfo.getInstance().getUserProfile())));
        comments = new TextArea();
        FieldLabel commentsLbl = new FieldLabel(comments, appearance.comments());
        commentsLbl.setLabelAlign(FormPanel.LabelAlign.TOP);
        vlc.add(commentsLbl, new VerticalLayoutContainer.VerticalLayoutData(1, 100, new Margins(10)));

        final CheckBox approvalChkBox = new CheckBox();
        approvalChkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {

                if (approvalChkBox.getValue()) {
                    submitBtn.enable();
                } else {
                    submitBtn.disable();
                }
            }
        });

        approvalChkBox.setBoxLabel(appearance.agreeToShare());
        vlc.add(approvalChkBox, new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(10)));
        vlc.add(submitBtn,
                new VerticalLayoutContainer.VerticalLayoutData(-1, -1, new Margins(20, 0, 10, 300)));

        add(vlc);

    }

    public String getComment() {
        return comments.getValue();
    }

    public void addSubmitSelectHandler(SelectEvent.SelectHandler handler) {
        submitBtn.addSelectHandler(handler);

    }

}
