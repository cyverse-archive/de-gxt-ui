package org.iplantc.de.theme.base.client.analyses.cells;

import org.iplantc.de.analysis.client.AnalysesView;
import org.iplantc.de.analysis.client.views.cells.AnalysisDotMenuCell;
import org.iplantc.de.apps.client.views.list.cells.AppDotMenuCell;
import org.iplantc.de.client.models.analysis.Analysis;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class AnalysisDotMenuDefaultAppearance implements AnalysisDotMenuCell.AnalysisDotMenuAppearance {

    private IplantResources iplantResources;
    private IplantDisplayStrings iplantDisplayStrings;
    private AppDotMenuCell.AppDotMenuAppearance appDotMenuAppearance;
    private AnalysesView.Appearance analysisAppearance;

    public AnalysisDotMenuDefaultAppearance() {
        this(GWT.<IplantResources>create(IplantResources.class),
             GWT.<IplantDisplayStrings>create(IplantDisplayStrings.class),
             GWT.<AppDotMenuCell.AppDotMenuAppearance>create(AppDotMenuCell.AppDotMenuAppearance.class),
             GWT.<AnalysesView.Appearance>create(AnalysesView.Appearance.class));
    }

    public AnalysisDotMenuDefaultAppearance(IplantResources iplantResources,
                                            IplantDisplayStrings iplantDisplayStrings,
                                            AppDotMenuCell.AppDotMenuAppearance appDotMenuAppearance,
                                            AnalysesView.Appearance analysisAppearance) {

        this.iplantResources = iplantResources;
        this.iplantDisplayStrings = iplantDisplayStrings;
        this.appDotMenuAppearance = appDotMenuAppearance;
        this.analysisAppearance = analysisAppearance;
    }

    @Override
    public void render(Cell.Context context, Analysis value, SafeHtmlBuilder sb, String debugId) {
        appDotMenuAppearance.render(context, sb, debugId);
    }

    @Override
    public String commentText() {
        return analysisAppearance.updateComments();
    }

    @Override
    public ImageResource commentIcon() {
        return iplantResources.userComment();
    }

    @Override
    public String relaunchText() {
        return analysisAppearance.relaunchAnalysis();
    }

    @Override
    public ImageResource relaunchIcon() {
        return analysisAppearance.runIcon();
    }

    @Override
    public String shareText() {
        return analysisAppearance.shareCollab();
    }

    @Override
    public ImageResource shareIcon() {
        return iplantResources.share();
    }
}
