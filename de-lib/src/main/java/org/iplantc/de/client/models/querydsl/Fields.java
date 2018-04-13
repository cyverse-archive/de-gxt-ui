package org.iplantc.de.client.models.querydsl;

import org.iplantc.de.client.models.sharing.PermissionValue;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

/**
 * The autobean presentation of the "fields" object in an elasticsearch query response
 */
public interface Fields {
    @PropertyName("permission")
    List<PermissionValue> getPermissions();
}
