package org.iplantc.de.tools.client.views.requests;

import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.commons.client.validators.DiskResourceNameValidator;
import org.iplantc.de.commons.client.validators.LengthRangeValidator;
import org.iplantc.de.commons.client.validators.UrlValidator;
import org.iplantc.de.commons.client.views.dialogs.IplantInfoBox;
import org.iplantc.de.diskResource.client.gin.factory.DiskResourceSelectorFieldFactory;
import org.iplantc.de.diskResource.client.views.widgets.FileSelectorField;
import org.iplantc.de.resources.client.constants.IplantValidationConstants;
import org.iplantc.de.resources.client.messages.I18N;
import org.iplantc.de.tools.shared.ToolsModule;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.AutoProgressMessageBox;
import com.sencha.gxt.widget.core.client.container.CardLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.Radio;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * A form to submit request to install new tools in condor
 *
 * @author sriram
 */
public final class NewToolRequestFormViewImpl extends Composite implements NewToolRequestFormView {

    private Mode mode;

    @UiTemplate("NewToolRequestFormView.ui.xml")
    interface NewToolRequestFormViewUiBinder extends UiBinder<Widget, NewToolRequestFormViewImpl> {
    }

    private static final NewToolRequestFormViewUiBinder uiBinder =
            GWT.create(NewToolRequestFormViewUiBinder.class);
    private final IplantValidationConstants validationConstants;

    @UiField
    VerticalLayoutContainer container;
    @UiField
    FieldLabel toolNameLbl;
    @UiField
    FieldLabel toolDescLbl;
    @UiField
    FieldLabel srcLbl;
    @UiField
    Radio toolLinkRdo;
    @UiField
    Radio toolUpldRdo;
    @UiField
    Radio toolSltRdo;
    @UiField
    Radio testUpldRdo;
    @UiField
    Radio testSltRdo;
    @UiField
    Radio otherUpldRdo;
    @UiField
    Radio otherSltRdo;
    @UiField
    FieldLabel docUrlLbl;
    @UiField
    FieldLabel versionLbl;
    @UiField
    FieldLabel cmdLineLbl;
    @UiField
    TextField toolName;
    @UiField
    TextArea toolDesc;
    @UiField
    TextArea toolAttrib;
    @UiField
    TextField toolLink;
    @UiField
    TextField toolDoc;
    @UiField
    TextField toolVersion;
    @UiField
    TextArea runInfo;
    @UiField
    TextArea otherInfo;
    @UiField
    UploadForm binUpld;
    @UiField
    UploadForm testDataUpld;
    @UiField
    UploadForm otherDataUpld;
    @UiField(provided = true)
    FileSelectorField binSelect;
    @UiField(provided = true)
    FileSelectorField testDataSelect;
    @UiField(provided = true)
    FileSelectorField otherDataSelect;
    @UiField
    FieldLabel testLbl;
    @UiField
    FieldLabel otherLbl;
    @UiField
    CardLayoutContainer binOptions;
    @UiField
    CardLayoutContainer testDataOptions;
    @UiField
    CardLayoutContainer otherDataOptions;
    @UiField
    HtmlLayoutContainer intro;

    private final AutoProgressMessageBox submissionProgressBox;

    private Presenter presenter;

    private final NewToolRequestFormView.NewToolRequestFormViewAppearance appearance;

    @Inject
    NewToolRequestFormViewImpl(final DiskResourceSelectorFieldFactory fileSelectorFieldFactory,
                               final IplantValidationConstants validationConstants,
                               final NewToolRequestFormView.NewToolRequestFormViewAppearance appearance) {
        this.validationConstants = validationConstants;
        this.appearance = appearance;
        this.binSelect = fileSelectorFieldFactory.defaultFileSelector();
        this.testDataSelect = fileSelectorFieldFactory.defaultFileSelector();
        this.otherDataSelect = fileSelectorFieldFactory.defaultFileSelector();
        initWidget(uiBinder.createAndBindUi(this));
        submissionProgressBox = new AutoProgressMessageBox(I18N.DISPLAY.submitRequest());
        submissionProgressBox.auto();
        container.setScrollMode(ScrollMode.AUTOY);
        container.setAdjustForScroll(true);
        initValidators();
        initRequiredLabels();

        ToggleGroup grp1 = new ToggleGroup();
        grp1.add(toolLinkRdo);
        grp1.add(toolSltRdo);
        grp1.add(toolUpldRdo);

        ToggleGroup grp2 = new ToggleGroup();
        grp2.add(testSltRdo);
        grp2.add(testUpldRdo);


        ToggleGroup grp3 = new ToggleGroup();
        grp3.add(otherSltRdo);
        grp3.add(otherUpldRdo);

    }

    @UiFactory
    HtmlLayoutContainer buildIntroContainer() {
        return new HtmlLayoutContainer((SafeHtml)() -> appearance.newToolInstruction());
    }

