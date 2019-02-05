package org.iplantc.de.desktop.client.views.windows;

import org.iplantc.de.analysis.client.AnalysesView;
import org.iplantc.de.analysis.shared.AnalysisModule;
import org.iplantc.de.client.models.AppTypeFilter;
import org.iplantc.de.client.models.WindowType;
import org.iplantc.de.client.models.analysis.AnalysisPermissionFilter;
import org.iplantc.de.commons.client.util.WindowUtil;
import org.iplantc.de.commons.client.views.window.configs.AnalysisWindowConfig;
import org.iplantc.de.commons.client.views.window.configs.ConfigFactory;
import org.iplantc.de.commons.client.views.window.configs.WindowConfig;
import org.iplantc.de.desktop.shared.DeModule;
import org.iplantc.de.intercom.client.IntercomFacade;
import org.iplantc.de.intercom.client.TrackingEventType;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.common.base.Strings;
import com.google.inject.Inject;

import com.sencha.gxt.core.shared.FastMap;

/**
 * @author sriram, jstroot
 */
public class MyAnalysesWindow extends WindowBase {

    public static final String ANALYSES = "#analyses";
    private final AnalysesView.Presenter presenter;
    private final AnalysesView.Appearance analysesAppearance;


    @Inject
    MyAnalysesWindow(final AnalysesView.Presenter presenter,
                     final IplantDisplayStrings displayStrings,
                     final AnalysesView.Appearance appearance) {
        this.presenter = presenter;
        this.analysesAppearance = appearance;

        ensureDebugId(DeModule.WindowIds.ANALYSES_WINDOW);
        setHeading(displayStrings.analyses());

        setMinWidth(appearance.windowMinWidth());
        setMinHeight(appearance.windowMinHeight());
        setBodyStyle("background-color: white;");
    }

    @Override
    public <C extends WindowConfig> void show(C windowConfig, String tag,
                                              boolean isMaximizable) {
        super.show(windowConfig, tag, isMaximizable);
        presenter.go(this,
                     DeModule.WindowIds.ANALYSES_WINDOW,
                     ((AnalysisWindowConfig)windowConfig).getSelectedAnalyses());
        btnHelp = createHelpButton();
        getHeader().insertTool(btnHelp,0);
        btnHelp.addSelectHandler(event -> {
            WindowUtil.open(constants.faqUrl() + ANALYSES);
            IntercomFacade.trackEvent(TrackingEventType.ANALYSES_FAQ_CLICKED, null);
        });
    }

    @Override
    public WindowConfig getWindowConfig() {
        AnalysisWindowConfig config = ConfigFactory.analysisWindowConfig();
        AnalysisPermissionFilter currentPermFilter = presenter.getCurrentPermFilter();
        AppTypeFilter currentTypeFilter = presenter.getCurrentTypeFilter();
        if (currentPermFilter != null) {
            config.setPermFilter(currentPermFilter.getFilterString());
        }
        if (currentTypeFilter != null) {
            config.setTypeFilter(currentTypeFilter.getFilterString());
        }
        return config;
    }

    @Override
    public <C extends WindowConfig> void update(C config) {
        super.update(config);
        AnalysisWindowConfig analysisWindowConfig = (AnalysisWindowConfig) config;
        if(analysisWindowConfig != null && presenter != null) {
            presenter.go(this,
                         DeModule.WindowIds.ANALYSES_WINDOW,
                         analysisWindowConfig.getSelectedAnalyses());
        }
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        presenter.setViewDebugId(baseID + AnalysisModule.Ids.ANALYSES_VIEW);
    }

    @Override
    public String getWindowType() {
        return WindowType.ANALYSES.toString();
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
            setSize((Strings.isNullOrEmpty(width)) ? analysesAppearance.windowWidth() : width,
                    (Strings.isNullOrEmpty(height)) ? analysesAppearance.windowHeight() : height);
        }

    }
}

