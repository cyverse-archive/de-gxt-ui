package org.iplantc.de.admin.desktop.client.appPublicationRequest;

import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import jsinterop.annotations.JsType;

@JsType
public interface AppPublicationRequestView extends IsWidget {

    @JsType
    interface Presenter {
        void getAppPublicationRequests(ReactSuccessCallback callback,
                                       ReactErrorCallback errorCallback);

        void go(HasOneWidget container);

        void publishApp(String appId,
                        String systemId,
                        ReactSuccessCallback callback,
                        ReactErrorCallback errorCallback);
    }

    void load(Presenter presenter);

    

}
