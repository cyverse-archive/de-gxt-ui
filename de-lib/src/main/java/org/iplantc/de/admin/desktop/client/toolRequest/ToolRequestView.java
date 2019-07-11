package org.iplantc.de.admin.desktop.client.toolRequest;

import org.iplantc.de.admin.desktop.client.toolRequest.events.AdminMakeToolPublicSelectedEvent;
import org.iplantc.de.admin.desktop.client.toolRequest.view.ToolRequestDetailsPanel;
import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.toolRequests.ToolRequest;
import org.iplantc.de.client.models.toolRequests.ToolRequestDetails;
import org.iplantc.de.client.models.toolRequests.ToolRequestUpdate;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.Splittable;

import java.util.List;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;

/**
 * @author jstroot
 */
public interface ToolRequestView extends IsWidget, IsMaskable {

    public interface ToolRequestViewAppearance {

        String dateSubmittedColumnLabel();

        int dateSubmittedColumnWidth();

        String dateUpdatedColumnLabel();

        int dateUpdatedColumnWidth();

        String nameColumnLabel();

        int nameColumnWidth();

        String statusColumnLabel();

        int statusColumnWidth();

        String submitBtnText();

        String updateBtnText();

        ImageResource updateIcon();

        double eastPanelSize();

        double northPanelSize();

        String updateToolRequestDlgHeading();

        String updateToolRequestDlgHeight();

        String updateToolRequestDlgWidth();

        String updatedByColumnLabel();

        int updatedByColumnWidth();

        String versionColumnLabel();

        int versionColumnWidth();

        String currentStatusLabel();

        String setStatusLabel();

        String setArbitraryStatusLabel();

        String commentsLabel();

        String detailsPanelHeading();

        String additionalDataFileLabel();

        String additionalInfoLabel();

        String architectureLabel();

        String attributionLabel();

        String cmdLineDescriptionLabel();

        String documentationUrlLabel();

        String multiThreadedLabel();

        String phoneLabel();

        String sourceUrlLabel();

        String submittedByLabel();

        String testDataPathLabel();

        String versionLabel();

        String makePublic();

        void renderRequestName(SafeHtmlBuilder safeHtmlBuilder, ToolRequest toolRequest);
    }

    @JsType
    interface Presenter
            extends AdminMakeToolPublicSelectedEvent.AdminMakeToolPublicSelectedEventHandler {

        interface ToolRequestPresenterAppearance {

            String getToolRequestDetailsLoadingMask();

            String getToolRequestsLoadingMask();

            String toolRequestUpdateSuccessMessage();

            String publishFailed();

            String publishSuccess();
        }

        /**
         * Submits the given update to the {@link org.iplantc.de.admin.desktop.client.toolRequest.service.ToolRequestServiceFacade#updateToolRequest} endpoint.
         * Upon success, the presenter will refresh the view.
         * 
         */
        @JsIgnore
        void updateToolRequest(String id, ToolRequestUpdate update);

        /**
         * Retrieves and assembles a {@link ToolRequestDetails} object via the
         * {@link org.iplantc.de.admin.desktop.client.toolRequest.service.ToolRequestServiceFacade#getToolRequestDetails} endpoint.
         * 
         * Upon success, the presenter will refresh the view.
         * 
         */
        @JsIgnore
        void fetchToolRequestDetails(ToolRequest toolRequest);

        @JsIgnore
        void go(HasOneWidget container);

        @JsIgnore
        void setViewDebugId(String baseId);

        @SuppressWarnings("unusable-by-js")
        void onPublish(Splittable toolSpl);

        void closeEditToolDlg();
    }

    void setPresenter(Presenter presenter);

    void setToolRequests(List<ToolRequest> toolRequests);

    void maskDetailsPanel(String loadingMask);

    void unmaskDetailsPanel();

    void setDetailsPanel(ToolRequestDetails result);

    void update(ToolRequestUpdate toolRequestUpdate, ToolRequestDetails toolRequestDetails);

    ToolRequestDetailsPanel getDetailsPanel();

}
