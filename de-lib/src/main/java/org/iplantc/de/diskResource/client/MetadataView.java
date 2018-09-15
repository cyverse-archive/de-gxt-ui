package org.iplantc.de.diskResource.client;

import org.iplantc.de.client.models.avu.Avu;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.MetadataTemplateAttribute;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;
import org.iplantc.de.diskResource.client.events.selection.ImportMetadataBtnSelected;
import org.iplantc.de.diskResource.client.events.selection.SaveMetadataToFileBtnSelected;
import org.iplantc.de.diskResource.client.events.selection.SelectTemplateBtnSelected;
import org.iplantc.de.diskResource.client.presenters.callbacks.DiskResourceMetadataUpdateCallback;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.Splittable;

import java.util.List;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;

/**
 * A view to allow users to create and modify metadata for any disk resource
 *
 * @author jstroot
 */
public interface MetadataView extends IsWidget,
                                      SelectTemplateBtnSelected.HasSelectTemplateBtnSelectedHandlers,
                                      ImportMetadataBtnSelected.HasImportMetadataBtnSelectedHandlers,
                                      SaveMetadataToFileBtnSelected.HasSaveMetadataToFileBtnSelectedHandlers {

    /**
     * An appearance class for MetadataView
     */
    interface Appearance {

        String attribute();

        String confirmAction();

        String newAttribute();

        String newValue();

        String newUnit();

        String paramValue();

        void renderMetadataCell(SafeHtmlBuilder sb, String value);

        String loadingMask();

        String userMetadata();

        String add();

        ImageResource addIcon();

        String delete();

        ImageResource deleteIcon();

        String additionalMetadata();

        String paramUnit();

        String selectTemplate();

        ImageResource editIcon();

        String edit();

        String importMdTooltip();

        String metadataLink();

        String readMore();

        ImageResource saveToFileIcon();

        String saveMetadataToFile();

        String loading();

        String backgroundStyle();

        String requiredGhostText();

        String importUMdBtnText();

        String metadataTermDlgWidth();

        String metadataTermDlgHeight();

        SafeHtml guideLabel(String name);

        SafeHtml guideHelpText(String description);

        String dialogWidth();

        String dialogHeight();

        int attributeColumnWidth();

        int valueColumnWidth();

        int unitColumnWidth();
    }

    /**
     * A presenter to handle the logic for MetadataView
     */
    @JsType
    interface Presenter {

        String AVU_BEAN_TAG_MODEL_KEY = "model-key";

        void setViewDebugId(String debugId);

        interface Appearance {

            String templateListingError();

            String loadMetadataError();

            String saveMetadataError();

            String templateinfoError();

            String selectTemplate();

            String templates();

            String error();

            String incomplete();

            ImageResource info();

            SafeHtml importMdMsg();

            SafeHtml importMd();

            String loadingMask();

            String dialogWidth();

            String dialogHeight();

            int infoColumnWidth();

            int downloadColumnWidth();
        }

        boolean isValid();

        @JsIgnore
        void setDiskResourceMetadata(DiskResourceMetadataUpdateCallback callback);

        boolean isDirty();

        @SuppressWarnings("unusable-by-js")
        void updateMetadataFromTemplateView(Splittable metadata, ReactSuccessCallback resolve, ReactErrorCallback reject);

        void closeMetadataTemplateDialog();

        void downloadTemplate(String templateId);

        @JsIgnore
        void go(HasOneWidget container, final DiskResource selected);
    }


    boolean isDirty();

    boolean isValid();

    List<Avu> getAvus();

    void loadMetadata(List<Avu> metadataList);

    void loadUserMetadata(List<Avu> metadataList);

    void mask();

    void unmask();

    void updateMetadataFromTemplateView(List<Avu> metadataList,
                                        List<MetadataTemplateAttribute> templateAttributes);

    List<Avu> getUserMetadata();

    void addToUserMetadata(List<Avu> umd);

    void removeImportedMetadataFromStore(List<Avu> umd);

    void init(boolean editable);

}
