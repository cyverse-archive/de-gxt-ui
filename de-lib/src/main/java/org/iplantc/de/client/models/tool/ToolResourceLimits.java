package org.iplantc.de.client.models.tool;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * Resource requirements and limits for a tool container.
 *
 * @author psarando
 */
public interface ToolResourceLimits extends ToolResourceRequirements {

    @PropertyName("memory_limit")
    Long getMemoryLimit();

    @PropertyName("memory_limit")
    void setMemoryLimit(Long memoryLimit);

    @PropertyName("cpu_shares")
    Integer getCpuShares();

    @PropertyName("cpu_shares")
    void setCpuShares(Integer cpuShares);

    @PropertyName("max_cpu_cores")
    Double getMaxCPUCores();

    @PropertyName("max_cpu_cores")
    void setMaxCPUCores(Double cores);
}
