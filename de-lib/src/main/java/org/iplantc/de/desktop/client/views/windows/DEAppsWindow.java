package org.iplantc.de.desktop.client.views.windows;

import org.iplantc.de.apps.client.AppsView;
import org.iplantc.de.apps.shared.AppsModule;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.UserSettings;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.client.models.WindowType;
import org.iplantc.de.commons.client.util.WindowUtil;
import org.iplantc.de.commons.client.views.window.configs.AppsWindowConfig;
import org.iplantc.de.commons.client.views.window.configs.ConfigFactory;
import org.iplantc.de.commons.client.views.window.configs.WindowConfig;
import org.iplantc.de.desktop.shared.DeModule;
import org.iplantc.de.intercom.client.IntercomFacade;
import org.iplantc.de.intercom.client.TrackingEventType;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.common.base.Strings;
import com.google.inject.Inject;

import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

/**
 * @author jstroot
 */
public class DEAppsWindow extends WindowBase {

    public static final String APPS = "#apps";
    public static final String DE_APPS_ACTIVEVIEW = "de.apps.activeView#";
    public static final String DE_APPS_WESTPANEL_WIDTH = "de.apps.westPanel.width#";
    public static final String DE_APPS_CATPANEL_COLLAPSE = "de.apps.catPanel.collapse#";
    @Inject
    UserSettings userSettings;
    @Inject
    UserInfo userInfo;
    private final AppsView.Presenter presenter;
    private final AppsView.AppsViewAppearance appsViewAppearance;

    @Inject
    DEAppsWindow(final AppsView.Presenter presenter,
                 final IplantDisplayStrings displayStrings,
                 final AppsView.AppsViewAppearance appsViewAppearance) {
        this.presenter = presenter;
        this.appsViewAppearance = appsViewAppearance;

        // This must be set before we render view
        ensureDebugId(DeModule.WindowIds.APPS_WINDOW);

        setMinWidth(appsViewAppearance.appsWindowMinWidth());
        setMinHeight(appsViewAppearance.appsWindowMinHeight());
        setHeading(displayStrings.applications());
    }

    @Override
    public <C extends WindowConfig> void show(final C windowConfig,
                                              final String tag,
                                              final boolean isMaximizable) {
        super.show(windowConfig, tag, isMaximizable);
        final AppsWindowConfig appsWindowConfig = (AppsWindowConfig)windowConfig;
        String activeView = null;
        String westWidth = null;
        String catPanelCollapsed = null;
        if (ws.getAdditionalWindowStates() != null) {
            activeView = ws.getAdditionalWindowStates().get(getKey(DE_APPS_ACTIVEVIEW, tag));
            westWidth = ws.getAdditionalWindowStates().get(getKey(DE_APPS_WESTPANEL_WIDTH, tag));
            catPanelCollapsed =
                    ws.getAdditionalWindowStates().get(getKey(DE_APPS_CATPANEL_COLLAPSE, tag));

        }
        presenter.go(this,
                     appsWindowConfig.getSelectedAppCategory(),
                     appsWindowConfig.getSelectedApp(),
                     activeView,
                     (catPanelCollapsed == null) ? false : Boolean.valueOf(catPanelCollapsed));
        if (!Strings.isNullOrEmpty(westWidth)) {
            presenter.setWestPanelWidth(westWidth);
        }
        btnHelp = createHelpButton();
        getHeader().insertTool(btnHelp,0);
        btnHelp.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                WindowUtil.open(constants.faqUrl() + APPS);
                IntercomFacade.trackEvent(TrackingEventType.APPS_FAQ_CLICKED, null);
            }
        });
    }

    @Override
    protected void afterShow() {
        super.afterShow();
        if (userSettings.isEnableHPCPrompt()){
            presenter.checkForAgaveRedirect();
        }
    }

    @Override
    public WindowConfig getWindowConfig() {
        AppsWindowConfig config = ConfigFactory.appsWindowConfig();
        config.setSelectedApp(presenter.getSelectedApp());
        config.setSelectedAppCategory(presenter.getSelectedAppCategory());
        return config;
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        presenter.setViewDebugId(baseID + AppsModule.Ids.APPS_VIEW);
    }

    @Override
    public String getWindowType() {
        return WindowType.APPS.toString();
    }

    @Override
    public FastMap<String> getAdditionalWindowStates() {
        FastMap<String> additionalData = new FastMap<>();
        additionalData.put(getKey(DE_APPS_ACTIVEVIEW, getStateId()),
                           presenter.getActiveView());
        additionalData.put(getKey(DE_APPS_WESTPANEL_WIDTH, getStateId()), presenter.getWestPanelWidth());
        additionalData.put(getKey(DE_APPS_CATPANEL_COLLAPSE, getStateId()),
                           presenter.isDetailsCollapsed() + "");
        return additionalData;
    }

    @Override
    public void restoreWindowState() {
        if (getStateId().equals(ws.getTag())) {
            super.restoreWindowState();
            String width = ws.getWidth();
            String height = ws.getHeight();
            setSize((width == null) ? appsViewAppearance.appsWindowWidth() : width,
                    (height == null) ? appsViewAppearance.appsWindowHeight() : height);
        }

    }

    private String getKey(String attribute, String tag) {
        return WindowState.ADDITIONAL + attribute + tag + "#" + userInfo.getUsername();
    }

}
