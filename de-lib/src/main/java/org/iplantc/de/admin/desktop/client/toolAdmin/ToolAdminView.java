package org.iplantc.de.admin.desktop.client.toolAdmin;

import org.iplantc.de.admin.desktop.client.toolAdmin.events.AddToolSelectedEvent;
import org.iplantc.de.admin.desktop.client.toolAdmin.events.DeleteToolSelectedEvent;
import org.iplantc.de.admin.desktop.client.toolAdmin.events.ToolSelectedEvent;
import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.tool.Tool;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.Splittable;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;

/**
 * @author aramsey
 */
public interface ToolAdminView extends IsWidget,
                                       IsMaskable,
                                       AddToolSelectedEvent.HasAddToolSelectedEventHandlers,
                                       ToolSelectedEvent.HasToolSelectedEventHandlers,
                                       DeleteToolSelectedEvent.HasDeleteToolSelectedEventHandlers {

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

        String addToolSuccessText();

        String updateToolSuccessText();

        String deleteBtnText();

        String deleteToolSuccessText();

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

        String loadingMask();
    }

    @JsType
    interface Presenter {

        @JsIgnore
        void go(HasOneWidget container);

        @JsIgnore
        void setViewDebugId(String baseId);

        @SuppressWarnings("unusable-by-js")
        void addTool(Splittable toolSpl);

        @SuppressWarnings("unusable-by-js")
        void updateTool(Splittable toolSpl);

        void closeEditToolDlg();
    }

    void toolSelected(Tool tool);
}
