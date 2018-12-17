package org.iplantc.de.admin.desktop.client.toolAdmin.model;

import org.iplantc.de.client.models.tool.ToolContainerPort;
import org.iplantc.de.client.models.tool.ToolVolumesFrom;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 * @author jstroot
 * @author aramsey
 */
public interface ToolContainerPortProperties extends PropertyAccess<ToolContainerPort> {

    ValueProvider<ToolContainerPort, Integer> hostPort();

    ValueProvider<ToolContainerPort, Integer> containerPort();

    ValueProvider<ToolContainerPort, Boolean> bindToHost();
}
