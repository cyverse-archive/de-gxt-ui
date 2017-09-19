package org.iplantc.de.diskResource.client.presenters.callbacks;

import org.iplantc.de.client.models.dataLink.UpdateTicketResponse;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.diskResource.client.DataLinkView;

import com.google.gwt.core.client.GWT;

import com.sencha.gxt.widget.core.client.tree.Tree;

import java.util.List;

/**
 * @author jstroot
 */
public class DeleteDataLinksCallback extends DiskResourceServiceCallback<UpdateTicketResponse> {

    private final Tree<DiskResource, DiskResource> tree;
    private final DataLinkView view;
    private final DiskResourceCallbackAppearance appearance = GWT.create(DiskResourceCallbackAppearance.class);

    public DeleteDataLinksCallback(final DataLinkView view) {
        super(view);
        this.view = view;
        this.tree = view.getTree();
    }

    @Override
    public void onFailure(Integer statusCode, Throwable caught) {
        super.onFailure(statusCode,caught);
        ErrorHandler.post(appearance.deleteDataLinksError(), caught);
        view.unmask();
    }

    @Override
    protected String getErrorMessageDefault() {
        return appearance.deleteDataLinksError();
    }

    @Override
    public void onSuccess(UpdateTicketResponse result) {
        List<String> tickets = result.getTickets();
        tickets.forEach(ticket -> {
            String ticketId = ticket.replace("\"", "");
            DiskResource m = tree.getStore().findModelWithKey(ticketId);
            if (m != null) {
                tree.getStore().remove(m);
            }
        });

        view.unmask();

    }
}
