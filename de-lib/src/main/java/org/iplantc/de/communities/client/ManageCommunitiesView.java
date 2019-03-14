package org.iplantc.de.communities.client;

import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;
import org.iplantc.de.communities.client.views.ReactCommunities;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.Splittable;

import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;

public interface ManageCommunitiesView extends IsWidget {

    void show(ReactCommunities.CommunitiesProps props);

    interface Appearance {
        int windowMinWidth();

        int windowMinHeight();

        String windowHeading();

        String communitiesHelp();

        String windowWidth();

        String windowHeight();

        String windowBackground();

        String appSelectionHeader();
    }

    @JsType
    interface Presenter {

        @JsFunction
        @SuppressWarnings("unusable-by-js")
        interface FetchCommunityPrivilegesCallback {
            void onSuccess(boolean privilege);
        }
        void fetchMyCommunities(ReactSuccessCallback callback, ReactErrorCallback errorCallback);

        void fetchAllCommunities(ReactSuccessCallback callback, ReactErrorCallback errorCallback);

        @SuppressWarnings("unusable-by-js")
        void fetchCommunityAdmins(String communityName, ReactSuccessCallback successCallback, ReactErrorCallback errorCallback);

        @SuppressWarnings("unusable-by-js")
        void fetchCommunityApps(String communityDisplayName, ReactSuccessCallback successCallback, ReactErrorCallback errorCallback);

        void searchCollaborators(String searchTerm, ReactSuccessCallback callback, ReactErrorCallback errorCallback);

        @SuppressWarnings("unusable-by-js")
        void getCommunityAdmins(String communityName, FetchCommunityPrivilegesCallback callback, ReactErrorCallback errorCallback);

        @SuppressWarnings("unusable-by-js")
        void getCommunityMembers(String communityName, FetchCommunityPrivilegesCallback callback, ReactErrorCallback errorCallback);

        @SuppressWarnings("unusable-by-js")
        void removeCommunityApps(String communityDisplayName, String appId, ReactSuccessCallback callback, ReactErrorCallback errorCallback);

        @SuppressWarnings("unusable-by-js")
        void removeCommunityAdmins(String communityName, Splittable adminList, ReactSuccessCallback callback, ReactErrorCallback errorCallback);

        @SuppressWarnings("unusable-by-js")
        void addCommunityAdmins(String communityName, Splittable adminList, ReactSuccessCallback successCallback, ReactErrorCallback errorCallback);

        void onAddCommunityAppsClicked(ReactSuccessCallback callback);

        @SuppressWarnings("unusable-by-js")
        void addAppToCommunity(String appId, String communityDisplayName, ReactSuccessCallback successCallback, ReactErrorCallback errorCallback);

        @SuppressWarnings("unusable-by-js")
        void deleteCommunity(String communityName, ReactSuccessCallback callback, ReactErrorCallback errorCallback);

        @SuppressWarnings("unusable-by-js")
        void joinCommunity(String communityName, ReactSuccessCallback callback, ReactErrorCallback errorCallback);

        @SuppressWarnings("unusable-by-js")
        void leaveCommunity(String communityName, ReactSuccessCallback callback, ReactErrorCallback errorCallback);

        @SuppressWarnings("unusable-by-js")
        void saveCommunity(String originalCommunityName, String name, String description, boolean retagApps, ReactSuccessCallback callback, ReactErrorCallback errorCallback);

        @JsIgnore
        void go(HasOneWidget container, String baseID);
    }
}