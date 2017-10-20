package org.iplantc.de.diskResource.client.views.metadata.cells;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;

import org.iplantc.de.client.models.diskResources.MetadataTemplateInfo;
import org.iplantc.de.diskResource.share.DiskResourceModule;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Node;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.Dialog;

/**
 * Created by sriram on 7/7/16.
 */
public class TemplateInfoCell extends AbstractCell<MetadataTemplateInfo> {

    public interface TemplateInfoCellAppearance {
        void render(SafeHtmlBuilder sb, MetadataTemplateInfo value, String id);

        String description();

        String background();

        String descriptionWidth();

        String descriptionHeight();
    }

    private TemplateInfoCellAppearance appearance;
    @Inject AsyncProviderWrapper<Dialog> dialogProvider;
    private String debugId;
    private HandlerManager handlerManager;

    @Inject
    public TemplateInfoCell(TemplateInfoCellAppearance appearance) {
       super(CLICK);
       this.appearance = appearance;
    }

    @Override
    public void render(Context context, MetadataTemplateInfo value, SafeHtmlBuilder sb) {
        String id = debugId + "." + value.getId() + DiskResourceModule.MetadataIds.TEMPLATE_INFO_CELL;
        appearance.render(sb, value, id);
    }

    @Override
    public void onBrowserEvent(Cell.Context context,
                               Element parent,
                               MetadataTemplateInfo value,
                               NativeEvent event,
                               ValueUpdater<MetadataTemplateInfo> valueUpdater) {
        Element eventTarget = Element.as(event.getEventTarget());
        if ((value == null) || !parent.isOrHasChild(eventTarget)) {
            return;
        }


        Element child = findAppNameElement(parent);
        if (child != null && child.isOrHasChild(eventTarget)) {
            switch (Event.as(event).getTypeInt()) {
                case Event.ONCLICK:
                    doOnClick(value);
                    break;
                default:
                    break;
            }

        }
    }

    private Element findAppNameElement(Element parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            Node childNode = parent.getChild(i);

            if (Element.is(childNode)) {
                Element child = Element.as(childNode);
                if (child.getAttribute("name").equalsIgnoreCase(appearance.description())) { //$NON-NLS-1$
                    return child;
                }
            }
        }
        return null;
    }

    private void doOnClick(MetadataTemplateInfo value) {
        dialogProvider.get(new AsyncCallback<Dialog>() {
            @Override
            public void onFailure(Throwable caught) {}

            @Override
            public void onSuccess(Dialog result) {
                result.setSize(appearance.descriptionWidth(), appearance.descriptionHeight());
                result.setHideOnButtonClick(true);
                result.setHeading(appearance.description());
                HTML desc = new HTML(value.getDescription());
                desc.setStylePrimaryName(appearance.background());
                result.add(desc);
                result.ensureDebugId(DiskResourceModule.Ids.METADATA_DESC_DLG);
                result.show();
            }
        });
    }

    HandlerManager ensureHandlers() {
        if (handlerManager == null) {
            handlerManager = new HandlerManager(this);
        }
        return handlerManager;
    }

    public void setBaseDebugId(String debugId) {
        this.debugId = debugId;
    }
}
