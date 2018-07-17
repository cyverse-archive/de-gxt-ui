package org.iplantc.de.diskResource.client;

import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.viewer.InfoType;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
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
                                     StoreUpdateHandler<DiskResource> {

    public static final String INFOTYPE_NOSELECT = "-";

    @JsIgnore
    DiskResource getBoundValue();

    void setInfoTypes(List<InfoType> infoTypes);

    @JsIgnore
    void setPresenter(DetailsView.Presenter detailsViewPresenter);

    void setDebugId(String baseID);

    @JsType
    interface Presenter extends SubmitDiskResourceQueryEvent.HasSubmitDiskResourceQueryEventHandlers,
            HasManageSharingSelectedEventHandlers,
            HasEditInfoTypeSelectedEventHandlers,
            HasSetInfoTypeSelectedHandlers,
            HasSendToTreeViewerSelectedHandlers,
            HasSendToCogeSelectedHandlers,
            HasSendToEnsemblSelectedHandlers,
            HasMd5ValueClickedHandlers {


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

        void fetchTagsForResource(String diskResourceId,
                                  TagsFetchCallback callback,
                                  ReactErrorCallback errorCallback);

        void searchTags(String searchVal, TagsSearchCallback callback, ReactErrorCallback errorCallback);

        void attachTag(String tagId,
                       String tagValue,
                       String diskResourceId,
                       TagAttachCallback callback,
                       ReactErrorCallback errorCallback);

        void detachTag(String tagId,
                       String tagValue,
                       String diskResourceId,
                       TagDetachCallback callback,
                       ReactErrorCallback errorCallback);

        void createTag(String tagValue,
                       String diskResourceId,
                       TagAttachCallback callback,
                       ReactErrorCallback errorCallback);

        void onTagSelection(String tagId, String tagValue);

        void onSharingClicked();

        void onSetInfoType(String infoType);

        void onSendToClicked(String infoType);
    }
}
