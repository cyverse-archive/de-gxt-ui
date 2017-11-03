package org.iplantc.de.diskResource.client.events.selection;

import org.iplantc.de.client.models.diskResources.MetadataTemplateInfo;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A GWT event that fires when the user hits the "info" button for a metadata template
 */
public class MetadataInfoBtnSelected
        extends GwtEvent<MetadataInfoBtnSelected.MetadataInfoBtnSelectedHandler> {
    public interface MetadataInfoBtnSelectedHandler extends EventHandler {
        void onMetadataInfoBtnSelected(MetadataInfoBtnSelected event);
    }

    public interface HasMetadataInfoBtnSelectedHandlers {
        HandlerRegistration addMetadataInfoBtnSelectedHandler(MetadataInfoBtnSelectedHandler handler);
    }

    private MetadataTemplateInfo templateInfo;

    public MetadataInfoBtnSelected(MetadataTemplateInfo templateInfo) {
        this.templateInfo = templateInfo;
    }

    public static Type<MetadataInfoBtnSelectedHandler> TYPE =
            new Type<MetadataInfoBtnSelectedHandler>();

    public Type<MetadataInfoBtnSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(MetadataInfoBtnSelectedHandler handler) {
        handler.onMetadataInfoBtnSelected(this);
    }

    public MetadataTemplateInfo getTemplateInfo() {
        return templateInfo;
    }
}
