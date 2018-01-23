package org.iplantc.de.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.storage.client.StorageMap;

import java.util.Map;

/**
 * A util class to interact with web storage
 *
 * Created by sriram on 1/2/18.
 */
public class WebStorageUtil {


    private static Storage store = Storage.getLocalStorageIfSupported();


    /**
     * Write key/value to web storage
     *
     * @param key
     * @param value
     */
    public static void writeToStorage(String key, String value) {
        if (store != null) {
            store.setItem(key, value);
            return;
        }

        GWT.log("Web storage not supported!");
    }


    /**
     * Read value for the given key from web storage
     *
     * @param key
     * @return  value for the give key
     */
    public static String readFromStorage(String key) {
        if (store == null) {
            GWT.log("Web storage not supported!");
            return null;
        }

        return store.getItem(key);
    }

    /**
     * Return local storage as map
     *
     * @return  a Map of all the entires in web local storage
     */
    public static Map<String, String> getStorageAsMap() {
        if (store == null) {
            GWT.log("Web storage not supported!");
            return null;
        }
        return new StorageMap(store);
    }


}

