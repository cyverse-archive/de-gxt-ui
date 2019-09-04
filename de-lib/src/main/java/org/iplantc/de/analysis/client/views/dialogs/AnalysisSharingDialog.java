package org.iplantc.de.analysis.client.views.dialogs;

import org.iplantc.de.analysis.client.gin.factory.AnalysisSharingPresenterFactory;
import org.iplantc.de.analysis.shared.AnalysisModule;
import org.iplantc.de.client.models.analysis.Analysis;
import org.iplantc.de.commons.client.presenter.SharingPresenter;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import java.util.List;

public class AnalysisSharingDialog extends IPlantDialog implements SelectHandler {

    private SharingPresenter sharingPresenter;
    private AnalysisSharingPresenterFactory factory;

    @Inject
    public AnalysisSharingDialog(AnalysisSharingPresenterFactory factory) {
        super(false);
        this.factory = factory;
        setPixelSize(600, 500);
        setHideOnButtonClick(true);
        setModal(true);
        setResizable(false);
        // addHelp(new HTML(appearance.sharePermissionsHelp()));
        setHeading("Manage Sharing");
        setOkButtonText("Done");
        addOkButtonSelectHandler(this);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        sharingPresenter.setViewDebugId(baseID);
        getOkButton().ensureDebugId(baseID + AnalysisModule.Ids.DONE_BTN);
    }

    @Override
    public void onSelect(SelectEvent event) {
        Preconditions.checkNotNull(sharingPresenter);
        sharingPresenter.processRequest();
    }

    public void show(List<Analysis> analyses) {
        sharingPresenter = factory.create(analyses);
        sharingPresenter.go(this);
        super.show();

        ensureDebugId(AnalysisModule.Ids.SHARING_DLG);
    }

    @Override
    public void show() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This method is not supported for this class. "
                                                + "Use show(List<Analysis>) instead.");
    }
}
