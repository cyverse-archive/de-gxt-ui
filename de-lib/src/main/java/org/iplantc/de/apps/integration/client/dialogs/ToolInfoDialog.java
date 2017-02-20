package org.iplantc.de.apps.integration.client.dialogs;

import org.iplantc.de.apps.integration.client.view.tools.DeployedComponentsListingView;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;

import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;

import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.widget.core.client.container.AbstractHtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

/**
 * @author aramsey
 */
public class ToolInfoDialog extends IPlantDialog {

    private DeployedComponentsListingView.DeployedComponentsListingViewAppearance appearance;

    @Inject
    public ToolInfoDialog(DeployedComponentsListingView.DeployedComponentsListingViewAppearance appearance) {
        this.appearance = appearance;
        getButtonBar().clear();
        setModal(true);
        setSize(appearance.infoDialogWidth(), appearance.infoDialogHeight());
    }

    public void show(Tool tool) {
        setHeading(tool.getName());
        HtmlLayoutContainer c = new HtmlLayoutContainer(appearance.detailsRenderer());
        VerticalLayoutContainer vlc = new VerticalLayoutContainer();
        c.add(new Label(appearance.attributionLabel() + ": "), new AbstractHtmlLayoutContainer.HtmlData(".cell1"));
        c.add(new Label(tool.getAttribution()), new AbstractHtmlLayoutContainer.HtmlData(".cell3"));
        c.add(new Label(appearance.descriptionLabel() + ": "), new AbstractHtmlLayoutContainer.HtmlData(".cell5"));
        c.add(new Label(tool.getDescription()), new AbstractHtmlLayoutContainer.HtmlData(".cell7"));
        vlc.add(c, new VerticalLayoutContainer.VerticalLayoutData(1, 1));
        vlc.setScrollMode(ScrollSupport.ScrollMode.AUTO);
        setWidget(vlc);
        show();
    }
}
