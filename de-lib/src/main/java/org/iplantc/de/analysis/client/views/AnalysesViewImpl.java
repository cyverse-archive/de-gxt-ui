package org.iplantc.de.analysis.client.views;

import org.iplantc.de.analysis.client.AnalysesView;
import org.iplantc.de.analysis.client.AnalysisParametersView;
import org.iplantc.de.analysis.client.ReactAnalyses;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.commons.client.util.CyVerseReactComponents;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * @author sriram, jstroot
 */
public class AnalysesViewImpl implements AnalysesView {

    HTMLPanel panel;
    Presenter presenter;
    AnalysisParametersView.Presenter paramPresenter;
    DiskResourceUtil diskResourceUtil;
    private String baseDebugId;


    @Inject
    AnalysesViewImpl(AnalysisParametersView.Presenter paramPresenter,
                     DiskResourceUtil diskResourceUtil) {
        panel = new HTMLPanel("<div></div>");
        this.paramPresenter = paramPresenter;
        this.diskResourceUtil = diskResourceUtil;
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setPresenter(Presenter presenter, String baseDebugId) {
        this.presenter = presenter;
        this.baseDebugId = baseDebugId;
    }

    @Override
    public void load() {
        ReactAnalyses.AnalysesProps props = new ReactAnalyses.AnalysesProps();
        props.presenter = this.presenter;
        final UserInfo instance = UserInfo.getInstance();
        props.username = instance.getFullUsername();
        props.email = instance.getEmail();
        props.name = instance.getFirstName() + " " + instance.getLastName();
        props.paramPresenter = paramPresenter;
        props.diskResourceUtil = diskResourceUtil;
        props.baseDebugId = baseDebugId;
        CyVerseReactComponents.render(ReactAnalyses.AnalysesView, props, panel.getElement());
    }
}
