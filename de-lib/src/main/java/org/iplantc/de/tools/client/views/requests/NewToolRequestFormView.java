package org.iplantc.de.tools.client.views.requests;

import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.Splittable;

import jsinterop.annotations.JsType;

/**
 *  Modified by Sriram 02/26/2019
 *
 */

@JsType
public interface NewToolRequestFormView extends IsWidget {

    void load(Presenter presenter);

    @JsType
    interface Presenter extends org.iplantc.de.commons.client.presenter.Presenter {
        void submitRequest(Splittable toolRequest,
                           ReactSuccessCallback callback,
                           ReactErrorCallback errorCallback);

    }


}

