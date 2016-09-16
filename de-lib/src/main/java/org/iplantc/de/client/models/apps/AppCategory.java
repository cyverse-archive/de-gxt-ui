package org.iplantc.de.client.models.apps;

import com.google.gwt.user.client.ui.HasName;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;
import org.iplantc.de.client.models.HasSettableId;

import java.util.List;

public interface AppCategory extends HasSettableId, HasName {

    @PropertyName("app_count")
    int getAppCount();

    List<AppCategory> getCategories();

    @PropertyName("is_public")
    boolean isPublic();

    @PropertyName("app_count")
    void setAppCount(int templateCount);

    void setCategories(List<AppCategory> categories);

    @PropertyName("is_public")
    void setIsPublic(boolean isPublic);
}
