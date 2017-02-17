package org.iplantc.de.client.models.apps.sharing;

import org.iplantc.de.client.models.apps.QualifiedAppId;
import org.iplantc.de.client.models.sharing.UserPermission;

import com.google.gwt.user.client.ui.HasName;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

/**
 * Created by sriram on 2/3/16.
 */
public interface AppUserPermissions extends QualifiedAppId, HasName {

    @PropertyName("permissions")
    List<UserPermission> getPermissions();
}
