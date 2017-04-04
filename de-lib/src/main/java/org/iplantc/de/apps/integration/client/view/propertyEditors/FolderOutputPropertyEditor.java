package org.iplantc.de.apps.integration.client.view.propertyEditors;

import org.iplantc.de.apps.integration.shared.AppIntegrationModule.Ids;
import org.iplantc.de.apps.integration.shared.AppIntegrationModule.PropertyPanelIds;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.converters.ArgumentEditorConverter;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.converters.SplittableToStringConverter;
import org.iplantc.de.apps.widgets.client.view.editors.widgets.CheckBoxAdapter;
import org.iplantc.de.client.models.apps.integration.Argument;
import org.iplantc.de.client.models.apps.integration.FileInfoType;
import org.iplantc.de.client.services.AppBuilderMetadataServiceFacade;
import org.iplantc.de.commons.client.validators.CmdLineArgCharacterValidator;
import org.iplantc.de.commons.client.validators.DiskResourceNameValidator;
import org.iplantc.de.resources.client.constants.IplantValidationConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * @author jstroot
 */
public class FolderOutputPropertyEditor extends AbstractArgumentPropertyEditor {

    interface EditorDriver extends SimpleBeanEditorDriver<Argument, FolderOutputPropertyEditor> {
    }

    interface FolderOutputPropertyEditorUiBinder extends UiBinder<Widget, FolderOutputPropertyEditor> {
    }

    @UiField(provided = true) PropertyEditorAppearance appearance;
    @UiField @Path("name") TextField argumentOptionEditor;
    @UiField(provided = true) ArgumentEditorConverter<String> defaultValueEditor;
    @UiField @Path("visible") CheckBoxAdapter doNotDisplay;
    @UiField(provided = true)
    @Ignore // FIXME Why is this ignored but still has a path annotation?
    @Path("fileParameters.fileInfoType")
    ComboBox<FileInfoType> fileInfoTypeComboBox;
    @UiField @Path("fileParameters.implicit") CheckBoxAdapter isImplicit;
    @UiField TextField label;
    @UiField CheckBoxAdapter omitIfBlank, requiredEditor;
    @UiField @Path("description") TextField toolTipEditor;
    @UiField FieldLabel toolTipLabel, argumentOptionLabel, defaultValueLabel;

    private static FolderOutputPropertyEditorUiBinder uiBinder = GWT.create(FolderOutputPropertyEditorUiBinder.class);
    private final EditorDriver editorDriver = GWT.create(EditorDriver.class);

    @Inject
    public FolderOutputPropertyEditor(final PropertyEditorAppearance appearance,
                                      final AppBuilderMetadataServiceFacade appMetadataService,
                                      final IplantValidationConstants validationConstants) {
        this.appearance = appearance;

        TextField textField = new TextField();
        textField.addValidator(new DiskResourceNameValidator());
        textField.setEmptyText(appearance.folderOutputEmptyText());
        defaultValueEditor = new ArgumentEditorConverter<>(textField, new SplittableToStringConverter());
        fileInfoTypeComboBox = createFileInfoTypeComboBox(appMetadataService);

        initWidget(uiBinder.createAndBindUi(this));

        argumentOptionEditor.addValidator(new CmdLineArgCharacterValidator(validationConstants.restrictedCmdLineChars()));

        defaultValueLabel.setHTML(appearance.createContextualHelpLabel(appearance.folderOutputDefaultLabel(), appearance.folderOutputDefaultValueHelp()));
        toolTipLabel.setHTML(appearance.createContextualHelpLabel(appearance.toolTipText(), appearance.toolTip()));
        argumentOptionLabel.setHTML(appearance.createContextualHelpLabel(appearance.argumentOption(), appearance.argumentOptionHelp()));
        doNotDisplay.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;").append(appearance.doNotDisplay()).toSafeHtml());

        requiredEditor.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;").append(appearance.isRequired()).toSafeHtml());

        omitIfBlank.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;")
                                                 .append(appearance.createContextualHelpLabelNoFloat(appearance.excludeWhenEmpty(), appearance.fileOutputExcludeArgument()))
                                                 .toSafeHtml());
        isImplicit.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;").append(appearance.createContextualHelpLabelNoFloat(appearance.doNotPass(), appearance.doNotPassHelp()))
                                                .toSafeHtml());


        editorDriver.initialize(this);
        editorDriver.accept(new InitializeTwoWayBinding(this));
        ensureDebugId(Ids.PROPERTY_EDITOR + Ids.FOLDER_OUTPUT);
    }

    @Override
    public void edit(Argument argument) {
        super.edit(argument);
        editorDriver.edit(argument);
    }

    @Override
    public com.google.gwt.editor.client.EditorDriver<Argument> getEditorDriver() {
        return editorDriver;
    }

    @Override
    @Ignore
    protected LeafValueEditor<Splittable> getDefaultValueEditor() {
        return defaultValueEditor;
    }

    @Override
    @Ignore
    protected ComboBox<FileInfoType> getFileInfoTypeComboBox() {
        return fileInfoTypeComboBox;
    }

    @Override
    protected void initLabelOnlyEditMode(boolean isLabelOnlyEditMode) {
        defaultValueEditor.setEnabled(!isLabelOnlyEditMode);
        doNotDisplay.setEnabled(!isLabelOnlyEditMode);
        fileInfoTypeComboBox.setEnabled(!isLabelOnlyEditMode);
        isImplicit.setEnabled(!isLabelOnlyEditMode);
        argumentOptionEditor.setEnabled(!isLabelOnlyEditMode);
        omitIfBlank.setEnabled(!isLabelOnlyEditMode);
        requiredEditor.setEnabled(!isLabelOnlyEditMode);

        if (isLabelOnlyEditMode) {
            defaultValueEditor.getValidators().clear();
            doNotDisplay.getValidators().clear();
            fileInfoTypeComboBox.getValidators().clear();
            isImplicit.getValidators().clear();
            argumentOptionEditor.getValidators().clear();
            omitIfBlank.getValidators().clear();
            requiredEditor.getValidators().clear();
        }
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        label.ensureDebugId(baseID + PropertyPanelIds.LABEL);
        isImplicit.getCheckBox().ensureDebugId(baseID + PropertyPanelIds.COMMAND_LINE);
        defaultValueEditor.ensureDebugId(baseID + PropertyPanelIds.DEFAULT_VALUE);
        argumentOptionEditor.ensureDebugId(baseID + PropertyPanelIds.ARGUMENT_OPTION);
        doNotDisplay.ensureDebugId(baseID + PropertyPanelIds.DO_NOT_DISPLAY);
        requiredEditor.ensureDebugId(baseID + PropertyPanelIds.REQUIRED);
        omitIfBlank.ensureDebugId(baseID + PropertyPanelIds.OMIT_IF_BLANK);
        toolTipEditor.ensureDebugId(baseID + PropertyPanelIds.TOOL_TIP);
        fileInfoTypeComboBox.ensureDebugId(baseID + PropertyPanelIds.FILE_INFO_TYPE);
    }

    @UiHandler("defaultValueEditor")
    void onDefaultValueChange(ValueChangeEvent<Splittable> event) {
        // Forward defaultValue onto value.
        model.setValue(event.getValue());
    }

}
