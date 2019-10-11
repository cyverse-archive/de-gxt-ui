package org.iplantc.de.admin.desktop.client.appPublicationRequest.presenter;

import org.iplantc.de.admin.desktop.client.appPublicationRequest.AppPublicationRequestView;
import org.iplantc.de.admin.desktop.client.services.AppAdminServiceFacade;
import org.iplantc.de.admin.desktop.shared.Belphegor;
import org.iplantc.de.commons.client.ErrorHandler;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 * @author sriram
 */
public class AppPublicationRequestPresenter implements AppPublicationRequestView.Presenter {


    final AppPublicationRequestView view;

    @Inject
    AppAdminServiceFacade appService;

    @Inject
    public AppPublicationRequestPresenter(AppPublicationRequestView view) {
        this.view = view;
    }

    @Override
    public void getAppPublicationRequests() {
        view.setLoading(true);
        appService.getAppPublicationRequests(new AsyncCallback<Splittable>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught.getMessage(), caught);
                view.setLoading(false);
            }

            @Override
            public void onSuccess(Splittable result) {
                view.setLoading(false);
                view.setRequests(result.get("publication_requests"));
            }
        });
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view);
        view.load(this);
        getAppPublicationRequests();
    }

    @Override
    public void publishApp(String appId,
                           String systemId) {
        view.setLoading(true);
        appService.publishApp(appId, systemId, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught.getMessage(), caught);
                view.setLoading(false);
            }

            @Override
            public void onSuccess(String result) {
                getAppPublicationRequests();
            }
        });

    }

    @Override
    public void setViewDebugId(String baseId) {
        view.asWidget().ensureDebugId(baseId + Belphegor.AppPublicationRequestsIds.VIEW);
    }
}
