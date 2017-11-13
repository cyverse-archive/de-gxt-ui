package org.iplantc.de.diskResource.client.events.selection;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A GWT event that fires when the user selects the Import Genomes from Coge button in the Data window
 */
public class ImportFromCogeBtnSelected
        extends GwtEvent<ImportFromCogeBtnSelected.ImportFromCogeBtnSelectedHandler> {
    public static interface ImportFromCogeBtnSelectedHandler extends EventHandler {
        void onImportFromCogeBtnSelected(ImportFromCogeBtnSelected event);
    }

    public interface HasImportFromCogeBtnSelectedHandlers {
        HandlerRegistration addImportFromCogeBtnSelectedHandler(ImportFromCogeBtnSelectedHandler handler);
    }

    public static Type<ImportFromCogeBtnSelectedHandler> TYPE =
            new Type<ImportFromCogeBtnSelectedHandler>();

    public Type<ImportFromCogeBtnSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(ImportFromCogeBtnSelectedHandler handler) {
        handler.onImportFromCogeBtnSelected(this);
    }
}
