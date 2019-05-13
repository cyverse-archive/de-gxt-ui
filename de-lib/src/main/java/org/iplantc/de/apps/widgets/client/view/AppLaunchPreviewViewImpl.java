package org.iplantc.de.apps.widgets.client.view;

import org.iplantc.de.apps.integration.shared.AppIntegrationModule;
import org.iplantc.de.apps.shared.AppsModule;
import org.iplantc.de.apps.widgets.client.events.CreateQuickLaunchEvent;
import org.iplantc.de.apps.widgets.client.events.RequestAnalysisLaunchEvent.RequestAnalysisLaunchEventHandler;
import org.iplantc.de.client.models.apps.integration.AppTemplate;
import org.iplantc.de.client.models.apps.integration.JobExecution;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

/**
 * @author jstroot
 */
public class AppLaunchPreviewViewImpl extends Window implements AppLaunchPreviewView {


    @UiTemplate("AppLaunchView.ui.xml")
    interface AppWizardPreviewUiBinder extends UiBinder<Widget, AppLaunchPreviewViewImpl> {}
    interface EditorDriver extends SimpleBeanEditorDriver<AppTemplate, AppLaunchPreviewView> { }

    @UiField(provided = true) AppLaunchViewAppearance appearance;
    @Ignore @UiField(provided = true) AppTemplateForm wizard;
    @UiField TextButton launchButton;

    private final EditorDriver editorDriver = GWT.create(EditorDriver.class);

    @Inject
    AppLaunchPreviewViewImpl(AppLaunchViewAppearance appearance,
                             AppWizardPreviewUiBinder binder,
                             final AppTemplateForm wizard) {
        this.appearance = appearance;
        this.wizard = wizard;
        setWidget(binder.createAndBindUi(this));
        setSize(appearance.launchPreviewWidth(), appearance.launchPreviewHeight());
        setBorders(false);
        editorDriver.initialize(this);
    }

    @Override
    public HandlerRegistration addRequestAnalysisLaunchEventHandler(RequestAnalysisLaunchEventHandler handler) {
        throw new UnsupportedOperationException("App Launch preview does not support launch request events.");
    }

    @Override
    public HandlerRegistration addCreateQuickLaunchEventHandler(CreateQuickLaunchEvent.CreateQuickLaunchEventHandler handler) {
        throw new UnsupportedOperationException(
                "App Launch preview does not support Quick Launch " + "request events.");
    }

    @Override
    public void analysisLaunchFailed() {

    }

    @Override
    public void edit(AppTemplate appTemplate, JobExecution je) {
        setHeading(appearance.launchPreviewHeader(appTemplate));
        editorDriver.edit(appTemplate);
    }

    @Override
    public void showOrHideCreateQuickLaunchView(ReactQuickLaunch.CreateQLProps props) {
        throw new UnsupportedOperationException(
                "App Launch preview does not support  Create Quick " + "Launch view");
    }

    @Override
    public AppTemplateForm getWizard() {
        return wizard;
    }

    /**
     * @param event
     */
    @UiHandler("launchButton")
    void onLaunchButtonClicked(SelectEvent event) {
        // Flush the editor driver to perform validations.
        editorDriver.flush();
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        Widget closeBtn = getHeader().getTool(0);
        if (closeBtn != null) {
            closeBtn.ensureDebugId(baseID + AppIntegrationModule.Ids.CLOSE_BTN);
        }
        launchButton.ensureDebugId(baseID + AppIntegrationModule.Ids.LAUNCH_BTN);
        wizard.asWidget().ensureDebugId(baseID + AppsModule.Ids.TEMPLATE_FORM);
    }
}
