/**
 * @author sriram
 */

package org.iplantc.de.apps.client.views.sharing.dialog;

import org.iplantc.de.apps.client.gin.factory.AppSharingPresenterFactory;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.sharing.SharingPresenter;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import java.util.List;

public class AppSharingDialog extends IPlantDialog implements SelectHandler {

    private AppSharingPresenterFactory factory;
    private SharingPresenter sharingPresenter;

    @Inject
    AppSharingDialog(AppSharingPresenterFactory factory) {
        super(false);
        this.factory = factory;
        setPixelSize(600, 500);
        setHideOnButtonClick(true);
        setModal(true);
        setResizable(false);
        /*
        TODO: Add contextual help
        addHelp(new HTML(appearance.sharePermissionsHelp()));
        */
        setHeading("Manage Sharing");
        setOkButtonText("Done");
        addOkButtonSelectHandler(this);
    }

    @Override
    public void onSelect(SelectEvent event) {
        Preconditions.checkNotNull(sharingPresenter);
        sharingPresenter.processRequest();
    }

    public void show(final List<App> resourcesToShare) {
        sharingPresenter = factory.create(resourcesToShare);
        sharingPresenter.go(this);
        super.show();
    }

    @Override
    public void show() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This method is not supported for this class. ");
    }

}
