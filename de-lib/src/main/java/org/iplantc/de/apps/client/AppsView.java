package org.iplantc.de.apps.client;

import org.iplantc.de.apps.client.events.selection.AppSelectionChangedEvent;
import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.commons.client.widgets.DETabPanel;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import com.sencha.gxt.dnd.core.client.DragSource;

import java.util.List;

/**
 * This is the primary view for the Apps module. It is a composite of all other views within the
 * module.
 *
 *
 * @author jstroot
 */
public interface AppsView extends IsWidget,
                                  IsMaskable {

    String getWestPanelWidth();

    void setWestPanelWidth(String width);

    interface AppsViewAppearance {
        String viewCategoriesHeader();

        String appsWindowWidth();

        String appsWindowHeight();

        int appsWindowMinWidth();

        int appsWindowMinHeight();

        String pipelineEdWindowWidth();

        String pipelineEdWindowHeight();

        int pipelineEdWindowMinWidth();

        int pipelineEdWindowMinHeight();
    }

    /**
     * This presenter is responsible for wiring all of the events from the other MVP modules
     * together.
     */
    interface Presenter {

        App getSelectedApp();

        AppCategory getSelectedAppCategory();

        void go(HasOneWidget container,
                HasId selectedAppCategory,
                HasId selectedApp,
                String activeView,
                boolean catPanelCollapsed);

        List<DragSource> getAppsDragSources();

        Presenter hideAppMenu();

        Presenter hideWorkflowMenu();

        void setViewDebugId(String baseId);

        void checkForAgaveRedirect();

        String getActiveView();

        void setActiveView(String activeView);

        String getWestPanelWidth();

        void setWestPanelWidth(String width);

        boolean isDetailsCollapsed();

        void addAppSelectionChangedHandler(AppSelectionChangedEvent.AppSelectionChangedEventHandler handler);
    }

    DETabPanel getCategoryTabPanel();

    void hideAppMenu();

    void hideWorkflowMenu();

    void clearTabPanel();

    boolean isNavPanelCollapsed();

    void setNavPanelCollapsed(boolean collapsed);
}
