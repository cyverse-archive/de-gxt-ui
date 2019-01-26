package org.iplantc.de.client.models;

import com.google.web.bindery.autobean.shared.AutoBean;

import java.util.List;

import jsinterop.annotations.JsType;

/**
 * An interface for an {@link AutoBean} which has a "list" key.
 * @author aramsey
 */

@JsType
public interface HasStringList {
    List<String> getList();
}
