package org.iplantc.de.admin.desktop.client.permIdRequest.views;

import org.iplantc.de.admin.desktop.client.permIdRequest.views.PermanentIdRequestView.PermanentIdRequestPresenterAppearance;
import org.iplantc.de.admin.desktop.shared.Belphegor;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.diskResource.client.MetadataView;
import org.iplantc.de.diskResource.client.presenters.callbacks.DiskResourceMetadataUpdateCallback;
import org.iplantc.de.resources.client.messages.I18N;

import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

/**
 * @author sriram
 */
public class MetadataDialog extends IPlantDialog {

    private class CancelSelectHandler implements SelectEvent.SelectHandler {
        @Override
        public void onSelect(SelectEvent event) {
            hide();
        }
    }

    private class OkSelectHandler implements SelectEvent.SelectHandler {
        @Override
        public void onSelect(SelectEvent event) {
            if (!view.isValid()) {
                ConfirmMessageBox cmb =
                        new ConfirmMessageBox(I18N.DISPLAY.error(), appearance.metadataSaveError());
                cmb.addDialogHideHandler(new DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton().equals(PredefinedButton.YES)) {
                            mask(I18N.DISPLAY.loadingMask());
                            meta_pre.setDiskResourceMetadata(new DiskResourceMetadataUpdateCallback(
                                    MetadataDialog.this));
                        }

                    }
                });
                cmb.show();
            } else {
                mask(I18N.DISPLAY.loadingMask());
                meta_pre.setDiskResourceMetadata(new DiskResourceMetadataUpdateCallback(MetadataDialog.this));
            }
        }
    }

    MetadataView.Presenter meta_pre;
    MetadataView view;
    PermanentIdRequestPresenterAppearance appearance;
    private final DiskResourceServiceFacade drsvc;

    @Inject
    public MetadataDialog(MetadataView.Presenter meta_pre,
                          MetadataView view,
                          PermanentIdRequestPresenterAppearance appearance,
                          DiskResourceServiceFacade drsvc) {
        this.view = view;
        this.meta_pre = meta_pre;
        this.appearance = appearance;
        this.drsvc = drsvc;

        setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
        setHeading("Metadata");
        setSize("600px", "400px");
        getButton(PredefinedButton.OK).addSelectHandler(new OkSelectHandler());
        getButton(PredefinedButton.CANCEL).addSelectHandler(new CancelSelectHandler());
    }


    public void show(Folder selectedFolder) {
        view.setEditable(DiskResourceUtil.getInstance().isWritable(selectedFolder));
        meta_pre.go(this, selectedFolder);
        super.show();
        ensureDebugId(Belphegor.PermIds.METADATA_DIALOG);
        show();
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        getButton(PredefinedButton.OK).ensureDebugId(baseID + Belphegor.PermIds.OK);
        view.asWidget().ensureDebugId(baseID + Belphegor.PermIds.VIEW);
    }
}
