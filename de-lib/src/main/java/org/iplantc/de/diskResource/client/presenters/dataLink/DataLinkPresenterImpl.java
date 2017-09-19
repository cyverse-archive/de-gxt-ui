package org.iplantc.de.diskResource.client.presenters.dataLink;

import org.iplantc.de.client.models.dataLink.DataLink;
import org.iplantc.de.client.models.dataLink.DataLinkFactory;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.commons.client.util.WindowUtil;
import org.iplantc.de.diskResource.client.DataLinkView;
import org.iplantc.de.diskResource.client.events.selection.AdvancedSharingSelected;
import org.iplantc.de.diskResource.client.events.selection.CreateDataLinkSelected;
import org.iplantc.de.diskResource.client.events.selection.DeleteDataLinkSelected;
import org.iplantc.de.diskResource.client.gin.factory.DataLinkViewFactory;
import org.iplantc.de.diskResource.client.presenters.callbacks.CreateDataLinkCallback;
import org.iplantc.de.diskResource.client.presenters.callbacks.DeleteDataLinksCallback;
import org.iplantc.de.diskResource.client.presenters.callbacks.ListDataLinksCallback;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import java.util.List;

/**
 * @author jstroot
 */
public class DataLinkPresenterImpl implements DataLinkView.Presenter,
                                              CreateDataLinkSelected.CreateDataLinkSelectedHandler,
                                              AdvancedSharingSelected.AdvancedSharingSelectedHandler {

    private final DataLinkView view;
    private final DiskResourceServiceFacade drService;
    private final DataLinkFactory dlFactory;
    private final DiskResourceUtil diskResourceUtil;
    private final DataLinkView.Appearance appearance;

    @Inject
    DataLinkPresenterImpl(final DiskResourceServiceFacade drService,
                          final DataLinkViewFactory dataLinkViewFactory,
                          final DataLinkFactory dlFactory,
                          final DiskResourceUtil diskResourceUtil,
                          final DataLinkView.Appearance appearance,
                          @Assisted List<DiskResource> resources) {
        this.drService = drService;
        this.dlFactory = dlFactory;
        this.appearance = appearance;
        this.diskResourceUtil = diskResourceUtil;
        view = dataLinkViewFactory.create(this, resources);

        view.addCreateDataLinkSelectedHandler(this);
        view.addAdvancedSharingSelectedHandler(this);

        // Remove Folders
        List<DiskResource> allowedResources = createDiskResourcesList();
        for(DiskResource m : resources){
            if(!(m instanceof Folder)){
                allowedResources.add(m);
            }
        }
        // Retrieve tickets for root nodes
        getExistingDataLinks(allowedResources);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    void getExistingDataLinks(List<DiskResource> resources) {
        view.addRoots(resources);
        drService.listDataLinks(diskResourceUtil.asStringPathList(resources),
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
    public String getSelectedDataLinkDownloadUrl() {
        DiskResource model = view.getTree().getSelectionModel().getSelectedItem();
        if (model instanceof DataLink) {
            return ((DataLink)model).getDownloadUrl();
        }
        return null;
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
