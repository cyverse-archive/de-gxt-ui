package org.iplantc.de.apps.client;

import org.iplantc.de.client.models.HasId;
import org.iplantc.de.commons.client.widgets.DETabPanel;

import com.google.gwt.user.client.ui.IsWidget;

import com.sencha.gxt.widget.core.client.tree.Tree;

import java.util.List;

/**
 * @author aramsey
 *
 * A view for managing displaying the two trees in the My Apps / Workspace tab (Apps Under Development,
 * Favorite Apps, My Public Apps, Shared with Me, and Communities)
 */
public interface WorkspaceView extends IsWidget {

    void deselectAll();

    void selectFirstItem();

    void setViewDebugId(String baseID);

    void go(DETabPanel panel, List<Tree> widgets);

    interface Presenter {
        void go(HasId selectedAppCategory, HasId selectedCommunity, boolean selectDefaultCategory, DETabPanel tabPanel);

        AppCategoriesView.Presenter getCategoriesPresenter();

        CommunitiesView.Presenter getCommunitiesPresenter();

        void setViewDebugId(String baseId);
    }
}
