package org.iplantc.de.client.models.diskResources;

import org.iplantc.de.client.models.HasPaths;

import java.util.List;

/**
 * Created by sriram on 7/27/17.
 */
public interface HTPathListRequest extends HasPaths {

    String getDest();

    void setDest(String dest);

    Boolean isFoldersOnly();

    void setFoldersOnly(Boolean foldersOnly);

    void setPattern(String pattern);

    String getPattern();

    List<String> getInfoTypes();

    void setInfoTypes(List<String> infoTypes);
}
