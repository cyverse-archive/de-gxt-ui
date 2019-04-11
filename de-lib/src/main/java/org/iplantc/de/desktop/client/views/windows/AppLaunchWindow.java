package org.iplantc.de.desktop.client.views.windows;

import org.iplantc.de.apps.widgets.client.events.AnalysisLaunchEvent;
import org.iplantc.de.apps.widgets.client.events.AnalysisLaunchEvent.AnalysisLaunchEventHandler;
import org.iplantc.de.apps.widgets.client.events.AppTemplateFetched;
import org.iplantc.de.apps.widgets.client.view.AppLaunchView;
import org.iplantc.de.client.DEClientConstants;
import org.iplantc.de.client.models.HasQualifiedId;
import org.iplantc.de.client.models.WindowType;
import org.iplantc.de.client.models.apps.integration.AppTemplate;
import org.iplantc.de.commons.client.views.window.configs.AppWizardConfig;
import org.iplantc.de.commons.client.views.window.configs.ConfigFactory;
import org.iplantc.de.commons.client.views.window.configs.WindowConfig;
import org.iplantc.de.desktop.client.events.WindowHeadingUpdatedEvent;
import org.iplantc.de.desktop.shared.DeModule;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;

/**
 * @author jstroot
 */
public class AppLaunchWindow extends WindowBase implements AnalysisLaunchEventHandler,
                                                           AppTemplateFetched.AppTemplateFetchedHandler {

    private final AppLaunchView.Presenter presenter;
    private final DEClientConstants deClientConstants;
    private AppLaunchView.AppLaunchViewAppearance appearance;
    private String systemId;
    private String appId;
    private String integratorEmail;

    @Inject
    AppLaunchWindow(final AppLaunchView.Presenter presenter,
                    final DEClientConstants deClientConstants,
                    AppLaunchView.AppLaunchViewAppearance appearance) {
        this.presenter = presenter;
        this.deClientConstants = deClientConstants;
        this.appearance = appearance;

        setMinWidth(appearance.windowMinWidth());
        setMinHeight(appearance.windowMinHeight());
        setBorders(false);

        presenter.addAnalysisLaunchHandler(this);
        presenter.addAppTemplateFetchedHandler(this);

        ensureDebugId(DeModule.WindowIds.APP_LAUNCH_WINDOW);
    }


    @Override
    public WindowConfig getWindowConfig() {
        AppWizardConfig config = ConfigFactory.appWizardConfig(integratorEmail, systemId, appId);
        config.setAppTemplate(AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(presenter.getAppTemplate())));
        return config;
    }

    private boolean isMatchingId(HasQualifiedId id) {
        return id.getSystemId().equalsIgnoreCase(systemId) && id.getId().equalsIgnoreCase(appId);
    }

    @Override
    public void onAnalysisLaunch(AnalysisLaunchEvent analysisLaunchEvent) {
        if (isMatchingId(analysisLaunchEvent.getAppTemplateId())) {
            hide();
        }
    }

    @Override
    public <C extends WindowConfig> void show(final C windowConfig,
                                              final String tag,
                                              final boolean isMaximizable) {
        super.show(windowConfig, tag, isMaximizable);
        final AppWizardConfig config1 = (AppWizardConfig) windowConfig;
        systemId = Strings.isNullOrEmpty(config1.getSystemId())
                ? deClientConstants.deSystemId()
                : config1.getSystemId();
        appId = config1.getAppId();
        integratorEmail = config1.getAppIntegratorEmail();
        init(config1);
    }

    private void init(AppWizardConfig config) {
        SimpleContainer sc = new SimpleContainer();
        this.setWidget(sc);

        sc.mask(appearance.loadingMask());
        presenter.go(this, config);
    }

    @Override
    public void onAppTemplateFetched(AppTemplateFetched event) {
        AppTemplate template = event.getTemplate();
        if (template != null) {
            if (template.isAppDisabled()) {
                hide();
                return;
            }
            setHeading(template.getLabel());
            fireEvent(new WindowHeadingUpdatedEvent());
        }
        forceLayout();
    }

    @Override
    public String getWindowType() {
        return WindowType.APP_WIZARD.toString();
    }

    @Override
    public FastMap<String> getAdditionalWindowStates() {
        return null;
    }

    @Override
    public void restoreWindowState() {
        if (getStateId().equals(ws.getTag())) {
            super.restoreWindowState();
            String width = ws.getWidth();
            String height = ws.getHeight();
            setSize((width == null) ? appearance.windowWidth() : width,
                    (height == null) ? appearance.windowHeight() : height);
        }

    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        presenter.setViewDebugId(baseID);
    }
}
