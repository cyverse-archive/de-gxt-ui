package org.iplantc.de.client.models.apps;

import com.google.web.bindery.autobean.shared.AutoBean;
import java.util.Date;

/**
 * Created by sriram on 10/21/16.
 */
public interface AppStats {

    @AutoBean.PropertyName("job_count")
    int getTotal();

    @AutoBean.PropertyName("job_count_completed")
    int getTotalCompleted();

    @AutoBean.PropertyName("job_count_failed")
    int getTotalFailed();

    @AutoBean.PropertyName("job_last_completed")
    Date getLastCompletedDate();

    @AutoBean.PropertyName("last_used")
    Date getLastUsedDate();

}
