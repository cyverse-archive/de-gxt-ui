package org.iplantc.de.client.services;


import org.iplantc.de.client.models.AppTypeFilter;
import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.HasQualifiedId;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.client.models.apps.AppCategoryList;
import org.iplantc.de.client.models.apps.AppList;
import org.iplantc.de.client.models.apps.proxy.AppListLoadResult;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.shared.DECallback;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.data.shared.SortDir;

import java.util.List;

/**
 * An interface that provides access to remote services related to apps.
 */
public interface AppServiceFacade {

    interface AppServiceAutoBeanFactory extends AutoBeanFactory {
        AutoBean<AppList> appList();

        AutoBean<AppListLoadResult> loadResult();

        AutoBean<HasId> hasId();

        AutoBean<AppCategoryList> appCategoryList();
    }

    /**
     * Retrieves list of templates in the given group.
     * @param appCategory unique identifier for the group to search in for apps.
     * @param filter filter to be used when getting apps.
     * @param callback called when the RPC call is complete.*/
    void getApps(HasQualifiedId appCategory, AppTypeFilter filter, DECallback<List<App>> callback);

    /**
     * Retrieves a paged listing of templates in the given group.
     *
     * @param appCategoryId unique identifier for the group to search in for apps.
     * @param limit
     * @param sortField
     * @param offset
     * @param sortDir
     * @param callback called when the RPC call is complete.
     */
    void getPagedApps(String appCategoryId, int limit, String sortField, int offset,
            SortDir sortDir, DECallback<String> callback);

    /**
     * Retrieves a hierarchy of public App Groups.
     *
     * @param callback
     * @param loadHpc TODO
     */
    void getPublicAppCategories(DECallback<List<AppCategory>> callback, boolean loadHpc);

    /**
     * Retrieves a hierarchy of all <code>AppCategory</code>s via a secured endpoint.
     *
     * @param callback
     */

    void getAppCategories(boolean privateOnly, DECallback<List<AppCategory>> callback);

    /**
     * Retrieves an app listing for the specified community
     * @param communityDisplayName
     * @param filter
     * @param callback
     */
    void getCommunityApps(String communityDisplayName, AppTypeFilter filter, DECallback<Splittable> callback);
}
