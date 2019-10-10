package org.iplantc.de.admin.desktop.client.appPublicationRequest.presenter;

import org.iplantc.de.admin.desktop.client.appPublicationRequest.AppPublicationRequestView;
import org.iplantc.de.admin.desktop.client.services.AppAdminServiceFacade;
import org.iplantc.de.admin.desktop.shared.Belphegor;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;
import org.iplantc.de.commons.client.ErrorHandler;

import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.Splittable;

public class AppPublicationRequestPresenter implements AppPublicationRequestView.Presenter {


    final AppPublicationRequestView view;

    @Inject
    AppAdminServiceFacade appService;

    @Inject
    public AppPublicationRequestPresenter(AppPublicationRequestView view) {
        this.view = view;
    }

    @Override
    public void getAppPublicationRequests(ReactSuccessCallback callback,
                                          ReactErrorCallback errorCallback) {
        appService.getAppPublicationRequests(new AsyncCallback<Splittable>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught.getMessage(), caught);
                if (errorCallback != null) {
                    errorCallback.onError(Response.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
                }
            }

            @Override
            public void onSuccess(Splittable result) {
                if (callback != null) {
                    callback.onSuccess(result);
                }
            }
        });
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view);
        view.load(this);
    }

    @Override
    public void publishApp(String appId,
                           String systemId,
                           ReactSuccessCallback callback,
                           ReactErrorCallback errorCallback) {

    }

    @Override
    public void setViewDebugId(String baseId) {
        view.asWidget().ensureDebugId(baseId + Belphegor.AppPublicationRequestsIds.VIEW);
    }
}
