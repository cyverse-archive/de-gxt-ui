package org.iplantc.de.apps.client.views.list.widgets;

import org.iplantc.de.apps.client.events.AppTypeFilterChangedEvent;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.AppTypeFilter;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;

import java.util.Arrays;

/**
 * Created by sriram on 7/3/18.
 */
public class AppTypeFilterCombo implements IsWidget {

    private SimpleComboBox<org.iplantc.de.client.models.AppTypeFilter> filterTypeCombo;

    private EventBus eventBus;

    @Inject
    public AppTypeFilterCombo(EventBus eventBus) {
        this.eventBus = eventBus;
        filterTypeCombo = new SimpleComboBox<>(new StringLabelProvider<>());
        filterTypeCombo.add(Arrays.asList(org.iplantc.de.client.models.AppTypeFilter.ALL,
                                          org.iplantc.de.client.models.AppTypeFilter.AGAVE,
                                          org.iplantc.de.client.models.AppTypeFilter.DE,
                                          org.iplantc.de.client.models.AppTypeFilter.INTERACTIVE,
                                          org.iplantc.de.client.models.AppTypeFilter.OSG));
        filterTypeCombo.setEditable(false);
        filterTypeCombo.setValue(org.iplantc.de.client.models.AppTypeFilter.ALL);
        filterTypeCombo.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        filterTypeCombo.addSelectionHandler(event -> {
            onTypeFilterChange(event.getSelectedItem());
        });
    }


    private void onTypeFilterChange(org.iplantc.de.client.models.AppTypeFilter af) {
        eventBus.fireEvent(new AppTypeFilterChangedEvent(af));
    }

    @Override
    public Widget asWidget() {
        return filterTypeCombo;
    }

    public void setFilter(AppTypeFilter filter) {
        filterTypeCombo.setValue(filter);
    }
}
