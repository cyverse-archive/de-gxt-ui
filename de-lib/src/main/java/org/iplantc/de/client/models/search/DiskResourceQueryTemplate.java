package org.iplantc.de.client.models.search;

import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.models.tags.Tag;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;
import com.google.web.bindery.autobean.shared.Splittable;

import java.util.Set;

/**
 * This object is used to collect the information required to build a search request for the endpoints
 * described <a href=
 * "https://github.com/iPlantCollaborativeOpenSource/Terrain/blob/dev/doc/endpoints/filesystem/search.md"
 * >here</a>
 * 
 * @author jstroot
 * 
 */
public interface DiskResourceQueryTemplate extends Folder {

    String getFileQuery();
    void setFileQuery(String query);

    Splittable getTemplate();
    void setTemplate(Splittable template);

    /**
     * Overrides the default property name binding of "id" to "label"
     * 
     * @see org.iplantc.de.client.models.HasId#getId()
     */
    @Override
    @PropertyName("label")
    String getId();
    /**
     * @return true if this template has unsaved changes, false otherwise.
     */
    boolean isDirty();

    /**
     * @return true if this template has been persisted, false otherwise.
     */
    boolean isSaved();

    /**
     * Sets the templates dirty state.
     * 
     * @param dirty true if the template has unsaved changes.
     */
    void setDirty(boolean dirty);

    /**
     * Overrides the default property name binding of "id" to "label"
     * 
     * @see org.iplantc.de.client.models.diskResources.DiskResource#setId(java.lang.String)
     */
    @Override
    @PropertyName("label")
    void setId(String id);


}
