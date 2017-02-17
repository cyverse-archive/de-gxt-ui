package org.iplantc.de.admin.desktop.client.services.model;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

public interface AppCategorizeRequest {
    interface CategoryId {
        @PropertyName("system_id")
        String getSystemId();

        @PropertyName("system_id")
        void setSystemId(String systemId);

        @PropertyName("id")
        String getId();

        @PropertyName("id")
        void setId(String id);
    }

    interface CategoryRequest {
        @PropertyName("system_id")
        String getSystemId();

        @PropertyName("system_id")
        void setSystemId(String systemId);

        @PropertyName("app_id")
        String getAppId();

        @PropertyName("app_id")
        void setAppId(String id);

        @PropertyName("category_ids")
        List<CategoryId> getCategories();

        @PropertyName("category_ids")
        void setCategories(List<CategoryId> categories);
    }

    List<CategoryRequest> getCategories();

    void setCategories(List<CategoryRequest> categories);


}
