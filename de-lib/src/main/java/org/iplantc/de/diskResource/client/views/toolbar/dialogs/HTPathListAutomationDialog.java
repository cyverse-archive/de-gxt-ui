package org.iplantc.de.diskResource.client.views.toolbar.dialogs;

import org.iplantc.de.client.models.diskResources.HTPathListRequest;
import org.iplantc.de.client.models.viewer.InfoType;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.diskResource.client.HTPathListAutomationView;

import com.google.inject.Inject;

import java.util.List;

/**
 * Created by sriram on 7/26/17.
 */
public class HTPathListAutomationDialog extends IPlantDialog {

    private HTPathListAutomationView.HTPathListAutomationAppearance htAppearance;
    private HTPathListAutomationView view;

    @Inject
    public HTPathListAutomationDialog(HTPathListAutomationView.HTPathListAutomationAppearance htAppearance,
                                      HTPathListAutomationView view) {
        this.htAppearance = htAppearance;
        this.view = view;
        setWidget(view);
        setHideOnButtonClick(false);
        setHeading(htAppearance.dialogHeading());
        setSize(htAppearance.dialogWidth(), htAppearance.dialogHeight());
        setModal(false);
    }

    public void show(List<InfoType> infoTypes) {
        view.addInfoTypes(infoTypes);

        super.show();
    }

    public HTPathListRequest getRequest() {
        return view.getRequest();
    }

    public boolean isValid() {
        return view.isValid();
    }

    @Override
    public void show() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("This method is not supported for this class. Use show(String List<InfoType>) instead.");
    }
}
