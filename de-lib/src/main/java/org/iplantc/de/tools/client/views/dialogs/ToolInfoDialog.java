package org.iplantc.de.tools.client.views.dialogs;

import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.apps.client.OntologyHierarchiesView;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.tools.client.gin.factory.ToolInfoViewFactory;
import org.iplantc.de.tools.client.views.manage.ToolInfoView;
import org.iplantc.de.tools.client.views.manage.ToolInfoViewImpl;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.widget.core.client.TabPanel;

/**
 *  A dialog that displays additional tool information including apps used by the selected tool.
 * @author aramsey sriram
 */
public class ToolInfoDialog extends IPlantDialog {

    private ToolInfoView.ToolInfoAppearance appearance;

    AppsListView.Presenter appPresenter;

    TabPanel infoTabPanel;

    OntologyHierarchiesView.Presenter ontPresenter;

    ToolInfoViewImpl infoView;

    ToolInfoViewFactory factory;

    @Inject
    public ToolInfoDialog(ToolInfoView.ToolInfoAppearance appearance,
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

    public void show(Tool tool, Splittable appList) {
        setHeading(tool.getName());
        appPresenter.addAppInfoSelectedEventHandler(ontPresenter);
        infoView = factory.build(tool);
        appPresenter.go((HasOneWidget)infoView.getAppListContainer());
        appPresenter.loadApps(appList);
        add(infoView.asWidget());
        show();
    }
}
