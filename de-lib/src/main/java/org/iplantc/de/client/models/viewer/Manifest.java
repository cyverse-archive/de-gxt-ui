package org.iplantc.de.client.models.viewer;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

/**
 * @author aramsey
 */
public interface Manifest {

    @PropertyName("content-type")
    String getContentType();

    @PropertyName("content-type")
    void setContentType(String contentType);

    String getInfoType();

    void setInfoType(String infoType);

    Integer getColumns();

    void setColumns(Integer columns);

    @PropertyName("is-path-list")
    Boolean isPathList();

    @PropertyName("is-path-list")
    void setPathList(Boolean pathList);

    List<VizUrl> getUrls();

    void setUrls(List<VizUrl> urls);
}
