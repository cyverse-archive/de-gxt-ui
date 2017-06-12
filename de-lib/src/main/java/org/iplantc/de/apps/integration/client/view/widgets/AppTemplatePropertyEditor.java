package org.iplantc.de.apps.integration.client.view.widgets;

import org.iplantc.de.apps.integration.client.events.UpdateCommandLinePreviewEvent;
import org.iplantc.de.apps.integration.client.events.UpdateCommandLinePreviewEvent.HasUpdateCommandLinePreviewEventHandlers;
import org.iplantc.de.apps.integration.client.events.UpdateCommandLinePreviewEvent.UpdateCommandLinePreviewEventHandler;
import org.iplantc.de.apps.integration.client.view.dialogs.DCListingDialog;
import org.iplantc.de.apps.integration.client.view.propertyEditors.PropertyEditorAppearance;
import org.iplantc.de.apps.integration.client.view.tools.ToolSearchField;
import org.iplantc.de.apps.integration.shared.AppIntegrationModule;
import org.iplantc.de.apps.widgets.client.events.AppTemplateSelectedEvent.AppTemplateSelectedEventHandler;
import org.iplantc.de.apps.widgets.client.events.AppTemplateSelectedEvent.HasAppTemplateSelectedEventHandlers;
import org.iplantc.de.apps.widgets.client.events.ArgumentGroupSelectedEvent;
import org.iplantc.de.apps.widgets.client.events.ArgumentGroupSelectedEvent.ArgumentGroupSelectedEventHandler;
import org.iplantc.de.apps.widgets.client.events.ArgumentSelectedEvent;
import org.iplantc.de.apps.widgets.client.events.ArgumentSelectedEvent.ArgumentSelectedEventHandler;
import org.iplantc.de.apps.widgets.client.view.HasLabelOnlyEditMode;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.apps.integration.AppTemplate;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.validators.AppNameValidator;
import org.iplantc.de.commons.client.widgets.PreventEntryAfterLimitHandler;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.tips.QuickTip;

import java.util.Arrays;
import java.util.logging.Logger;

/**
 * @author jstroot
 */
