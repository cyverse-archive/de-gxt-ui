package org.iplantc.de.apps.widgets.client.view;

import org.iplantc.de.apps.shared.AppsModule;
import org.iplantc.de.apps.widgets.client.events.CreateQuickLaunchEvent;
import org.iplantc.de.apps.widgets.client.events.CreateQuickLaunchEvent.CreateQuickLaunchEventHandler;
import org.iplantc.de.apps.widgets.client.events.RequestAnalysisLaunchEvent;
import org.iplantc.de.apps.widgets.client.events.RequestAnalysisLaunchEvent.RequestAnalysisLaunchEventHandler;
import org.iplantc.de.apps.widgets.client.gin.factory.AppStepResourcesViewFactory;
import org.iplantc.de.apps.widgets.client.view.editors.AppStepResourcesView;
import org.iplantc.de.apps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.de.client.DEClientConstants;
import org.iplantc.de.client.models.apps.integration.AppTemplate;
import org.iplantc.de.client.models.apps.integration.AppTemplateStepLimits;
import org.iplantc.de.client.models.apps.integration.AppTemplateStepRequirements;
import org.iplantc.de.client.models.apps.integration.JobExecution;
import org.iplantc.de.client.util.AppTemplateUtils;
import org.iplantc.de.commons.client.util.CyVerseReactComponents;
import org.iplantc.de.commons.client.widgets.CustomMask;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import java.util.List;

/**
 * @author jstroot
 */
public class AppLaunchViewImpl extends Composite implements AppLaunchView {

    private final DEClientConstants constants;

    @UiTemplate("AppLaunchView.ui.xml")
    interface AppWizardViewUIUiBinder extends UiBinder<Widget, AppLaunchViewImpl> {}

    interface EditorDriver extends SimpleBeanEditorDriver<AppTemplate, AppLaunchViewImpl> {}

    @UiField @Ignore TextButton launchButton;
    @UiField @Ignore TextButton createQuickLaunchButton;

    @UiField(provided = true) @Path("") AppTemplateForm wizard;
    @UiField(provided = true) AppLaunchViewAppearance appearance;

    private final AppTemplateWizardAppearance wizardAppearance;
    private final AppTemplateUtils appTemplateUtils;

    private final EditorDriver editorDriver = GWT.create(EditorDriver.class);

    private final LaunchAnalysisView law;

    @Inject
    AppStepResourcesViewFactory resourcesViewFactory;
    private List<AppStepResourcesView> resourcesViews;

    private CustomMask customMask;

    HTMLPanel panel;

    @Inject
    public AppLaunchViewImpl(final AppWizardViewUIUiBinder binder,
                             final LaunchAnalysisView law,
                             final AppTemplateForm wizard,
                             final AppTemplateUtils appTemplateUtils,
                             AppLaunchViewAppearance appearance,
                             AppTemplateWizardAppearance wizardAppearance,
                             CustomMask customMask,
                             DEClientConstants constants) {
        this.law = law;
        this.wizard = wizard;
        this.appTemplateUtils = appTemplateUtils;
        this.appearance = appearance;
        this.wizardAppearance = wizardAppearance;
        this.customMask = customMask;
        this.constants = constants;
        panel = new HTMLPanel("<div></div>");
        initWidget(binder.createAndBindUi(this));
        launchButton.addStyleName(appearance.launchButtonPositionClassName());
        editorDriver.initialize(this);
    }

    @Override
    public HandlerRegistration addRequestAnalysisLaunchEventHandler(RequestAnalysisLaunchEventHandler handler) {
        return addHandler(handler, RequestAnalysisLaunchEvent.TYPE);
    }

    @Override
    public HandlerRegistration addCreateQuickLaunchEventHandler(CreateQuickLaunchEventHandler handler) {
        return addHandler(handler, CreateQuickLaunchEvent.TYPE);
    }

    @Override
    public void analysisLaunchFailed() {
        launchButton.setEnabled(true);
        unmask();
    }

