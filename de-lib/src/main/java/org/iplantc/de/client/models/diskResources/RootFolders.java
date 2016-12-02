package org.iplantc.de.client.models.diskResources;

import org.iplantc.de.client.models.bootstrap.DataInfo;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;


public interface RootFolders {

    List<Folder> getRoots();

    void setRoots(List<Folder> roots);

    @PropertyName("base-paths")
    DataInfo getBasePaths();

}
