package org.iplantc.de.analysis.client.views.widget;

import org.iplantc.de.analysis.client.events.selection.AnalysisCommentSelectedEvent;
import org.iplantc.de.analysis.client.events.selection.RelaunchAnalysisSelected;
import org.iplantc.de.analysis.client.events.selection.ShareAnalysisSelected;
import org.iplantc.de.analysis.client.views.cells.AnalysisDotMenuCell;
import org.iplantc.de.analysis.shared.AnalysisModule;
import org.iplantc.de.client.models.analysis.Analysis;

import com.google.common.collect.Lists;
import com.google.gwt.event.shared.HasHandlers;

import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

/**
 * A menu containing the Analysis relaunch, share, and comment buttons as menu items
 * @author aramsey
 */
public class AnalysisDotMenu extends Menu {

    private AnalysisDotMenuCell.AnalysisDotMenuAppearance appearance;
    private MenuItem relaunchBtn;
    private MenuItem shareBtn;
    private MenuItem commentBtn;

    public AnalysisDotMenu(AnalysisDotMenuCell.AnalysisDotMenuAppearance appearance,
                           Analysis analysis,
                           HasHandlers hasHandlers) {
        super();
        this.appearance = appearance;

        addMenuItems();
        addHandlers(hasHandlers, analysis);
    }

    void addHandlers(HasHandlers hasHandlers, Analysis analysis) {
        if (hasHandlers != null) {
            relaunchBtn.addSelectionHandler(event -> hasHandlers.fireEvent(new RelaunchAnalysisSelected(analysis)));
            shareBtn.addSelectionHandler(event -> hasHandlers.fireEvent(new ShareAnalysisSelected(Lists.newArrayList(analysis))));
            commentBtn.addSelectionHandler(event -> hasHandlers.fireEvent(new AnalysisCommentSelectedEvent(analysis)));
        }
    }

    void addMenuItems() {
        relaunchBtn = new MenuItem(appearance.relaunchText(), appearance.relaunchIcon());
        shareBtn = new MenuItem(appearance.shareText(), appearance.shareIcon());
        commentBtn = new MenuItem(appearance.commentText(), appearance.commentIcon());

        add(relaunchBtn);
        add(shareBtn);
        add(commentBtn);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        relaunchBtn.setId(baseID + AnalysisModule.Ids.MENUITEM_RELAUNCH);
        shareBtn.setId(baseID + AnalysisModule.Ids.SHARE_COLLAB);
        commentBtn.setId(baseID + AnalysisModule.Ids.MENUITEM_UPDATE_COMMENTS);
    }
}
