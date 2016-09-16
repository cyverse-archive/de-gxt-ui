package org.iplantc.de.client.models.diskResources;

import com.google.gwt.user.client.TakesValue;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;
import org.iplantc.de.client.models.HasSettableId;

/**
 * @author Sriram
 */
public interface TemplateAttributeSelectionItem extends HasSettableId, TakesValue<String> {

    @PropertyName("is_default")
    boolean isDefaultValue();

    @PropertyName("is_default")
    void setDefaultValue(boolean isDefault);
}
