package org.iplantc.de.tools.client.views.dialogs;

import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.tools.client.views.cells.ToolInfoCell;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;

import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.AbstractHtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

import java.util.List;

/**
 * @author aramsey sriram
 */
public class ToolInfoDialog extends IPlantDialog {

    private ToolInfoCell.ToolInfoCellAppearance appearance;

    AppsListView.Presenter appPresenter;

    TabPanel infoTabPanel;


    @Inject
    public ToolInfoDialog(ToolInfoCell.ToolInfoCellAppearance appearance,
                          AppsListView.Presenter appPresenter) {
        this.appearance = appearance;
        this.appPresenter = appPresenter;
        infoTabPanel = new TabPanel();
        infoTabPanel.setSize(appearance.tabWidth(), appearance.tabHeight());
        getButtonBar().clear();
        setModal(true);
        setSize(appearance.detailsDialogWidth(), appearance.detailsDialogHeight());
    }

    public void show(Tool tool, List<App> appList) {
        setHeading(tool.getName());
        HtmlLayoutContainer c = new HtmlLayoutContainer(appearance.detailsRenderer());
        VerticalLayoutContainer infoContainer = new VerticalLayoutContainer();
        c.add(new Label(appearance.attributionLabel() + ": "),
              new AbstractHtmlLayoutContainer.HtmlData(".cell1"));
        c.add(new Label(tool.getAttribution()), new AbstractHtmlLayoutContainer.HtmlData(".cell3"));
        c.add(new Label(appearance.descriptionLabel() + ": "),
              new AbstractHtmlLayoutContainer.HtmlData(".cell5"));
        c.add(new Label(tool.getDescription()), new AbstractHtmlLayoutContainer.HtmlData(".cell7"));
        infoContainer.add(c, new VerticalLayoutContainer.VerticalLayoutData(1, 1));
        infoContainer.setScrollMode(ScrollSupport.ScrollMode.AUTO);
        infoTabPanel.add(infoContainer, appearance.toolInformation());

        SimpleContainer appListContainer = new SimpleContainer();
        infoTabPanel.add(appListContainer, appearance.appsUsingTool());
        appPresenter.go((HasOneWidget)appListContainer);
        appPresenter.loadApps(appList);
        add(infoTabPanel);
        show();
    }
}
