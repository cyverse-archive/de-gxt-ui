package org.iplantc.de.client.models.analysis;

import com.google.web.bindery.autobean.shared.AutoBean;

import java.util.List;

/**
 * @author sriram 
 *
 */
public interface VICELogs {

    @AutoBean.PropertyName("since_time")
    String getSinceTime();

    List<String> getLines();

}
