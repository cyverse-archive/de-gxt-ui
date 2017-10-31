package org.iplantc.de.diskResource.client.events.selection;

import org.iplantc.de.client.models.genomes.Genome;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A GWT event that gets fired when the user selects the "Import Genome" button in GenomeSearchView
 */
public class ImportGenomeFromCogeSelected
        extends GwtEvent<ImportGenomeFromCogeSelected.ImportGenomeFromCogeSelectedHandler> {
    public interface ImportGenomeFromCogeSelectedHandler extends EventHandler {
        void onImportGenomeFromCogeSelected(ImportGenomeFromCogeSelected event);
    }

    public interface HasImportGenomeFromCogeSelectedHandlers {
        HandlerRegistration addImportGenomeFromCogeSelectedHandler(ImportGenomeFromCogeSelectedHandler handler);
    }

    private Genome selectedGenome;

    public ImportGenomeFromCogeSelected(Genome selectedGenome) {
        this.selectedGenome = selectedGenome;
    }

    public static Type<ImportGenomeFromCogeSelectedHandler> TYPE =
            new Type<ImportGenomeFromCogeSelectedHandler>();

    public Type<ImportGenomeFromCogeSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(ImportGenomeFromCogeSelectedHandler handler) {
        handler.onImportGenomeFromCogeSelected(this);
    }

    public Genome getSelectedGenome() {
        return selectedGenome;
    }
}
