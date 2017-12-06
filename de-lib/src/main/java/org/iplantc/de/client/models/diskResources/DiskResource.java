package org.iplantc.de.client.models.diskResources;

import org.iplantc.de.client.models.HasPath;
import org.iplantc.de.client.models.HasSettableId;
import org.iplantc.de.client.models.sharing.PermissionValue;

import com.google.gwt.user.client.ui.HasName;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.Date;

/**
 * @author jstroot
 */
public interface DiskResource extends HasSettableId, HasName, HasPath {

    String INFO_TYPE_KEY = "infoType";
    String NAME_KEY = "name";

    String getInfoType();
    void setInfoType(String infoType);

    void setPath(String path);

    @Override
    @PropertyName("label")
    String getName();

    @Override
    @PropertyName("label")
    void setName(String name);

    @PropertyName("date-created")
    Date getDateCreated();

    @PropertyName("date-created")
    void setDateCreated(Date date);

    @PropertyName("date-modified")
    Date getLastModified();

    @PropertyName("date-modified")
    void setLastModified(Date date);

    @PropertyName("permission")
    PermissionValue getPermission();

    @PropertyName("permission")
    void setPermission(PermissionValue permission);

    @PropertyName("badName")
    boolean isFilter();

    @PropertyName("badName")
    void setFilter(boolean filter);

    @PropertyName("isFavorite")
    boolean isFavorite();

    @PropertyName("isFavorite")
    void setFavorite(boolean favorite);

    @PropertyName("share-count")
    int getShareCount();

    @PropertyName("share-count")
    void setShareCount(int count);

    void setStatLoaded(boolean loaded);

    boolean isStatLoaded();
}
