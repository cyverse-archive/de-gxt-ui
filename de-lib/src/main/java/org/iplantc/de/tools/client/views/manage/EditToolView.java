package org.iplantc.de.tools.client.views.manage;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 * @author aramsey
 */
public interface EditToolView extends IsWidget {

    void edit(Splittable tool);

    void setToolTypes(String[] toolTypes);

    void mask();

    void unmask();

    void close();
}
