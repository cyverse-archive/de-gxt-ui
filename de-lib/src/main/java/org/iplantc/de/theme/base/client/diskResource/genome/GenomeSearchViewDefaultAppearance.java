package org.iplantc.de.theme.base.client.diskResource.genome;

import org.iplantc.de.diskResource.client.GenomeSearchView;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.theme.base.client.diskResource.DiskResourceMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;

import com.sencha.gxt.widget.core.client.box.MessageBox;

public class GenomeSearchViewDefaultAppearance implements GenomeSearchView.GenomeSearchViewAppearance {

    private DiskResourceMessages diskResourceMessages;
    private IplantResources iplantResources;

    public GenomeSearchViewDefaultAppearance() {
        this(GWT.<DiskResourceMessages>create(DiskResourceMessages.class),
             GWT.<IplantResources>create(IplantResources.class));
    }

    public GenomeSearchViewDefaultAppearance(DiskResourceMessages diskResourceMessages,
                                             IplantResources iplantResources) {
        this.diskResourceMessages = diskResourceMessages;
        this.iplantResources = iplantResources;
    }

    @Override
    public String cogeSearchError() {
        return diskResourceMessages.cogeSearchError();
    }

    @Override
    public String cogeImportGenomeError() {
        return diskResourceMessages.cogeImportGenomeError();
    }

    @Override
    public String importFromCoge() {
        return diskResourceMessages.importFromCoge();
    }

    @Override
    public String cogeImportGenomeSuccess() {
        return diskResourceMessages.cogeImportGenomeSuccess();
    }

    @Override
    public String heading() {
        return diskResourceMessages.heading();
    }

    @Override
    public String loading() {
        return diskResourceMessages.loading();
    }

    @Override
    public String importText() {
        return diskResourceMessages.importText();
    }

    @Override
    public String searchGenome() {
        return diskResourceMessages.searchGenome();
    }

    @Override
    public String organismName() {
        return diskResourceMessages.organismName();
    }

    @Override
    public String version() {
        return diskResourceMessages.version();
    }

    @Override
    public String chromosomeCount() {
        return diskResourceMessages.chromosomeCount();
    }

    @Override
    public String sequenceType() {
        return diskResourceMessages.sequenceType();
    }

    @Override
    public String noRecords() {
        return diskResourceMessages.noRecords();
    }

    @Override
    public String dialogWidth() {
        return "600px";
    }

    @Override
    public String dialogHeight() {
        return "200px";
    }

    @Override
    public ImageResource importDataIcon() {
        return iplantResources.importDataIcon();
    }

    @Override
    public String searchLabel() {
        return diskResourceMessages.searchGenomeLabel();
    }

    @Override
    public ImageResource infoIcon() {
        return MessageBox.ICONS.info();
    }

}
