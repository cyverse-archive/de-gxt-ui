package org.iplantc.de.admin.desktop.client.workshopAdmin.gin.factory;

import com.sencha.gxt.data.shared.ListStore;
import org.iplantc.de.admin.desktop.client.workshopAdmin.WorkshopAdminView;
import org.iplantc.de.client.models.groups.Member;

/**
 * @author sarahr
 */
public interface WorkshopAdminViewFactory {
    WorkshopAdminView create(ListStore<Member> listStore);
}
