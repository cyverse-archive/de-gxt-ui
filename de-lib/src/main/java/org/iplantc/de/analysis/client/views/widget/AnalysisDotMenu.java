package org.iplantc.de.analysis.client.views.widget;

import static org.iplantc.de.client.models.analysis.AnalysisExecutionStatus.CANCELED;
import static org.iplantc.de.client.models.analysis.AnalysisExecutionStatus.COMPLETED;
import static org.iplantc.de.client.models.analysis.AnalysisExecutionStatus.FAILED;
import static org.iplantc.de.client.models.analysis.AnalysisExecutionStatus.IDLE;
import static org.iplantc.de.client.models.analysis.AnalysisExecutionStatus.RUNNING;
import static org.iplantc.de.client.models.analysis.AnalysisExecutionStatus.SUBMITTED;

import org.iplantc.de.analysis.client.events.selection.AnalysisCommentSelectedEvent;
import org.iplantc.de.analysis.client.events.selection.AnalysisJobInfoSelected;
import org.iplantc.de.analysis.client.events.selection.CancelAnalysisSelected;
import org.iplantc.de.analysis.client.events.selection.DeleteAnalysisSelected;
import org.iplantc.de.analysis.client.events.selection.GoToAnalysisFolderSelected;
import org.iplantc.de.analysis.client.events.selection.RelaunchAnalysisSelected;
import org.iplantc.de.analysis.client.events.selection.RenameAnalysisSelected;
import org.iplantc.de.analysis.client.events.selection.ShareAnalysisSelected;
import org.iplantc.de.analysis.client.events.selection.ViewAnalysisParamsSelected;
import org.iplantc.de.analysis.client.views.cells.AnalysisDotMenuCell;
import org.iplantc.de.analysis.shared.AnalysisModule;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.analysis.Analysis;

import com.google.common.collect.Lists;
import com.google.gwt.event.shared.HasHandlers;

import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

/**
 * A menu containing all of the Analysis window buttons as menu items
 * @author aramsey
 */
public class AnalysisDotMenu extends Menu {

    private AnalysisDotMenuCell.AnalysisDotMenuAppearance appearance;
    private MenuItem outputFolderBtn;
    private MenuItem parametersBtn;
    private MenuItem relaunchBtn;
    private MenuItem infoBtn;
    private MenuItem completeBtn;
    private MenuItem cancelBtn;
    private MenuItem deleteBtn;
    private MenuItem renameBtn;
    private MenuItem commentBtn;
    private MenuItem shareBtn;
    private UserInfo userInfo = UserInfo.getInstance();

    public AnalysisDotMenu(AnalysisDotMenuCell.AnalysisDotMenuAppearance appearance,
                           Analysis analysis,
                           HasHandlers hasHandlers) {
        super();
        this.appearance = appearance;

        addMenuItems();
        checkButtonStatus(analysis);
        addHandlers(hasHandlers, analysis);
    }

    void addHandlers(HasHandlers hasHandlers, Analysis analysis) {
        if (hasHandlers != null) {
            outputFolderBtn.addSelectionHandler(event -> hasHandlers.fireEvent(new GoToAnalysisFolderSelected(analysis)));
            parametersBtn.addSelectionHandler(event -> hasHandlers.fireEvent(new ViewAnalysisParamsSelected(analysis)));
            infoBtn.addSelectionHandler(event -> hasHandlers.fireEvent(new AnalysisJobInfoSelected(analysis)));
            // TODO: Should set status completed, not canceled
            completeBtn.addSelectionHandler(event -> hasHandlers.fireEvent(new CancelAnalysisSelected(Lists.newArrayList(analysis))));
            cancelBtn.addSelectionHandler(event -> hasHandlers.fireEvent(new CancelAnalysisSelected(Lists.newArrayList(analysis))));
            deleteBtn.addSelectionHandler(event -> hasHandlers.fireEvent(new DeleteAnalysisSelected(Lists.newArrayList(analysis))));
            relaunchBtn.addSelectionHandler(event -> hasHandlers.fireEvent(new RelaunchAnalysisSelected(analysis)));
            shareBtn.addSelectionHandler(event -> hasHandlers.fireEvent(new ShareAnalysisSelected(Lists.newArrayList(analysis))));
            commentBtn.addSelectionHandler(event -> hasHandlers.fireEvent(new AnalysisCommentSelectedEvent(analysis)));
            renameBtn.addSelectionHandler(event -> hasHandlers.fireEvent(new RenameAnalysisSelected(analysis)));
        }
    }

