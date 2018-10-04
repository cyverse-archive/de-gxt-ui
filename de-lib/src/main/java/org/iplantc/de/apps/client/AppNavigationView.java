package org.iplantc.de.apps.client;

import org.iplantc.de.client.models.IsMaskable;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

/**
 * A view in the Apps window for navigating groups of app categories or hierarchy roots
 * This is effectively the same as TabPanel, except the tabs are replaced with a dropdown menu
 * Adding a String + Widget to the view ties a dropdown selection with that name to a Widget
 * that should be displayed below the dropdown. Changing the dropdown selection will then change
 * the widget displayed.
 */
public interface AppNavigationView extends IsWidget,
                                           IsMaskable,
                                           HasSelectionHandlers<Widget> {

    /**
     * Add a category/hierarchy root to the view with the specified name
     * @param widget
     * @param name
     */
    void add(Widget widget, String name);

    /**
     * Add a category/hierarchy root to the view at the specified index with the specified name
     * @param widget
     * @param name
     */
    void insert(Widget widget, int index, String name);

    /**
     * Remove all options from the view
     */
    void clear();

    /**
     * Set the navigation view to the specified widget
     * @param widget
     */
    void setActiveWidget(Widget widget);

    /**
     * Return the widget at that particular index
     * @param index
     */
    Widget getWidget(int index);

    /**
     * @return true if there are no options in the view
     */
    boolean isEmpty();

    /**
     * @return the list of all widgets added to the view
     */
    List<Widget> getWidgets();

    /**
     * @return the count of all the options in the view
     */
    int getWidgetCount();
}
