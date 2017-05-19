/**
 *
 */
package org.iplantc.de.diskResource.client.views.sharing.dialogs;


import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.commons.client.views.sharing.SharingAppearance;
import org.iplantc.de.commons.client.presenter.SharingPresenter;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.diskResource.client.gin.factory.DataSharingPresenterFactory;

import com.google.common.base.Preconditions;
import com.google.gwt.user.client.ui.HTML;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import java.util.List;

/**
 * @author sriram, jstroot
 */
public class DataSharingDialog extends IPlantDialog implements SelectHandler {

    private SharingPresenter sharingPresenter;
    private DataSharingPresenterFactory factory;

    @Inject
    DataSharingDialog(DataSharingPresenterFactory factory,
                      final SharingAppearance appearance) {
        super(true);
        this.factory = factory;
        setPixelSize(600, 500);
        setHideOnButtonClick(true);
        setModal(true);
        setResizable(false);
        addHelp(new HTML(appearance.sharePermissionsHelp()));
        setHeading(appearance.manageSharing());
        setOkButtonText(appearance.done());
        addOkButtonSelectHandler(this);

    }

    @Override
    public void onSelect(SelectEvent event) {
        Preconditions.checkNotNull(sharingPresenter);
        sharingPresenter.processRequest();
    }

    public void show(final List<DiskResource> resourcesToShare) {
        sharingPresenter = factory.create(resourcesToShare);
        sharingPresenter.go(this);
        super.show();
    }

    @Override
    public void show() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This method is not supported for this class. " +
                                                    "Use show(List<DiskResource>) instead.");
    }

}