    void addMenuItems() {
        outputFolderBtn = new MenuItem(appearance.outputFolderText(), appearance.outputFolderIcon());
        parametersBtn = new MenuItem(appearance.parametersText(), appearance.parametersIcon());
        infoBtn = new MenuItem(appearance.infoText(), appearance.infoIcon());
        completeBtn = new MenuItem(appearance.completeText(), appearance.completeIcon());
        cancelBtn = new MenuItem(appearance.cancelText(), appearance.cancelIcon());
        deleteBtn = new MenuItem(appearance.deleteText(), appearance.deleteIcon());
        renameBtn = new MenuItem(appearance.renameText(), appearance.renameIcon());
        relaunchBtn = new MenuItem(appearance.relaunchText(), appearance.relaunchIcon());
        shareBtn = new MenuItem(appearance.shareText(), appearance.shareIcon());
        commentBtn = new MenuItem(appearance.commentText(), appearance.commentIcon());

        add(outputFolderBtn);
        add(parametersBtn);
        add(relaunchBtn);
        add(infoBtn);
        add(completeBtn);
        add(cancelBtn);
        add(deleteBtn);
        add(renameBtn);
        add(commentBtn);
        add(shareBtn);
    }

    void checkButtonStatus(Analysis analysis) {
        completeBtn.setEnabled(canCancel(analysis) && isOwner(analysis));
        cancelBtn.setEnabled(canCancel(analysis) && isOwner(analysis));
        deleteBtn.setEnabled(canDelete(analysis) && isOwner(analysis));
        shareBtn.setEnabled(canShare(analysis) && isOwner(analysis));
    }

    boolean canCancel(Analysis analysis) {
        String status = analysis.getStatus();
        return SUBMITTED.toString().equalsIgnoreCase(status) ||
               IDLE.toString().equalsIgnoreCase(status) ||
               RUNNING.toString().equalsIgnoreCase(status) ||
               (analysis.isBatch() && (analysis.getBatchStatus().getSubmitted() > 0 ||
                                       analysis.getBatchStatus().getRunning() > 0));
    }

    boolean canDelete(Analysis analysis) {
        String status = analysis.getStatus();
        return FAILED.toString().equalsIgnoreCase(status)
              || COMPLETED.toString().equalsIgnoreCase(status)
              || CANCELED.toString().equalsIgnoreCase(status);
    }

    boolean canShare(Analysis analysis) {
        return analysis.isShareable();
    }

    boolean isOwner(Analysis analysis) {
        return analysis.getUserName().equals(userInfo.getFullUsername());
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        outputFolderBtn.setId(baseID + AnalysisModule.Ids.MENUITEM_GO_TO_FOLDER);
        parametersBtn.setId(baseID + AnalysisModule.Ids.MENUITEM_VIEW_PARAMS);
        infoBtn.setId(baseID + AnalysisModule.Ids.MENUITEM_VIEW_ANALYSES_INFO);
        completeBtn.setId(baseID + AnalysisModule.Ids.MENUITEM_COMPLETE);
        cancelBtn.setId(baseID + AnalysisModule.Ids.MENUITEM_CANCEL);
        deleteBtn.setId(baseID + AnalysisModule.Ids.MENUITEM_DELETE);
        renameBtn.setId(baseID + AnalysisModule.Ids.MENUITEM_RENAME);
        relaunchBtn.setId(baseID + AnalysisModule.Ids.MENUITEM_RELAUNCH);
        shareBtn.setId(baseID + AnalysisModule.Ids.SHARE_COLLAB);
        commentBtn.setId(baseID + AnalysisModule.Ids.MENUITEM_UPDATE_COMMENTS);
    }
}
