package org.iplantc.de.analysis.client.views;

import org.iplantc.de.analysis.client.AnalysesView;
import org.iplantc.de.analysis.client.AnalysisParametersView;
import org.iplantc.de.analysis.client.ReactAnalyses;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.analysis.Analysis;
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
    AnalysisParametersView.Presenter paramPresenter;
    DiskResourceUtil diskResourceUtil;
    UserInfo userInfo;

    private ReactAnalyses.AnalysesProps props;

    @Inject
    AnalysesViewImpl(AnalysisParametersView.Presenter paramPresenter,
                     DiskResourceUtil diskResourceUtil,
                     UserInfo userInfo) {
        panel = new HTMLPanel("<div></div>");
        this.paramPresenter = paramPresenter;
        this.diskResourceUtil = diskResourceUtil;
        this.userInfo = userInfo;
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void load(Presenter presenter,
                     String baseDebugId,
                     Analysis selectedAnalysis) {
        props = new ReactAnalyses.AnalysesProps();
        props.presenter = presenter;
        props.username = userInfo.getFullUsername();
        props.email = userInfo.getEmail();
        props.name = userInfo.getFirstName() + " " + userInfo.getLastName();
        props.paramPresenter = paramPresenter;
        props.diskResourceUtil = diskResourceUtil;
        props.baseDebugId = baseDebugId;
        if (selectedAnalysis != null) {
            props.nameFilter = "";
            props.permFilter = "";
            props.appTypeFilter = "";
            props.idFilter = selectedAnalysis.getId();
            props.parentId = "";
            props.appNameFilter = "";
        } else {
            props.permFilter = "All";
            props.appTypeFilter = "All";
            props.appNameFilter = "";
            props.nameFilter = "";
            props.idFilter = "";
            props.parentId = "";
        }
        CyVerseReactComponents.render(ReactAnalyses.AnalysesView, props, panel.getElement());
    }

    @Override
    public void updateFilter(String permFilter,
                             String appTypeFilter,
                             String nameFilter,
                             String appNameFilter,
                             String idFilter,
                             String parentId) {
        props.permFilter = permFilter;
        props.appTypeFilter = appTypeFilter;
        props.nameFilter = nameFilter;
        props.appNameFilter = appNameFilter;
        props.idFilter = idFilter;
        props.parentId = parentId;
        CyVerseReactComponents.render(ReactAnalyses.AnalysesView, props, panel.getElement());
    }

}
