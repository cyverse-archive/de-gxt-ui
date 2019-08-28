package org.iplantc.de.analysis.client.views;

import org.iplantc.de.analysis.client.AnalysesView;
import org.iplantc.de.analysis.client.ReactAnalyses;
import org.iplantc.de.commons.client.util.CyVerseReactComponents;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import javax.inject.Inject;


/**
 * @author sriram
 */
public class ViceLogsView implements IsWidget {

    HTMLPanel panel;
    private ReactAnalyses.ViceLogsProps viceLogsProps;

    @Inject
    public ViceLogsView() {
        panel = new HTMLPanel("<div></div>");
        viceLogsProps = new ReactAnalyses.ViceLogsProps();
        viceLogsProps.logs = "";
    }

    public void load(AnalysesView.Presenter presenter, String analysisName, String logs) {
        viceLogsProps.logs = viceLogsProps.logs.concat(logs);
        viceLogsProps.analysisName = analysisName;
        viceLogsProps.loading = false;
        viceLogsProps.dialogOpen = true;
        viceLogsProps.presenter = presenter;
        CyVerseReactComponents.render(ReactAnalyses.ViceLogsViewer, viceLogsProps, panel.getElement());
    }

    public void mask(boolean loading) {
        viceLogsProps.loading = loading;
        CyVerseReactComponents.render(ReactAnalyses.ViceLogsViewer, viceLogsProps, panel.getElement());
    }

    public void closeViceLogsViewer() {
        viceLogsProps.dialogOpen = false;
        CyVerseReactComponents.render(ReactAnalyses.ViceLogsViewer, viceLogsProps, panel.getElement());
    }

    public void update(String logs) {
        viceLogsProps.loading = false;
        viceLogsProps.logs = viceLogsProps.logs.concat(logs);
        CyVerseReactComponents.render(ReactAnalyses.ViceLogsViewer, viceLogsProps, panel.getElement());
    }


    @Override
    public Widget asWidget() {
        return panel;
    }
}
