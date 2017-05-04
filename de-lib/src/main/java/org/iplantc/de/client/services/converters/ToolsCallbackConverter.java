package org.iplantc.de.client.services.converters;

import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.models.tool.ToolAutoBeanFactory;
import org.iplantc.de.client.models.tool.ToolList;
import org.iplantc.de.shared.AppsCallback;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

import java.util.List;

public class ToolsCallbackConverter extends DECallbackConverter<String, List<Tool>> {

    private final ToolAutoBeanFactory factory;

    public ToolsCallbackConverter(AppsCallback<List<Tool>> callback, ToolAutoBeanFactory factory) {
        super(callback);
        this.factory = factory;
    }

    @Override
    protected List<Tool> convertFrom(String object) {
        AutoBean<ToolList> autoBean = AutoBeanCodex.decode(factory, ToolList.class, object);
        List<Tool> items = autoBean.as().getToolList();
        return items;
    }

}
