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
    }

    @JsType
    interface Presenter {

        @JsFunction
        @SuppressWarnings("unusable-by-js")
        interface FetchCommunityPrivilegesCallback {
            void onSuccess(Boolean isAdmin, Boolean isMember);
        }
        void fetchMyCommunities(ReactSuccessCallback callback);

        void fetchAllCommunities(ReactSuccessCallback callback);

        @SuppressWarnings("unusable-by-js")
        void fetchCommunityAdmins(Splittable community, ReactSuccessCallback successCallback, ReactErrorCallback errorCallback);

        @SuppressWarnings("unusable-by-js")
        void fetchCommunityApps(Splittable community, ReactSuccessCallback successCallback, ReactErrorCallback errorCallback);

        void searchCollaborators(String searchTerm, ReactSuccessCallback callback);

        @SuppressWarnings("unusable-by-js")
        void fetchCommunityPrivileges(Splittable community, FetchCommunityPrivilegesCallback callback);

        @SuppressWarnings("unusable-by-js")
        void removeCommunityApps(Splittable community, Splittable app, ReactSuccessCallback callback);

        @SuppressWarnings("unusable-by-js")
        void removeCommunityAdmins(Splittable community, Splittable subject, ReactSuccessCallback callback);

        @SuppressWarnings("unusable-by-js")
        void addCommunityAdmins(Splittable community, Splittable adminList, ReactSuccessCallback successCallback, ReactErrorCallback errorCallback);

        void onAddCommunityAppsClicked(ReactSuccessCallback callback);

        @SuppressWarnings("unusable-by-js")
        void addAppToCommunity(Splittable app, Splittable community, ReactSuccessCallback successCallback, ReactErrorCallback errorCallback);

        @SuppressWarnings("unusable-by-js")
        void deleteCommunity(Splittable community, ReactSuccessCallback callback);

        @SuppressWarnings("unusable-by-js")
        void joinCommunity(Splittable community, ReactSuccessCallback callback);

        @SuppressWarnings("unusable-by-js")
        void leaveCommunity(Splittable community, ReactSuccessCallback callback);

        @SuppressWarnings("unusable-by-js")
        void saveCommunity(Splittable originalCommunity, String name, String description, ReactSuccessCallback callback);

        @JsIgnore
        void go(HasOneWidget container, String baseID);
    }
}