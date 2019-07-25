package org.iplantc.de.client.models.apps;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface AppExtra {

    @PropertyName("htcondor")
    HTCondorRequirements getHTCondor();

    @PropertyName("htcondor")
    void setHTCondor(HTCondorRequirements requirements);
}
