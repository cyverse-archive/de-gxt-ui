package org.iplantc.de.diskResource.client.presenters.callbacks;

import org.iplantc.de.client.models.errors.diskResources.DiskResourceErrorAutoBeanFactory;
import org.iplantc.de.client.models.errors.diskResources.ErrorUpdateMetadata;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.http.client.Response;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

/**
 * @author jstroot psarando
 */
@SuppressWarnings("unusable-by-js")
public class DiskResourceMetadataUpdateCallback extends DiskResourceServiceAsyncCallback<String> {

    private final DiskResourceCallbackAppearance appearance = GWT.create(DiskResourceCallbackAppearance.class);
    private final ReactSuccessCallback resolve;
    private final ReactErrorCallback reject;

    public DiskResourceMetadataUpdateCallback(ReactSuccessCallback resolve, ReactErrorCallback reject) {
        super(null);
        this.resolve = resolve;
        this.reject = reject;
    }

    @Override
    public void onSuccess(String result) {
        super.onSuccess(result);

        if (resolve != null) {
            resolve.onSuccess(null);
        }

        IplantAnnouncer.getInstance().schedule(new SuccessAnnouncementConfig(appearance.metadataSuccess()));
    }

    @Override
    public void onFailure(Throwable caught) {
        try {
            DiskResourceErrorAutoBeanFactory factory = GWT.create(DiskResourceErrorAutoBeanFactory.class);
            final ErrorUpdateMetadata updateMetadataErr =
                    AutoBeanCodex.decode(factory, ErrorUpdateMetadata.class, caught.getMessage()).as();

            ErrorHandler.post(updateMetadataErr, caught);
        }
        catch (Exception e) {
            ErrorHandler.postReact(getErrorMessageDefault(), caught);
        }

        if (reject != null) {
            reject.onError(Response.SC_INTERNAL_SERVER_ERROR, caught.getMessage());
        }
    }

    @Override
    protected String getErrorMessageDefault() {
        return appearance.metadataUpdateFailed();
    }

}
