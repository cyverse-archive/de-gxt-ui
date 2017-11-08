package org.iplantc.de.diskResource.client.views.toolbar.dialogs;

import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.diskResource.client.FileUploadByUrlView;
import org.iplantc.de.diskResource.client.gin.factory.FileUploadByUrlViewFactory;

import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.Status;
import com.sencha.gxt.widget.core.client.event.InvalidEvent;
import com.sencha.gxt.widget.core.client.event.ValidEvent;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;

import java.util.Map;

/**
 * @author jstroot
 */
public class FileUploadByUrlDialog extends IPlantDialog implements HasPending<Map.Entry<Field<String>, Status>>,
                                                                   InvalidEvent.InvalidHandler,
                                                                   ValidEvent.ValidHandler {

    private FileUploadByUrlViewFactory factory;
    private FileUploadByUrlView view;

    @Inject
    public FileUploadByUrlDialog(FileUploadByUrlView.FileUploadByUrlViewAppearance appearance,
                                 FileUploadByUrlViewFactory factory) {
        this.factory = factory;
        setAutoHide(false);
        setHideOnButtonClick(false);
        // Reset the "OK" button text.
        getOkButton().setText(appearance.urlImport());
        getOkButton().setEnabled(false);
        setHeading(appearance.importLabel());
    }

    public void show(Folder uploadDest) {
        this.view = factory.create(uploadDest);

        view.addInvalidHandler(this);
        view.addValidHandler(this);

        setWidget(view);
        super.show();
    }

    public Map<Field<String>, Status> getFieldToStatusMap() {
        return view.getFieldToStatusMap();
    }

    @Override
    public boolean addPending(Map.Entry<Field<String>, Status> obj) {
        return view.addPending(obj);
    }

    @Override
    public boolean hasPending() {
        return view.hasPending();
    }

    @Override
    public boolean removePending(Map.Entry<Field<String>, Status> obj) {
        return view.removePending(obj);
    }

    @Override
    public int getNumPending() {
        return view.getNumPending();
    }

    @Override
    public void onInvalid(InvalidEvent event) {
        getOkButton().setEnabled(false);
    }

    @Override
    public void onValid(ValidEvent event) {
        getOkButton().setEnabled(FormPanelHelper.isValid(this, true) && view.isValidForm());
    }
}
