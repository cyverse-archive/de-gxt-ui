/**
 *
 */
package org.iplantc.de.tools.client.presenter;

import org.iplantc.de.client.gin.ServicesInjector;
import org.iplantc.de.client.models.toolRequests.ToolRequestAutoBeanFactory;
import org.iplantc.de.client.services.ToolRequestServiceFacade;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;
import org.iplantc.de.tools.client.views.requests.NewToolRequestFormView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 * @author sriram
 */
public class NewToolRequestFormPresenterImpl implements NewToolRequestFormView.Presenter {

    private static final ToolRequestAutoBeanFactory REQ_FACTORY = GWT.create(ToolRequestAutoBeanFactory.class);

    private final ToolRequestServiceFacade reqServices =
            ServicesInjector.INSTANCE.getToolRequestServiceProvider();

    private final NewToolRequestFormView view;

    @Inject
    NewToolRequestFormView.NewToolRequestFormViewAppearance appearance;

    @Inject
    NewToolRequestFormPresenterImpl(final NewToolRequestFormView view) {
        this.view = view;
    }

    /* (non-Javadoc)
     * @see org.iplantc.de.commons.client.presenter.Presenter#go(com.google.gwt.user.client.ui.HasOneWidget)
     */
    @Override
    public void go(final HasOneWidget container) {
        container.setWidget(view);
    }


    @Override
    public void submitRequest(Splittable toolRequest,
                               ReactSuccessCallback callback,
                               ReactErrorCallback errorCallback) {

        GWT.log("Tool request ->" + toolRequest.getPayload());
     /*   reqServices.requestInstallation(req, new AsyncCallback<ToolRequestDetails>() {
            @Override
            public void onFailure(final Throwable caught) {
            }

            @Override
            public void onSuccess(final ToolRequestDetails response) {
            }
        });*/
    }

    @Override
    public void onClose() {
        view.onClose();
    }









}
