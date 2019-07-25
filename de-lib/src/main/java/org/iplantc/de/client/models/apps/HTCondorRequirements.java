package org.iplantc.de.client.models.apps;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface HTCondorRequirements {

    @PropertyName("extra_requirements")
    String getExtraRequirements();

    @PropertyName("extra_requirements")
    void setExtraRequirements(String extraRequirements);
}