public class AppTemplatePropertyEditor extends Composite implements ValueAwareEditor<AppTemplate>,
                                                                    HasLabelOnlyEditMode,
                                                                    HasAppTemplateSelectedEventHandlers,
                                                                    ArgumentGroupSelectedEventHandler,
                                                                    ArgumentSelectedEventHandler,
                                                                    HasUpdateCommandLinePreviewEventHandlers {

    interface AppTemplatePropertyEditorUiBinder extends UiBinder<Widget, AppTemplatePropertyEditor> {
    }

    @UiField(provided = true)
    AppTemplateContentPanel cp;
    @UiField
    TextArea description;
    @UiField
    TextField name;

    @Path("name")
    HeaderEditor nameEditor;
    @Ignore
    @UiField
    TextButton searchBtn;
    @Ignore
    @UiField(provided = true)
    ToolSearchField tool;
    @UiField
    FieldLabel toolLabel, appNameLabel, appDescriptionLabel;
    @UiField(provided = true)
    PropertyEditorAppearance appearance;

    private static AppTemplatePropertyEditorUiBinder BINDER =
            GWT.create(AppTemplatePropertyEditorUiBinder.class);

    private boolean labelOnlyEditMode = false;

    private AppTemplate model;
    Logger LOG = Logger.getLogger("App template");

    @Inject
    AsyncProviderWrapper<DCListingDialog> dcListingDialogProvider;
    @Inject
    EventBus eventBus;

    @Inject
    public AppTemplatePropertyEditor(PropertyEditorAppearance appearance,
                                     AppTemplateContentPanel cp,
                                     ToolSearchField tool) {
        this.appearance = appearance;
        this.tool = tool;
        this.cp = cp;

        initWidget(BINDER.createAndBindUi(this));
        nameEditor = new HeaderEditor(cp.getHeader(), appearance);

        name.addKeyDownHandler(new PreventEntryAfterLimitHandler(name));
        name.addValidator(new MaxLengthValidator(PreventEntryAfterLimitHandler.DEFAULT_LIMIT));
        name.addValidator(new AppNameValidator());
        description.addKeyDownHandler(new PreventEntryAfterLimitHandler(description));

        initLabels();
    }

    @Override
    public HandlerRegistration addAppTemplateSelectedEventHandler(AppTemplateSelectedEventHandler handler) {
        return cp.addAppTemplateSelectedEventHandler(handler);
    }

    @Override
    public HandlerRegistration addUpdateCommandLinePreviewEventHandler(
            UpdateCommandLinePreviewEventHandler handler) {
        return addHandler(handler, UpdateCommandLinePreviewEvent.TYPE);
    }

    @Override
    public void flush() {
        if (model == null) {
            return;
        }

        model.setTools(Arrays.asList(tool.getValue()));
    }

    @Override
    public boolean isLabelOnlyEditMode() {
        return labelOnlyEditMode;
    }

    @Override
    public void setLabelOnlyEditMode(boolean labelOnlyEditMode) {
        this.labelOnlyEditMode = labelOnlyEditMode;
        toolLabel.setEnabled(!labelOnlyEditMode);
        appNameLabel.setEnabled(!labelOnlyEditMode);
    }

    @Override
    public void onArgumentGroupSelected(ArgumentGroupSelectedEvent event) {
        cp.getHeader().removeStyleName(appearance.appHeaderSelect());
    }

    @Override
    public void onArgumentSelected(ArgumentSelectedEvent event) {
        cp.getHeader().removeStyleName(appearance.appHeaderSelect());
    }

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}

    @Override
    public void setDelegate(EditorDelegate<AppTemplate> delegate) {/* Do Nothing */}

    @Override
    public void setValue(AppTemplate value) {
        if (value == null) {
            return;
        }

        this.model = value;

        if (value.getTools() != null && value.getTools().size() > 0) {
            tool.setValue(value.getTools().get(0));
        } else {
            tool.clear();
        }
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        tool.ensureDebugId(baseID + AppIntegrationModule.Ids.TOOL);
        name.ensureDebugId(baseID + AppIntegrationModule.Ids.APP_NAME);
        description.ensureDebugId(baseID + AppIntegrationModule.Ids.APP_DESCRIPTION);
    }

    /**
     * @param event
     */
    @UiHandler("name")
    void onNameChanged(ValueChangeEvent<String> event) {
        if (!Strings.isNullOrEmpty(event.getValue())) {
            nameEditor.setValue(event.getValue());
        }
    }

    /**
     * @param event
     */
    @UiHandler("searchBtn")
    void onSearchBtnClick(SelectEvent event) {
        dcListingDialogProvider.get(new AsyncCallback<DCListingDialog>(){
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(DCListingDialog dialog) {
                dialog.addHideHandler(new HideEvent.HideHandler() {

                    @Override
                    public void onHide(HideEvent event) {
                        Tool dc = dialog.getSelectedTool();
                        // Set the deployed component in the AppTemplate
                        if (dc != null) {
                            tool.setValue(dc);
                            // presenter.onArgumentPropertyValueChange();
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    /**
     * @param event
     */
    @UiHandler("tool")
    void onToolValueChanged(ValueChangeEvent<Tool> event) {
        // presenter.onArgumentPropertyValueChange();
        fireEvent(new UpdateCommandLinePreviewEvent());
    }

    private void initLabels() {
        String requiredHtml = appearance.fieldLabelRequired().asString();

        String toolHelp = appearance.appToolUsed();
        SafeHtml toolLabelHtml =
                appearance.createContextualHelpLabel(appearance.toolUsedLabel(), toolHelp);
        toolLabel.setHTML(SafeHtmlUtils.fromTrustedString(toolLabelHtml.asString()));
        new QuickTip(toolLabel).getToolTipConfig().setDismissDelay(0);

        appNameLabel.setHTML(SafeHtmlUtils.fromTrustedString(requiredHtml + appearance.appNameLabel()));
        appDescriptionLabel.setHTML(SafeHtmlUtils.fromTrustedString(
                requiredHtml + appearance.appDescriptionLabel()));
    }
}
