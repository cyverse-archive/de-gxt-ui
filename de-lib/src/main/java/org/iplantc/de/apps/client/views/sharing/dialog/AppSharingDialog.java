/**
 * @author sriram
 */

package org.iplantc.de.apps.client.views.sharing.dialog;

import org.iplantc.de.apps.client.presenter.sharing.AppSharingPresenter;
import org.iplantc.de.apps.client.views.sharing.AppSharingView;
import org.iplantc.de.apps.client.views.sharing.AppSharingViewImpl;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.services.AppUserServiceFacade;
import org.iplantc.de.client.sharing.SharingPresenter;
import org.iplantc.de.collaborators.client.util.CollaboratorsUtil;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import java.util.List;

public class AppSharingDialog extends IPlantDialog implements SelectHandler {

    private final AppUserServiceFacade appService;
    private SharingPresenter sharingPresenter;

    @Inject
    CollaboratorsUtil collaboratorsUtil;

    @Inject
    AppSharingDialog(final AppUserServiceFacade appService) {
        super(false);
        this.appService = appService;
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
        AppSharingView view = new AppSharingViewImpl();
        view.setSelectedApps(resourcesToShare);
        sharingPresenter = new AppSharingPresenter(appService, resourcesToShare, view, collaboratorsUtil);
        sharingPresenter.go(this);
        super.show();
    }

    @Override
    public void show() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This method is not supported for this class. ");
    }

}
