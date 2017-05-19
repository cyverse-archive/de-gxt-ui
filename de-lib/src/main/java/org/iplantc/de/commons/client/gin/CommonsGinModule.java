package org.iplantc.de.commons.client.gin;

import org.iplantc.de.commons.client.gin.factory.SharingPermissionViewFactory;
import org.iplantc.de.commons.client.views.sharing.SharingPermissionView;
import org.iplantc.de.commons.client.views.sharing.SharingPermissionsPanel;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;

/**
 * @author aramsey
 */
public class CommonsGinModule extends AbstractGinModule {
    @Override
    protected void configure() {
        install(new GinFactoryModuleBuilder().implement(SharingPermissionView.class,
                                                        SharingPermissionsPanel.class)
                                             .build(SharingPermissionViewFactory.class));
    }
}
