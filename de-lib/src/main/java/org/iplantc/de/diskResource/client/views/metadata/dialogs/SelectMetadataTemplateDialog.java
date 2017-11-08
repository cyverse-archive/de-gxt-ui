package org.iplantc.de.diskResource.client.views.metadata.dialogs;

import org.iplantc.de.client.models.diskResources.MetadataTemplateInfo;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.diskResource.client.MetadataView;
import org.iplantc.de.diskResource.client.MetadataView.Presenter.Appearance;
import org.iplantc.de.diskResource.client.events.selection.MetadataInfoBtnSelected;
import org.iplantc.de.diskResource.client.views.metadata.SelectMetadataTemplateView;
import org.iplantc.de.diskResource.share.DiskResourceModule;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;

import java.util.List;

/**
 * A dialog that is displayed when the user either selects the "Download Template" button in the Data window
 * or chooses "Select Template..." in the ManageMetadata dialog
 */
public class SelectMetadataTemplateDialog extends IPlantDialog implements IsWidget,
                                                                          SelectionChangedEvent.SelectionChangedHandler<MetadataTemplateInfo>,
                                                                          MetadataInfoBtnSelected.HasMetadataInfoBtnSelectedHandlers {

    private MetadataView.Presenter.Appearance appearance;
    private SelectMetadataTemplateView view;

    @Inject
    public SelectMetadataTemplateDialog(Appearance appearance,
                                        SelectMetadataTemplateView view) {
        super();
        this.view = view;
        this.appearance = appearance;
        getOkButton().disable();
        setModal(false);
        setSize(appearance.dialogWidth(), appearance.dialogHeight());
        setHeading(appearance.selectTemplate());
        setWidget(view);

        view.addSelectionChangedHandler(this);
    }

    public void show(List<MetadataTemplateInfo> templates, boolean showDownloadColumn) {
        view.loadTemplates(templates);
        view.showDownloadColumn(showDownloadColumn);

        super.show();
        ensureDebugId(DiskResourceModule.MetadataIds.SELECT_TEMPLATE_DLG);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        getOkButton().ensureDebugId(baseID + DiskResourceModule.MetadataIds.SELECT_TEMPLATE_OK_BTN_ID);
        view.ensureDebugId(baseID + DiskResourceModule.Ids.VIEW);
    }

    public MetadataTemplateInfo getSelectedTemplate() {
        return view.getSelectedTemplate();
    }

    @Override
    public void onSelectionChanged(SelectionChangedEvent<MetadataTemplateInfo> event) {
        if (event.getSelection().size() == 0) {
            getOkButton().disable();
        } else {
            getOkButton().enable();
        }
    }

    @Override
    public HandlerRegistration addMetadataInfoBtnSelectedHandler(MetadataInfoBtnSelected.MetadataInfoBtnSelectedHandler handler) {
        return view.addMetadataInfoBtnSelectedHandler(handler);
    }
}
