package org.iplantc.de.collaborators.client.views;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.collaborators.client.ManageCollaboratorsView;
import org.iplantc.de.collaborators.client.events.AddGroupSelected;
import org.iplantc.de.collaborators.client.events.GroupNameSelected;
import org.iplantc.de.collaborators.client.events.RemoveCollaboratorSelected;
import org.iplantc.de.collaborators.client.events.UserSearchResultSelected;
import org.iplantc.de.collaborators.client.models.SubjectKeyProvider;
import org.iplantc.de.collaborators.client.util.UserSearchField;
import org.iplantc.de.collaborators.client.views.cells.SubjectNameCell;
import org.iplantc.de.collaborators.shared.CollaboratorsModule;

import com.google.common.base.Strings;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
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
import com.sencha.gxt.dnd.core.client.DragSource;
import com.sencha.gxt.dnd.core.client.DropTarget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.ViewReadyEvent;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;
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
    @UiField TextButton deleteBtn;
    @UiField TextButton addGroup;
    @UiField Grid<Subject> grid;
    @UiField GridView<Subject> gridView;
    @UiField(provided = true) UserSearchField searchField;
    private CollaboratorDNDHandler dndHandler;
    @UiField ToolBar toolbar;

    @UiField(provided = true) ManageCollaboratorsView.Appearance appearance;

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    private final CheckBoxSelectionModel<Subject> checkBoxModel;
    private String baseID;

    @Inject
    public ManageCollaboratorsViewImpl(ManageCollaboratorsView.Appearance appearance,
                                       UserSearchField searchField,
                                       @Assisted CollaboratorDNDHandler dndHandler) {
        this.appearance = appearance;
        this.searchField = searchField;
        this.dndHandler = dndHandler;
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
    public Subject getSubjectFromElement(Element as) {
        Element row = gridView.findRow(as);
        int dropIndex = gridView.findRowIndex(row);
        if (dropIndex > listStore.size() - 1) {
            return null;
        }
        return listStore.get(dropIndex);
    }

    @Override
    public List<Subject> getSelectedSubjects() {
        return grid.getSelectionModel().getSelectedItems();
    }

    @Override
    public void updateCollabList(Subject group) {
        listStore.update(group);
    }

    @Override
    public void removeCollaboratorsById(List<String> userIds) {
        if (userIds != null && !userIds.isEmpty()) {
            for (String id : userIds) {
                Subject found = listStore.findModelWithKey(id);
                if (found != null) {
                    listStore.remove(found);
                }
            }
        }
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
        super.mask(maskText);
    }

    @Override
    public void onSelectionChanged(SelectionChangedEvent<Subject> event) {
        if (event.getSelection() != null
                && event.getSelection().size() > 0) {
            deleteBtn.enable();
        } else {
            deleteBtn.disable();
        }
    }

    @Override
    public void removeCollaborators(List<Subject> models) {
        if (models != null && !models.isEmpty()) {
            for (Subject c : models) {
                Subject found = listStore.findModel(c);
                if (found != null) {
                    listStore.remove(found);
                }
            }
        }
    }

    @Override
    public void unmaskCollaborators() {
        super.unmask();
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        this.baseID = baseID;
        deleteBtn.ensureDebugId(baseID + CollaboratorsModule.Ids.DELETE);
        addGroup.ensureDebugId(baseID + CollaboratorsModule.Ids.ADD_GROUP);
        //Checkbox column config is at index 0
        grid.getView().getHeader().getHead(0).getElement().setId(baseID + CollaboratorsModule.Ids.CHECKBOX_HEADER);
        searchField.setViewDebugId(baseID + CollaboratorsModule.Ids.SEARCH_LIST);

        for (ColumnConfig<Subject, ?> columnConfig : cm.getColumns()) {
            Cell<?> cell = columnConfig.getCell();
            if (cell instanceof SubjectNameCell) {
                ((SubjectNameCell)cell).setBaseDebugId(baseID);
            }
        }
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

    private void init() {
        grid.getSelectionModel().addSelectionChangedHandler(this);

        DropTarget gridDropTarget = new DropTarget(grid);
        gridDropTarget.setAllowSelfAsSource(true);
        gridDropTarget.addDropHandler(dndHandler);
        gridDropTarget.addDragEnterHandler(dndHandler);
        gridDropTarget.addDragMoveHandler(dndHandler);
        DragSource gridDragSource = new DragSource(grid);
        gridDragSource.addDragStartHandler(dndHandler);
    }

    @Override
    public HandlerRegistration addRemoveCollaboratorSelectedHandler(RemoveCollaboratorSelected.RemoveCollaboratorSelectedHandler handler) {
        return addHandler(handler, RemoveCollaboratorSelected.TYPE);
    }

    @Override
    public HandlerRegistration addAddGroupSelectedHandler(AddGroupSelected.AddGroupSelectedHandler handler) {
        return addHandler(handler, AddGroupSelected.TYPE);
    }

    @Override
    public HandlerRegistration addGroupNameSelectedHandler(GroupNameSelected.GroupNameSelectedHandler handler) {
        return ((CollaboratorsColumnModel)cm).addGroupNameSelectedHandler(handler);
    }

    @Override
    public HandlerRegistration addUserSearchResultSelectedEventHandler(UserSearchResultSelected.UserSearchResultSelectedEventHandler handler) {
        return searchField.addUserSearchResultSelectedEventHandler(handler);
    }
}
