package org.iplantc.de.diskResource.client;

import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.diskResource.client.events.selection.ImportGenomeFromCogeSelected;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

public interface GenomeSearchView extends IsWidget,
                                          IsMaskable,
                                          ImportGenomeFromCogeSelected.HasImportGenomeFromCogeSelectedHandlers {

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

    interface GenomeSearchPresenter {

        void go(HasOneWidget container);

        void setViewDebugId(String baseID);
    }
}
