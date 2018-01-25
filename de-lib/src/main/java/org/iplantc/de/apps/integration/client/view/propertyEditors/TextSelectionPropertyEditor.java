package org.iplantc.de.apps.integration.client.view.propertyEditors;

import static com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction.ALL;
import static org.iplantc.de.apps.integration.shared.AppIntegrationModule.PropertyPanelIds;

import org.iplantc.de.apps.integration.client.view.propertyEditors.widgets.SelectionItemPropertyEditor;
import org.iplantc.de.apps.integration.shared.AppIntegrationModule;
import org.iplantc.de.apps.widgets.client.models.SelectionItemModelKeyProvider;
import org.iplantc.de.apps.widgets.client.models.SelectionItemProperties;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.ClearComboBoxSelectionKeyDownHandler;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.converters.ArgumentEditorConverter;
import org.iplantc.de.apps.widgets.client.view.editors.arguments.converters.SplittableToSelectionArgConverter;
import org.iplantc.de.apps.widgets.client.view.editors.widgets.CheckBoxAdapter;
import org.iplantc.de.client.models.apps.integration.Argument;
import org.iplantc.de.client.models.apps.integration.SelectionItem;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.commons.client.widgets.ContextualHelpPopup;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.I18N;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.data.client.editor.ListStoreEditor;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

import java.util.Collection;

/**
 * @author jstroot
 */
public class TextSelectionPropertyEditor extends AbstractArgumentPropertyEditor {

    interface EditorDriver extends SimpleBeanEditorDriver<Argument, TextSelectionPropertyEditor> { }

    interface TextSelectionPropertyEditorUiBinder extends UiBinder<Widget, TextSelectionPropertyEditor> { }

    final ListStoreEditor<SelectionItem> selectionItemsEditor;

    @UiField(provided = true) PropertyEditorAppearance appearance;
    @UiField(provided = true) ArgumentEditorConverter<SelectionItem> defaultValueEditor;
    @UiField @Ignore TextButton editSimpleListBtn;
    @UiField TextField label;
    @UiField CheckBoxAdapter omitIfBlank, requiredEditor;
    @UiField @Path("description") TextField toolTipEditor;
    @UiField FieldLabel toolTipLabel, selectionItemDefaultValueLabel;

    private static TextSelectionPropertyEditorUiBinder uiBinder = GWT.create(TextSelectionPropertyEditorUiBinder.class);
    private final EditorDriver editorDriver = GWT.create(EditorDriver.class);

    private final ComboBox<SelectionItem> selectionItemsComboBox;

    @Inject
    public TextSelectionPropertyEditor(PropertyEditorAppearance appearance,
                                       SelectionItemProperties props) {
        this.appearance = appearance;

        // Setup selectionItems and defaultValue editors
        selectionItemsEditor = new ListStoreEditor<>(new ListStore<>(new SelectionItemModelKeyProvider()));

        selectionItemsComboBox = new ComboBox<>(selectionItemsEditor.getStore(), props.displayLabel());
        selectionItemsComboBox.setEmptyText(appearance.emptyListSelectionText());
        selectionItemsComboBox.setTriggerAction(ALL);
        selectionItemsComboBox.setMinChars(1);
        ClearComboBoxSelectionKeyDownHandler handler = new ClearComboBoxSelectionKeyDownHandler(selectionItemsComboBox);
        selectionItemsComboBox.addKeyDownHandler(handler);

        defaultValueEditor = new ArgumentEditorConverter<>(selectionItemsComboBox, new SplittableToSelectionArgConverter());

        initWidget(uiBinder.createAndBindUi(this));

        selectionItemDefaultValueLabel.setHTML(appearance.createContextualHelpLabel(appearance.singleSelectionDefaultValue(), appearance.singleSelectDefaultItem()));

        toolTipLabel.setHTML(appearance.createContextualHelpLabel(appearance.toolTipText(), appearance.toolTip()));

        requiredEditor.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;").append(appearance.isRequired()).toSafeHtml());

        omitIfBlank.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;")
                                                 .append(appearance.createContextualHelpLabelNoFloat(appearance.excludeWhenEmpty(), appearance.singleSelectExcludeArgument())).toSafeHtml());

        editorDriver.initialize(this);
        editorDriver.accept(new InitializeTwoWayBinding(this));
        ensureDebugId(AppIntegrationModule.Ids.PROPERTY_EDITOR + AppIntegrationModule.Ids.SINGLE_SELECT);
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
    protected void initLabelOnlyEditMode(boolean isLabelOnlyEditMode) {
        defaultValueEditor.setEnabled(!isLabelOnlyEditMode);
        omitIfBlank.setEnabled(!isLabelOnlyEditMode);
        requiredEditor.setEnabled(!isLabelOnlyEditMode);
        selectionItemsComboBox.setEnabled(!isLabelOnlyEditMode);
        editSimpleListBtn.setEnabled(!isLabelOnlyEditMode);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        label.ensureDebugId(baseID + PropertyPanelIds.LABEL);
        defaultValueEditor.ensureDebugId(baseID + PropertyPanelIds.DEFAULT_VALUE);
        requiredEditor.ensureDebugId(baseID + PropertyPanelIds.REQUIRED);
        omitIfBlank.ensureDebugId(baseID + PropertyPanelIds.OMIT_IF_BLANK);
        toolTipEditor.ensureDebugId(baseID + PropertyPanelIds.TOOL_TIP);
        editSimpleListBtn.ensureDebugId(baseID + PropertyPanelIds.EDIT_LIST);
    }

    @UiHandler("defaultValueEditor")
    void onDefaultValueChange(ValueChangeEvent<Splittable> event) {
        // Forward defaultValue onto value.
        model.setValue(event.getValue());
    }

    @UiHandler("editSimpleListBtn")
    void onEditSimpleListBtnClicked(@SuppressWarnings("unused") SelectEvent event) {

        IPlantDialog dlg = new IPlantDialog();
        dlg.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
        dlg.setHeading(appearance.singleSelectionCreateLabel());
        dlg.setModal(true);
        dlg.setOkButtonText(I18N.DISPLAY.done());
        dlg.setAutoHide(false);
        final SelectionItemPropertyEditor selectionItemListEditor = new SelectionItemPropertyEditor(model.getSelectionItems(), model.getType());
        dlg.setSize("640", "480");
        dlg.add(selectionItemListEditor);
        dlg.addOkButtonSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                selectionItemsEditor.getStore().clear();
                Collection<? extends SelectionItem> values = selectionItemListEditor.getValues();
                selectionItemsEditor.getStore().addAll(values);
                /*
                 * The backing model is updated, now firing an arbitrary VCE which we know will be picked
                 * up in the InitializeTwoWayBinding. This will cause the corresponding center panel
                 * editor to be updated with the changed list.
                 */
                ValueChangeEvent.fire(defaultValueEditor, defaultValueEditor.getValue());
            }
        });
        final ToolButton toolBtn = new ToolButton(IplantResources.RESOURCES.getContextualHelpStyle().contextualHelp());
        toolBtn.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                ContextualHelpPopup popup = new ContextualHelpPopup();
                popup.setWidth(450);
                popup.add(new HTML(appearance.singleSelectionCreateList()));
                popup.showAt(toolBtn.getAbsoluteLeft(), toolBtn.getAbsoluteTop() + 15);
            }
        });
        dlg.addTool(toolBtn);

        dlg.show();

    }

}
