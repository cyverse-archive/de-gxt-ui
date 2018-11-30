package org.iplantc.de.admin.desktop.client.communities.views;

import org.iplantc.de.admin.desktop.client.communities.ManageCommunitiesView;
import org.iplantc.de.admin.desktop.client.communities.events.AddCommunityAdminSelected;
import org.iplantc.de.admin.desktop.client.communities.events.RemoveCommunityAdminSelected;
import org.iplantc.de.admin.desktop.shared.Belphegor;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.collaborators.client.models.SubjectKeyProvider;
import org.iplantc.de.collaborators.client.util.UserSearchField;
import org.iplantc.de.collaborators.client.views.CollaboratorsColumnModel;
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

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FieldSet;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import java.util.List;

public class ManageCommunitiesViewImpl extends Composite implements ManageCommunitiesView, Editor<Group> {

    interface EditorDriver extends SimpleBeanEditorDriver<Group, ManageCommunitiesViewImpl> {
    }

    interface MyUiBinder extends UiBinder<Widget, ManageCommunitiesViewImpl> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    private final EditorDriver editorDriver = GWT.create(ManageCommunitiesViewImpl.EditorDriver.class);
    @UiField(provided = true) Appearance appearance;
    @UiField VerticalLayoutContainer vlc;
    @UiField FieldLabel communityNameLabel;
    @UiField TextField nameEditor;
    @UiField FieldLabel communityDescLabel;
    @UiField TextArea descriptionEditor;
    @UiField FieldSet adminsFieldSet;
    @UiField FieldLabel adminPrivilegesExplanation;
    @UiField ToolBar adminToolbar;
    @UiField(provided = true) UserSearchField adminSearch;
    @Ignore @UiField TextButton removeAdmin;
    @UiField Grid<Subject> adminGrid;
    @UiField ColumnModel<Subject> adminCm;
    @UiField ListStore<Subject> adminListStore;
    String currentUserId;
    Group originalCommunity;

    @Inject
    public ManageCommunitiesViewImpl(ManageCommunitiesView.Appearance appearance,
                                     UserSearchField adminSearch,
                                     UserInfo userInfo) {
        this.appearance = appearance;
        this.adminSearch = adminSearch;
        this.currentUserId = userInfo.getUsername();
        adminSearch.addUserSearchResultSelectedEventHandler(event -> {
            fireEvent(new AddCommunityAdminSelected(originalCommunity, event.getSubject()));
        });

        initWidget(uiBinder.createAndBindUi(this));
        editorDriver.initialize(this);

        nameEditor.addValidator(new GroupNameValidator());
    }

    @UiFactory
    ColumnModel<Subject> createColumnModels() {
        return new CollaboratorsColumnModel(null);
    }

    @UiFactory
    ListStore<Subject> createListStores() {
        return new ListStore<>(new SubjectKeyProvider());
    }

    @UiHandler("removeAdmin")
    void onRemoveAdminClicked(SelectEvent event) {
        Subject selectedSubject = adminGrid.getSelectionModel().getSelectedItem();
        if (selectedSubject != null) {
            fireEvent(new RemoveCommunityAdminSelected(originalCommunity, selectedSubject));
        }
    }

    @Override
    public void edit(Group community) {
        editorDriver.edit(community);
        originalCommunity = community;
    }

    @Override
    public void addAdmins(List<Subject> communityAdmins) {
        adminListStore.addAll(communityAdmins);
    }

    @Override
    public boolean isValid() {
        return nameEditor.isValid();
    }

    @Override
    public Group getUpdatedCommunity() {
        return editorDriver.flush();
    }

    @Override
    public void removeAdmin(Subject admin) {
        adminListStore.remove(admin);
    }

    @Override
    public List<Subject> getAdmins() {
        return adminListStore.getAll();
    }

    @Override
    public HandlerRegistration addAddCommunityAdminSelectedHandler(AddCommunityAdminSelected.AddCommunityAdminSelectedHandler handler) {
        return addHandler(handler, AddCommunityAdminSelected.TYPE);
    }

    @Override
    public HandlerRegistration addRemoveCommunityAdminSelectedHandler(RemoveCommunityAdminSelected.RemoveCommunityAdminSelectedHandler handler) {
        return addHandler(handler, RemoveCommunityAdminSelected.TYPE);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        nameEditor.setId(baseID + Belphegor.CommunityIds.COMMUNITY_NAME);
        descriptionEditor.setId(baseID + Belphegor.CommunityIds.DESCRIPTION);
        String adminToolbarId = baseID + Belphegor.CommunityIds.TOOLBAR;
        adminToolbar.ensureDebugId(adminToolbarId);
        removeAdmin.ensureDebugId(adminToolbarId + Belphegor.CommunityIds.REMOVE_BTN);
        adminSearch.asWidget().ensureDebugId(adminToolbarId + Belphegor.CommunityIds.USER_SEARCH);
        adminGrid.ensureDebugId(baseID + Belphegor.CommunityIds.ADMIN_GRID);
    }
}
