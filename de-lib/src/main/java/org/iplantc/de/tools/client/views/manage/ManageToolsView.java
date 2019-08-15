
package org.iplantc.de.tools.client.views.manage;

import org.iplantc.de.tools.client.events.ToolSelectionChangedEvent;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.Splittable;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;

/**
 * Created by sriram on 4/20/17.
 */
public interface ManageToolsView extends IsWidget {

    interface ManageToolsViewAppearance {
        String name();

        String shareTools();

        String deleteTool();

        String confirmDelete();

        String appsLoadError();

        String toolAdded(String name);

        String toolUpdated(String name);

        String toolDeleted(String name);

        int sharingDialogWidth();

        int sharingDialogHeight();

        String manageSharing();

        String done();

        String toolInfoError();

        String windowWidth();

        String windowHeight();

        int windowMinWidth();

        int windowMinHeight();

        String create();

        String edit();
    }


    @JsType
    interface Presenter extends org.iplantc.de.commons.client.presenter.Presenter,
                                ToolSelectionChangedEvent.HasToolSelectionChangedEventHandlers {
        @JsIgnore
        void setViewDebugId(String baseId);

        void loadTools(Boolean isPublic, String searchTerm, String order, String orderBy, int limit, int offset);

        @SuppressWarnings("unusable-by-js")
        void onToolSelectionChanged(Splittable tool);

        @SuppressWarnings("unusable-by-js")
        void addTool(Splittable tool);

        @SuppressWarnings("unusable-by-js")
        void updateTool(Splittable tool);

        void onShowToolInfo(String toolId);

        void closeEditToolDlg();

        void onNewToolSelected();

        void onRequestToolSelected();

        void onEditToolSelected(String toolId);

        void onDeleteToolsSelected(String toolId, String toolName);

        @SuppressWarnings("unusable-by-js")
        void useToolInNewApp(Splittable tool);

        @SuppressWarnings("unusable-by-js")
        void onShareToolsSelected(Splittable tool);

        @SuppressWarnings("unusable-by-js")
        void onRequestToMakeToolPublicSelected(Splittable tool);
    }

    Splittable getCurrentToolList();

    void loadTools(Splittable tools);

    void mask();

    void unmask();

    void setBaseId(String id);
}
