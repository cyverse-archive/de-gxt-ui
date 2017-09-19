package org.iplantc.de.diskResource.client.presenters.dataLink;

import org.iplantc.de.client.models.dataLink.DataLink;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.commons.client.util.WindowUtil;
import org.iplantc.de.diskResource.client.DataLinkView;
import org.iplantc.de.diskResource.client.events.selection.AdvancedSharingSelected;
import org.iplantc.de.diskResource.client.events.selection.CreateDataLinkSelected;
import org.iplantc.de.diskResource.client.events.selection.DeleteDataLinkSelected;
import org.iplantc.de.diskResource.client.events.selection.ShowDataLinkSelected;
import org.iplantc.de.diskResource.client.presenters.callbacks.CreateDataLinkCallback;
import org.iplantc.de.diskResource.client.presenters.callbacks.DeleteDataLinksCallback;
import org.iplantc.de.diskResource.client.presenters.callbacks.ListDataLinksCallback;
import org.iplantc.de.diskResource.client.views.dialogs.DataLinkDialog;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import java.util.List;

/**
 * @author jstroot
 */
public class DataLinkPresenterImpl implements DataLinkView.Presenter,
                                              CreateDataLinkSelected.CreateDataLinkSelectedHandler,
                                              AdvancedSharingSelected.AdvancedSharingSelectedHandler,
                                              ShowDataLinkSelected.ShowDataLinkSelectedHandler,
                                              DeleteDataLinkSelected.DeleteDataLinkSelectedHandler {

    private final DataLinkView view;
    private final DiskResourceServiceFacade drService;
    private final DiskResourceUtil diskResourceUtil;
    private final DataLinkView.Appearance appearance;

    @Inject AsyncProviderWrapper<DataLinkDialog> dataLinkDlgProvider;

    @Inject
    DataLinkPresenterImpl(final DiskResourceServiceFacade drService,
                          final DataLinkView view,
                          final DiskResourceUtil diskResourceUtil,
                          final DataLinkView.Appearance appearance,
                          @Assisted List<DiskResource> resources) {
        this.drService = drService;
        this.appearance = appearance;
        this.diskResourceUtil = diskResourceUtil;
        this.view = view;

        view.addCreateDataLinkSelectedHandler(this);
        view.addAdvancedSharingSelectedHandler(this);
        view.addShowDataLinkSelectedHandler(this);
        view.addDeleteDataLinkSelectedHandler(this);

        // Retrieve tickets for root nodes
        getExistingDataLinks(resources);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    void getExistingDataLinks(List<DiskResource> resources) {
        // Remove Folders
        List<DiskResource> allowedResources = createDiskResourcesList();
        for(DiskResource m : resources){
            if(!(m instanceof Folder)){
                allowedResources.add(m);
            }
        }

        view.addRoots(allowedResources);
        drService.listDataLinks(diskResourceUtil.asStringPathList(allowedResources),
                                new ListDataLinksCallback(view.getTree()));
    }

    @Override
    public void onDeleteDataLinkSelected(DeleteDataLinkSelected event) {
        DataLink link = event.getLink();
        drService.deleteDataLinks(Lists.newArrayList(link.getId()),
                                  new DeleteDataLinksCallback(view));
    }

    @Override
    public void onCreateDataLinkSelected(CreateDataLinkSelected event) {
        List<DiskResource> selectedItems = event.getSelectedDiskResources();
        final List<String> drResourceIds = createDiskResourceIdsList();
        for(DiskResource dr : selectedItems){
            if(!(dr instanceof DataLink)){
                drResourceIds.add(dr.getPath());
            }
        }

        view.mask(appearance.loadingMask());
        drService.createDataLinks(drResourceIds, new CreateDataLinkCallback(view));
    }

    @Override
    public void onShowDataLinkSelected(ShowDataLinkSelected event) {
        DiskResource selectedResource = event.getSelectedResource();
        if (selectedResource instanceof DataLink) {
            String downloadUrl = ((DataLink)selectedResource).getDownloadUrl();
            dataLinkDlgProvider.get(new AsyncCallback<DataLinkDialog>() {
                @Override
                public void onFailure(Throwable throwable) { }

                @Override
                public void onSuccess(DataLinkDialog dialog) {
                    dialog.show(downloadUrl);
                }
            });
        }
    }

    @Override
    public void onAdvancedSharingSelected(AdvancedSharingSelected event) {
        DiskResource selectedResource = event.getSelectedResource();
        if (selectedResource instanceof DataLink) {
            String url = ((DataLink)selectedResource).getDownloadPageUrl();
            WindowUtil.open(url);
        }
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view.asWidget());
    }

    List<DiskResource> createDiskResourcesList() {
        return Lists.newArrayList();
    }

    List<String> createDiskResourceIdsList() {
        return Lists.newArrayList();
    }
}
