package org.iplantc.de.client.gin;

import org.iplantc.de.client.gin.factory.SharingPermissionViewFactory;
import org.iplantc.de.client.sharing.SharingPermissionView;
import org.iplantc.de.client.sharing.SharingPermissionsPanel;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;

/**
 * @author aramsey
 */
public class ClientGinModule extends AbstractGinModule {
    @Override
    protected void configure() {
        install(new GinFactoryModuleBuilder().implement(SharingPermissionView.class,
                                                        SharingPermissionsPanel.class)
                                             .build(SharingPermissionViewFactory.class));
    }
}
