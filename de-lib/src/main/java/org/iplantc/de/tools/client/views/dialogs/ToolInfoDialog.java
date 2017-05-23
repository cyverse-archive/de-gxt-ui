package org.iplantc.de.tools.client.views.dialogs;

import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.tools.client.views.cells.ToolInfoCell;

import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;

import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.widget.core.client.container.AbstractHtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

import java.util.List;

/**
 * @author aramsey sriram
 */
public class ToolInfoDialog extends IPlantDialog {

    private ToolInfoCell.ToolInfoCellAppearance appearance;

    AppsListView.Presenter appPresenter;

    @Inject
    public ToolInfoDialog(ToolInfoCell.ToolInfoCellAppearance appearance,
                          AppsListView.Presenter appPresenter) {
        this.appearance = appearance;
        this.appPresenter = appPresenter;
        getButtonBar().clear();
        setModal(true);
        setSize(appearance.detailsDialogWidth(), appearance.detailsDialogHeight());
    }

    public void show(Tool tool, List<App> appList) {
        setHeading(tool.getName());
        HtmlLayoutContainer c = new HtmlLayoutContainer(appearance.detailsRenderer());
        VerticalLayoutContainer vlc = new VerticalLayoutContainer();
        c.add(new Label(appearance.attributionLabel() + ": "),
              new AbstractHtmlLayoutContainer.HtmlData(".cell1"));
        c.add(new Label(tool.getAttribution()), new AbstractHtmlLayoutContainer.HtmlData(".cell3"));
        c.add(new Label(appearance.descriptionLabel() + ": "),
              new AbstractHtmlLayoutContainer.HtmlData(".cell5"));
        c.add(new Label(tool.getDescription()), new AbstractHtmlLayoutContainer.HtmlData(".cell7"));
        vlc.add(c, new VerticalLayoutContainer.VerticalLayoutData(1, 1));
        vlc.setScrollMode(ScrollSupport.ScrollMode.AUTO);
        setWidget(vlc);
        show();
    }
}
