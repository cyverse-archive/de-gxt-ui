package org.iplantc.de.diskResource.client;

import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.diskResource.client.events.selection.ImportGenomeFromCogeSelected;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * An interface for the view when searching genomes in the Data window when choosing "Import Genome from Coge"
 */
public interface GenomeSearchView extends IsWidget,
                                          IsMaskable,
                                          ImportGenomeFromCogeSelected.HasImportGenomeFromCogeSelectedHandlers {

    /**
     * Appearance class for the GenomeSearchView
     */
    interface GenomeSearchViewAppearance {
        String cogeSearchError();

        String cogeImportGenomeError();

        String importFromCoge();

        String cogeImportGenomeSuccess();

        String heading();

        String loading();

        String importText();

        String searchGenome();

        String organismName();

        String version();

        String chromosomeCount();

        String sequenceType();

        String noRecords();

        String dialogWidth();

        String dialogHeight();

        ImageResource importDataIcon();

        String searchLabel();

        ImageResource infoIcon();
    }

    /**
     * Presenter which handles all the logic for the GenomeSearchView
     */
    interface GenomeSearchPresenter {

        /**
         * Adds the view to the dialog/window
         * @param container
         */
        void go(HasOneWidget container);

        /**
         * Sets the debug ID for the view
         * @param baseID
         */
        void setViewDebugId(String baseID);
    }
}
