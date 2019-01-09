package org.iplantc.de.communities.client.presenter;

import org.iplantc.de.apps.client.AppsView;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppAutoBeanFactory;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.collaborators.SubjectMemberList;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.models.groups.PrivilegeType;
import org.iplantc.de.client.models.groups.UpdateMemberResult;
import org.iplantc.de.client.services.AppMetadataServiceFacade;
import org.iplantc.de.client.services.AppUserServiceFacade;
import org.iplantc.de.client.services.CollaboratorsServiceFacade;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;
import org.iplantc.de.collaborators.client.util.CollaboratorsUtil;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.communities.client.CommunitiesView;
import org.iplantc.de.communities.client.views.ReactCommunities;
import org.iplantc.de.pipelines.client.views.AppSelectionDialog;
import org.iplantc.de.shared.AppsCallback;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

import org.eclipse.jetty.server.Response;

import java.util.List;

public class CommunitiesPresenterImpl implements CommunitiesView.Presenter,
                                                 AppSelectionDialog.Presenter {

    private GroupServiceFacade serviceFacade;
    private AppUserServiceFacade appUserServiceFacade;
    private CollaboratorsServiceFacade collaboratorsServiceFacade;
    private GroupAutoBeanFactory factory;
    private AppMetadataServiceFacade metadataServiceFacade;
    private AppAutoBeanFactory appAutoBeanFactory;
    private UserInfo userInfo;
    private CollaboratorsUtil collaboratorsUtil;
    private CommunitiesView view;
    private AppSelectionDialog appSelectView;
    private AppsView.Presenter appsPresenter;
    private ReactSuccessCallback selectAppsCallback;

    @Inject
    public CommunitiesPresenterImpl(GroupServiceFacade serviceFacade,
                                    AppUserServiceFacade appUserServiceFacade,
                                    CollaboratorsServiceFacade collaboratorsServiceFacade,
                                    GroupAutoBeanFactory factory,
                                    AppMetadataServiceFacade metadataServiceFacade,
                                    AppAutoBeanFactory appAutoBeanFactory,
                                    UserInfo userInfo,
                                    CollaboratorsUtil collaboratorsUtil,
                                    CommunitiesView view,
                                    AppsView.Presenter appsPresenter) {
        this.serviceFacade = serviceFacade;
        this.appUserServiceFacade = appUserServiceFacade;
        this.collaboratorsServiceFacade = collaboratorsServiceFacade;
        this.factory = factory;
        this.metadataServiceFacade = metadataServiceFacade;
        this.appAutoBeanFactory = appAutoBeanFactory;
        this.userInfo = userInfo;
        this.collaboratorsUtil = collaboratorsUtil;
        this.view = view;
        this.appsPresenter = appsPresenter;
    }

    @Override
    public void go(HasOneWidget container, String baseId) {
        ReactCommunities.CommunitiesProps props = new ReactCommunities.CommunitiesProps();
        props.parentId = baseId;
        props.presenter = this;
        props.collaboratorsUtil = collaboratorsUtil;

        view.show(props);

        container.setWidget(view);
    }

    @Override
    public void fetchMyCommunities(ReactSuccessCallback callback) {
        serviceFacade.getMyCommunities(new AsyncCallback<Splittable>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
            }

            @Override
            public void onSuccess(Splittable result) {
                callback.onSuccess(result);
            }
        });
    }

    @Override
    public void fetchAllCommunities(ReactSuccessCallback callback) {
        serviceFacade.getCommunities(new AsyncCallback<Splittable>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
            }

            @Override
            public void onSuccess(Splittable result) {
                callback.onSuccess(result);
            }
        });
    }

    @Override
    public void fetchCommunityAdmins(Splittable community,
                                     ReactSuccessCallback successCallback,
                                     ReactErrorCallback errorCallback) {
        Group group = getGroupFromSplittable(community);
        serviceFacade.getCommunityAdmins(group, new AsyncCallback<Splittable>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
                errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
            }

            @Override
            public void onSuccess(Splittable result) {
                successCallback.onSuccess(result);
            }
        });
    }

    @Override
    public void fetchCommunityApps(Splittable community,
                                   ReactSuccessCallback successCallback,
                                   ReactErrorCallback errorCallback) {
        Group group = getGroupFromSplittable(community);

        appUserServiceFacade.getCommunityApps(group, null, new AppsCallback<Splittable>() {
            @Override
            public void onFailure(Integer statusCode, Throwable exception) {
                ErrorHandler.postReact(exception);
                errorCallback.onError(statusCode, exception.getMessage());
            }

            @Override
            public void onSuccess(Splittable result) {
                successCallback.onSuccess(result);
            }
        });
    }

    @Override
    public void searchCollaborators(String searchTerm, ReactSuccessCallback callback) {
        collaboratorsServiceFacade.searchCollaborators(searchTerm, new AsyncCallback<List<Subject>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
            }

            @Override
            public void onSuccess(List<Subject> result) {
                Splittable data = StringQuoter.createIndexed();
                result.forEach(subject -> {
                    Splittable splSubject = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(subject));
                    splSubject.assign(data, data.size());
                });
                callback.onSuccess(data);
            }
        });
    }

    @Override
    public void fetchCommunityPrivileges(Splittable community, FetchCommunityPrivilegesCallback callback) {
        Group group = getGroupFromSplittable(community);
        serviceFacade.getCommunityAdmins(group, new AsyncCallback<Splittable>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
            }

            @Override
            public void onSuccess(Splittable result) {
                List<Subject> admins = AutoBeanCodex.decode(factory, SubjectMemberList.class, result.getPayload()).as().getSubjects();
                fetchCommunityMembers(group, callback, admins);
            }
        });
    }

    void fetchCommunityMembers(Group community, FetchCommunityPrivilegesCallback callback, List<Subject> admins) {
        serviceFacade.getCommunityMembers(community, new AsyncCallback<List<Subject>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
            }

            @Override
            public void onSuccess(List<Subject> members) {
                boolean isAdmin = isAdmin(admins);
                boolean isMember = isMember(members);

                callback.onSuccess(isAdmin, isMember);
            }
        });
    }

    boolean isAdmin(List<Subject> adminList) {
        if (adminList == null || adminList.isEmpty()) {
            return false;
        }
        return adminList.stream()
                        .anyMatch(subject -> subject.getId().equals(userInfo.getUsername()));
    }

    boolean isMember(List<Subject> members) {
        return members.stream()
                      .anyMatch(member -> userInfo.getUsername().equals(member.getId()));
    }

    @Override
    public void removeCommunityApps(Splittable community,
                                    Splittable appSpl,
                                    ReactSuccessCallback callback) {
        Group group = getGroupFromSplittable(community);
        App app = getAppFromSplittable(appSpl);

        metadataServiceFacade.deleteAppCommunityTags(group, app, new AsyncCallback<Splittable>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
            }

            @Override
            public void onSuccess(Splittable result) {
                callback.onSuccess(null);
            }
        });
    }

    @Override
    public void removeCommunityAdmins(Splittable community,
                                      Splittable admin,
                                      ReactSuccessCallback callback) {
        Group group = getGroupFromSplittable(community);
        Subject subject = getSubjectFromSplittable(admin);

        serviceFacade.removeCommunityAdmins(group, Lists.newArrayList(subject), new AsyncCallback<List<UpdateMemberResult>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
            }

            @Override
            public void onSuccess(List<UpdateMemberResult> result) {
                callback.onSuccess(null);
            }
        });
    }

    @Override
    public void addCommunityAdmin(Splittable community,
                                  Splittable admin,
                                  ReactSuccessCallback callback) {
        Group group = getGroupFromSplittable(community);
        Subject subject = getSubjectFromSplittable(admin);

        serviceFacade.addCommunityAdmins(group, Lists.newArrayList(subject), new AsyncCallback<List<UpdateMemberResult>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
            }

            @Override
            public void onSuccess(List<UpdateMemberResult> result) {
                callback.onSuccess(null);
            }
        });
    }

    @Override
    public void onAddCommunityAppsClicked(ReactSuccessCallback selectAppsCallback) {
        this.selectAppsCallback = selectAppsCallback;
        appSelectView = new AppSelectionDialog();
        appSelectView.setPresenter(this);
        appsPresenter.hideAppMenu().hideWorkflowMenu().go(appSelectView, null, null, null, false);
        appSelectView.show();
        appSelectView.setZIndex(1000000);
    }

    @Override
    public void addAppToCommunity(Splittable appSpl, Splittable community, ReactSuccessCallback callback) {
        Group group = getGroupFromSplittable(community);
        App app = getAppFromSplittable(appSpl);

        metadataServiceFacade.updateAppCommunityTags(group, app, new AsyncCallback<Splittable>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
            }

            @Override
            public void onSuccess(Splittable result) {
                callback.onSuccess(null);
            }
        });
    }

    @Override
    public void onAddAppClick() {
        App selectedApp = appsPresenter.getSelectedApp();
        selectAppsCallback.onSuccess(AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(selectedApp)));
        appSelectView.hide();
    }

    @Override
    public void deleteCommunity(Splittable community, ReactSuccessCallback callback) {
        Group group = getGroupFromSplittable(community);

        serviceFacade.deleteCommunity(group, new AsyncCallback<Group>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
            }

            @Override
            public void onSuccess(Group result) {
                callback.onSuccess(null);
            }
        });
    }

    @Override
    public void joinCommunity(Splittable community, ReactSuccessCallback callback) {
        Group group = getGroupFromSplittable(community);

        serviceFacade.joinCommunity(group, new AsyncCallback<List<UpdateMemberResult>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
            }

            @Override
            public void onSuccess(List<UpdateMemberResult> result) {
                callback.onSuccess(null);
            }
        });
    }

    @Override
    public void leaveCommunity(Splittable community, ReactSuccessCallback callback) {
        Group group = getGroupFromSplittable(community);

        serviceFacade.leaveCommunity(group, new AsyncCallback<List<UpdateMemberResult>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
            }

            @Override
            public void onSuccess(List<UpdateMemberResult> result) {
                callback.onSuccess(null);
            }
        });
    }

    @Override
    public void saveCommunity(Splittable originalCommunity,
                              String name,
                              String description,
                              Splittable admins,
                              Splittable apps,
                              ReactSuccessCallback callback) {
        Group newCommunity = factory.getGroup().as();
        newCommunity.setName(name);
        newCommunity.setDescription(description);

        if (originalCommunity == null) {
            List<PrivilegeType> publicPrivileges = Lists.newArrayList(PrivilegeType.read);
            serviceFacade.addCommunity(newCommunity, publicPrivileges, new AsyncCallback<Group>() {
                @Override
                public void onFailure(Throwable caught) {
                    ErrorHandler.post(caught);
                }

                @Override
                public void onSuccess(Group result) {
                    callback.onSuccess(null);
                }
            });
        } else {
            Group group = getGroupFromSplittable(originalCommunity);
            serviceFacade.updateCommunity(group.getName(), newCommunity, new AsyncCallback<Group>() {
                @Override
                public void onFailure(Throwable caught) {
                    ErrorHandler.post(caught);
                }

                @Override
                public void onSuccess(Group result) {
                    callback.onSuccess(null);
                }
            });
        }
    }

    Group getGroupFromSplittable(Splittable community) {
        return AutoBeanCodex.decode(factory, Group.class, community.getPayload()).as();
    }

    Subject getSubjectFromSplittable(Splittable subject) {
        return AutoBeanCodex.decode(factory, Group.class, subject.getPayload()).as();
    }

    App getAppFromSplittable(Splittable app) {
        return AutoBeanCodex.decode(appAutoBeanFactory, App.class, app.getPayload()).as();
    }
}
