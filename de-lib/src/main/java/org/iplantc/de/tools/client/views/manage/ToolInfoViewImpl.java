package org.iplantc.de.tools.client.views.manage;

import org.iplantc.de.client.models.tool.Tool;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.AbstractHtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

/**
 *  A tab based view of tool information
 * Created by sriram on 5/30/17.
 */
public class ToolInfoViewImpl extends Composite implements ToolInfoView {

    @UiTemplate("ToolInfoViewImpl.ui.xml")
    interface ToolInfoViewUiBinder extends UiBinder<Widget, ToolInfoViewImpl> {
    }

    private static final ToolInfoViewUiBinder uiBinder = GWT.create(ToolInfoViewUiBinder.class);


    @UiField
    TabPanel infoTabPanel;

    @UiField
    HtmlLayoutContainer htmlContainer;

    @UiField
    SimpleContainer appListContainer;

    @UiField
    VerticalLayoutContainer infoContainer;

    @UiField ToolInfoAppearance appearance;

    @Inject
    public ToolInfoViewImpl(@Assisted Tool tool) {
        initWidget(uiBinder.createAndBindUi(this));
        buildToolInfoView(tool);
        infoContainer.setScrollMode(ScrollSupport.ScrollMode.AUTO);
    }

    @UiFactory
    HtmlLayoutContainer buildHtmlContainer() {
        return new HtmlLayoutContainer(appearance.detailsRenderer());
    }

    private void buildToolInfoView(Tool tool) {
        htmlContainer.add(new Label(appearance.attributionLabel() + ": "),
                          new AbstractHtmlLayoutContainer.HtmlData(".cell1"));
        htmlContainer.add(new Label(tool.getAttribution()),
                          new AbstractHtmlLayoutContainer.HtmlData(".cell3"));
        htmlContainer.add(new Label(appearance.descriptionLabel() + ": "),
                          new AbstractHtmlLayoutContainer.HtmlData(".cell5"));
        htmlContainer.add(new Label(tool.getDescription()),
                          new AbstractHtmlLayoutContainer.HtmlData(".cell7"));
        htmlContainer.add(new Label(appearance.restrictions()),
                          new AbstractHtmlLayoutContainer.HtmlData(".cell9"));
        htmlContainer.add(new Label(
                                  appearance.memLimit() + ((tool.getContainer().getMemoryLimit() == null) ?
                                                           appearance.notApplicable() :
                                                           tool.getContainer().getMemoryLimit())),
                          new AbstractHtmlLayoutContainer.HtmlData(".cell11"));
        htmlContainer.add(new Label(
                                  appearance.pidsLimit() + ((tool.getContainer().getPidsLimit() == null) ?
                                                            appearance.notApplicable() :
                                                            tool.getContainer().getPidsLimit())),
                          new AbstractHtmlLayoutContainer.HtmlData(".cell12"));
        htmlContainer.add(new Label(appearance.timeLimit().toString() + ((tool.getTimeLimit() == null
                                                                          || tool.getTimeLimit() == 0) ?
                                                                         appearance.notApplicable() :
                                                                         tool.getTimeLimit())),
                          new AbstractHtmlLayoutContainer.HtmlData(".cell13"));
        htmlContainer.add(new Label(
                                  appearance.networkingMode() + ((Strings.isNullOrEmpty(tool.getContainer()
                                                                                            .getNetworkMode())
                                                                  || tool.getContainer()
                                                                         .getNetworkMode()
                                                                         .equalsIgnoreCase(appearance.bridge())) ?
                                                                 appearance.enabled() :
                                                                 appearance.disabled())),
                          new AbstractHtmlLayoutContainer.HtmlData(".cell14"));
    }

    @Override
    public SimpleContainer getAppListContainer() {
        return appListContainer;
    }

}
