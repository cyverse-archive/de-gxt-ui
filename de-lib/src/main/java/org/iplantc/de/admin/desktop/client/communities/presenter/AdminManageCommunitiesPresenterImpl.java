package org.iplantc.de.admin.desktop.client.communities.presenter;

import org.iplantc.de.admin.desktop.client.communities.ManageCommunitiesView;
import org.iplantc.de.admin.desktop.client.communities.events.AddCommunityAdminSelected;
import org.iplantc.de.admin.desktop.client.communities.events.RemoveCommunityAdminSelected;
import org.iplantc.de.admin.desktop.client.communities.service.AdminCommunityServiceFacade;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.models.groups.UpdateMemberResult;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

import java.util.List;

public class AdminManageCommunitiesPresenterImpl implements ManageCommunitiesView.Presenter {

    private ManageCommunitiesView view;
    private ManageCommunitiesView.Appearance appearance;
    private AdminCommunityServiceFacade serviceFacade;
    private GroupAutoBeanFactory factory;
    private IplantAnnouncer announcer;
    private ManageCommunitiesView.MODE mode;

    @Inject
    public AdminManageCommunitiesPresenterImpl(ManageCommunitiesView view,
                                               ManageCommunitiesView.Appearance appearance,
                                               AdminCommunityServiceFacade serviceFacade,
                                               GroupAutoBeanFactory factory,
                                               IplantAnnouncer announcer) {
        this.view = view;
        this.appearance = appearance;
        this.serviceFacade = serviceFacade;
        this.factory = factory;
        this.announcer = announcer;

        view.addAddCommunityAdminSelectedHandler(this);
        view.addRemoveCommunityAdminSelectedHandler(this);
    }

    @Override
    public void go(HasOneWidget widget, ManageCommunitiesView.MODE mode) {
        this.mode = mode;
        widget.setWidget(view);
    }

    @Override
    public void editCommunity(Group community) {
        if (community == null) {
            community = factory.getGroup().as();
        }
        view.edit(community);

        if (ManageCommunitiesView.MODE.EDIT == mode) {
           getCommunityAdmins(community);
        }
    }

    @Override
    public boolean isViewValid() {
        return view.isValid();
    }

    @Override
    public Group getUpdatedCommunity() {
        return view.getUpdatedCommunity();
    }

    @Override
    public List<Subject> getCommunityAdmins() {
        return view.getAdmins();
    }

    void getCommunityAdmins(Group community) {
        view.mask(appearance.loadingMask());
        serviceFacade.getCommunityAdmins(community, new AsyncCallback<List<Subject>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                view.unmask();
            }

            @Override
            public void onSuccess(List<Subject> result) {
                view.addAdmins(result);
                view.unmask();
            }
        });
    }

    @Override
    public void onAddCommunityAdminSelected(AddCommunityAdminSelected event) {
        Group community = event.getCommunity();
        Subject admin = event.getAdmin();

        if (ManageCommunitiesView.MODE.CREATE == mode) {
            view.addAdmins(Lists.newArrayList(admin));
            return;
        }

        view.mask(appearance.loadingMask());

        serviceFacade.addCommunityAdmins(community, Lists.newArrayList(admin), new AsyncCallback<List<UpdateMemberResult>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                view.unmask();
            }

            @Override
            public void onSuccess(List<UpdateMemberResult> result) {
                result.forEach(updateMemberResult -> {
                    if (updateMemberResult.isSuccess()) {
                        view.addAdmins(Lists.newArrayList(admin));
                    } else {
                        announcer.schedule(new ErrorAnnouncementConfig(appearance.failedToAddCommunityAdmin(admin, community)));
                    }
                });
                view.unmask();
            }
        });
    }

    @Override
    public void onRemoveAdminSelected(RemoveCommunityAdminSelected event) {
        Subject admin = event.getAdmin();
        Group community = event.getCommunity();

        if (ManageCommunitiesView.MODE.CREATE == mode) {
            view.removeAdmin(admin);
            return;
        }

        view.mask(appearance.loadingMask());
        serviceFacade.deleteCommunityAdmins(community, Lists.newArrayList(admin), new AsyncCallback<List<UpdateMemberResult>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                view.unmask();
            }

            @Override
            public void onSuccess(List<UpdateMemberResult> result) {
                result.forEach(updateMemberResult -> {
                    if (updateMemberResult.isSuccess()) {
                        view.removeAdmin(admin);
                    } else {
                        announcer.schedule(new ErrorAnnouncementConfig(appearance.failedToRemoveCommunityAdmin(admin, community)));
                    }
                });
                view.unmask();
            }
        });
    }
}
