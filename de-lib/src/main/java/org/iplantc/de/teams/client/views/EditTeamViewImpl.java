package org.iplantc.de.teams.client.views;

import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.Privilege;
import org.iplantc.de.client.models.groups.PrivilegeType;
import org.iplantc.de.collaborators.client.events.UserSearchResultSelected;
import org.iplantc.de.collaborators.client.util.UserSearchField;
import org.iplantc.de.collaborators.client.views.cells.SubjectNameCell;
import org.iplantc.de.commons.client.validators.GroupNameValidator;
import org.iplantc.de.teams.client.EditTeamView;
import org.iplantc.de.teams.client.TeamsView;
import org.iplantc.de.teams.client.events.AddPublicUserSelected;
import org.iplantc.de.teams.client.events.RemoveMemberPrivilegeSelected;
import org.iplantc.de.teams.client.events.RemoveNonMemberPrivilegeSelected;
import org.iplantc.de.teams.client.models.PrivilegeKeyProvider;
import org.iplantc.de.teams.client.models.PrivilegeProperties;
import org.iplantc.de.teams.shared.Teams;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.event.StoreAddEvent;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FieldSet;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import java.util.Comparator;
import java.util.List;

public class EditTeamViewImpl extends Composite implements EditTeamView,
                                                           Editor<Group>,
                                                           SelectionChangedEvent.SelectionChangedHandler<Privilege> {

    interface EditorDriver extends SimpleBeanEditorDriver<Group, EditTeamViewImpl> {
    }

    interface MyUiBinder extends UiBinder<Widget, EditTeamViewImpl> {
    }

    class TeamMemberComparator implements Comparator<Subject> {

        @Override
        public int compare(Subject o1, Subject o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    }

    private static MyUiBinder uiBinder = GWT.create(EditTeamViewImpl.MyUiBinder.class);
    private final EditorDriver editorDriver = GWT.create(EditTeamViewImpl.EditorDriver.class);

    @UiField VerticalLayoutContainer vlc;
    @UiField @Ignore FieldLabel teamNameLabel;
    @UiField @Ignore FieldLabel teamDescLabel;
    @UiField TextField nameEditor;
    @UiField TextArea descriptionEditor;
    @UiField ToolBar memberToolbar;
    @UiField @Ignore TextButton removeMember;
    @UiField @Ignore FieldLabel memberOptOutExplanation;
    @UiField Grid<Privilege> nonMembersGrid;
    @UiField(provided = true) ListStore<Privilege> nonMembersListStore;
    @UiField(provided = true) ColumnModel<Privilege> nonMembersCm;
    @UiField FieldSet nonMembersFieldSet;
    @UiField ToolBar nonMemberToolbar;
    @UiField @Ignore TextButton removeNonMember;
    @UiField @Ignore TextButton addPublicUser;
    @UiField(provided = true) UserSearchField memberSearch;
    @UiField Grid<Privilege> membersGrid;
    @UiField(provided = true) ListStore<Privilege> membersListStore;
    @UiField(provided = true) ColumnModel<Privilege> membersCm;
    @UiField(provided = true) UserSearchField nonMemberSearch;
    private PrivilegeProperties privProps;
    @UiField(provided = true) TeamsView.TeamsViewAppearance appearance;
    String currentUserId;
    private SubjectNameCell memberNameCell;
    private SubjectNameCell nonMemberNameCell;
    List<ColumnConfig<Privilege, PrivilegeType>> privilegeColumns = Lists.newArrayList();

    @Inject
    public EditTeamViewImpl(TeamsView.TeamsViewAppearance appearance,
                            UserSearchField nonMemberSearch,
                            UserSearchField memberSearch,
                            PrivilegeProperties privProps,
                            UserInfo userInfo) {
        this.appearance = appearance;
        this.nonMemberSearch = nonMemberSearch;
        this.memberSearch = memberSearch;
        this.privProps = privProps;
        this.currentUserId = userInfo.getUsername();
        createListStores();
        createColumnModels();

        initWidget(uiBinder.createAndBindUi(this));
        editorDriver.initialize(this);

        nameEditor.addValidator(new GroupNameValidator());

        memberSearch.setTag(SEARCH_MEMBERS_TAG);
        nonMemberSearch.setTag(SEARCH_NON_MEMBERS_TAG);

        membersGrid.getSelectionModel().addSelectionChangedHandler(this);
        membersGrid.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        nonMembersGrid.getSelectionModel().addSelectionChangedHandler(this);
        nonMembersGrid.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
    }

    void createColumnModels() {
        List<ColumnConfig<Privilege, ?>> nonMemberConfigs = Lists.newArrayList();
        ColumnConfig<Privilege, Subject> nonMemberName = new ColumnConfig<>(privProps.name(),
                                                                           appearance.nameColumnWidth(),
                                                                           appearance.nameColumnLabel());
        ColumnConfig<Privilege, String> nonMemberInstitution = new ColumnConfig<>(privProps.institution(),
                                                                                  appearance.institutionColumnWidth(),
                                                                                  appearance.institutionColumnLabel());
        ColumnConfig<Privilege, PrivilegeType> nonMemberPrivilege = new ColumnConfig<>(privProps.privilegeType(),
                                                                                appearance.privilegeColumnWidth(),
                                                                                appearance.privilegeColumnLabel());
        nonMemberNameCell = new SubjectNameCell(false);
        nonMemberName.setCell(nonMemberNameCell);
        nonMemberName.setComparator(new TeamMemberComparator());
        nonMemberPrivilege.setCell(createPrivilegeComboBox(false));
        nonMemberPrivilege.setHideable(false);
        nonMemberConfigs.add(nonMemberName);
        nonMemberConfigs.add(nonMemberInstitution);
        nonMemberConfigs.add(nonMemberPrivilege);
        nonMembersCm = new ColumnModel<>(nonMemberConfigs);


        List<ColumnConfig<Privilege, ?>> memberConfigs = Lists.newArrayList();
        ColumnConfig<Privilege, Subject> memberName = new ColumnConfig<>(privProps.name(),
                                                                        appearance.nameColumnWidth(),
                                                                        appearance.nameColumnLabel());
        ColumnConfig<Privilege, String> memberInstitution = new ColumnConfig<>(privProps.institution(),
                                                                               appearance.institutionColumnWidth(),
                                                                               appearance.institutionColumnLabel());
        ColumnConfig<Privilege, PrivilegeType> memberPrivilege = new ColumnConfig<>(privProps.privilegeType(),
                                                                                    appearance.privilegeColumnWidth(),
                                                                                    appearance.privilegeColumnLabel());

        memberNameCell = new SubjectNameCell(false);
        memberName.setCell(memberNameCell);
        memberName.setComparator(new TeamMemberComparator());
        memberPrivilege.setCell(createPrivilegeComboBox(true));
        memberPrivilege.setHideable(false);
        memberConfigs.add(memberName);
        memberConfigs.add(memberInstitution);
        memberConfigs.add(memberPrivilege);
        membersCm = new ColumnModel<>(memberConfigs);

        privilegeColumns.add(nonMemberPrivilege);
        privilegeColumns.add(memberPrivilege);
    }

    ComboBoxCell<PrivilegeType> createPrivilegeComboBox(boolean forMembers) {
        ListStore<PrivilegeType> comboListStore = new ListStore<>(PrivilegeType::getLabel);
        comboListStore.addAll(forMembers ? getMemberPrivilegeList() : getNonMemberPrivilegeList());
        ComboBoxCell<PrivilegeType> combo = new ComboBoxCell<>(comboListStore, PrivilegeType::getLabel);
        combo.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        combo.setForceSelection(true);
        combo.setAllowBlank(false);
        combo.setWidth(appearance.privilegeComboWidth());
        return combo;
    }

    List<PrivilegeType> getMemberPrivilegeList() {
        return Lists.newArrayList(PrivilegeType.admin,
                                  PrivilegeType.readOptin,
                                  PrivilegeType.read);
    }

    List<PrivilegeType> getNonMemberPrivilegeList() {
        return Lists.newArrayList(PrivilegeType.admin,
                                  PrivilegeType.readOptin,
                                  PrivilegeType.read,
                                  PrivilegeType.optin,
                                  PrivilegeType.view);
    }

    void createListStores() {
        nonMembersListStore = new ListStore<>(new PrivilegeKeyProvider());
        nonMembersListStore.setAutoCommit(true);

        membersListStore = new ListStore<>(new PrivilegeKeyProvider());
        membersListStore.setAutoCommit(true);
    }

    public void edit(Group group) {
        editorDriver.edit(group);
        nameEditor.setValue(group.getGroupShortName());
    }

    @Override
    public void addNonMembers(List<Privilege> privilegeList) {
        nonMembersListStore.addAll(privilegeList);
    }

    @Override
    public void addMembers(List<Privilege> privilegeList) {
        membersListStore.addAll(privilegeList);
    }

    @Override
    public boolean isValid() {
        return nameEditor.isValid();
    }

    @Override
    public Group getTeam() {
        return editorDriver.flush();
    }

    @Override
    public List<Privilege> getMemberPrivileges() {
        return membersListStore.getAll();
    }

    @Override
    public List<Privilege> getNonMemberPrivileges() {
        return nonMembersListStore.getAll();
    }

    @Override
    public void removeMemberPrivilege(Privilege privilege) {
        membersListStore.remove(privilege);
    }

    @Override
    public void removeNonMemberPrivilege(Privilege privilege) {
        nonMembersListStore.remove(privilege);
    }

    @Override
    public void setPublicUserButtonVisibility(boolean isVisible) {
        addPublicUser.setVisible(isVisible);
        nonMemberToolbar.forceLayout();
    }

    @Override
    public void showAdminMode(boolean adminMode) {
        if (adminMode) {
            nonMembersFieldSet.show();
            memberOptOutExplanation.show();
            memberToolbar.show();

            nameEditor.enable();
            descriptionEditor.enable();
        } else {
            privilegeColumns.forEach(columnConfig -> columnConfig.setHidden(true));
            nonMembersGrid.getView().refresh(true);
            membersGrid.getView().refresh(true);
        }
        vlc.forceLayout();
    }

    @UiHandler("removeNonMember")
    void onRemoveNonMemberClicked(SelectEvent event) {
        fireEvent(new RemoveNonMemberPrivilegeSelected(nonMembersGrid.getSelectionModel().getSelectedItem()));
    }

    @UiHandler("removeMember")
    void onRemoveMemberClicked(SelectEvent event) {
        fireEvent(new RemoveMemberPrivilegeSelected(membersGrid.getSelectionModel().getSelectedItem()));
    }

    @UiHandler("addPublicUser")
    void onAddPublicUserClicked(SelectEvent event) {
        fireEvent(new AddPublicUserSelected());
    }

    @Override
    public void onSelectionChanged(SelectionChangedEvent<Privilege> selectionChangedEvent) {
        Privilege memberSelection = membersGrid.getSelectionModel().getSelectedItem();
        boolean isSelfMember = memberSelection != null && currentUserId.equals(memberSelection.getSubject().getId());
        removeMember.setEnabled(memberSelection != null && !isSelfMember);

        Privilege nonMemberSelection = nonMembersGrid.getSelectionModel().getSelectedItem();
        boolean isSelfNonMember = nonMemberSelection != null && currentUserId.equals(nonMemberSelection.getSubject().getId());
        removeNonMember.setEnabled(nonMemberSelection != null && !isSelfNonMember);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        nameEditor.ensureDebugId(baseID + Teams.Ids.TEAM_NAME);
        descriptionEditor.ensureDebugId(baseID + Teams.Ids.TEAM_DESCRIPTION);
        removeMember.ensureDebugId(baseID + Teams.Ids.REMOVE_MEMBER_BTN);
        removeNonMember.ensureDebugId(baseID + Teams.Ids.REMOVE_NON_MEMBER_BTN);
        memberSearch.asWidget().ensureDebugId(baseID + Teams.Ids.MEMBER_SEARCH);
        nonMemberSearch.asWidget().ensureDebugId(baseID + Teams.Ids.NON_MEMBER_SEARCH);
        membersGrid.ensureDebugId(baseID + Teams.Ids.MEMBERS_GRID);
        nonMembersGrid.ensureDebugId(baseID + Teams.Ids.NON_MEMBERS_GRID);
        memberNameCell.setBaseDebugId(baseID + Teams.Ids.MEMBERS_GRID);
        nonMemberNameCell.setBaseDebugId(baseID + Teams.Ids.NON_MEMBERS_GRID);
    }

    @Override
    public HandlerRegistration addUserSearchResultSelectedEventHandler(UserSearchResultSelected.UserSearchResultSelectedEventHandler handler) {
        memberSearch.addUserSearchResultSelectedEventHandler(handler);
        return nonMemberSearch.addUserSearchResultSelectedEventHandler(handler);
    }

    @Override
    public HandlerRegistration addRemoveMemberPrivilegeSelectedHandler(RemoveMemberPrivilegeSelected.RemoveMemberPrivilegeSelectedHandler handler) {
        return addHandler(handler, RemoveMemberPrivilegeSelected.TYPE);
    }

    @Override
    public HandlerRegistration addRemoveNonMemberPrivilegeSelectedHandler(
            RemoveNonMemberPrivilegeSelected.RemoveNonMemberPrivilegeSelectedHandler handler) {
        return addHandler(handler, RemoveNonMemberPrivilegeSelected.TYPE);
    }

    @Override
    public HandlerRegistration addAddPublicUserSelectedHandler(AddPublicUserSelected.AddPublicUserSelectedHandler handler) {
        return addHandler(handler, AddPublicUserSelected.TYPE);
    }

    @Override
    public HandlerRegistration addStoreAddHandler(StoreAddEvent.StoreAddHandler<Privilege> handler) {
        return nonMembersListStore.addStoreAddHandler(handler);
    }

    @Override
    public HandlerRegistration addStoreRemoveHandler(StoreRemoveEvent.StoreRemoveHandler<Privilege> handler) {
        return nonMembersListStore.addStoreRemoveHandler(handler);
    }
}
