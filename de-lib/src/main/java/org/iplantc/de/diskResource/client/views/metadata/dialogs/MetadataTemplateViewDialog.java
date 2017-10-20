package org.iplantc.de.diskResource.client.views.metadata.dialogs;

import org.iplantc.de.client.models.avu.Avu;
import org.iplantc.de.client.models.diskResources.MetadataTemplateAttribute;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.diskResource.client.MetadataView;
import org.iplantc.de.diskResource.client.views.metadata.MetadataTemplateView;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sriram on 5/9/16.
 */
public class MetadataTemplateViewDialog extends IPlantDialog {

    MetadataView.Appearance appearance;
    MetadataTemplateView view;

    @Inject AsyncProviderWrapper<MetadataTermGuideDialog> termGuideDialogProvider;

    @Inject
    public MetadataTemplateViewDialog(MetadataTemplateView view, MetadataView.Appearance appearance) {
        this.view = view;
        this.appearance = appearance;
        setSize(appearance.dialogWidth(), appearance.dialogHeight());
    }

    public void show(MetadataView.Presenter presenter,
                     List<Avu> templateMd,
                     boolean writable,
                     List<MetadataTemplateAttribute> attributes) {
        view.buildMdTermDictionary(attributes, getHeader().getText());
        view.initTemplate(presenter, templateMd, writable, attributes);
        add(view.asWidget());
        super.show();
    }

    public ArrayList<Avu> getMetadataFromTemplate() {
        return view.getMetadataFromTemplate();
    }

    public boolean isValid() {
        return view.isValid();
    }


}
