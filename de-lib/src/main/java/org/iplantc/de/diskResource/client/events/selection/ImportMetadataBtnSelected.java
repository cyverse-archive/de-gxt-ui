package org.iplantc.de.diskResource.client.events.selection;

import org.iplantc.de.client.models.avu.Avu;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

public class ImportMetadataBtnSelected
        extends GwtEvent<ImportMetadataBtnSelected.ImportMetadataBtnSelectedHandler> {
    public static interface ImportMetadataBtnSelectedHandler extends EventHandler {
        void onImportMetadataBtnSelected(ImportMetadataBtnSelected event);
    }

    public interface HasImportMetadataBtnSelectedHandlers {
        HandlerRegistration addImportMetadataBtnSelectedHandler(ImportMetadataBtnSelectedHandler handler);
    }

    private List<Avu> avuList;

    public ImportMetadataBtnSelected(List<Avu> avuList) {
        this.avuList = avuList;
    }

    public static Type<ImportMetadataBtnSelectedHandler> TYPE =
            new Type<ImportMetadataBtnSelectedHandler>();

    public Type<ImportMetadataBtnSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(ImportMetadataBtnSelectedHandler handler) {
        handler.onImportMetadataBtnSelected(this);
    }

    public List<Avu> getAvuList() {
        return avuList;
    }
}
