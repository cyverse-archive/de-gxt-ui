package org.iplantc.de.notifications.client.views.cells;

import org.iplantc.de.client.models.notifications.NotificationMessage;
import org.iplantc.de.notifications.client.utils.NotificationUtil;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.inject.Inject;

/**
 * A cell to render notification messages in a Grid
 *
 * @author sriram
 */
public class NotificationMessageCell extends AbstractCell<NotificationMessage> {

    public interface NotificationMessageCellAppearance {
        void render(Cell.Context context, NotificationMessage value, SafeHtmlBuilder sb);
    }

    private final NotificationMessageCellAppearance appearance =
            GWT.create(NotificationMessageCellAppearance.class);

    @Inject NotificationUtil notificationUtil;

    @Inject
    public NotificationMessageCell() {
        super("click"); //$NON-NLS-1$
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, NotificationMessage value,
            NativeEvent event, ValueUpdater<NotificationMessage> valueUpdater) {
        if (value == null) {
            return;
        }

        // Call the super handler, which handlers the enter key.
        super.onBrowserEvent(context, parent, value, event, valueUpdater);

        if ("click".equals(event.getType())) { //$NON-NLS-1$
            notificationUtil.onNotificationClick(value);
        }
    }

    @Override
    public void render(Context context, NotificationMessage value, SafeHtmlBuilder sb) {
        appearance.render(context, value, sb);
    }

}
