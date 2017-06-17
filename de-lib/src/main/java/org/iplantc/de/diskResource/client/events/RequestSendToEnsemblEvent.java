package org.iplantc.de.diskResource.client.events;

import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.diskResource.client.events.RequestSendToEnsemblEvent.RequestSendToEnsemblEventHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import java.util.List;

public class RequestSendToEnsemblEvent extends GwtEvent<RequestSendToEnsemblEventHandler> {

    public static final GwtEvent.Type<RequestSendToEnsemblEventHandler> TYPE = new GwtEvent.Type<>();

    public interface RequestSendToEnsemblEventHandler extends EventHandler {

        void onRequestSendToEnsembl(RequestSendToEnsemblEvent event);
    }

    private final List<DiskResource> resourcesToSend;

    public RequestSendToEnsemblEvent(List<DiskResource> resourcesToSend) {
        this.resourcesToSend = resourcesToSend;
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<RequestSendToEnsemblEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RequestSendToEnsemblEventHandler handler) {
        handler.onRequestSendToEnsembl(this);

    }

    public List<DiskResource> getResourcesToSend() {
        return resourcesToSend;
    }
}
