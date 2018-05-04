package org.iplantc.de.diskResource.client.views.details;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.models.sharing.PermissionValue;
import org.iplantc.de.client.models.viewer.InfoType;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.commons.client.util.CyVerseReactComponents;
import org.iplantc.de.diskResource.client.DetailsView;
import org.iplantc.de.diskResource.client.events.DiskResourceSelectionChangedEvent;
import org.iplantc.de.diskResource.client.events.FetchDetailsCompleted;
import org.iplantc.de.diskResource.share.DiskResourceModule;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * View is updated on grid selection changed.
 * View is updated when a store update event occurs
 *
 * @author jstroot
 */
public class DetailsViewImpl implements DetailsView {

    private List<InfoType> infoTypes;
    private Presenter presenter;


    HTMLPanel panel;
    final Appearance appearance;

    private final Logger LOG = Logger.getLogger(DetailsViewImpl.class.getSimpleName());
    private DiskResource boundValue;
    DiskResourceUtil diskResourceUtil = DiskResourceUtil.getInstance();

    @Inject
    DetailsViewImpl(final DetailsView.Appearance appearance) {
        this.appearance = appearance;
        panel = new HTMLPanel("<div></div>");
    }

    @Override
    public void onDiskResourceSelectionChanged(DiskResourceSelectionChangedEvent event) {
        if (event.getSelection().isEmpty()
                || event.getSelection().size() != 1 || event.getSelection().get(0).isFilter()) {
            bind(null);
            return;
        }
        DiskResource singleSelection = event.getSelection().iterator().next();
        bind(singleSelection);
    }


    @Override
    public void onUpdate(StoreUpdateEvent<DiskResource> event) {
        // Must match the currently bound DiskResource
        if (event.getItems().size() != 1
                || event.getItems().iterator().next() != boundValue) {
            return;
        }
        bind(event.getItems().iterator().next());
    }

    @Override
    public DiskResource getBoundValue() {
        return boundValue;
    }

    @Override
    public void setInfoTypes(List<InfoType> infoTypes) {
        this.infoTypes = infoTypes;
    }

    @Override
    public void setPresenter(Presenter detailsViewPresenter) {
        this.presenter = detailsViewPresenter;
    }

    void bind(final DiskResource resource) {
        this.boundValue = resource;

        Scheduler.get().scheduleFinally(() -> {
            Splittable dataJson = null;
            if (resource != null) {
                dataJson = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(resource));
            }
            ReactDataDetails.DataDetailsProps detailsProps = new ReactDataDetails.DataDetailsProps();

            detailsProps.data = dataJson;
            detailsProps.drUtil = diskResourceUtil;
            detailsProps.appearance = appearance;
            detailsProps.owner = PermissionValue.own.toString();
            detailsProps.presenter = presenter;
            detailsProps.DETAILS_DATE_SUBMITTED = DiskResourceModule.Ids.DETAILS_DATE_SUBMITTED;
            detailsProps.DETAILS_DATE_SUBMITTED = DiskResourceModule.Ids.DETAILS_DATE_SUBMITTED;
            detailsProps.DETAILS_PERMISSIONS = DiskResourceModule.Ids.DETAILS_PERMISSIONS;
            detailsProps.DETAILS_SHARE = DiskResourceModule.Ids.DETAILS_SHARE;
            detailsProps.DETAILS_SIZE = DiskResourceModule.Ids.DETAILS_SIZE;
            detailsProps.DETAILS_TYPE = DiskResourceModule.Ids.DETAILS_TYPE;
            detailsProps.DETAILS_INFO_TYPE = DiskResourceModule.Ids.DETAILS_INFO_TYPE;
            detailsProps.DETAILS_MD5 = DiskResourceModule.Ids.DETAILS_MD5;
            detailsProps.DETAILS_SEND_TO = DiskResourceModule.Ids.DETAILS_SEND_TO;
            detailsProps.DETAILS_TAGS = DiskResourceModule.Ids.DETAILS_TAGS;

            List<String> types = new ArrayList<>();
            if (infoTypes != null && infoTypes.size() > 0) {
                types = infoTypes.stream()
                                 .map(type -> type.getTypeString())
                                 .collect(Collectors.toList());

            }


            String[] typeArray = new String[types.size() + 1];
            typeArray[0] = DetailsView.INFOTYPE_NOSELECT;
            for (int i = 0; i < types.size(); i++) {
                typeArray[i + 1] = types.get(i);
            }

            detailsProps.infoTypes = typeArray;
            detailsProps.isFolder = resource instanceof Folder;

            CyVerseReactComponents.render(ReactDataDetails.dataDetails,
                                          detailsProps,
                                          panel.getElement());

        });


    }


    @Override
    public void onFetchDetailsCompleted(FetchDetailsCompleted event) {
        bind(getBoundValue());
    }


    @Override
    public Widget asWidget() {
        return panel;
    }
}
