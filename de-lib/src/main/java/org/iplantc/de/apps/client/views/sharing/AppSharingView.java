/**
 * 
 * @author sriram
 */

package org.iplantc.de.apps.client.views.sharing;

import org.iplantc.de.client.models.apps.App;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

public interface AppSharingView extends IsWidget {

    void addShareWidget(Widget widget);

    void setSelectedApps(List<App> models);
}
