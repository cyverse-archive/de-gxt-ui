package org.iplantc.de.client.models;

import com.google.web.bindery.autobean.shared.AutoBean;

import java.util.Map;

/**
 * @author aramsey
 */
public interface RootLevelMap {

    class Payload {
        public static String get(String json) {
            return "{\"" + RootLevelMap.MAP_JSON_KEY + "\": " + json + "}";
        }
    }

    String MAP_JSON_KEY = "root_map";

    @AutoBean.PropertyName(MAP_JSON_KEY)
    Map<String, String> getRootMap();
}
