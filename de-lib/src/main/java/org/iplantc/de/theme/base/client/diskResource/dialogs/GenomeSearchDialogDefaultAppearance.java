package org.iplantc.de.theme.base.client.diskResource.dialogs;

import org.iplantc.de.diskResource.client.views.dialogs.GenomeSearchDialog;
import org.iplantc.de.theme.base.client.diskResource.DiskResourceMessages;

import com.google.gwt.core.client.GWT;

public class GenomeSearchDialogDefaultAppearance implements GenomeSearchDialog.Appearance {


    private DiskResourceMessages diskResourceMessages;

    public GenomeSearchDialogDefaultAppearance() {
        this(GWT.<DiskResourceMessages>create(DiskResourceMessages.class));
    }

    public GenomeSearchDialogDefaultAppearance(DiskResourceMessages diskResourceMessages) {
        this.diskResourceMessages = diskResourceMessages;
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

}
