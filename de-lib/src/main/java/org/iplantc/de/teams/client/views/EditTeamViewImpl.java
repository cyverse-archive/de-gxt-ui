package org.iplantc.de.teams.client.views;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.Privilege;
import org.iplantc.de.collaborators.client.events.UserSearchResultSelected;
import org.iplantc.de.collaborators.client.util.UserSearchField;
import org.iplantc.de.teams.client.EditTeamView;
import org.iplantc.de.teams.client.TeamsView;
import org.iplantc.de.teams.client.models.PrivilegeKeyProvider;
import org.iplantc.de.teams.client.models.PrivilegeProperties;

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

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

import java.util.List;

public class EditTeamViewImpl extends Composite implements EditTeamView,
                                                           Editor<Group> {

    interface EditorDriver extends SimpleBeanEditorDriver<Group, EditTeamViewImpl> {
    }

    interface MyUiBinder extends UiBinder<Widget, EditTeamViewImpl> {
    }

    private static MyUiBinder uiBinder = GWT.create(EditTeamViewImpl.MyUiBinder.class);
    private final EditorDriver editorDriver = GWT.create(EditTeamViewImpl.EditorDriver.class);

    @UiField @Ignore FieldLabel teamNameLabel;
    @UiField @Ignore FieldLabel teamDescLabel;
    @UiField TextField nameEditor;
    @UiField TextArea descriptionEditor;
    @UiField @Ignore TextButton removeMember;
    @UiField Grid<Privilege> nonMembersGrid;
    @UiField(provided = true) ListStore<Privilege> nonMembersListStore;
    @UiField(provided = true) ColumnModel<Privilege> nonMembersCm;
    @UiField @Ignore TextButton removeNonMember;
    @UiField(provided = true) UserSearchField memberSearch;
    @UiField Grid<Privilege> membersGrid;
    @UiField(provided = true) ListStore<Privilege> membersListStore;
    @UiField(provided = true) ColumnModel<Privilege> membersCm;
    @UiField(provided = true) UserSearchField nonMemberSearch;
    private PrivilegeProperties privProps;
    @UiField(provided = true) TeamsView.TeamsViewAppearance appearance;

    @Inject
    public EditTeamViewImpl(TeamsView.TeamsViewAppearance appearance,
                            UserSearchField nonMemberSearch,
                            UserSearchField memberSearch,
                            PrivilegeProperties privProps) {
        this.appearance = appearance;
        this.nonMemberSearch = nonMemberSearch;
        this.memberSearch = memberSearch;
        this.privProps = privProps;
        createListStores();
        createColumnModels();

        initWidget(uiBinder.createAndBindUi(this));
        editorDriver.initialize(this);

        nonMembersGrid.getView().setForceFit(true);
        membersGrid.getView().setForceFit(true);
    }

    void createColumnModels() {
        List<ColumnConfig<Privilege, ?>> nonMemberConfigs = Lists.newArrayList();
        ColumnConfig<Privilege, String> nonMemberName = new ColumnConfig<>(privProps.name(),
                                                                           appearance.nameColumnWidth(),
                                                                           appearance.nameColumnLabel());
        ColumnConfig<Privilege, String> nonMemberPrivilege = new ColumnConfig<>(privProps.privilege(),
                                                                                appearance.nameColumnWidth(),
                                                                                appearance.privilegeColumnLabel());
        nonMemberConfigs.add(nonMemberName);
        nonMemberConfigs.add(nonMemberPrivilege);
        nonMembersCm = new ColumnModel<>(nonMemberConfigs);


        List<ColumnConfig<Privilege, ?>> memberConfigs = Lists.newArrayList();
        ColumnConfig<Privilege, String> memberName = new ColumnConfig<>(privProps.name(),
                                                                        appearance.nameColumnWidth(),
                                                                        appearance.nameColumnLabel());
        ColumnConfig<Privilege, String> memberPrivilege = new ColumnConfig<>(privProps.privilege(),
                                                                             appearance.nameColumnWidth(),
                                                                             appearance.privilegeColumnLabel());
        memberConfigs.add(memberName);
        memberConfigs.add(memberPrivilege);
        membersCm = new ColumnModel<>(memberConfigs);
    }

    void createListStores() {
        nonMembersListStore = new ListStore<>(new PrivilegeKeyProvider());
        membersListStore = new ListStore<>(new PrivilegeKeyProvider());
    }

    public void edit(Group group) {
        editorDriver.edit(group);
    }

    @Override
    public void addNonMembers(List<Privilege> privilegeList) {
        nonMembersListStore.addAll(privilegeList);
    }

    @UiHandler("removeNonMember")
    void onRemoveNonMemberClicked(SelectEvent event) {

    }

    @UiHandler("removeMember")
    void onRemoveMemberClicked(SelectEvent event) {

    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
    }

    //FIXME eek, 2 handlers?
    @Override
    public HandlerRegistration addUserSearchResultSelectedEventHandler(UserSearchResultSelected.UserSearchResultSelectedEventHandler handler) {
        return nonMemberSearch.addUserSearchResultSelectedEventHandler(handler);
    }
}