    private void initRequiredLabels() {
        toolNameLbl.setHTML(appearance.buildRequiredFieldLabel(I18N.DISPLAY.toolNameLabel()));
        toolDescLbl.setHTML(appearance.buildRequiredFieldLabel(I18N.DISPLAY.toolDesc()));
        srcLbl.setHTML(appearance.buildRequiredFieldLabel(I18N.DISPLAY.srcLinkPrompt()));
        docUrlLbl.setHTML(appearance.buildRequiredFieldLabel(I18N.DISPLAY.docLink()));
        versionLbl.setHTML(appearance.buildRequiredFieldLabel(I18N.DISPLAY.version()));
        cmdLineLbl.setHTML(appearance.buildRequiredFieldLabel(I18N.DISPLAY.cmdLineRun()));
        testLbl.setHTML(appearance.buildRequiredFieldLabel((I18N.DISPLAY.upldTestData())));
    }

    private void initValidators() {
        toolName.addValidator(new LengthRangeValidator(I18N.DISPLAY.toolName(),
                                                       1,
                                                       validationConstants.maxToolNameLength()));
        toolName.addValidator(new DiskResourceNameValidator());
        toolLink.addValidator(new UrlValidator());
        toolDoc.addValidator(new UrlValidator());
        binUpld.addValidator(new DiskResourceNameValidator());
        testDataUpld.addValidator(new DiskResourceNameValidator());
        otherDataUpld.addValidator(new DiskResourceNameValidator());
        otherDataUpld.setAllowBlank(true);
        otherDataSelect.setRequired(false);
        binSelect.setRequired(false);
        toolLink.setAllowBlank(true);
        binUpld.setAllowBlank(false);
        testDataUpld.setAllowBlank(false);
        testDataSelect.setRequired(false);
    }

    @UiHandler("toolLinkRdo")
    void onBinLinkSelect(final ValueChangeEvent<Boolean> unused) {
        if (presenter != null) {
            presenter.onToolSelectionModeChange();
        }
    }

    @UiHandler("toolUpldRdo")
    void onBinUploadSelect(final ValueChangeEvent<Boolean> unused) {
        if (presenter != null) {
            presenter.onToolSelectionModeChange();
        }
    }

    @UiHandler("toolSltRdo")
    void onBinSelect(final ValueChangeEvent<Boolean> unused) {
        if (presenter != null) {
            presenter.onToolSelectionModeChange();
        }
    }

    @UiHandler("testSltRdo")
    void onTestDataSelect(final ValueChangeEvent<Boolean> unused) {
        if (presenter != null) {
            presenter.onTestDataSelectionModeChange();
        }

    }

    @UiHandler("testUpldRdo")
    void onTestDataUpload(final ValueChangeEvent<Boolean> unused) {
        if (presenter != null) {
            presenter.onTestDataSelectionModeChange();
        }

    }

    @UiHandler("otherUpldRdo")
    void onOtherDataUpload(final ValueChangeEvent<Boolean> unused) {
        if (presenter != null) {
            presenter.onOtherDataSeelctionModeChange();
        }

    }

    @UiHandler("otherSltRdo")
    void onOtherDataSelect(final ValueChangeEvent<Boolean> unused) {
        if (presenter != null) {
            presenter.onOtherDataSeelctionModeChange();
        }

    }


    @Override
    public Uploader getOtherDataUploader() {
        return otherDataUpld;
    }

    @Override
    public Uploader getTestDataUploader() {
        return testDataUpld;
    }

    @Override
    public Uploader getToolBinaryUploader() {
        return binUpld;
    }

    @Override
    public final void indicateSubmissionStart() {
        submissionProgressBox.setProgressText(I18N.DISPLAY.submitting());
        submissionProgressBox.getProgressBar().reset();
        submissionProgressBox.show();
    }

    @Override
    public final void indicateSubmissionFailure(final SafeHtml reason) {
        submissionProgressBox.hide();
        final AlertMessageBox amb = new AlertMessageBox(I18N.DISPLAY.alert(), reason);
        amb.show();
    }

    @Override
    public final void indicateSubmissionSuccess() {
        submissionProgressBox.hide();
        final IplantInfoBox successMsg =
                new IplantInfoBox(I18N.DISPLAY.success(), I18N.DISPLAY.requestConfirmMsg());
        successMsg.show();
    }

    @Override
    public boolean isValid() {
        return FormPanelHelper.isValid(container, false);
    }

    @Override
    public void setMode(Mode mode) {
        this.mode = mode;
        switch (mode) {
            case NEWTOOL:
                break;
            case MAKEPUBLIC:
                toolLinkRdo.setValue(true);
                intro.setHTML(() -> appearance.newToolInstruction());
                break;
        }
    }

    @Override
    public void setPresenter(final Presenter p) {
        this.presenter = p;
    }

