package org.iplantc.de.diskResource.client;

import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.DiskResourceMetadataList;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.Splittable;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;

/**
 * A view to allow users to create and modify metadata for any disk resource
 *
 * @author jstroot psarando
 */
public interface MetadataView extends IsWidget {

    /**
     * A presenter to handle the logic for MetadataView
     */
    @JsType
    interface Presenter {

        void setViewDebugId(String debugId);

        interface Appearance {

            String templateListingError();

            String loadMetadataError();

            String templateinfoError();

            String selectTemplate();

            String templates();

            String error();

            String incomplete();

            String dialogWidth();

            String dialogHeight();

            int infoColumnWidth();

            int downloadColumnWidth();
        }

        @SuppressWarnings("unusable-by-js")
        void setDiskResourceMetadata(Splittable metadata, ReactSuccessCallback resolve, ReactErrorCallback reject);

        @SuppressWarnings("unusable-by-js")
        void updateMetadataFromTemplateView(Splittable metadata, ReactSuccessCallback resolve, ReactErrorCallback reject);

        @SuppressWarnings("unusable-by-js")
        void onSelectTemplateBtnSelected(Splittable metadata);

        void closeMetadataDialog();

        void closeMetadataTemplateDialog();

        @SuppressWarnings("unusable-by-js")
        void searchOLSTerms(String inputValue, Splittable loaderSettings, ReactSuccessCallback callback);

        void searchAstroThesaurusTerms(String inputValue, ReactSuccessCallback callback);

        void onSaveMetadataToFileBtnSelected();

        @JsIgnore
        void go(final DiskResource selected);
    }

    void loadMetadata(DiskResourceMetadataList metadata);

    void mask();

    void unmask();

    void updateMetadataFromTemplateView(Splittable metadata);

    void init(Presenter presenter, boolean editable, final DiskResource selectedResource);

    void showMetadataDialog();

    void closeMetadataDialog();
}
