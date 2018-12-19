/**
 *
 */
package org.iplantc.de.client.models.tool;

import org.iplantc.de.client.models.errorHandling.SimpleServiceError;
import org.iplantc.de.shared.DEProperties;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

/**
 * @author sriram
 */
public interface ToolAutoBeanFactory extends AutoBeanFactory {

    AutoBean<Tool> getTool();

    AutoBean<ToolList> getToolList();

    AutoBean<ToolContainer> getContainer();

    AutoBean<ToolDevice> getDevice();

    AutoBean<ToolVolume> getVolume();

    AutoBean<ToolVolumesFrom> getVolumesFrom();

    AutoBean<ToolImage> getImage();

    AutoBean<ToolImplementation> getImplementation();

    AutoBean<ToolTestData> getTest();

    AutoBean<SimpleServiceError> simpleServiceError();

    AutoBean<ToolTypeList> getToolTypeList();

    AutoBean<ToolContainerPort> getToolContainerPort();

    AutoBean<InteractiveApp> getInteractiveApp();

    /**
     * Build a tool with default values
     *
     * @return  a tool
     */
    default AutoBean<Tool> getDefaultTool() {
        Tool tool = getTool().as();
        ToolImage image = getImage().as();
        InteractiveApp interactiveApp = getInteractiveApp().as();
        ToolContainer container = getContainer().as();

        container.setImage(image);
        container.setInteractiveApps(interactiveApp);
        tool.setContainer(container);

        return AutoBeanUtils.getAutoBean(tool);
    }

    default void appendDefaultInteractiveAppValues(Tool tool, DEProperties deProperties) {
        InteractiveApp interactiveApp = getInteractiveApp().as();
        interactiveApp.setImage(deProperties.getDefaultViceImage());
        interactiveApp.setName(deProperties.getDefaultViceName());
        interactiveApp.setCasUrl(deProperties.getDefaultViceCasUrl());
        interactiveApp.setCasValidate(deProperties.getDefaultViceCasValidate());

        tool.getContainer().setInteractiveApps(interactiveApp);
    }
}
