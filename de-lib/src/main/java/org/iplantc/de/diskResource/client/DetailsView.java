package org.iplantc.de.diskResource.client;

import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.viewer.InfoType;
import org.iplantc.de.diskResource.client.events.DiskResourceSelectionChangedEvent.DiskResourceSelectionChangedEventHandler;
import org.iplantc.de.diskResource.client.events.FetchDetailsCompleted;
import org.iplantc.de.diskResource.client.events.search.SubmitDiskResourceQueryEvent;
import org.iplantc.de.diskResource.client.events.selection.EditInfoTypeSelected.HasEditInfoTypeSelectedEventHandlers;
import org.iplantc.de.diskResource.client.events.selection.ManageSharingSelected.HasManageSharingSelectedEventHandlers;
import org.iplantc.de.diskResource.client.events.selection.Md5ValueClicked.HasMd5ValueClickedHandlers;
import org.iplantc.de.diskResource.client.events.selection.SendToCogeSelected.HasSendToCogeSelectedHandlers;
import org.iplantc.de.diskResource.client.events.selection.SendToEnsemblSelected.HasSendToEnsemblSelectedHandlers;
import org.iplantc.de.diskResource.client.events.selection.SendToTreeViewerSelected.HasSendToTreeViewerSelectedHandlers;
import org.iplantc.de.diskResource.client.events.selection.SetInfoTypeSelected.HasSetInfoTypeSelectedHandlers;
import org.iplantc.de.diskResource.client.presenters.callbacks.TagAttachCallback;
import org.iplantc.de.diskResource.client.presenters.callbacks.TagDetachCallback;
import org.iplantc.de.diskResource.client.presenters.callbacks.TagsFetchCallback;
import org.iplantc.de.diskResource.client.presenters.callbacks.TagsSearchCallback;

import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.IsWidget;

import com.sencha.gxt.data.shared.event.StoreUpdateEvent.StoreUpdateHandler;

import java.util.List;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;

/**
 * Created by jstroot on 2/2/15.
 * @author jstroot
 */
@JsType
public interface DetailsView extends IsWidget,
                                     DiskResourceSelectionChangedEventHandler,
                                     FetchDetailsCompleted.FetchDetailsCompletedHandler,
                                     StoreUpdateHandler<DiskResource>,
                                     HasManageSharingSelectedEventHandlers,
                                     HasEditInfoTypeSelectedEventHandlers,
                                     HasSetInfoTypeSelectedHandlers,
                                     HasSendToTreeViewerSelectedHandlers,
                                     HasSendToCogeSelectedHandlers,
                                     HasSendToEnsemblSelectedHandlers,
                                     HasMd5ValueClickedHandlers {

    void fireSharingEvent();

    void fireSetInfoTypeEvent(String infoType);

    void onSendToClicked(String infoType);

    void setInfoTypes(List<InfoType> infoTypes);

    void setPresenter(DetailsView.Presenter detailsViewPresenter);

    @JsType
    interface Appearance {
        @JsType
        interface DetailsViewStyle extends CssResource {

            String disabledHyperlink();

            String label();

            String value();

            String hidden();

            String hyperlink();

            String deselectIcon();

            String table();

            String tagSearch();

            String row();

            String emptyDetails();

            String tagPanelStyle();

            String tagDivStyle();

            String tagStyle();

            String tagRemoveStyle();
        }

        String coge();

        String createdDate();

        String delete();

        String ensembl();

        String files();

        String folders();

        String infoTypeDisabled();

        String lastModified();

        String noDetails();

        String beginSharing();

        String permissions();

        String selectInfoType();

        String sendTo();

        String share();

        String sharingDisabled();

        String size();

        String treeViewer();

        DetailsViewStyle css();

        String viewersDisabled();

        @JsIgnore
        ImageResource deselectInfoTypeIcon();

        String tagsLabel();

        String filesFoldersLabel();

        String sendToLabel();

        String infoTypeLabel();

        String md5CheckSum();

        String typeLabel();

        String sizeLabel();

        String shareLabel();

        String permissionsLabel();

        String createdDateLabel();

        String lastModifiedLabel();

        String loadingMask();

        String searchTags();

        String removeTag();

        String okLabel();

        String emptyValue();
    }

    @JsType
    interface Presenter extends SubmitDiskResourceQueryEvent.HasSubmitDiskResourceQueryEventHandlers {

        interface Appearance {
            @JsIgnore
            String tagAttachError();

            @JsIgnore
            String tagAttached(String name, String value);

            @JsIgnore
            String tagDetachError();

            @JsIgnore
            String tagDetached(String value, String name);

            @JsIgnore
            String tagFetchError();
        }

        @JsIgnore
        DetailsView getView();

        void fetchTagsForResource(String diskResourceId, TagsFetchCallback callback);

        void searchTags(String searchVal, TagsSearchCallback callback);

        void attachTag(String tagId, String tagValue, String diskResourceId, TagAttachCallback callback);

        void detachTag(String tagId, String tagValue, String diskResourceId, TagDetachCallback callback);

        void createTag(String tagValue, String diskResourceId, TagAttachCallback callback);

        void onTagSelection(String tagId, String tagValue);
    }
}
