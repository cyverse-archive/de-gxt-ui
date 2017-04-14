package org.iplantc.de.client.models.notifications.payload;

import com.google.web.bindery.autobean.shared.AutoBean;

import java.util.List;

/**
 * An AutoBean interface for a notification regarding tool sharing
 *
 * @author aramsey
 */
public interface PayloadToolMsg {

    String getAction();

    @AutoBean.PropertyName("tools")
    List<PayloadTool> getTools();
}
