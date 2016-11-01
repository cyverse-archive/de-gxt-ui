package org.iplantc.de.client.models.apps.proxy;

import org.iplantc.de.client.models.apps.App;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import com.sencha.gxt.data.shared.loader.PagingLoadResult;

import java.util.List;

public interface AppListLoadResult extends PagingLoadResult {

    @PropertyName("apps")
    void setData(List<App> data);

    @PropertyName("apps")
    List<App> getData();

    @PropertyName("total")
    int getTotal();

    @PropertyName("total")
    void setTotal(int total);
}

