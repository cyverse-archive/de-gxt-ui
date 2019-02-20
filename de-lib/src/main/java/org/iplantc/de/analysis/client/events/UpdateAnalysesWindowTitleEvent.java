package org.iplantc.de.analysis.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 *
 * An event that is fired when analyses window title needs to updated.
 *
 */
public class UpdateAnalysesWindowTitleEvent extends GwtEvent<UpdateAnalysesWindowTitleEvent.UpdateAnalysesWindowTitleEventHandler> {

    public static final Type<UpdateAnalysesWindowTitleEventHandler> TYPE = new Type<>();
    private final String title;


    public String getTitle() {
        return title;
    }

    @Override
    public Type<UpdateAnalysesWindowTitleEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(UpdateAnalysesWindowTitleEventHandler handler) {
          handler.onUpdateAnalysesWindowTitle(this);
    }

    public interface UpdateAnalysesWindowTitleEventHandler extends EventHandler {
        void onUpdateAnalysesWindowTitle(UpdateAnalysesWindowTitleEvent event);
    }

    public UpdateAnalysesWindowTitleEvent(String title) {
        this.title = title;
    }

}
