package org.iplantc.de.apps.client.gin.factory;

import org.iplantc.de.apps.client.views.details.doc.AppDocEditView;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppDoc;

/**
 * @author aramsey
 */
public interface AppDocEditViewFactory {
    AppDocEditView create(App app, AppDoc appDoc);
}
