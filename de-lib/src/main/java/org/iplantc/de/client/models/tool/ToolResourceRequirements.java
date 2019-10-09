package org.iplantc.de.client.models.tool;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * Resource requirements for a tool container or analysis submission.
 *
 * @author psarando
 */
public interface ToolResourceRequirements {

    @PropertyName("min_memory_limit")
    Long getMinMemoryLimit();

    @PropertyName("min_memory_limit")
    void setMinMemoryLimit(Long memoryLimit);

    @PropertyName("min_cpu_cores")
    Double getMinCPUCores();

    @PropertyName("min_cpu_cores")
    void setMinCPUCores(Double cores);

    @PropertyName("min_disk_space")
    Long getMinDiskSpace();

    @PropertyName("min_disk_space")
    void setMinDiskSpace(Long diskSpace);
}
