package org.iplantc.de.diskResource.client.model;

import org.iplantc.de.client.models.diskResources.MetadataTemplateInfo;

import com.sencha.gxt.data.shared.ModelKeyProvider;

public final class TemplateInfoModelKeyProvider implements ModelKeyProvider<MetadataTemplateInfo> {
    @Override
    public String getKey(MetadataTemplateInfo item) {
        return item.getId();
    }
}
