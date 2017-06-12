package org.iplantc.de.apps.client.events.tools;

/**
 * @author sriram
 */
import org.iplantc.de.client.models.apps.App;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

public class ShareToolsSelected extends
                                   GwtEvent<ShareToolsSelected.ShareToolsSelectedHandler> {

    public static interface ShareToolsSelectedHandler extends EventHandler {
        void onShareToolselected(ShareToolsSelected event);
    }

    public static interface HasShareToolselectedHandlers {
        HandlerRegistration addShareToolselectedHandler(ShareToolsSelectedHandler handler);
    }

    public static final Type<ShareToolsSelectedHandler> TYPE = new Type<>();
    private final List<App> selectedTools;

    public ShareToolsSelected(List<App> selectedTools) {
        this.selectedTools = selectedTools;
    }

    @Override
    public Type<ShareToolsSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ShareToolsSelectedHandler handler) {
        handler.onShareToolselected(this);
    }

    public List<App> getSelectedTools() {
        return selectedTools;
    }

}
