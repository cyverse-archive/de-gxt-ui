package org.iplantc.de.apps.client.gin.factory;

import org.iplantc.de.apps.client.views.details.doc.AppDocEditViewImpl;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppDoc;

/**
 * @author aramsey
 */
public interface AppDocEditViewFactory {
    AppDocEditViewImpl create(App app, AppDoc appDoc);
}
