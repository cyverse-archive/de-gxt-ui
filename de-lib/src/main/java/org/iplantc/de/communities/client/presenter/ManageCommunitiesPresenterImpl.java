package org.iplantc.de.communities.client.presenter;

import org.iplantc.de.admin.desktop.client.communities.views.dialogs.RetagAppsConfirmationDialog;
import org.iplantc.de.apps.client.AppsView;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppAutoBeanFactory;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.collaborators.SubjectMemberList;
import org.iplantc.de.client.models.errorHandling.ServiceErrorCode;
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
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.communities.client.ManageCommunitiesView;
import org.iplantc.de.communities.client.views.ReactCommunities;
import org.iplantc.de.pipelines.client.views.AppSelectionDialog;
import org.iplantc.de.shared.AppsCallback;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;


import com.sencha.gxt.widget.core.client.Dialog;

import java.util.List;

public class ManageCommunitiesPresenterImpl implements ManageCommunitiesView.Presenter,
                                                       AppSelectionDialog.Presenter {

    private GroupServiceFacade serviceFacade;
    private AppUserServiceFacade appUserServiceFacade;
    private CollaboratorsServiceFacade collaboratorsServiceFacade;
    private GroupAutoBeanFactory factory;
    private AppMetadataServiceFacade metadataServiceFacade;
    private AppAutoBeanFactory appAutoBeanFactory;
    private UserInfo userInfo;
    private CollaboratorsUtil collaboratorsUtil;
    private ManageCommunitiesView view;
    private AppSelectionDialog appSelectView;
    private AppsView.Presenter appsPresenter;
    private ReactSuccessCallback selectAppsCallback;
    @Inject AsyncProviderWrapper<RetagAppsConfirmationDialog> retagAppsConfirmationDlgProvider;

    @Inject
    public ManageCommunitiesPresenterImpl(GroupServiceFacade serviceFacade,
                                          AppUserServiceFacade appUserServiceFacade,
                                          CollaboratorsServiceFacade collaboratorsServiceFacade,
                                          GroupAutoBeanFactory factory,
                                          AppMetadataServiceFacade metadataServiceFacade,
                                          AppAutoBeanFactory appAutoBeanFactory,
                                          UserInfo userInfo,
                                          CollaboratorsUtil collaboratorsUtil,
                                          ManageCommunitiesView view,
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
        Subject selfSubject = getSelfSubject();
        Splittable subjectSpl = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(selfSubject));

        ReactCommunities.CommunitiesProps props = new ReactCommunities.CommunitiesProps();
        props.parentId = baseId;
        props.presenter = this;
        props.collaboratorsUtil = collaboratorsUtil;
        props.currentUser = subjectSpl;

        view.show(props);

        container.setWidget(view);
    }

    Subject getSelfSubject() {
        Subject subject = factory.getSubject().as();
        subject.setName(getFullName());
        subject.setId(userInfo.getUsername());
        subject.setDisplayName(subject.getName());

        return subject;
    }

    String getFullName() {
        String firstName = userInfo.getFirstName();
        String lastName = userInfo.getLastName();
        String name = Strings.isNullOrEmpty(firstName) ? "" : firstName;
        name += Strings.isNullOrEmpty(lastName) ? "" : " " + lastName;
        return name;
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
    public void addCommunityAdmins(Splittable community,
                                   Splittable adminList,
                                   ReactSuccessCallback successCallback,
                                   ReactErrorCallback errorCallback) {
        Group group = getGroupFromSplittable(community);
        List<Subject> subjectList = getSubjectListFromSplittable(adminList);

        serviceFacade.addCommunityAdmins(group, subjectList, new AsyncCallback<List<UpdateMemberResult>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
                errorCallback.onError(500, caught.getMessage());
            }

            @Override
            public void onSuccess(List<UpdateMemberResult> result) {
                successCallback.onSuccess(null);
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
    public void addAppToCommunity(Splittable appSpl, Splittable community, ReactSuccessCallback successCallback, ReactErrorCallback errorCallback) {
        Group group = getGroupFromSplittable(community);
        App app = getAppFromSplittable(appSpl);

        metadataServiceFacade.updateAppCommunityTags(group, app, new AsyncCallback<Splittable>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
                errorCallback.onError(500, caught.getMessage());
            }

            @Override
            public void onSuccess(Splittable result) {
                successCallback.onSuccess(null);
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
                              boolean retagApps,
                              ReactSuccessCallback callback,
                              ReactErrorCallback errorCallback) {
        Group newCommunity = factory.getGroup().as();
        newCommunity.setName(name);
        newCommunity.setDescription(description);

        if (originalCommunity == null) {
            List<PrivilegeType> publicPrivileges = Lists.newArrayList(PrivilegeType.read);
            serviceFacade.addCommunity(newCommunity, publicPrivileges, new AsyncCallback<Group>() {
                @Override
                public void onFailure(Throwable caught) {
                    ErrorHandler.postReact(caught);
                    errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
                }

                @Override
                public void onSuccess(Group result) {
                    Splittable newCommunity = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(result));
                    callback.onSuccess(newCommunity);
                }
            });
        } else {
            Group originalGroup = getGroupFromSplittable(originalCommunity);
            updateCommunity(originalGroup, newCommunity, retagApps, callback, errorCallback);
        }
    }

    void updateCommunity(Group originalCommunity, Group updatedCommunity, boolean retagApps, ReactSuccessCallback callback, ReactErrorCallback errorCallback) {
        serviceFacade.updateCommunity(originalCommunity.getName(), updatedCommunity, retagApps, new AsyncCallback<Group>() {
            @Override
            public void onFailure(Throwable caught) {
                if (ServiceErrorCode.ERR_EXISTS.toString().equals(ErrorHandler.getServiceError(caught))) {
                    confirmReTagApps(originalCommunity, updatedCommunity, callback, errorCallback);
                } else {
                    ErrorHandler.postReact(caught);
                    errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
                }
            }

            @Override
            public void onSuccess(Group result) {
                Splittable newCommunity = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(result));
                callback.onSuccess(newCommunity);
            }
        });
    }

    void confirmReTagApps(Group originalCommunity, Group updatedCommunity, ReactSuccessCallback callback, ReactErrorCallback errorCallback) {
        retagAppsConfirmationDlgProvider.get(new AsyncCallback<RetagAppsConfirmationDialog>() {
            @Override
            public void onFailure(Throwable caught) {}

            @Override
            public void onSuccess(RetagAppsConfirmationDialog result) {
                result.show(originalCommunity.getName());
                result.setZIndex(1000000);
                result.addDialogHideHandler(event -> {
                    if (Dialog.PredefinedButton.YES.equals(event.getHideButton())) {
                        updateCommunity(originalCommunity, updatedCommunity, true, callback, errorCallback);
                    } else {
                        errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, null);
                    }
                });
            }
        });
    }

    Group getGroupFromSplittable(Splittable community) {
        if (community != null) {
            return AutoBeanCodex.decode(factory, Group.class, community.getPayload()).as();
        }
        return null;
    }

    Subject getSubjectFromSplittable(Splittable subject) {
        if (subject != null) {
            return AutoBeanCodex.decode(factory, Group.class, subject.getPayload()).as();
        }
        return null;
    }

    List<Subject> getSubjectListFromSplittable(Splittable subjectList) {
        List<Subject> subjects = Lists.newArrayList();
        if (subjectList != null && subjectList.isIndexed()) {
            for (int index = 0; index < subjectList.size(); index ++) {
                subjects.add(getSubjectFromSplittable(subjectList.get(index)));
            }
        }
        return subjects;
    }

    App getAppFromSplittable(Splittable app) {
        if (app != null) {
            return AutoBeanCodex.decode(appAutoBeanFactory, App.class, app.getPayload()).as();
        }
        return null;
    }
}
