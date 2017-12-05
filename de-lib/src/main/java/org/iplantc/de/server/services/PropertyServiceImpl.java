package org.iplantc.de.server.services;

import org.iplantc.de.shared.DEProperties;
import org.iplantc.de.shared.services.PropertyService;

import com.google.gwt.user.client.rpc.SerializationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.List;

/**
 * @author jstroot, sriram, psarando
 */
public class PropertyServiceImpl implements PropertyService{

//    private static final long serialVersionUID = 1L;

    /**
     * The configuration settings.
     */
    private final Environment environment;

    private final Logger LOG = LoggerFactory.getLogger(PropertyServiceImpl.class);

    public PropertyServiceImpl(Environment environment) {
        this.environment = environment;
    }

    @Override
    public HashMap<String, String> getProperties() throws SerializationException {
        List<String> uikeys = DEProperties.getInstance().getPropertyList();
        LOG.info("property list:" + uikeys);
        HashMap<String, Object> propertyMap = new HashMap<>();
        for (PropertySource propertySource : ((AbstractEnvironment)environment).getPropertySources()) {
            if (propertySource instanceof MapPropertySource) {
                propertyMap.putAll(((MapPropertySource)propertySource).getSource());
            }
        }

        HashMap<String, String> stringProps = new HashMap<>();
        for (String key : uikeys) {
            Object value = propertyMap.get(key);
            if (value != null) {
                stringProps.put(key, value.toString());
            } else {
                LOG.warn("Missing property key:" + key, new SerializationException(key));
            }
        }
        return stringProps;
    }
}