    @Override
    public void edit(final AppTemplate appTemplate, final JobExecution je) {
        law.edit(je, appTemplate.getAppType());
        editorDriver.edit(appTemplate);
        wizard.insertFirstInAccordion(law);

        resourcesViews = Lists.newArrayList();
        if (appTemplate.getRequirements() != null) {
            if (appTemplate.getRequirements().size() == 1) {
                final AppStepResourcesView resourcesView =
                        appendAppStepResourcesView(appTemplate.getRequirements().get(0));
                resourcesView.setHeading(wizardAppearance.resourceRequirements());
            } else {
                for (AppTemplateStepLimits requirements : appTemplate.getRequirements()) {
                    final AppStepResourcesView resourcesView = appendAppStepResourcesView(requirements);
                    resourcesView.setHeading(wizardAppearance.resourceRequirementsForStep(
                            requirements.getStepNumber() + 1));
                }
            }
        }

        if(appTemplate.getSystemId().equals(constants.hpcSystemId())) {
            createQuickLaunchButton.setEnabled(false);
        }
        if (appTemplate.isDeleted()) {
            customMask(appearance.deprecatedAppMask());
            launchButton.setEnabled(false);
            createQuickLaunchButton.setEnabled(false);
        }
    }

    private AppStepResourcesView appendAppStepResourcesView(AppTemplateStepLimits requirements) {
        final AppStepResourcesView resourcesView = resourcesViewFactory.create(requirements);
        resourcesViews.add(resourcesView);
        wizard.appendResourceRequirements(resourcesView);

        return resourcesView;
    }

    @UiHandler("launchButton")
    void onLaunchButtonClicked(SelectEvent event) {
      // Flush the editor driver to perform validations before calling back to presenter.
        final AppTemplate cleaned = appTemplateUtils.removeEmptyGroupArguments(editorDriver.flush());
        final JobExecution je = law.flushJobExecution();
        if (editorDriver.hasErrors() || law.hasErrors()) {
            GWT.log("Editor has errors");
            List<EditorError> errors = Lists.newArrayList();
            errors.addAll(editorDriver.getErrors());
            errors.addAll(law.getErrors());
            for (EditorError error : errors) {
                GWT.log("\t-- " + ": " + error.getMessage());
            }
        } else {
            List<AppTemplateStepRequirements> requirements = Lists.newArrayList();
            for (AppStepResourcesView resourcesView : resourcesViews) {
                requirements.add(resourcesView.getRequirements());
            }
            je.setRequirements(requirements);

            launch(cleaned, je);
        }
    }

    @UiHandler("createQuickLaunchButton")
    void onCreateQuickLaunchButtonClicked(SelectEvent event) {
        // Flush the editor driver to perform validations before calling back to presenter.
        final AppTemplate cleaned = appTemplateUtils.removeEmptyGroupArguments(editorDriver.flush());
        final JobExecution je = law.flushJobExecution();
        fireEvent(new CreateQuickLaunchEvent(cleaned ,je));
    }

    private void launch(AppTemplate cleaned, JobExecution je) {
        fireEvent(new RequestAnalysisLaunchEvent(cleaned, je));
        mask();
        launchButton.setEnabled(false);
        createQuickLaunchButton.setEnabled(false);
    }

    public void customMask(String message) {
        mask = true;
        maskMessage = message;
        customMask.mask(this, message);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        launchButton.ensureDebugId(baseID + AppsModule.Ids.APP_LAUNCH_BTN);
        createQuickLaunchButton.ensureDebugId(baseID+ AppsModule.Ids.QUICK_LAUNCH_BTN);
        wizard.asWidget().ensureDebugId(baseID + AppsModule.Ids.TEMPLATE_FORM);
        law.asWidget().ensureDebugId(baseID + AppsModule.Ids.TEMPLATE_FORM + AppsModule.Ids.LAUNCH_ANALYSIS_GROUP);

        if (resourcesViews != null) {
            for (AppStepResourcesView resourcesView : resourcesViews) {
                String resourcesViewDebugId =
                        baseID + AppsModule.Ids.TEMPLATE_FORM + AppsModule.Ids.APP_LAUNCH_RESOURCE_REQUESTS;

                final AppTemplateStepRequirements requirements = resourcesView.getRequirements();
                if (requirements != null) {
                    resourcesViewDebugId += requirements.getStepNumber();
                }

                resourcesView.asWidget().ensureDebugId(resourcesViewDebugId);
            }
        }
    }

    @Override
    public void showOrHideCreateQuickLaunchView(ReactQuickLaunch.CreateQLProps props) {
        CyVerseReactComponents.render(ReactQuickLaunch.CreateQuickLaunchDialog, props, panel.getElement());

    }

}
