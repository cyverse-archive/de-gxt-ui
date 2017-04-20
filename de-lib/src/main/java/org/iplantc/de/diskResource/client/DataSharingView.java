/**
 * 
 */
package org.iplantc.de.diskResource.client;

import org.iplantc.de.client.models.diskResources.DiskResource;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

/**
 * @author sriram
 *
 */
public interface DataSharingView extends IsWidget {

    void addShareWidget(Widget widget);

    void setSelectedDiskResources(List<DiskResource> models);

}
