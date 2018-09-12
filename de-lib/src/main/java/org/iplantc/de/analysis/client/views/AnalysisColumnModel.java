package org.iplantc.de.analysis.client.views;

import org.iplantc.de.analysis.client.AnalysesView;
import org.iplantc.de.analysis.client.events.HTAnalysisExpandEvent;
import org.iplantc.de.analysis.client.events.InteractiveIconClicked;
import org.iplantc.de.analysis.client.events.selection.AnalysisAppSelectedEvent;
import org.iplantc.de.analysis.client.events.selection.AnalysisCommentSelectedEvent;
import org.iplantc.de.analysis.client.events.selection.AnalysisJobInfoSelected;
import org.iplantc.de.analysis.client.events.selection.AnalysisNameSelectedEvent;
import org.iplantc.de.analysis.client.events.selection.AnalysisUserSupportRequestedEvent;
import org.iplantc.de.analysis.client.events.selection.CancelAnalysisSelected;
import org.iplantc.de.analysis.client.events.selection.CompleteAnalysisSelected;
import org.iplantc.de.analysis.client.events.selection.DeleteAnalysisSelected;
import org.iplantc.de.analysis.client.events.selection.GoToAnalysisFolderSelected;
import org.iplantc.de.analysis.client.events.selection.RelaunchAnalysisSelected;
import org.iplantc.de.analysis.client.events.selection.RenameAnalysisSelected;
import org.iplantc.de.analysis.client.events.selection.ShareAnalysisSelected;
import org.iplantc.de.analysis.client.events.selection.ViewAnalysisParamsSelected;
import org.iplantc.de.analysis.client.views.cells.AnalysisAppNameCell;
import org.iplantc.de.analysis.client.views.cells.AnalysisDotMenuCell;
import org.iplantc.de.analysis.client.views.cells.AnalysisNameCell;
import org.iplantc.de.analysis.client.views.cells.AnalysisUserSupportCell;
import org.iplantc.de.analysis.client.views.cells.EndDateTimeCell;
import org.iplantc.de.analysis.client.views.cells.StartDateTimeCell;
import org.iplantc.de.client.models.analysis.Analysis;

import com.google.common.collect.Lists;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;

import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import java.util.List;

/**
 * @author jstroot
 */
