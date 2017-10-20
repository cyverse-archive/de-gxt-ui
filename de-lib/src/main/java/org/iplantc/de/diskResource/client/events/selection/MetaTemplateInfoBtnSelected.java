package org.iplantc.de.diskResource.client.events.selection;

import org.iplantc.de.client.models.diskResources.MetadataTemplateInfo;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A GWT event that fires when the user hits the "info" button for a metadata template
 */
public class MetaTemplateInfoBtnSelected
        extends GwtEvent<MetaTemplateInfoBtnSelected.MetaTemplateInfoBtnSelectedHandler> {
    public static interface MetaTemplateInfoBtnSelectedHandler extends EventHandler {
        void onMetaTemplateInfoBtnSelected(MetaTemplateInfoBtnSelected event);
    }

    public interface HasMetaTemplateInfoBtnSelectedHandlers {
        HandlerRegistration addMetaTemplateInfoBtnSelectedHandler(MetaTemplateInfoBtnSelectedHandler handler);
    }

    private MetadataTemplateInfo templateInfo;

    public MetaTemplateInfoBtnSelected(MetadataTemplateInfo templateInfo) {
        this.templateInfo = templateInfo;
    }

    public static Type<MetaTemplateInfoBtnSelectedHandler> TYPE =
            new Type<MetaTemplateInfoBtnSelectedHandler>();

    public Type<MetaTemplateInfoBtnSelectedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(MetaTemplateInfoBtnSelectedHandler handler) {
        handler.onMetaTemplateInfoBtnSelected(this);
    }

    public MetadataTemplateInfo getTemplateInfo() {
        return templateInfo;
    }
}
