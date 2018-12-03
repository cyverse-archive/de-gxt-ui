package org.iplantc.de.admin.desktop.client.toolAdmin;

import org.iplantc.de.admin.desktop.client.toolAdmin.events.AddToolSelectedEvent;
import org.iplantc.de.admin.desktop.client.toolAdmin.events.DeleteToolSelectedEvent;
import org.iplantc.de.admin.desktop.client.toolAdmin.events.PublishToolEvent;
import org.iplantc.de.admin.desktop.client.toolAdmin.events.SaveToolSelectedEvent;
import org.iplantc.de.admin.desktop.client.toolAdmin.events.ToolSelectedEvent;
import org.iplantc.de.admin.desktop.client.toolAdmin.view.dialogs.ToolAdminDetailsDialog;
import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.tool.Tool;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

/**
 * @author aramsey
 */
public interface ToolAdminView extends IsWidget,
                                       IsMaskable,
                                       AddToolSelectedEvent.HasAddToolSelectedEventHandlers,
                                       ToolSelectedEvent.HasToolSelectedEventHandlers,
                                       SaveToolSelectedEvent.HasSaveToolSelectedEventHandlers,
                                       DeleteToolSelectedEvent.HasDeleteToolSelectedEventHandlers,
                                       PublishToolEvent.HasPublishToolEventHandlers {

    interface ToolAdminViewAppearance {

        String add();

        String filter();

        ImageResource addIcon();

        ImageResource deleteIcon();

        String nameColumnLabel();

        int nameColumnWidth();

        String descriptionColumnLabel();

        int descriptionColumnWidth();

        String attributionColumnLabel();

        int attributionColumnWidth();

        String locationColumnInfoLabel();

        int locationColumnInfoWidth();

        String versionColumnInfoLabel();

        int versionColumnInfoWidth();

        String typeColumnInfoLabel();

        int typeColumnInfoWidth();

        String toolImportDescriptionLabel();

        SafeHtml toolImportNameLabel();

        SafeHtml toolImportTypeLabel();

        String toolImportTypeDefaultValue();

        String toolImportAttributionLabel();

        SafeHtml toolImportVersionLabel();

        String toolImportLocationLabel();

        String containerDetailsFieldSetLabel();

        String containerNameLabel();

        String containerWorkingDirLabel();

        String containerEntryPointLabel();

        String containerMemoryLimitLabel();

        int containerMemoryLimitDefaultValue();

        String containerCPUSharesLabel();

        int containerCPUSharesDefaultValue();

        String containerNetworkModeLabel();

        SafeHtml containerDevicesLabel();

        String containerDevicesHostPathLabel();

        int containerDevicesHostPathWidth();

        String containerDevicesContainerPathLabel();

        int containerDevicesContainerPathWidth();

        SafeHtml containerVolumesLabel();

        String containerVolumesHostPathLabel();

        int containerVolumesHostPathWidth();

        String containerVolumesContainerPathLabel();

        int containerVolumesContainerPathWidth();

        String containerImageFieldSetLabel();

        SafeHtml containerImageNameLabel();

        String containerImageTagLabel();

        String containerImageURLLabel();

        SafeHtml containerVolumesFromLabel();

        String containerVolumesFromNameLabel();

        int containerVolumesFromNameWidth();

        String containerVolumesFromTagLabel();

        int containerVolumesFromTagWidth();

        String containerVolumesFromURLLabel();

        int containerVolumesFromURLWidth();

        String containerVolumesFromNamePrefixLabel();

        int containerVolumesFromNamePrefixWidth();

        String containerVolumesFromReadOnlyLabel();

        int containerVolumesFromReadOnlyWidth();

        String toolImplementationFieldSetLabel();

        SafeHtml toolImplementationImplementorLabel();

        SafeHtml toolImplementationImplementorEmailLabel();

        int toolTestDataParamsWidth();

        SafeHtml toolTestDataInputFilesLabel();

        String toolTestDataInputFilesColumnLabel();

        int toolTestDataInputFilesWidth();

        SafeHtml toolTestDataOutputFilesLabel();

        String toolTestDataOutputFilesColumnLabel();

        int toolTestDataOutputFilesWidth();

        String dialogWindowName();

        String dialogWindowUpdateBtnText();

        String addToolSuccessText();

        String updateToolSuccessText();

        String deleteBtnText();

        String deleteToolSuccessText();

        String completeRequiredFieldsError();

        String confirmOverwriteTitle();

        String confirmOverwriteDangerZone();

        String confirmOverwriteBody();

        String deletePublicToolTitle();

        String deletePublicToolBody();

        int publicAppNameColumnWidth();

        String publicAppNameLabel();

        int publicAppIntegratorColumnWidth();

        String publicAppIntegratorLabel();

        int publicAppIntegratorEmailColumnWidth();

        String publicAppIntegratorEmailLabel();

        int publicAppDisabledColumnWidth();

        String publicAppDisabledLabel();

        SafeHtml toolAdminHelp();

        SafeHtml toolEntryPointWarning();

        SafeHtml toolVolumeWarning();

        String dialogMakePublicText();

        SafeHtml timeLimit();

        String restricted();

        String defaultImgPrefix();

        /**
         * Field Label for PIDs limit field
         *
         * @return
         */
        String pidsLimit();

        String timeLimitEmptyText();

        String interactive();

        String maxCPUCoresLabel();

        String minMemoryLimitLabel();

        String minCPUCoresLabel();

        String skipTmpMountLabel();

        String minDiskSpaceLabel();

        SafeHtml osgImagePathLabel();
    }

    interface Presenter {

        void go(HasOneWidget container);

        void setViewDebugId(String baseId);
    }

    void editToolDetails(Tool tool, ToolAdminDetailsDialog.Mode mode);

    void toolSelected(Tool tool);

    void setToolTypes(List<String> toolTypes);
}
