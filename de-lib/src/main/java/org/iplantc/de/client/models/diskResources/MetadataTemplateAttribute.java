package org.iplantc.de.client.models.diskResources;

import org.iplantc.de.client.models.HasDescription;
import org.iplantc.de.client.models.HasId;

import com.google.gwt.user.client.ui.HasName;
import com.google.web.bindery.autobean.shared.Splittable;

import java.util.List;

public interface MetadataTemplateAttribute extends HasId, HasName, HasDescription {

    String getType();

    void setType(String type);

    void setRequired(Boolean required);

    Boolean isRequired();

    // Applicable only for Enum type
    List<TemplateAttributeSelectionItem> getValues();

    // Applicable only for Enum type
    void setValues(List<TemplateAttributeSelectionItem> values);

    /**
     * Arbitrary JSON settings, set by Belphegor admins for use in the DE Metadata Template view.
     * For example, as OLS query param settings for metadata term attributes.
     *
     * @return Arbitrary JSON settings.
     */
    Splittable getSettings();

    /**
     * Arbitrary JSON settings, set by Belphegor admins for use in the DE Metadata Template view.
     * For example, as OLS query param settings for metadata term attributes.
     *
     * @param settings Arbitrary JSON settings.
     */
    void setSettings(Splittable settings);


    /**
     * @return Nested Metadata Template Attributes grouped under or associated with this Attribute.
     */
    List<MetadataTemplateAttribute> getAttributes();

    /**
     * @param attributes Nested Metadata Template Attributes grouped under or associated with this Attribute.
     */
    void setAttributes(List<MetadataTemplateAttribute> attributes);
}
