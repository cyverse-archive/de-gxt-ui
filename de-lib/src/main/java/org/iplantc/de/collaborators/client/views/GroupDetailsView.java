package org.iplantc.de.collaborators.client.views;

import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.collaborators.Collaborator;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.collaborators.client.GroupView;
import org.iplantc.de.collaborators.client.events.UserSearchResultSelected;
import org.iplantc.de.collaborators.client.models.CollaboratorKeyProvider;
import org.iplantc.de.collaborators.client.util.UserSearchField;
import org.iplantc.de.collaborators.shared.CollaboratorsModule;

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
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import java.util.List;


/**
 * @author aramsey
 */
public class GroupDetailsView extends Composite implements Editor<Group> {

    interface GroupDetailsViewUiBinder extends UiBinder<Widget, GroupDetailsView> {
    }

    interface EditorDriver extends SimpleBeanEditorDriver<Group, GroupDetailsView> {}

    private class CollaboratorSelectedHandler
            implements UserSearchResultSelected.UserSearchResultSelectedEventHandler {
        @Override
        public void onUserSearchResultSelected(UserSearchResultSelected userSearchResultSelected) {
            if (UserSearchResultSelected.USER_SEARCH_EVENT_TAG.GROUP.toString().equals(userSearchResultSelected.getTag())) {
                listStore.add(userSearchResultSelected.getCollaborator());
            }
        }
    }

    final EditorDriver editorDriver = GWT.create(EditorDriver.class);
    static GroupDetailsViewUiBinder uiBinder = GWT.create(GroupDetailsViewUiBinder.class);

    @UiField @Ignore FieldLabel groupNameLabel;
    @UiField @Ignore FieldLabel groupDescLabel;
    @UiField TextField nameEditor;
    @UiField TextArea descriptionEditor;
    @UiField(provided = true) UserSearchField searchField;
    @UiField ToolBar toolbar;
    @UiField @Ignore TextButton deleteBtn;
    @UiField ListStore<Collaborator> listStore;
    @UiField Grid<Collaborator> grid;
    @UiField ColumnModel<Collaborator> cm;
    @UiField(provided = true) GroupView.GroupViewAppearance appearance;

    EventBus eventBus;
    HandlerRegistration handlerRegistration;

    private CheckBoxSelectionModel<Collaborator> checkBoxModel;
    String baseID;

    @Inject
    public GroupDetailsView(GroupView.GroupViewAppearance appearance,
                            EventBus eventBus) {
        this.appearance = appearance;
        this.eventBus = eventBus;
        searchField = new UserSearchField(UserSearchResultSelected.USER_SEARCH_EVENT_TAG.GROUP);
        checkBoxModel = new CheckBoxSelectionModel<>(new IdentityValueProvider<Collaborator>());
        initWidget(uiBinder.createAndBindUi(this));

        groupNameLabel.setHTML(appearance.groupNameLabel());

        checkBoxModel.setSelectionMode(Style.SelectionMode.MULTI);
        grid.setSelectionModel(checkBoxModel);
        grid.getView().setEmptyText(appearance.noCollaborators());
        grid.addViewReadyHandler(new ViewReadyEvent.ViewReadyHandler() {
            @Override
            public void onViewReady(ViewReadyEvent event) {
                setGridCheckBoxDebugIds();
            }
        });
        grid.getSelectionModel().addSelectionChangedHandler(new SelectionChangedEvent.SelectionChangedHandler<Collaborator>() {
            @Override
            public void onSelectionChanged(SelectionChangedEvent<Collaborator> event) {
                deleteBtn.setEnabled(!event.getSelection().isEmpty());
            }
        });

        handlerRegistration =
                eventBus.addHandler(UserSearchResultSelected.TYPE, new CollaboratorSelectedHandler());

        editorDriver.initialize(this);
    }

    @UiHandler("deleteBtn")
    void onDeleteButtonSelected(SelectEvent event) {
        List<Collaborator> selectedCollab = grid.getSelectionModel().getSelectedItems();
        if (selectedCollab != null && !selectedCollab.isEmpty()) {
            selectedCollab.forEach(collaborator -> listStore.remove(collaborator));
        }
    }

    @UiFactory
    ListStore<Collaborator> createListStore() {
        return new ListStore<Collaborator>(new CollaboratorKeyProvider());
    }

    @UiFactory
    ColumnModel<Collaborator> buildColumnModel() {
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
    }

    void setGridCheckBoxDebugIds() {
        for (int i = 0; i < listStore.size(); i++) {
            grid.getView().getCell(i, 0).setId(baseID + CollaboratorsModule.Ids.CHECKBOX_ITEM + i);
        }
    }

    public void edit(Group group) {
        editorDriver.edit(group);
    }

    public void clearHandlers() {
        handlerRegistration.removeHandler();
    }

    public Group getGroup() {
        return editorDriver.flush();
    }

    public List<Collaborator> getCollaborators() {
        return listStore.getAll();
    }
}
