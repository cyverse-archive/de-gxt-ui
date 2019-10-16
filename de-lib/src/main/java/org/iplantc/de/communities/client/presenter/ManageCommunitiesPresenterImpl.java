package org.iplantc.de.communities.client.presenter;

import org.iplantc.de.admin.desktop.client.communities.views.dialogs.RetagAppsConfirmationDialog;
import org.iplantc.de.apps.client.AppsView;
import org.iplantc.de.client.models.HasStringList;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppAutoBeanFactory;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.collaborators.SubjectMemberList;
import org.iplantc.de.client.models.errorHandling.ServiceErrorCode;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.models.groups.UpdateMemberResult;
import org.iplantc.de.client.services.AppMetadataServiceFacade;
import org.iplantc.de.client.services.AppUserServiceFacade;
import org.iplantc.de.client.services.CollaboratorsServiceFacade;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;
import org.iplantc.de.collaborators.client.util.CollaboratorsUtil;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.communities.client.ManageCommunitiesView;
import org.iplantc.de.communities.client.views.ReactCommunities;
import org.iplantc.de.pipelines.client.views.AppSelectionDialog;
import org.iplantc.de.shared.AppsCallback;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.common.base.Strings;
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
import java.util.stream.Collectors;

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
    private ManageCommunitiesView.Appearance appearance;
    private AppsView.Presenter appsPresenter;
    private ReactSuccessCallback selectAppsCallback;
    @Inject AsyncProviderWrapper<RetagAppsConfirmationDialog> retagAppsConfirmationDlgProvider;
    @Inject IplantAnnouncer announcer;

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
                                          ManageCommunitiesView.Appearance appearance,
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
        this.appearance = appearance;
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
    public void fetchMyCommunities(ReactSuccessCallback callback, ReactErrorCallback errorCallback) {
        serviceFacade.getMyCommunities(new AsyncCallback<Splittable>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
                errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
            }

            @Override
            public void onSuccess(Splittable result) {
                callback.onSuccess(result);
            }
        });
    }

    @Override
    public void fetchAllCommunities(ReactSuccessCallback callback, ReactErrorCallback errorCallback) {
        serviceFacade.getCommunities(new AsyncCallback<Splittable>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
                errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
            }

            @Override
            public void onSuccess(Splittable result) {
                callback.onSuccess(result);
            }
        });
    }

    @Override
    public void fetchCommunityAdmins(String communityName,
                                     ReactSuccessCallback successCallback,
                                     ReactErrorCallback errorCallback) {
        serviceFacade.getCommunityAdmins(communityName, new AsyncCallback<Splittable>() {
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
    public void fetchCommunityApps(String communityDisplayName,
                                   String sortField,
                                   String sortDir,
                                   ReactSuccessCallback successCallback,
                                   ReactErrorCallback errorCallback) {
        appUserServiceFacade.getCommunityApps(communityDisplayName,
                                              null,
                                              sortField,
                                              sortDir,
                                              new AppsCallback<Splittable>() {
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
    public void searchCollaborators(String searchTerm, ReactSuccessCallback callback, ReactErrorCallback errorCallback) {
        collaboratorsServiceFacade.searchCollaborators(searchTerm, new AsyncCallback<List<Subject>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
                errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
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
    public void getCommunityAdmins(String communityName, FetchCommunityPrivilegesCallback callback, ReactErrorCallback errorCallback) {
        serviceFacade.getCommunityAdmins(communityName, new AsyncCallback<Splittable>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
                errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
            }

            @Override
            public void onSuccess(Splittable result) {
                List<Subject> admins = AutoBeanCodex.decode(factory, SubjectMemberList.class, result.getPayload()).as().getSubjects();
                boolean isAdmin = isAdmin(admins);
                callback.onSuccess(isAdmin);
            }
        });
    }

    @Override
    public void getCommunityMembers(String communityName, FetchCommunityPrivilegesCallback callback, ReactErrorCallback errorCallback) {
        serviceFacade.getCommunityMembers(communityName, new AsyncCallback<List<Subject>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
                errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
            }

            @Override
            public void onSuccess(List<Subject> members) {
                boolean isMember = isMember(members);
                callback.onSuccess(isMember);
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
    public void removeCommunityApps(String communityDisplayName,
                                    String appId,
                                    ReactSuccessCallback callback,
                                    ReactErrorCallback errorCallback) {
        metadataServiceFacade.deleteAppCommunityTags(communityDisplayName, appId, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
                errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                callback.onSuccess(null);
            }
        });
    }

    @Override
    @SuppressWarnings("unusable-by-js")
    public void removeCommunityAdmins(String communityName,
                                      Splittable adminList,
                                      ReactSuccessCallback callback,
                                      ReactErrorCallback errorCallback) {
        List<String> ids = AutoBeanCodex.decode(factory, HasStringList.class, adminList).as().getList();
        serviceFacade.removeCommunityAdmins(communityName, ids, new AsyncCallback<List<UpdateMemberResult>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
                errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
            }

            @Override
            public void onSuccess(List<UpdateMemberResult> result) {
                List<String> failedUsers = getFailedMemberUpdateNames(result);

                if (failedUsers.size() > 0) {
                    announcer.schedule(new ErrorAnnouncementConfig(appearance.removeCommunityAdminFailure(failedUsers)));
                    errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, "Admin update error");
                } else {
                    callback.onSuccess(null);
                }
            }
        });
    }

    @Override
    @SuppressWarnings("unusable-by-js")
    public void addCommunityAdmins(String communityName,
                                   Splittable adminIds,
                                   ReactSuccessCallback successCallback,
                                   ReactErrorCallback errorCallback) {
        List<String> ids = AutoBeanCodex.decode(factory, HasStringList.class, adminIds).as().getList();
        serviceFacade.addCommunityAdmins(communityName, ids, new AsyncCallback<List<UpdateMemberResult>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
                errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
            }

            @Override
            public void onSuccess(List<UpdateMemberResult> result) {
                List<String> failedUsers = getFailedMemberUpdateNames(result);

                if (failedUsers.size() > 0) {
                    announcer.schedule(new ErrorAnnouncementConfig(appearance.addCommunityAdminFailure(failedUsers)));
                    errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, "Admin update error");
                } else {
                    successCallback.onSuccess(null);
                }
            }
        });
    }

    @Override
    public void onAddCommunityAppsClicked(ReactSuccessCallback selectAppsCallback) {
        this.selectAppsCallback = selectAppsCallback;
        appSelectView = new AppSelectionDialog(false);
        appSelectView.setHeading(appearance.appSelectionHeader());
        appSelectView.addDialogHideHandler(event -> selectAppsCallback.onSuccess(null));
        appSelectView.setPresenter(this);
        appsPresenter.hideAppMenu().hideWorkflowMenu().go(appSelectView, null, null, null, false);
        appsPresenter.addAppSelectionChangedHandler(event -> {
            App selectedApp = appsPresenter.getSelectedApp();
            if (selectedApp != null && App.EXTERNAL_APP.equalsIgnoreCase(selectedApp.getAppType())) {
                appSelectView.disableAddAppBtn(appearance.agaveAppsNotSupportedToolTip());
            } else {
                appSelectView.enableAddAppBtn();
            }
        });
        appSelectView.show();
    }

    @Override
    public void addAppToCommunity(String appId, String communityDisplayName, ReactSuccessCallback successCallback, ReactErrorCallback errorCallback) {
        metadataServiceFacade.updateAppCommunityTags(communityDisplayName, appId, new AsyncCallback<Splittable>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
                errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
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
    public void deleteCommunity(String communityName, ReactSuccessCallback callback, ReactErrorCallback errorCallback) {
        serviceFacade.deleteCommunity(communityName, new AsyncCallback<Group>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
                errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
            }

            @Override
            public void onSuccess(Group result) {
                callback.onSuccess(null);
            }
        });
    }

    @Override
    public void joinCommunity(String communityName, ReactSuccessCallback callback, ReactErrorCallback errorCallback) {
        serviceFacade.joinCommunity(communityName, new AsyncCallback<List<UpdateMemberResult>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
                errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
            }

            @Override
            public void onSuccess(List<UpdateMemberResult> result) {
                List<String> failedUsers = getFailedMemberUpdateNames(result);

                if (failedUsers.size() > 0) {
                    announcer.schedule(new ErrorAnnouncementConfig(appearance.joinCommunityFailure()));
                    errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, "Admin update error");
                } else {
                    callback.onSuccess(null);
                }
            }
        });
    }

    @Override
    public void leaveCommunity(String communityName, ReactSuccessCallback callback, ReactErrorCallback errorCallback) {
        serviceFacade.leaveCommunity(communityName, new AsyncCallback<List<UpdateMemberResult>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
                errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
            }

            @Override
            public void onSuccess(List<UpdateMemberResult> result) {
                List<String> failedUsers = getFailedMemberUpdateNames(result);

                if (failedUsers.size() > 0) {
                    announcer.schedule(new ErrorAnnouncementConfig(appearance.leaveCommunityFailure()));
                    errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, "Admin update error");
                } else {
                    callback.onSuccess(null);
                }
            }
        });
    }

    List<String> getFailedMemberUpdateNames(List<UpdateMemberResult> results) {
        return results.stream()
                      .filter(memberResult -> !memberResult.isSuccess())
                      .map(UpdateMemberResult::getSubjectName)
                      .collect(Collectors.toList());
    }

    @Override
    public void saveCommunity(String originalCommunityName,
                              String name,
                              String description,
                              boolean retagApps,
                              ReactSuccessCallback callback,
                              ReactErrorCallback errorCallback) {
        Group newCommunity = factory.getGroup().as();
        newCommunity.setName(name);
        newCommunity.setDescription(description);

        if (originalCommunityName == null) {
            serviceFacade.addCommunity(newCommunity, new AsyncCallback<Group>() {
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
            updateCommunity(originalCommunityName, newCommunity, retagApps, callback, errorCallback);
        }
    }

    void updateCommunity(String originalCommunityName, Group updatedCommunity, boolean retagApps, ReactSuccessCallback callback, ReactErrorCallback errorCallback) {
        serviceFacade.updateCommunity(originalCommunityName, updatedCommunity, retagApps, new AsyncCallback<Group>() {
            @Override
            public void onFailure(Throwable caught) {
                if (ServiceErrorCode.ERR_EXISTS.toString().equals(ErrorHandler.getServiceError(caught))) {
                    confirmReTagApps(originalCommunityName, updatedCommunity, callback, errorCallback);
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

    void confirmReTagApps(String originalCommunityName, Group updatedCommunity, ReactSuccessCallback callback, ReactErrorCallback errorCallback) {
        retagAppsConfirmationDlgProvider.get(new AsyncCallback<RetagAppsConfirmationDialog>() {
            @Override
            public void onFailure(Throwable caught) {}

            @Override
            public void onSuccess(RetagAppsConfirmationDialog result) {
                result.show(originalCommunityName);
                result.setZIndex(1000000);
                result.addDialogHideHandler(event -> {
                    if (Dialog.PredefinedButton.YES.equals(event.getHideButton())) {
                        updateCommunity(originalCommunityName, updatedCommunity, true, callback, errorCallback);
                    } else {
                        errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, null);
                    }
                });
            }
        });
    }
}
