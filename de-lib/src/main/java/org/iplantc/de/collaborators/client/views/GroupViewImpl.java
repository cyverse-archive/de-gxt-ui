package org.iplantc.de.collaborators.client.views;

import org.iplantc.de.client.models.collaborators.Collaborator;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.collaborators.client.GroupView;
import org.iplantc.de.collaborators.client.events.DeleteGroupSelected;
import org.iplantc.de.collaborators.client.events.GroupNameSelected;
import org.iplantc.de.collaborators.client.events.GroupSaved;
import org.iplantc.de.collaborators.client.models.GroupProperties;
import org.iplantc.de.collaborators.client.views.cells.GroupNameCell;
import org.iplantc.de.collaborators.client.views.dialogs.GroupDetailsDialog;
import org.iplantc.de.collaborators.shared.CollaboratorsModule;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import java.util.List;

/**
 * @author aramsey
 */
public class GroupViewImpl extends Composite implements GroupView {

    interface GroupViewImplUiBinder extends UiBinder<Widget, GroupViewImpl> {}

    private static GroupViewImplUiBinder uiBinder = GWT.create(GroupViewImplUiBinder.class);

    @UiField ToolBar toolBar;
    @UiField TextButton addGroup;
    @UiField TextButton deleteGroup;
    @UiField Grid<Group> grid;
    @UiField ListStore<Group> listStore;
    @UiField ColumnModel<Group> cm;
    @UiField(provided = true) GroupViewAppearance appearance;

    @Inject AsyncProviderWrapper<GroupDetailsDialog> groupDetailsDialog;

    private final GroupProperties props;

    @Inject
    public GroupViewImpl(GroupViewAppearance appearance,
                         GroupProperties props) {
        this.appearance = appearance;
        this.props = props;

        initWidget(uiBinder.createAndBindUi(this));
        grid.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        grid.getView().setEmptyText(appearance.noCollabLists());
    }

    @UiFactory
    ColumnModel<Group> createColumnModel() {
        List<ColumnConfig<Group, ?>> columns = Lists.newArrayList();

        ColumnConfig<Group, Group> nameCol = new ColumnConfig<>(new IdentityValueProvider<Group>("name"),
                                                                appearance.nameColumnWidth(),
                                                                appearance.nameColumnLabel());
        ColumnConfig<Group, String> descriptionCol = new ColumnConfig<>(props.description(),
                                                                        appearance.descriptionColumnWidth(),
                                                                        appearance.descriptionColumnLabel());
        GroupNameCell nameCell = new GroupNameCell();
        nameCell.addGroupNameSelectedHandler(this);
        nameCol.setCell(nameCell);
        columns.add(nameCol);
        columns.add(descriptionCol);
        return new ColumnModel<>(columns);
    }

    @UiFactory
    ListStore<Group> createListStore() {
        return new ListStore<>(props.id());
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        addGroup.ensureDebugId(baseID + CollaboratorsModule.Ids.ADD_GROUP);
        deleteGroup.ensureDebugId(baseID + CollaboratorsModule.Ids.DELETE_GROUP);
        grid.ensureDebugId(baseID + CollaboratorsModule.Ids.GRID);
    }

    @Override
    public void addCollabLists(List<Group> result) {
        listStore.addAll(result);
    }

    @Override
    public void removeCollabList(Group result) {
        listStore.remove(result);
    }

    @Override
    public void editCollabList(Group group, List<Collaborator> members) {

    }

    @UiHandler("addGroup")
    void addGroupSelected(SelectEvent event) {
        groupDetailsDialog.get(new AsyncCallback<GroupDetailsDialog>() {
            @Override
            public void onFailure(Throwable caught) {}

            @Override
            public void onSuccess(GroupDetailsDialog result) {
                result.show();
                result.addGroupSavedHandler(new GroupSaved.GroupSavedHandler() {
                    @Override
                    public void onGroupSaved(GroupSaved event) {
                        List<Group> groups = event.getGroups();
                        listStore.addAll(groups);
                    }
                });
            }
        });
    }

    @UiHandler("deleteGroup")
    void deleteGroupSelected(SelectEvent event) {
        Group group = grid.getSelectionModel().getSelectedItem();
        if (group == null) {
            return;
        }
        ConfirmMessageBox deleteAlert = new ConfirmMessageBox(appearance.deleteGroupConfirmHeading(group),
                                                            appearance.deleteGroupConfirm(group));
        deleteAlert.show();
        deleteAlert.addDialogHideHandler(new DialogHideEvent.DialogHideHandler() {
            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    fireEvent(new DeleteGroupSelected(group));
                }
            }
        });
    }

    @Override
    public void onGroupNameSelected(GroupNameSelected event) {
        Group group = event.getGroup();
        groupDetailsDialog.get(new AsyncCallback<GroupDetailsDialog>() {
            @Override
            public void onFailure(Throwable caught) {}

            @Override
            public void onSuccess(GroupDetailsDialog result) {
                result.show(group);
            }
        });
    }

    @Override
    public HandlerRegistration addDeleteGroupSelectedHandler(DeleteGroupSelected.DeleteGroupSelectedHandler handler) {
        return addHandler(handler, DeleteGroupSelected.TYPE);
    }
}
