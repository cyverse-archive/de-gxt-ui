package org.iplantc.de.diskResource.client.model;

import org.iplantc.de.client.models.diskResources.MetadataTemplateInfo;

import com.sencha.gxt.data.shared.LabelProvider;

public final class TemplateInfoLabelProvider implements LabelProvider<MetadataTemplateInfo> {
    @Override
    public String getLabel(MetadataTemplateInfo item) {
        return item.getName();
    }
}
