package org.iplantc.de.analysis.client.views;

import org.iplantc.de.analysis.client.AnalysesView;
import org.iplantc.de.analysis.client.ReactAnalyses;
import org.iplantc.de.analysis.client.views.widget.AnalysisSearchField;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.commons.client.util.CyVerseReactComponents;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * @author sriram, jstroot
 */
public class AnalysesViewImpl implements AnalysesView/*,
                                         AnalysisCommentSelectedEvent.AnalysisCommentSelectedEventHandler,
                                         SelectionChangedHandler<Analysis> */ {

    private String parentAnalysisId;

/*    @UiField ColumnModel<Analysis> cm;
    @UiField(provided = true) final ListStore<Analysis> listStore;
    @UiField(provided = true) final AnalysisToolBarView toolBar;
    @UiField Appearance appearance;
    @UiField Grid<Analysis> grid;
    @UiField LiveGridView<Analysis> gridView;
    @UiField ToolBar pagingToolBar;
    @UiField Status selectionStatus;
    CheckBoxSelectionModel<Analysis> checkBoxModel;
    private AnalysisColumnModel acm;*/

    AnalysisSearchField searchField;
    HTMLPanel panel;
    Presenter presenter;

    @Inject
    AnalysesViewImpl(/*final AnalysisToolBarFactory toolBarFactory,
                     @Assisted final ListStore<Analysis> listStore,
                     @Assisted final PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Analysis>> loader*/) {
        panel = new HTMLPanel("<div></div>");
/*        this.listStore = listStore;
        this.toolBar = toolBarFactory.create(loader);
        checkBoxModel = new CheckBoxSelectionModel<>(new IdentityValueProvider<Analysis>());
        
        this.acm = (AnalysisColumnModel) cm;

        pagingToolBar.addStyleName(appearance.pagingToolbarStyle());
        pagingToolBar.setBorders(false);

        // Init Grid
        grid.setLoader(loader);
        grid.setLoadMask(true);
        grid.setSelectionModel(checkBoxModel);
        checkBoxModel.setSelectionMode(Style.SelectionMode.MULTI);

        // Init Toolbar
        pagingToolBar.insert(new LiveToolItem(grid), 0);
        setSelectionCount(0);

        // Wire up eventHandlers
     //   grid.getSelectionModel().addSelectionChangedHandler(this);
        grid.getSelectionModel().addSelectionChangedHandler(toolBar);
        this.searchField = toolBar.getSearchField();*/

    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void load() {
        ReactAnalyses.AnalysesProps props = new ReactAnalyses.AnalysesProps();
        props.presenter = this.presenter;
        props.username = UserInfo.getInstance().getFullUsername();
        CyVerseReactComponents.render(ReactAnalyses.AnalysesView, props, panel.getElement());
    }

/*    @UiFactory
    LiveGridView<Analysis> createLiveGridView() {
        LiveGridView<Analysis> liveGridView = new LiveGridView<Analysis>(){
            @Override
            protected void insertRows(int firstRow, int lastRow, boolean isUpdate) {
                super.insertRows(firstRow, lastRow, isUpdate);

                setRowHeight(appearance.liveGridRowHeight());
            }
        };

        liveGridView.setAutoFill(true);
        liveGridView.setForceFit(true);

        return liveGridView;
    }*/



/*    //<editor-fold desc="Handler Registrations">
    @Override
    public HandlerRegistration addAnalysisAppSelectedEventHandler(AnalysisAppSelectedEvent.AnalysisAppSelectedEventHandler handler) {
        return ((AnalysisColumnModel) cm).addAnalysisAppSelectedEventHandler(handler);
    }

    @Override
    public HandlerRegistration addAnalysisNameSelectedEventHandler(AnalysisNameSelectedEvent.AnalysisNameSelectedEventHandler handler) {
        return ((AnalysisColumnModel) cm).addAnalysisNameSelectedEventHandler(handler);
    }

    @Override
    public HandlerRegistration addAnalysisUserSupportRequestedEventHandler(AnalysisUserSupportRequestedEvent.AnalysisUserSupportRequestedEventHandler handler){
        return ((AnalysisColumnModel)cm).addAnalysisUserSupportRequestedEventHandler(handler);
    }

    @Override
    public HandlerRegistration addHTAnalysisExpandEventHandler(HTAnalysisExpandEventHandler handler) {
        return ((AnalysisColumnModel) cm).addHTAnalysisExpandEventHandler(handler);
    }
    //</editor-fold>*/

/*
    @UiFactory
    ColumnModel<Analysis> createColumnModel() {
        AnalysisColumnModel columnModel = new AnalysisColumnModel(checkBoxModel);
    //    columnModel.addAnalysisCommentSelectedEventHandler(this);
        return columnModel;
    }
*/

/*    @Override
    public void filterByAnalysisId(String analysisId, String name) {
        toolBar.filterByAnalysisId(analysisId, name);
    }

    @Override
    public void filterByParentAnalysisId(String id) {
        this.parentAnalysisId = id;
        toolBar.filterByParentAnalysisId(id);
    }*/

/*    @Override
    public List<Analysis> getSelectedAnalyses() {
        return grid.getSelectionModel().getSelectedItems();
    }*/

   /* @Override
    public void onAnalysisCommentSelected(final AnalysisCommentSelectedEvent event) {
        fireEvent(new AnalysisCommentUpdate(event.getValue()));
    }

    @Override
    public void onSelectionChanged(SelectionChangedEvent<Analysis> event) {
        setSelectionCount(event.getSelection().size());
    }*/

/*    @Override
    public void setSelectedAnalyses(List<Analysis> selectedAnalyses) {
        if (selectedAnalyses != null) {
            grid.getSelectionModel().setSelection(selectedAnalyses);

            if (!selectedAnalyses.isEmpty()) {
                grid.getView().ensureVisible(listStore.indexOf(selectedAnalyses.get(0)), 0, false);
            }
        }
    }

    @Override
    public void setPermFilterInView(AnalysisPermissionFilter permFilter, AppTypeFilter typeFilter) {
        toolBar.setFilterInView(permFilter, typeFilter);
    }*/

/*    @Override
    public String getParentAnalysisId() {
        return parentAnalysisId;
    }*/

   /* @Override
    public AnalysisSearchField getSearchField() {
        return searchField;
    }
*/

   /* @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        toolBar.asWidget().ensureDebugId(baseID + AnalysisModule.Ids.MENUBAR);
        grid.ensureDebugId(baseID + AnalysisModule.Ids.GRID);
        acm.ensureDebugId(baseID + AnalysisModule.Ids.GRID);
    }*/

/*    private void setSelectionCount(int count) {
        selectionStatus.setText(appearance.selectionCount(count));
    }*/

/*    @Override
    public AnalysisToolBarView getToolBarView() {
        return toolBar;
    }*/

    /* @Override
    public HandlerRegistration addAnalysisCommentUpdateHandler(AnalysisCommentUpdate.AnalysisCommentUpdateHandler handler) {
        return addHandler(handler, AnalysisCommentUpdate.TYPE);
    }

    @Override
    public HandlerRegistration addRelaunchAnalysisSelectedHandler(RelaunchAnalysisSelected.RelaunchAnalysisSelectedHandler handler) {
        return acm.addRelaunchAnalysisSelectedHandler(handler);
    }

    @Override
    public HandlerRegistration addShareAnalysisSelectedHandler(ShareAnalysisSelected.ShareAnalysisSelectedHandler handler) {
        return acm.addShareAnalysisSelectedHandler(handler);
    }

    @Override
    public HandlerRegistration addAnalysisJobInfoSelectedHandler(AnalysisJobInfoSelected.AnalysisJobInfoSelectedHandler handler) {
        return acm.addAnalysisJobInfoSelectedHandler(handler);
    }

    @Override
    public HandlerRegistration addCancelAnalysisSelectedHandler(CancelAnalysisSelected.CancelAnalysisSelectedHandler handler) {
        return acm.addCancelAnalysisSelectedHandler(handler);
    }

    @Override
    public HandlerRegistration addCompleteAnalysisSelectedHandler(CompleteAnalysisSelected.CompleteAnalysisSelectedHandler handler) {
        return acm.addCompleteAnalysisSelectedHandler(handler);
    }

    @Override
    public HandlerRegistration addDeleteAnalysisSelectedHandler(DeleteAnalysisSelected.DeleteAnalysisSelectedHandler handler) {
        return acm.addDeleteAnalysisSelectedHandler(handler);
    }

    @Override
    public HandlerRegistration addGoToAnalysisFolderSelectedHandler(GoToAnalysisFolderSelected.GoToAnalysisFolderSelectedHandler handler) {
        return acm.addGoToAnalysisFolderSelectedHandler(handler);
    }

    @Override
    public HandlerRegistration addRenameAnalysisSelectedHandler(RenameAnalysisSelected.RenameAnalysisSelectedHandler handler) {
        return acm.addRenameAnalysisSelectedHandler(handler);
    }

    @Override
    public HandlerRegistration addViewAnalysisParamsSelectedHandler(ViewAnalysisParamsSelected.ViewAnalysisParamsSelectedHandler handler) {
        return acm.addViewAnalysisParamsSelectedHandler(handler);

    }

    @Override
    public HandlerRegistration addInteractiveIconClickedHandler(InteractiveIconClicked.InteractiveIconClickedHandler handler) {
        return acm.addInteractiveIconClickedHandler(handler);
    }
    }*/
}
