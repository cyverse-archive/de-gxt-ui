package org.iplantc.de.collaborators.client.views;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.collaborators.client.GroupDetailsView;
import org.iplantc.de.collaborators.client.GroupView;
import org.iplantc.de.collaborators.client.events.AddGroupMemberSelected;
import org.iplantc.de.collaborators.client.events.DeleteMembersSelected;
import org.iplantc.de.collaborators.client.events.UserSearchResultSelected;
import org.iplantc.de.collaborators.client.models.SubjectKeyProvider;
import org.iplantc.de.collaborators.client.util.UserSearchField;
import org.iplantc.de.collaborators.client.views.cells.SubjectNameCell;
import org.iplantc.de.collaborators.shared.CollaboratorsModule;
import org.iplantc.de.commons.client.validators.GroupNameValidator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.ViewReadyEvent;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import java.util.List;


/**
 * @author aramsey
 */

public class GroupDetailsViewImpl extends Composite implements GroupDetailsView,
                                                               Editor<Group>,
                                                               UserSearchResultSelected.UserSearchResultSelectedEventHandler {

    interface GroupDetailsViewImplUiBinder extends UiBinder<Widget, GroupDetailsViewImpl> {
    }

    interface EditorDriver extends SimpleBeanEditorDriver<Group, GroupDetailsViewImpl> {}

    final EditorDriver editorDriver = GWT.create(EditorDriver.class);
    static GroupDetailsViewImplUiBinder uiBinder = GWT.create(GroupDetailsViewImplUiBinder.class);

    @UiField @Ignore FieldLabel groupNameLabel;
    @UiField @Ignore FieldLabel groupDescLabel;
    @UiField TextField nameEditor;
    @UiField TextArea descriptionEditor;
    @UiField(provided = true) UserSearchField searchField;
    @UiField ToolBar toolbar;
    @UiField @Ignore TextButton deleteBtn;
    @UiField ListStore<Subject> listStore;
    @UiField Grid<Subject> grid;
    @UiField ColumnModel<Subject> cm;
    @UiField(provided = true) GroupView.GroupViewAppearance appearance;

    private CheckBoxSelectionModel<Subject> checkBoxModel;
    String baseID;
    private MODE mode;

    @Inject
    public GroupDetailsViewImpl(GroupView.GroupViewAppearance appearance,
                                UserSearchField searchField) {
        this.appearance = appearance;
        this.searchField = searchField;
        checkBoxModel = new CheckBoxSelectionModel<>(new IdentityValueProvider<Subject>());
        initWidget(uiBinder.createAndBindUi(this));

        groupNameLabel.setHTML(appearance.groupNameLabel());
        nameEditor.addValidator(new GroupNameValidator());

        searchField.addUserSearchResultSelectedEventHandler(this);
        checkBoxModel.setSelectionMode(Style.SelectionMode.MULTI);
        grid.setSelectionModel(checkBoxModel);
        grid.getView().setEmptyText(appearance.noCollaborators());
        grid.addViewReadyHandler(new ViewReadyEvent.ViewReadyHandler() {
            @Override
            public void onViewReady(ViewReadyEvent event) {
                setGridCheckBoxDebugIds();
            }
        });
        grid.getSelectionModel().addSelectionChangedHandler(new SelectionChangedEvent.SelectionChangedHandler<Subject>() {
            @Override
            public void onSelectionChanged(SelectionChangedEvent<Subject> event) {
                deleteBtn.setEnabled(!event.getSelection().isEmpty());
            }
        });

        editorDriver.initialize(this);
    }

    @Override
    public void onUserSearchResultSelected(UserSearchResultSelected userSearchResultSelected) {
        Subject subject = userSearchResultSelected.getSubject();
        if (MODE.EDIT == mode) {
            mask();
            fireEvent(new AddGroupMemberSelected(getGroup(), subject));
        } else {
            listStore.add(subject);
        }
    }

    @UiHandler("deleteBtn")
    void onDeleteButtonSelected(SelectEvent event) {
        List<Subject> selectedItems = grid.getSelectionModel().getSelectedItems();
        if (selectedItems != null && !selectedItems.isEmpty()) {
            fireEvent(new DeleteMembersSelected(getGroup(), selectedItems));
        }
    }

    @UiFactory
    ListStore<Subject> createListStore() {
        return new ListStore<Subject>(new SubjectKeyProvider());
    }

    @UiFactory
    ColumnModel<Subject> buildColumnModel() {
        return new CollaboratorsColumnModel(checkBoxModel);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        this.baseID = baseID;

        groupNameLabel.ensureDebugId(baseID + CollaboratorsModule.Ids.GROUP_NAME_LABEL);
        groupDescLabel.ensureDebugId(baseID + CollaboratorsModule.Ids.GROUP_DESC_LABEL);
        nameEditor.ensureDebugId(baseID + CollaboratorsModule.Ids.GROUP_NAME);
        descriptionEditor.ensureDebugId(baseID + CollaboratorsModule.Ids.GROUP_DESC);
        searchField.asWidget().ensureDebugId(baseID + CollaboratorsModule.Ids.SEARCH_LIST);
        toolbar.ensureDebugId(baseID + CollaboratorsModule.Ids.GROUP_TOOLBAR);
        deleteBtn.ensureDebugId(baseID + CollaboratorsModule.Ids.GROUP_DELETE_BTN);
        grid.ensureDebugId(baseID + CollaboratorsModule.Ids.GROUP_GRID);

        for (ColumnConfig<Subject, ?> cc : cm.getColumns()) {
            if (cc.getCell() instanceof SubjectNameCell) {
                ((SubjectNameCell)cc.getCell()).setBaseDebugId(baseID);
            }
        }
    }

    void setGridCheckBoxDebugIds() {
        for (int i = 0; i < listStore.size(); i++) {
            grid.getView().getCell(i, 0).setId(baseID + CollaboratorsModule.Ids.CHECKBOX_ITEM + i);
        }
    }

    @Override
    public void edit(Group group, MODE mode) {
        this.mode = mode;
        editorDriver.edit(group);
    }

    public Group getGroup() {
        return editorDriver.flush();
    }

    @Override
    public boolean isValid() {
        return nameEditor.isValid();
    }

    @Override
    public List<Subject> getMembers() {
        return listStore.getAll();
    }

    @Override
    public void addMembers(List<Subject> members) {
        if (members != null) {
            listStore.addAll(members);
            setGridCheckBoxDebugIds();
        }
    }

    @Override
    public void deleteMembers(List<Subject> members) {
        if (members != null) {
            members.forEach(subject -> listStore.remove(subject));
        }
    }

    @Override
    public HandlerRegistration addAddGroupMemberSelectedHandler(AddGroupMemberSelected.AddGroupMemberSelectedHandler handler) {
        return addHandler(handler, AddGroupMemberSelected.TYPE);
    }

    @Override
    public HandlerRegistration addDeleteMembersSelectedHandler(DeleteMembersSelected.DeleteMembersSelectedHandler handler) {
        return addHandler(handler, DeleteMembersSelected.TYPE);
    }
}