    @Override
    public void setToolSelectionMode() {
        if (toolLinkRdo.getValue()) {
            binOptions.setActiveWidget(binOptions.getWidget(1));
            presenter.setToolMode(SELECTION_MODE.LINK);
            binUpld.setAllowBlank(true);
            binSelect.setRequired(false);
            toolLink.setAllowBlank(false);
        } else if (toolUpldRdo.getValue()) {
            binOptions.setActiveWidget(binOptions.getWidget(0));
            presenter.setToolMode(SELECTION_MODE.UPLOAD);
            binUpld.setAllowBlank(false);
            binSelect.setRequired(false);
            toolLink.setAllowBlank(true);
        } else if (toolSltRdo.getValue()) {
            binOptions.setActiveWidget(binOptions.getWidget(2));
            presenter.setToolMode(SELECTION_MODE.SELECT);
            binUpld.setAllowBlank(true);
            binSelect.setRequired(true);
            toolLink.setAllowBlank(true);
        }
    }

    @Override
    public IsField<String> getNameField() {
        return toolName;
    }

    @Override
    public IsField<String> getDescriptionField() {
        return toolDesc;
    }

    @Override
    public IsField<String> getAttributionField() {
        return toolAttrib;
    }

    @Override
    public IsField<String> getSourceURLField() {
        return toolLink;
    }

    @Override
    public IsField<String> getDocURLField() {
        return toolDoc;
    }

    @Override
    public IsField<String> getVersionField() {
        return toolVersion;
    }

    @Override
    public IsField<String> getInstructionsField() {
        return runInfo;
    }

    @Override
    public IsField<String> getAdditionalInfoField() {
        return otherInfo;
    }


    @Override
    public FileSelectorField getBinSelectField() {
        return binSelect;
    }

    @Override
    public FileSelectorField getTestDataSelectField() {
        return testDataSelect;
    }

    @Override
    public FileSelectorField getOtherDataSelectField() {
        return otherDataSelect;
    }

    @Override
    public void setTestDataSelectMode() {
        if (testUpldRdo.getValue()) {
            testDataOptions.setActiveWidget(testDataOptions.getWidget(0));
            presenter.setTestDataMode(SELECTION_MODE.UPLOAD);
            testDataUpld.setAllowBlank(false);
            testDataSelect.setRequired(false);
        } else if (testSltRdo.getValue()) {
            testDataOptions.setActiveWidget(testDataOptions.getWidget(1));
            presenter.setTestDataMode(SELECTION_MODE.SELECT);
            testDataUpld.setAllowBlank(true);
            testDataSelect.setRequired(true);
        }

    }

    @Override
    public void setOtherDataSelectMode() {
        if (otherUpldRdo.getValue()) {
            otherDataOptions.setActiveWidget(otherDataOptions.getWidget(0));
            presenter.setOtherDataMode(SELECTION_MODE.UPLOAD);
        } else if (otherSltRdo.getValue()) {
            otherDataOptions.setActiveWidget(otherDataOptions.getWidget(1));
            presenter.setOtherDataMode(SELECTION_MODE.SELECT);
        }
    }

    @Override
    public void setTool(Tool tool) {
       toolName.setValue(tool.getName());
       toolDesc.setValue(tool.getDescription());
       toolVersion.setValue(tool.getVersion());
       toolLink.setValue(tool.getLocation());
       toolUpldRdo.disable();
       toolSltRdo.disable();
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        toolLink.ensureDebugId(baseID + ToolsModule.RequestToolIds.toolLink);
        toolUpldRdo.ensureDebugId(baseID + ToolsModule.RequestToolIds.toolUpld);
        toolSltRdo.ensureDebugId(baseID + ToolsModule.RequestToolIds.toolSlt);
        testUpldRdo.ensureDebugId(baseID + ToolsModule.RequestToolIds.testUpld);
        otherUpldRdo.ensureDebugId(baseID + ToolsModule.RequestToolIds.otherUpld);
        otherSltRdo.ensureDebugId(baseID + ToolsModule.RequestToolIds.otherSlt);
        toolName.ensureDebugId(baseID + ToolsModule.RequestToolIds.toolName);
        toolDesc.ensureDebugId(baseID + ToolsModule.RequestToolIds.toolDesc);
        toolAttrib.ensureDebugId(baseID + ToolsModule.RequestToolIds.toolAttrib);
        toolDoc.ensureDebugId(baseID + ToolsModule.RequestToolIds.toolDoc);
        toolVersion.ensureDebugId(baseID + ToolsModule.RequestToolIds.toolVer);
        runInfo.ensureDebugId(baseID + ToolsModule.RequestToolIds.runInfo);
        otherInfo.ensureDebugId(baseID + ToolsModule.RequestToolIds.otherInfo);
        binUpld.ensureDebugId(baseID + ToolsModule.RequestToolIds.binUpld);
        testDataUpld.ensureDebugId(baseID + ToolsModule.RequestToolIds.testDataUpld);
        otherDataUpld.ensureDebugId(baseID + ToolsModule.RequestToolIds.otherDataUpld);
    }

}
