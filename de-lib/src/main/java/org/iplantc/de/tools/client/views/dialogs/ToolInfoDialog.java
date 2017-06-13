package org.iplantc.de.tools.client.views.dialogs;

import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.apps.client.OntologyHierarchiesView;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.tools.client.gin.factory.ToolInfoViewFactory;
import org.iplantc.de.tools.client.views.cells.ToolInfoCell;
import org.iplantc.de.tools.client.views.manage.ToolInfoViewImpl;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.TabPanel;

import java.util.List;

/**
 *  A dialog that displays additional tool information including apps used by the selected tool.
 * @author aramsey sriram
 */
public class ToolInfoDialog extends IPlantDialog {

    private ToolInfoCell.ToolInfoCellAppearance appearance;

    AppsListView.Presenter appPresenter;

    TabPanel infoTabPanel;

    OntologyHierarchiesView.Presenter ontPresenter;

    ToolInfoViewImpl infoView;

    ToolInfoViewFactory factory;

    @Inject
    public ToolInfoDialog(ToolInfoCell.ToolInfoCellAppearance appearance,
                          AppsListView.Presenter appPresenter,
                          OntologyHierarchiesView.Presenter ontPresenter,
                          ToolInfoViewFactory factory) {
        this.appearance = appearance;
        this.appPresenter = appPresenter;
        this.ontPresenter = ontPresenter;
        this.factory = factory;
        infoTabPanel = new TabPanel();
        infoTabPanel.setSize(appearance.tabWidth(), appearance.tabHeight());
        getButtonBar().clear();
        setModal(true);
        setSize(appearance.detailsDialogWidth(), appearance.detailsDialogHeight());
    }

    public void show(Tool tool, List<App> appList) {
        setHeading(tool.getName());
        appPresenter.addAppInfoSelectedEventHandler(ontPresenter);
        infoView = factory.build(tool);
        appPresenter.go((HasOneWidget)infoView.getAppListContainer());
        appPresenter.loadApps(appList);
        add(infoView.asWidget());
        show();
    }
}