public class AnalysisColumnModel extends ColumnModel<Analysis> implements
                                                              AnalysisNameSelectedEvent.HasAnalysisNameSelectedEventHandlers,
                                                              AnalysisAppSelectedEvent.HasAnalysisAppSelectedEventHandlers,
                                                              AnalysisCommentSelectedEvent.HasAnalysisCommentSelectedEventHandlers,
                                                              HTAnalysisExpandEvent.HasHTAnalysisExpandEventHandlers,
                                                              AnalysisUserSupportRequestedEvent.HasAnalysisUserSupportRequestedEventHandlers,
                                                              RelaunchAnalysisSelected.HasRelaunchAnalysisSelectedHandlers,
                                                              ShareAnalysisSelected.HasShareAnalysisSelectedHandlers,
                                                              RenameAnalysisSelected.HasRenameAnalysisSelectedHandlers,
                                                              GoToAnalysisFolderSelected.HasGoToAnalysisFolderSelectedHandlers,
                                                              DeleteAnalysisSelected.HasDeleteAnalysisSelectedHandlers,
                                                              CancelAnalysisSelected.HasCancelAnalysisSelectedHandlers,
                                                              CompleteAnalysisSelected.HasCompleteAnalysisSelectedHandlers,
                                                              ViewAnalysisParamsSelected.HasViewAnalysisParamsSelectedHandlers,
                                                              AnalysisJobInfoSelected.HasAnalysisJobInfoSelectedHandlers,
                                                              InteractiveIconClicked.HasInteractiveIconClickedHandlers {

    AnalysisColumnModel(final CheckBoxSelectionModel<Analysis> checkBoxSelectionModel) {
        this(checkBoxSelectionModel,
             GWT.<AnalysesView.Appearance>create(AnalysesView.Appearance.class));
    }

    public AnalysisColumnModel(CheckBoxSelectionModel<Analysis> checkBoxSelectionModel,
                               AnalysesView.Appearance appearance) {
        super(createColumnConfigList(checkBoxSelectionModel, appearance));

        // Set handler managers on appropriate cells so they can fire events.
        for (ColumnConfig<Analysis, ?> cc : configs) {
            if (cc.getCell() instanceof AnalysisNameCell) {
                ((AnalysisNameCell)cc.getCell()).setHasHandlers(ensureHandlers());
            } else if (cc.getCell() instanceof AnalysisAppNameCell) {
                ((AnalysisAppNameCell)cc.getCell()).setHasHandlers(ensureHandlers());
            } else if (cc.getCell() instanceof AnalysisDotMenuCell) {
                ((AnalysisDotMenuCell)cc.getCell()).setHasHandlers(ensureHandlers());
            } else if(cc.getCell() instanceof  AnalysisUserSupportCell) {
                ((AnalysisUserSupportCell)cc.getCell()).setHasHandlers(ensureHandlers());
            }
        }
    }

    public static List<ColumnConfig<Analysis, ?>>
            createColumnConfigList(final CheckBoxSelectionModel<Analysis> checkBoxSelectionModel,
                                   final AnalysesView.Appearance appearance) {
        ColumnConfig<Analysis, Analysis> colCheckBox = checkBoxSelectionModel.getColumn();
        ColumnConfig<Analysis, Analysis> name = new ColumnConfig<>(new IdentityValueProvider<Analysis>("name"),
                                                                   150);
        ColumnConfig<Analysis, Analysis> app = new ColumnConfig<>(new IdentityValueProvider<Analysis>("app_name"),
                                                                  100);
        ColumnConfig<Analysis, Analysis> startDate = new ColumnConfig<>(new IdentityValueProvider<Analysis>("startdate"),
                                                                        125);
        ColumnConfig<Analysis, Analysis> endDate = new ColumnConfig<>(new IdentityValueProvider<Analysis>("enddate"),
                                                                      125);

        ColumnConfig<Analysis, String> username = new ColumnConfig<>(new ValueProvider<Analysis, String>() {

                                                                         @Override
                                                                         public String
                                                                                 getValue(Analysis object) {
                                                                             return object.getUserName();
                                                                         }

                                                                         @Override
                                                                         public void
                                                                                 setValue(Analysis object,
                                                                                          String value) {

                                                                         }

                                                                         @Override
                                                                         public String getPath() {
                                                                             return "username";
                                                                         }
                                                                     },
                                                                     125);
        ColumnConfig<Analysis, Analysis> status = new ColumnConfig<Analysis, Analysis>(new IdentityValueProvider<Analysis>("status"),75);

        ColumnConfig<Analysis, Analysis> dotMenu = new ColumnConfig<Analysis, Analysis>(new IdentityValueProvider<>(""), appearance.dotMenuWidth());

        name.setHeader(appearance.name());
        name.setCell(new AnalysisNameCell());

        username.setHeader("Owner");

        dotMenu.setMenuDisabled(true);
        dotMenu.setCell(new AnalysisDotMenuCell());
        dotMenu.setSortable(false);
        dotMenu.setHeader("");
        dotMenu.setHideable(false);

        app.setHeader(appearance.appName());
        app.setCell(new AnalysisAppNameCell());

        startDate.setCell(new StartDateTimeCell());
        startDate.setHeader(appearance.startDate());

        endDate.setCell(new EndDateTimeCell());
        endDate.setHeader(appearance.endDate());

        status.setHeader(appearance.status());
        status.setMenuDisabled(false);
        status.setCell(new AnalysisUserSupportCell());
        status.setSortable(true);
        status.setHideable(false);

        List<ColumnConfig<Analysis, ?>> ret = Lists.newArrayList();
        ret.add(colCheckBox);
        ret.add(name);
        ret.add(username);
        ret.add(app);
        ret.add(startDate);
        ret.add(endDate);
        ret.add(status);
        ret.add(dotMenu);
        return ret;
    }

    @Override
    public HandlerRegistration
            addAnalysisAppSelectedEventHandler(AnalysisAppSelectedEvent.AnalysisAppSelectedEventHandler handler) {
        return ensureHandlers().addHandler(AnalysisAppSelectedEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration
            addAnalysisCommentSelectedEventHandler(AnalysisCommentSelectedEvent.AnalysisCommentSelectedEventHandler handler) {
        return ensureHandlers().addHandler(AnalysisCommentSelectedEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration
            addAnalysisNameSelectedEventHandler(AnalysisNameSelectedEvent.AnalysisNameSelectedEventHandler handler) {
        return ensureHandlers().addHandler(AnalysisNameSelectedEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration
            addHTAnalysisExpandEventHandler(HTAnalysisExpandEvent.HTAnalysisExpandEventHandler handler) {
        return ensureHandlers().addHandler(HTAnalysisExpandEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration addAnalysisUserSupportRequestedEventHandler(
            AnalysisUserSupportRequestedEvent.AnalysisUserSupportRequestedEventHandler handler) {
        return ensureHandlers().addHandler(AnalysisUserSupportRequestedEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration addRelaunchAnalysisSelectedHandler(RelaunchAnalysisSelected.RelaunchAnalysisSelectedHandler handler) {
        return ensureHandlers().addHandler(RelaunchAnalysisSelected.TYPE, handler);
    }

    @Override
    public HandlerRegistration addShareAnalysisSelectedHandler(ShareAnalysisSelected.ShareAnalysisSelectedHandler handler) {
        return ensureHandlers().addHandler(ShareAnalysisSelected.TYPE, handler);
    }

    @Override
    public HandlerRegistration addAnalysisJobInfoSelectedHandler(AnalysisJobInfoSelected.AnalysisJobInfoSelectedHandler handler) {
        return ensureHandlers().addHandler(AnalysisJobInfoSelected.TYPE, handler);
    }

    @Override
    public HandlerRegistration addCancelAnalysisSelectedHandler(CancelAnalysisSelected.CancelAnalysisSelectedHandler handler) {
        return ensureHandlers().addHandler(CancelAnalysisSelected.TYPE, handler);
    }

    @Override
    public HandlerRegistration addCompleteAnalysisSelectedHandler(CompleteAnalysisSelected.CompleteAnalysisSelectedHandler handler) {
        return ensureHandlers().addHandler(CompleteAnalysisSelected.TYPE, handler);
    }

    @Override
    public HandlerRegistration addDeleteAnalysisSelectedHandler(DeleteAnalysisSelected.DeleteAnalysisSelectedHandler handler) {
        return ensureHandlers().addHandler(DeleteAnalysisSelected.TYPE, handler);
    }

    @Override
    public HandlerRegistration addGoToAnalysisFolderSelectedHandler(GoToAnalysisFolderSelected.GoToAnalysisFolderSelectedHandler handler) {
        return ensureHandlers().addHandler(GoToAnalysisFolderSelected.TYPE, handler);
    }

    @Override
    public HandlerRegistration addRenameAnalysisSelectedHandler(RenameAnalysisSelected.RenameAnalysisSelectedHandler handler) {
        return ensureHandlers().addHandler(RenameAnalysisSelected.TYPE, handler);
    }

    @Override
    public HandlerRegistration addViewAnalysisParamsSelectedHandler(ViewAnalysisParamsSelected.ViewAnalysisParamsSelectedHandler handler) {
        return ensureHandlers().addHandler(ViewAnalysisParamsSelected.TYPE, handler);
    }

    @Override
    public HandlerRegistration addInteractiveIconClickedHandler(InteractiveIconClicked.InteractiveIconClickedHandler handler) {
        return ensureHandlers().addHandler(InteractiveIconClicked.TYPE, handler);
    }

    public void ensureDebugId(String baseID) {
        for (ColumnConfig<Analysis, ?> cc : configs) {
            Cell<?> cell = cc.getCell();
            if (cell instanceof AnalysisDotMenuCell) {
                ((AnalysisDotMenuCell)cell).setBaseDebugId(baseID);
            } else if (cell instanceof AnalysisNameCell) {
                ((AnalysisNameCell)cell).setBaseDebugId(baseID);
            } else if (cell instanceof AnalysisAppNameCell) {
                ((AnalysisAppNameCell)cell).setBaseDebugId(baseID);
            } else if (cell instanceof AnalysisUserSupportCell) {
                ((AnalysisUserSupportCell)cell).setBaseDebugId(baseID);
            }
        }
    }
}
