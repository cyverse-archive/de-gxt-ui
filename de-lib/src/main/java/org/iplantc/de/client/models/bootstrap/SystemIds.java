package org.iplantc.de.client.models.bootstrap;

import org.iplantc.de.client.models.HasSplittableError;

import com.google.web.bindery.autobean.shared.AutoBean;

import java.util.List;

/**
 * Created by sriram on 2/24/17.
 */
public interface SystemIds extends HasSplittableError {

    @AutoBean.PropertyName("de_system_id")
    String getDESytemId();

    @AutoBean.PropertyName("all_system_ids")
    List<String> getAllSystemIds();
}
