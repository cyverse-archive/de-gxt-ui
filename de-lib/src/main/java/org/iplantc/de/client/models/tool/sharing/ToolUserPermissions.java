package org.iplantc.de.client.models.tool.sharing;

import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.sharing.UserPermission;

import com.google.gwt.user.client.ui.HasName;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

/**
 * Created by sriram.
 */
public interface ToolUserPermissions extends HasId, HasName {

    @PropertyName("permissions")
    List<UserPermission> getPermissions();
}
