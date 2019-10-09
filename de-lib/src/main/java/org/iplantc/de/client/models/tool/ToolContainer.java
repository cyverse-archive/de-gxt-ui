package org.iplantc.de.client.models.tool;

import com.google.gwt.user.client.ui.HasName;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

/**
 * @author aramsey
 */


public interface ToolContainer extends HasName, ToolResourceRequirements {

    enum NetworkMode {
        none, bridge;
    }

    @PropertyName("working_directory")
    String getWorkingDirectory();

    @PropertyName("working_directory")
    void setWorkingDirectory(String directory);

    @PropertyName("entrypoint")
    String getEntryPoint();

    @PropertyName("entrypoint")
    void setEntryPoint(String entryPoint);

    @PropertyName("uid")
    void setUID(Integer uid);

    @PropertyName("uid")
    Integer getUID();

    @PropertyName("network_mode")
    String getNetworkMode();

    @PropertyName("network_mode")
    void setNetworkMode(String networkMode);

    @PropertyName("container_devices")
    List<ToolDevice> getDeviceList();

    @PropertyName("container_devices")
    void setDeviceList(List<ToolDevice> devices);

    @PropertyName("container_volumes")
    List<ToolVolume> getContainerVolumes();

    @PropertyName("container_volumes")
    void setContainerVolumes(List<ToolVolume> containerVolumes);

    @PropertyName("image")
    ToolImage getImage();

    @PropertyName("image")
    void setImage(ToolImage toolImage);

    @PropertyName("container_volumes_from")
    List<ToolVolumesFrom> getContainerVolumesFrom();

    @PropertyName("container_volumes_from")
    void setContainerVolumesFrom(List<ToolVolumesFrom> toolVolumesFroms);

    @PropertyName("pids_limit")
    void setPidsLimit(Integer pidLimit);

    @PropertyName("pids_limit")
    Integer getPidsLimit();

    @PropertyName("skip_tmp_mount")
    Boolean isSkipTmpMount();

    @PropertyName("skip_tmp_mount")
    void setSkipTmpMount(Boolean skip);

    @PropertyName("container_ports")
    List<ToolContainerPort> getContainerPorts();

    @PropertyName("container_ports")
    void setContainerPorts(List<ToolContainerPort> ports);

    @PropertyName("interactive_apps")
    InteractiveApp getInteractiveApps();

    @PropertyName("interactive_apps")
    void setInteractiveApps(InteractiveApp app);
}
