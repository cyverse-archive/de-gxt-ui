package org.iplantc.de.desktop.client.views.windows;

import org.iplantc.de.apps.client.AppsView;
import org.iplantc.de.apps.shared.AppsModule;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.client.util.WebStorageUtil;
import org.iplantc.de.commons.client.util.WindowUtil;
import org.iplantc.de.commons.client.views.window.configs.AppsWindowConfig;
import org.iplantc.de.commons.client.views.window.configs.ConfigFactory;
import org.iplantc.de.commons.client.views.window.configs.WindowConfig;
import org.iplantc.de.desktop.shared.DeModule;
import org.iplantc.de.intercom.client.IntercomFacade;
import org.iplantc.de.intercom.client.TrackingEventType;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.event.SelectEvent;

/**
 * @author jstroot
 */
public class DEAppsWindow extends IplantWindowBase {

    public static final String APPS = "#apps";
    public static final String DE_APPS_ACTIVEVIEW = "de.apps.activeview#";
    private final AppsView.Presenter presenter;

    @Inject
    DEAppsWindow(final AppsView.Presenter presenter, final IplantDisplayStrings displayStrings) {
        this.presenter = presenter;

        // This must be set before we render view
        ensureDebugId(DeModule.WindowIds.APPS_WINDOW);
        setSize("820", "400");
        setMinWidth(540);
        setHeading(displayStrings.applications());
    }

    @Override
    public <C extends WindowConfig> void show(final C windowConfig,
                                              final String tag,
                                              final boolean isMaximizable) {
        final AppsWindowConfig appsWindowConfig = (AppsWindowConfig)windowConfig;
        presenter.go(this,
                     appsWindowConfig.getSelectedAppCategory(),
                     appsWindowConfig.getSelectedApp(),
                     WebStorageUtil.readFromStorage(
                             DE_APPS_ACTIVEVIEW + UserInfo.getInstance().getUsername()));
        super.show(windowConfig, tag, isMaximizable);
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
        presenter.checkForAgaveRedirect();
    }

    @Override
    public void doHide() {
        WebStorageUtil.writeToStorage(DE_APPS_ACTIVEVIEW + UserInfo.getInstance().getUsername(),
                                      presenter.getActiveView());
        super.doHide();
    }

    @Override
    public WindowState getWindowState() {
        AppsWindowConfig config = ConfigFactory.appsWindowConfig();
        config.setSelectedApp(presenter.getSelectedApp());
        config.setSelectedAppCategory(presenter.getSelectedAppCategory());
        return createWindowState(config);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        presenter.setViewDebugId(baseID + AppsModule.Ids.APPS_VIEW);
    }

}
