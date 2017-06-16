package org.iplantc.de.collaborators.client.views;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.collaborators.client.GroupView;
import org.iplantc.de.collaborators.client.ManageCollaboratorsView;
import org.iplantc.de.collaborators.client.events.AddGroupSelected;
import org.iplantc.de.collaborators.client.events.DeleteGroupSelected;
import org.iplantc.de.collaborators.client.events.GroupNameSelected;
import org.iplantc.de.collaborators.client.events.RemoveCollaboratorSelected;
import org.iplantc.de.collaborators.client.events.UserSearchResultSelected;
import org.iplantc.de.collaborators.client.models.SubjectKeyProvider;
import org.iplantc.de.collaborators.client.util.UserSearchField;
import org.iplantc.de.collaborators.shared.CollaboratorsModule;
import org.iplantc.de.commons.client.widgets.DETabPanel;

import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.ViewReadyEvent;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import java.util.List;

/**
 * @author jstroot
 */
public class ManageCollaboratorsViewImpl extends Composite implements ManageCollaboratorsView,
                                                                      SelectionChangedHandler<Subject> {

    @UiTemplate("ManageCollaboratorsView.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, ManageCollaboratorsViewImpl> {
    }
    @UiField ColumnModel<Subject> cm;
    @UiField ListStore<Subject> listStore;
    @UiField BorderLayoutContainer con;
    @UiField TextButton deleteBtn;
    @UiField TextButton addGroup;
    @UiField TextButton deleteGroup;
    @UiField Grid<Subject> grid;
    @UiField TextButton manageBtn;
    @UiField(provided = true) UserSearchField searchField;
    @UiField HorizontalLayoutContainer searchPanel;
    @UiField ToolBar toolbar;
    private DETabPanel tabPanel;
    @UiField FramedPanel collaboratorListPnl;
    private GroupView groupView;

    @UiField(provided = true) ManageCollaboratorsView.Appearance appearance;

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    private final CheckBoxSelectionModel<Subject> checkBoxModel;
    private MODE mode;
    private String baseID;

    @Inject
    public ManageCollaboratorsViewImpl(@Assisted final MODE mode,
                                       ManageCollaboratorsView.Appearance appearance,
                                       GroupView groupView,
                                       UserSearchField searchField) {
        this.appearance = appearance;
        this.groupView = groupView;
        this.searchField = searchField;
        checkBoxModel = new CheckBoxSelectionModel<>(new IdentityValueProvider<Subject>());
        initWidget(uiBinder.createAndBindUi(this));

        grid.setSelectionModel(checkBoxModel);
        checkBoxModel.setSelectionMode(SelectionMode.MULTI);
        grid.getView().setEmptyText(appearance.noCollaborators());
        grid.addViewReadyHandler(new ViewReadyEvent.ViewReadyHandler() {
            @Override
            public void onViewReady(ViewReadyEvent event) {
                setGridCheckBoxDebugIds();
            }
        });
        init();
        setMode(mode);
    }

    @Override
    public void addCollaborators(List<Subject> models) {
        listStore.addAll(models);
        setGridCheckBoxDebugIds();
    }

    @Override
    public List<Subject> getCollaborators() {
        return listStore.getAll();
    }

    @Override
    public boolean hasCollaboratorsTabSelected() {
        return tabPanel.getActiveWidget() != groupView;
    }

    @Override
    public MODE getMode() {
        return mode;
    }

    @Override
    public List<Subject> getSelectedSubjects() {
        List<Group> selectedCollabLists = groupView.getSelectedCollabLists();
        List<Subject> selectedCollaborators = grid.getSelectionModel().getSelectedItems();
        return Lists.newArrayList(Iterables.concat(selectedCollabLists, selectedCollaborators));
    }

    @Override
    public void addCollabLists(List<Group> result) {
        groupView.addCollabLists(result);
    }

    @Override
    public void removeCollabList(Group result) {
        groupView.removeCollabList(result);
    }

    @Override
    public void maskCollabLists(String loadingMask) {
        groupView.mask(loadingMask);
    }

    @Override
    public void unmaskCollabLists() {
        groupView.unmask();
    }
    
    @Override
    public void updateCollabList(Group group) {
        groupView.updateCollabList(group);
    }

    @Override
    public List<Group> getSelectedCollaboratorLists() {
        return groupView.getSelectedCollabLists();
    }

    @Override
    public void loadData(List<Subject> models) {
        listStore.clear();
        listStore.addAll(models);
    }

    @Override
    public void maskCollaborators(String maskText) {
        if (Strings.isNullOrEmpty(maskText)) {
            maskText = appearance.loadingMask();
        }
        collaboratorListPnl.mask(maskText);
    }

    @Override
    public void onSelectionChanged(SelectionChangedEvent<Subject> event) {
        if (event.getSelection() != null
                && event.getSelection().size() > 0
                && MODE.MANAGE.equals(mode)) {
            deleteBtn.enable();
        } else {
            deleteBtn.disable();
        }
    }

    @Override
    public void removeCollaborators(List<Subject> models) {
        if (models != null && !models.isEmpty()) {
            for (Subject c : models) {
                if (listStore.findModel(c) != null) {
                    listStore.remove(c);
                }
            }
        }
    }

    @Override
    public void setMode(MODE mode) {
        this.mode = mode;
        groupView.setMode(mode);
        switch (mode) {
            case MANAGE:
                grid.getView().setEmptyText(appearance.noCollaborators());
                manageBtn.setVisible(false);
                searchField.asWidget().setVisible(true);
                toolbar.setVisible(true);
                break;
            case SELECT:
                grid.getView().setEmptyText(appearance.noCollaborators());
                manageBtn.setVisible(true);
                searchField.asWidget().setVisible(false);
                toolbar.setVisible(false);
                break;
        }
        toolbar.forceLayout();
    }

    @Override
    public void unmaskCollaborators() {
        collaboratorListPnl.unmask();
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        this.baseID = baseID;
        deleteBtn.ensureDebugId(baseID + CollaboratorsModule.Ids.DELETE);
        addGroup.ensureDebugId(baseID + CollaboratorsModule.Ids.ADD_GROUP);
        deleteGroup.ensureDebugId(baseID + CollaboratorsModule.Ids.DELETE_GROUP);
        //Checkbox column config is at index 0
        grid.getView().getHeader().getHead(0).getElement().setId(baseID + CollaboratorsModule.Ids.CHECKBOX_HEADER);
        searchField.setViewDebugId(CollaboratorsModule.Ids.SEARCH_LIST);
        groupView.asWidget().ensureDebugId(baseID + CollaboratorsModule.Ids.GROUPS_VIEW);
    }

    void setGridCheckBoxDebugIds() {
        for (int i = 0; i < listStore.size(); i++) {
            grid.getView().getCell(i, 0).setId(baseID + CollaboratorsModule.Ids.CHECKBOX_ITEM + i);
        }
    }

    @UiFactory
    ListStore<Subject> createListStore() {
        return new ListStore<>(new SubjectKeyProvider());
    }

    @UiFactory
    ColumnModel<Subject> buildColumnModel() {
        return new CollaboratorsColumnModel(checkBoxModel);
    }

    @UiHandler("deleteBtn")
    void deleteCollaborator(SelectEvent event) {
        fireEvent(new RemoveCollaboratorSelected(grid.getSelectionModel().getSelectedItems()));
    }

    @UiHandler("addGroup")
    void addGroupSelected(SelectEvent event) {
        fireEvent(new AddGroupSelected());
    }

    @UiHandler("deleteGroup")
    void deleteGroupSelected(SelectEvent event) {
//        fireEvent(new DeleteGroupSelected(grid.getSelectionModel().getSelectedItem()));
    }

    @UiHandler("manageBtn")
    void manageCollaborators(SelectEvent event) {
        setMode(MODE.MANAGE);
    }

    private void init() {
        grid.getSelectionModel().addSelectionChangedHandler(this);
    }

    @Override
    public HandlerRegistration addRemoveCollaboratorSelectedHandler(RemoveCollaboratorSelected.RemoveCollaboratorSelectedHandler handler) {
        return addHandler(handler, RemoveCollaboratorSelected.TYPE);
    }

    @Override
    public HandlerRegistration addDeleteGroupSelectedHandler(DeleteGroupSelected.DeleteGroupSelectedHandler handler) {
        return addHandler(handler, DeleteGroupSelected.TYPE);
    }

    @Override
    public HandlerRegistration addAddGroupSelectedHandler(AddGroupSelected.AddGroupSelectedHandler handler) {
        return addHandler(handler, AddGroupSelected.TYPE);
    }

    @Override
    public HandlerRegistration addGroupNameSelectedHandler(GroupNameSelected.GroupNameSelectedHandler handler) {
        return groupView.addGroupNameSelectedHandler(handler);
    }

    @Override
    public HandlerRegistration addUserSearchResultSelectedEventHandler(UserSearchResultSelected.UserSearchResultSelectedEventHandler handler) {
        return searchField.addUserSearchResultSelectedEventHandler(handler);
    }
}
