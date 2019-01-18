package org.iplantc.de.analysis.client.models;

import com.google.web.bindery.autobean.shared.AutoBean;

public interface FilterBean {
    @AutoBean.PropertyName("field")
    String getField();

    @AutoBean.PropertyName("value")
    String getValue();
}

