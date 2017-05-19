package org.iplantc.de.commons.client.gin.factory;

import org.iplantc.de.client.models.sharing.SharedResource;
import org.iplantc.de.commons.client.views.sharing.SharingPermissionView;
import org.iplantc.de.commons.client.presenter.SharingPresenter;

import com.sencha.gxt.core.shared.FastMap;

/**
 * @author aramsey
 */
public interface SharingPermissionViewFactory {
    SharingPermissionView create(SharingPresenter presenter, FastMap<SharedResource> resources);
}
