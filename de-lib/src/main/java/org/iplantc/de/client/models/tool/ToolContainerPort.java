package org.iplantc.de.client.models.tool;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface ToolContainerPort {
    @PropertyName("host_port")
    Integer getHostPort();

    @PropertyName("host_port")
    void setHostPort(Integer port);

    @PropertyName("container_port")
    Integer getContainerPort();

    @PropertyName("container_port")
    void setContainerPort(Integer port);

    @PropertyName("bind_to_host")
    Boolean isBindToHost();

    @PropertyName("bind_to_host")
    void setBindToHost(Boolean bind);
}
