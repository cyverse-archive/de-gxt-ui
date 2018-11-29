package org.iplantc.de.admin.desktop.client.communities.events;

import org.iplantc.de.client.models.apps.App;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author aramsey
 * A GWT event that fires when the user selects the Categorize button in the Community tab
 */
public class CategorizeButtonClicked extends GwtEvent<CategorizeButtonClicked.CategorizeButtonClickedHandler> {
    public interface CategorizeButtonClickedHandler extends EventHandler {
        void onCategorizeButtonClicked(CategorizeButtonClicked event);
    }

    public interface HasCategorizeButtonClickedHandlers {
        HandlerRegistration addCategorizeButtonClickedHandler(CategorizeButtonClickedHandler handler);
    }

    private App targetApp;

    public CategorizeButtonClicked(App targetApp) {
        this.targetApp = targetApp;
    }

    public static Type<CategorizeButtonClickedHandler> TYPE = new Type<CategorizeButtonClickedHandler>();

    public Type<CategorizeButtonClickedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(CategorizeButtonClickedHandler handler) {
        handler.onCategorizeButtonClicked(this);
    }

    public App getTargetApp() {
        return targetApp;
    }
}
