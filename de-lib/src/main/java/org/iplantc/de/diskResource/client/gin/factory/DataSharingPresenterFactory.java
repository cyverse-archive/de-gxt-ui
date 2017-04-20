package org.iplantc.de.diskResource.client.gin.factory;

import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.sharing.SharingPresenter;

import java.util.List;

/**
 * @author aramsey
 */
public interface DataSharingPresenterFactory {
    SharingPresenter create(List<DiskResource> resources);
}
