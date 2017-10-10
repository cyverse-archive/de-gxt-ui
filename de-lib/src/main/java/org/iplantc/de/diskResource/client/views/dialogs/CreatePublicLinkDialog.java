package org.iplantc.de.diskResource.client.views.dialogs;

import static com.sencha.gxt.widget.core.client.Dialog.PredefinedButton.OK;

import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.diskResource.client.DataLinkView;
import org.iplantc.de.diskResource.client.gin.factory.DataLinkPresenterFactory;
import org.iplantc.de.diskResource.share.DiskResourceModule;

import com.google.gwt.user.client.ui.HTML;
import com.google.inject.Inject;

import java.util.List;

public class CreatePublicLinkDialog extends IPlantDialog {

    private DataLinkPresenterFactory factory;
    DataLinkView.Presenter presenter;

    @Inject
    public CreatePublicLinkDialog(DataLinkPresenterFactory dataLinkPresenterFactory,
                                  DataLinkView.Appearance appearance) {
        super(true);
        this.factory = dataLinkPresenterFactory;
        setPredefinedButtons(OK);
        setHeading(appearance.manageDataLinks());
        setHideOnButtonClick(true);
        setWidth(appearance.manageDataLinksDialogWidth());
        setHeight(appearance.manageDataLinksDialogHeight());
        setOkButtonText(appearance.done());
        addHelp(new HTML(appearance.manageDataLinksHelp()));
    }

    public void show(List<DiskResource> selectedDiskResources) {
        presenter = factory.createDataLinkPresenter(selectedDiskResources);
        presenter.go(this);

        super.show();

        ensureDebugId(DiskResourceModule.Ids.CREATE_PUBLIC_LINK_DLG);
    }

    @Override
    public void show() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("This method is not supported. Use 'show(List<DiskResource)' instead.");
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        presenter.setViewDebugId(baseID);
        getButton(PredefinedButton.OK).ensureDebugId(baseID + DiskResourceModule.Ids.DONE_BTN);
    }
}
