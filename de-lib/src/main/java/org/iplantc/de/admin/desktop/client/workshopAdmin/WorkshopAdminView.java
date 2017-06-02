package org.iplantc.de.admin.desktop.client.workshopAdmin;

import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.collaborators.client.events.UserSearchResultSelected;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author dennis
 */
public interface WorkshopAdminView extends IsWidget,
                                           IsMaskable,
                                           UserSearchResultSelected.HasUserSearchResultSelectedEventHandlers{

    interface WorkshopAdminViewAppearance {

        String delete();

        ImageResource deleteIcon();

        String save();

        ImageResource saveIcon();

        String refresh();

        ImageResource refreshIcon();

        int nameColumnWidth();

        String nameColumnLabel();

        int emailColumnWidth();

        String emailColumnLabel();

        int institutionColumnWidth();

        String institutionColumnLabel();

        String partialGroupSaveMsg();

        String loadingMask();
    }

    interface Presenter {

        void go(HasOneWidget container);

        void setViewDebugId(String baseId);
    }

    <H extends EventHandler> void addLocalEventHandler(GwtEvent.Type<H> type, H handler);
}
