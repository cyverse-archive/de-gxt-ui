package org.iplantc.de.diskResource.client;

import org.iplantc.de.client.models.avu.Avu;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.MetadataTemplateAttribute;
import org.iplantc.de.client.models.diskResources.MetadataTemplateInfo;
import org.iplantc.de.diskResource.client.presenters.callbacks.DiskResourceMetadataUpdateCallback;
import org.iplantc.de.diskResource.client.views.search.MetadataTermSearchField;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

/**
 * Created by jstroot on 2/10/15.
 *
 * @author jstroot
 */
public interface MetadataView extends IsWidget {

    interface Appearance {

        String attribute();

        SafeHtml boldHeader(String name);

        SafeHtml buildLabelWithDescription(String label, String description, boolean allowBlank);

        String confirmAction();

        String metadataTemplateConfirmRemove();

        String metadataTemplateRemove();

        String metadataTemplateSelect();

        String newAttribute();

        String newValue();

        String newUnit();

        String paramValue();

        SafeHtml renderComboBoxHtml(MetadataTemplateInfo object);

        void renderMetadataCell(SafeHtmlBuilder sb, String value);

        String loadingMask();

        String userMetadata();

        String add();

        ImageResource addIcon();

        String delete();

        String metadataTermGuide();

        ImageResource deleteIcon();

        String additionalMetadata();

        String paramUnit();

        String selectTemplate();

        SafeHtml importMd();

        String panelWidth();

        String panelHeight();

        ImageResource editIcon();

        String edit();

        String importMdTooltip();

        String metadataLink();

        String readMore();

        ImageResource saveToFileIcon();

        String saveMetadataToFile();

        String loading();

        String backgroundStyle();

        String urlGhostText();

        String requiredGhostText();

        String importUMdBtnText();

        String metadataTermDlgWidth();

        String metadataTermDlgHeight();

        SafeHtml guideLabel(String name);

        SafeHtml guideHelpText(String description);
    }

    public interface Presenter {

        final String AVU_BEAN_TAG_MODEL_KEY = "model-key";

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

        Avu setAvuModelKey(Avu avu);

        DiskResource getSelectedResource();

        MetadataTermSearchField createMetadataTermSearchField(MetadataTemplateAttribute attribute);

        void onTemplateSelected(String templateId);

        void setDiskResourceMetadata(DiskResourceMetadataUpdateCallback callback);

        void onSelectTemplate();

        void onImport(List<Avu> selectedItems);

        boolean isDirty();

        void downloadTemplate(String templateid);

        void onSaveToFile();

        void go(HasOneWidget container, final DiskResource selected);
    }


    boolean isDirty();

    boolean isValid();

    List<Avu> getAvus();

    void loadMetadata(List<Avu> metadataList);

    void loadUserMetadata(List<Avu> metadataList);

    void setPresenter(Presenter p);

    void mask();

    void unmask();

    void updateMetadataFromTemplateView(List<Avu> metadataList,
                                        List<MetadataTemplateAttribute> templateAttributes);

    List<Avu> getUserMetadata();

    void addToUserMetadata(List<Avu> umd);

    void removeImportedMetadataFromStore(List<Avu> umd);

    void init(boolean editable);

}
