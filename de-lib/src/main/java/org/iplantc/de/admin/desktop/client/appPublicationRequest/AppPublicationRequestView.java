package org.iplantc.de.admin.desktop.client.appPublicationRequest;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.Splittable;

import jsinterop.annotations.JsType;

@JsType
public interface AppPublicationRequestView extends IsWidget {

    @JsType
    interface Presenter {
        void getAppPublicationRequests();

        void go(HasOneWidget container);

        void publishApp(String appId,
                        String systemId);

        void setViewDebugId(String baseId);
    }

    void load(Presenter presenter);

    void setRequests(Splittable requests);

    void setLoading(boolean loading);

    

}
