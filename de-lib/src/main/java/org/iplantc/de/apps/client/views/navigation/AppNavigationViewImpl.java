package org.iplantc.de.apps.client.views.navigation;

import org.iplantc.de.apps.client.AppNavigationView;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.CardLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;

import java.util.HashMap;
import java.util.List;

/**
 * @author aramsey
 */
public class AppNavigationViewImpl extends Composite implements AppNavigationView {

    interface AppNavigationViewImplUiBinder extends UiBinder<Widget, AppNavigationViewImpl> {}
    private static final AppNavigationViewImplUiBinder uiBinder = GWT.create(AppNavigationViewImplUiBinder.class);

    @UiField VerticalLayoutContainer vlc;
    @UiField SimpleComboBox<String> combo;
    @UiField CardLayoutContainer container;
    private HashMap<Widget, String> selectionMap;

    @Inject
    public AppNavigationViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        selectionMap = new HashMap<>();
    }

    @UiFactory
    SimpleComboBox<String> createComboBox() {
        SimpleComboBox<String> combo = new SimpleComboBox<>(new StringLabelProvider<>());
        combo.addSelectionHandler(event -> {
            String selectedName = event.getSelectedItem();
            int index = combo.getStore().indexOf(selectedName);
            container.setActiveWidget(container.getWidget(index));
            SelectionEvent.fire(this, container.getActiveWidget());
        });
        return combo;
    }

    @Override
    public void mask() {
        vlc.mask();
    }

    @Override
    public void unmask() {
        vlc.unmask();
    }

    @Override
    public void add(Widget widget, String name) {
        insert(widget, container.getWidgetCount(), name);
    }

    @Override
    public void insert(Widget widget, int index, String name) {
        if (!selectionMap.containsKey(widget)) {
            combo.getStore().add(index, name);
            selectionMap.put(widget, name);
            container.insert(widget, index);
            // Make sure if this is the only Widget, that Widget is shown
            if (container.getWidgetCount() == 1) {
                container.setActiveWidget(container.getWidget(0));
            }
        }
    }

    @Override
    public void clear() {
        selectionMap.clear();
        combo.getStore().clear();
        container.clear();
    }

    @Override
    public void setActiveWidget(Widget widget) {
        container.setActiveWidget(widget);
        combo.setText(selectionMap.get(widget));
    }


    public Widget getWidget(int index) {
        return container.getWidget(index);
    }

    public List<Widget> getWidgets() {
        return Lists.newArrayList(selectionMap.keySet());
    }

    @Override
    public boolean isEmpty() {
        return container.getWidgetCount() == 0;
    }

    @Override
    public int getWidgetCount() {
        return container.getWidgetCount();
    }

    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<Widget> handler) {
        return addHandler(handler, SelectionEvent.getType());
    }
}
