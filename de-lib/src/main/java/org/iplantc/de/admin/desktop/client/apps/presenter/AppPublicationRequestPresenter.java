package org.iplantc.de.admin.desktop.client.apps.presenter;

import org.iplantc.de.admin.apps.client.AppPublicationRequestView;
import org.iplantc.de.admin.desktop.client.services.AppAdminServiceFacade;
import org.iplantc.de.admin.desktop.shared.Belphegor;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 * @author sriram
 */
public class AppPublicationRequestPresenter implements AppPublicationRequestView.Presenter {


    final AppPublicationRequestView view;
    private IplantAnnouncer announcer;

    @Inject
    AppAdminServiceFacade appService;

    @Inject
    AppPublicationRequestView.AppPublicationRequestAppearance appearance;

    @Inject
    public AppPublicationRequestPresenter(AppPublicationRequestView view,
                                          IplantAnnouncer announcer,
                                          AppPublicationRequestView.AppPublicationRequestAppearance appearance) {
        this.view = view;
        this.announcer = announcer;
        this.appearance = appearance;
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
                           String appName,
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
                announcer.schedule(new SuccessAnnouncementConfig(appearance.publicationRequestSuccess(
                        appName)));
                getAppPublicationRequests();
            }
        });

    }

    @Override
    public void setViewDebugId(String baseId) {
        view.asWidget().ensureDebugId(baseId + Belphegor.AppPublicationRequestsIds.VIEW);
    }
}
