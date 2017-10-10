package org.iplantc.de.diskResource.client;

import org.iplantc.de.client.models.IsMaskable;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

public interface GenomeSearchView extends IsWidget,
                                          IsMaskable {

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
    }

    interface GenomeSearchPresenter {

        void go(HasOneWidget container);

        void importGenomeFromCoge(Integer id);
    }
}
