package org.iplantc.de.diskResource.client.views.metadata.dialogs;

import org.iplantc.de.client.models.diskResources.MetadataTemplateAttribute;
import org.iplantc.de.diskResource.client.MetadataView;

import com.google.gwt.user.client.ui.HTML;
import com.google.inject.Inject;

import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

import java.util.List;

/**
 * Created by sriram on 3/16/17.
 */
public class MetadataTermGuideDialog extends Dialog {

    private MetadataView.Appearance appearance;

    @Inject
    public MetadataTermGuideDialog(MetadataView.Appearance appearance) {
        this.appearance = appearance;
        setHideOnButtonClick(true);
        setSize(appearance.metadataTermDlgWidth(), appearance.metadataTermDlgHeight());
        setPredefinedButtons(PredefinedButton.OK);
        setBodyStyle(appearance.backgroundStyle());
    }


    public void show(List<MetadataTemplateAttribute> attributes, String header) {
        setHeading(header);
        setWidget(buildGuide(attributes));
        show();
    }

    private VerticalLayoutContainer buildGuide(List<MetadataTemplateAttribute> attributes) {
        VerticalLayoutContainer helpVlc = new VerticalLayoutContainer();
        helpVlc.setScrollMode(ScrollSupport.ScrollMode.AUTOY);
        for (MetadataTemplateAttribute mta : attributes) {
            HTML label = new HTML(appearance.guideLabel(mta.getName()));
            HTML helpText = new HTML(appearance.guideHelpText(mta.getDescription()));
            helpVlc.add(label, new VerticalLayoutContainer.VerticalLayoutData(.25, -1));
            helpVlc.add(helpText, new VerticalLayoutContainer.VerticalLayoutData(.90, -1));
        }
        return helpVlc;
    }
}
