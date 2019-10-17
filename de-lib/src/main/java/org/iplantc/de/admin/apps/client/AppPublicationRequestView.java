package org.iplantc.de.admin.apps.client;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.Splittable;

import jsinterop.annotations.JsType;

@JsType
public interface AppPublicationRequestView extends IsWidget {
     interface AppPublicationRequestAppearance {
      String publicationRequestSuccess(String appName);
    };

    @JsType
    interface Presenter {
        void getAppPublicationRequests();

        void go(HasOneWidget container);

        void publishApp(String appId,
                        String appName,
                        String systemId);

        void setViewDebugId(String baseId);
    }

    void load(Presenter presenter);

    void setRequests(Splittable requests);

    void setLoading(boolean loading);

    

}
