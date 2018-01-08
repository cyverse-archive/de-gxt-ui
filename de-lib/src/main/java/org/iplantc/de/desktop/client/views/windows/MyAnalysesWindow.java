package org.iplantc.de.desktop.client.views.windows;

import org.iplantc.de.analysis.client.AnalysesView;
import org.iplantc.de.analysis.shared.AnalysisModule;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.client.models.WindowType;
import org.iplantc.de.client.models.analysis.Analysis;
import org.iplantc.de.commons.client.util.WindowUtil;
import org.iplantc.de.commons.client.views.window.configs.AnalysisWindowConfig;
import org.iplantc.de.commons.client.views.window.configs.ConfigFactory;
import org.iplantc.de.commons.client.views.window.configs.WindowConfig;
import org.iplantc.de.desktop.shared.DeModule;
import org.iplantc.de.intercom.client.IntercomFacade;
import org.iplantc.de.intercom.client.TrackingEventType;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.event.SelectEvent;

import java.util.List;

/**
 * @author sriram, jstroot
 */
public class MyAnalysesWindow extends IplantWindowBase {

    public static final String ANALYSES = "#analyses";
    private final AnalysesView.Presenter presenter;


    @Inject
    MyAnalysesWindow(final AnalysesView.Presenter presenter,
                     final IplantDisplayStrings displayStrings,
                     final UserInfo userInfo) {
        this.presenter = presenter;
        this.userInfo = userInfo;

        ensureDebugId(DeModule.WindowIds.ANALYSES_WINDOW);
        setHeading(displayStrings.analyses());
        String width = getSavedWidth(WindowType.ANALYSES.toString());
        String height = getSavedHeight(WindowType.ANALYSES.toString());
        setSize((width == null) ? "670" : width, (height == null) ? "375" : height);
        setMinWidth(590);
    }

    @Override
    public <C extends WindowConfig> void show(C windowConfig, String tag,
                                              boolean isMaximizable) {
        presenter.go(this, ((AnalysisWindowConfig)windowConfig).getSelectedAnalyses());
        super.show(windowConfig, tag, isMaximizable);
        btnHelp = createHelpButton();
        getHeader().insertTool(btnHelp,0);
        btnHelp.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                WindowUtil.open(constants.faqUrl() + ANALYSES);
                IntercomFacade.trackEvent(TrackingEventType.ANALYSES_FAQ_CLICKED, null);
            }
        });
    }

    @Override
    public WindowState getWindowState() {
        AnalysisWindowConfig config = ConfigFactory.analysisWindowConfig();
        List<Analysis> selectedAnalyses = Lists.newArrayList();
        selectedAnalyses.addAll(presenter.getSelectedAnalyses());
        config.setSelectedAnalyses(selectedAnalyses);
        config.setFilter(presenter.getCurrentFilter());
        return createWindowState(config);
    }

    @Override
    public <C extends WindowConfig> void update(C config) {
        super.update(config);

        if (config instanceof AnalysisWindowConfig) {
            AnalysisWindowConfig analysisWindowConfig = (AnalysisWindowConfig) config;
            presenter.setSelectedAnalyses(analysisWindowConfig.getSelectedAnalyses());
            presenter.setFilterInView(((AnalysisWindowConfig)config).getFilter());
        }
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        presenter.setViewDebugId(baseID + AnalysisModule.Ids.ANALYSES_VIEW);
    }

    @Override
    public void hide() {
        saveWidth(WindowType.ANALYSES.toString());
        saveHeight(WindowType.ANALYSES.toString());
        super.hide();
    }
}
