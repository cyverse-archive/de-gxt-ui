package org.iplantc.de.diskResource.client.views.details;

import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.models.sharing.PermissionValue;
import org.iplantc.de.client.models.viewer.InfoType;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.commons.client.util.CyVerseReactComponents;
import org.iplantc.de.diskResource.client.DetailsView;
import org.iplantc.de.diskResource.client.events.DiskResourceSelectionChangedEvent;
import org.iplantc.de.diskResource.client.events.FetchDetailsCompleted;
import org.iplantc.de.diskResource.client.events.selection.EditInfoTypeSelected;
import org.iplantc.de.diskResource.client.events.selection.ManageSharingSelected;
import org.iplantc.de.diskResource.client.events.selection.Md5ValueClicked;
import org.iplantc.de.diskResource.client.events.selection.SendToCogeSelected;
import org.iplantc.de.diskResource.client.events.selection.SendToEnsemblSelected;
import org.iplantc.de.diskResource.client.events.selection.SendToTreeViewerSelected;
import org.iplantc.de.diskResource.client.events.selection.SetInfoTypeSelected;
import org.iplantc.de.diskResource.share.DiskResourceModule;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.data.shared.event.StoreUpdateEvent;
import com.sencha.gxt.widget.core.client.Composite;

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
public class DetailsViewImpl extends Composite implements DetailsView {

    public static final String INFOTYPE_NOSELECT = "-";
    private List<InfoType> infoTypes;
    private Presenter presenter;


    HTMLPanel panel;
    final Appearance appearance;
    DiskResourceUtil diskResourceUtil = DiskResourceUtil.getInstance();

    private final Logger LOG = Logger.getLogger(DetailsViewImpl.class.getSimpleName());
    private DiskResource boundValue;

    @Inject
    DetailsViewImpl(final DetailsView.Appearance appearance) {
        this.appearance = appearance;
        panel = new HTMLPanel("<div></div>");
        initWidget(panel);
    }

    @Override
    public HandlerRegistration addEditInfoTypeSelectedEventHandler(EditInfoTypeSelected.EditInfoTypeSelectedEventHandler handler) {
        return addHandler(handler, EditInfoTypeSelected.TYPE);
    }

    @Override
    public HandlerRegistration addManageSharingSelectedEventHandler(ManageSharingSelected.ManageSharingSelectedEventHandler handler) {
        return addHandler(handler, ManageSharingSelected.TYPE);
    }

    @Override
    public HandlerRegistration addResetInfoTypeSelectedHandler(SetInfoTypeSelected.SetInfoTypeSelectedHandler handler) {
        return addHandler(handler, SetInfoTypeSelected.TYPE);
    }

    @Override
    public HandlerRegistration addMd5ValueClickedHandler(Md5ValueClicked.Md5ValueClickedHandler handler) {
        return addHandler(handler, Md5ValueClicked.TYPE);
    }

    @Override
    public HandlerRegistration addSendToCogeSelectedHandler(SendToCogeSelected.SendToCogeSelectedHandler handler) {
        return addHandler(handler, SendToCogeSelected.TYPE);
    }

    @Override
    public HandlerRegistration addSendToEnsemblSelectedHandler(SendToEnsemblSelected.SendToEnsemblSelectedHandler handler) {
        return addHandler(handler, SendToEnsemblSelected.TYPE);
    }

    @Override
    public HandlerRegistration addSendToTreeViewerSelectedHandler(SendToTreeViewerSelected.SendToTreeViewerSelectedHandler handler) {
        return addHandler(handler, SendToTreeViewerSelected.TYPE);
    }

    @Override
    public void onDiskResourceSelectionChanged(DiskResourceSelectionChangedEvent event) {
        if (event.getSelection().isEmpty()
                || event.getSelection().size() != 1 || event.getSelection().get(0).isFilter()) {
            bind(null);
            return;
        }
        mask(appearance.loadingMask());
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

    public void fireSharingEvent() {
        fireEvent(new ManageSharingSelected(boundValue));
    }

    public void fireSetInfoTypeEvent(String infoType) {
        if (infoType.equals(INFOTYPE_NOSELECT)) {
            fireEvent(new SetInfoTypeSelected(boundValue, ""));
        } else {
            fireEvent(new SetInfoTypeSelected(boundValue, infoType));
        }
    }

    @Override
    public void onSendToClicked(String infoType) {
        if (boundValue == null) {
            return;
        }
        InfoType resInfoType = InfoType.fromTypeString(boundValue.getInfoType());
        if (resInfoType == null) {
            return;
        }

        final ArrayList<DiskResource> resources = Lists.newArrayList(boundValue);
        if (diskResourceUtil.isTreeInfoType(resInfoType)) {
            fireEvent(new SendToTreeViewerSelected(resources));
        } else if (diskResourceUtil.isGenomeVizInfoType(resInfoType)) {
            fireEvent(new SendToCogeSelected(resources));
        } else if (diskResourceUtil.isEnsemblInfoType(resInfoType)) {
            fireEvent(new SendToEnsemblSelected(resources));
        }
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
            detailsProps.view = DetailsViewImpl.this;
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
            typeArray[0] = INFOTYPE_NOSELECT;
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
        unmask();
    }


    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

     /* lastModified.ensureDebugId(baseID + DiskResourceModule.Ids.DETAILS_LAST_MODIFIED);
        dateCreated.ensureDebugId(baseID + DiskResourceModule.Ids.DETAILS_DATE_SUBMITTED);
        permission.ensureDebugId(baseID + DiskResourceModule.Ids.DETAILS_PERMISSIONS);
        sharing.ensureDebugId(baseID + DiskResourceModule.Ids.DETAILS_SHARE);
        size.ensureDebugId(baseID + DiskResourceModule.Ids.DETAILS_SIZE);
        mimeType.ensureDebugId(baseID + DiskResourceModule.Ids.DETAILS_TYPE);
        infoType.ensureDebugId(baseID + DiskResourceModule.Ids.DETAILS_INFO_TYPE);
        md5link.ensureDebugId(baseID + DiskResourceModule.Ids.DETAILS_MD5);
        sendTo.ensureDebugId(baseID + DiskResourceModule.Ids.DETAILS_SEND_TO);
        tagListView.asWidget().ensureDebugId(baseID + DiskResourceModule.Ids.DETAILS_TAGS);*/
    }
}
