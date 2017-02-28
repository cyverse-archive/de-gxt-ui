package org.iplantc.de.apps.integration.client.dialogs;

import org.iplantc.de.apps.integration.client.model.ArgumentProperties;
import org.iplantc.de.apps.integration.client.view.AppsEditorView;
import org.iplantc.de.client.models.apps.integration.Argument;
import org.iplantc.de.resources.client.uiapps.integration.AppIntegrationMessages;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent.DndDropHandler;
import com.sencha.gxt.dnd.core.client.GridDragSource;
import com.sencha.gxt.dnd.core.client.GridDropTarget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

import java.util.List;

/**
 * @author jstroot
 * 
 */
public class CommandLineOrderingView extends Composite {

    interface CommandLineOrderingPanelUiBinder extends UiBinder<Widget, CommandLineOrderingView> {}

    private final class ArgNameValueProvider implements ValueProvider<Argument, String> {
        @Override
        public String getPath() {
            return "name"; //$NON-NLS-1$
        }

        @Override
        public String getValue(Argument object) {
            String retVal = object.getName();
            if (Strings.isNullOrEmpty(retVal)) {
                retVal = object.getLabel();
            }
            return retVal;
        }

        @Override
        public void setValue(Argument object, String value) {
        }
    }

    private final class DropHandler implements DndDropHandler {
        @Override
        public void onDrop(DndDropEvent event) {
            updateArgumentOrdering();
        }
    }

    private static CommandLineOrderingPanelUiBinder BINDER = GWT.create(CommandLineOrderingPanelUiBinder.class);

    @UiField(provided = true)
    ColumnModel<Argument> cm;

    @UiField
    Grid<Argument> orderedGrid;

    @UiField ListStore<Argument> orderedStore;

    private final ArgumentProperties argProps;

    private final AppIntegrationMessages messages;

    private final StoreSortInfo<Argument> orderStoreSortInfo;

    private final AppsEditorView.Presenter presenter;

    private final List<String> uuids;

    public CommandLineOrderingView(List<Argument> arguments, AppsEditorView.Presenter presenter, AppIntegrationMessages messages, List<String> uuids) {
        this.presenter = presenter;
        this.messages = messages;
        this.uuids = uuids;
        argProps = GWT.create(ArgumentProperties.class);
        orderStoreSortInfo = new StoreSortInfo<Argument>(argProps.order(), SortDir.ASC);
        initColumnModels();
        initListStores(arguments);
        initWidget(BINDER.createAndBindUi(this));

        new GridDragSource<Argument>(orderedGrid);
        GridDropTarget<Argument> ordDropTarget = new GridDropTarget<Argument>(orderedGrid);

        DropHandler dropHandler = new DropHandler();
        ordDropTarget.addDropHandler(dropHandler);
        ordDropTarget.setAllowSelfAsSource(true);
        ordDropTarget.setFeedback(Feedback.BOTH);

    }

    private void initColumnModels() {
        ArgNameValueProvider valueProvider = new ArgNameValueProvider();
        ColumnConfig<Argument, String> ordName = new ColumnConfig<Argument, String>(valueProvider, 140, messages.argumentLabel());
        ColumnConfig<Argument, Integer> order = new ColumnConfig<Argument, Integer>(argProps.order(), 30, messages.orderLabel());

        ordName.setSortable(false);
        ordName.setMenuDisabled(true);
        order.setSortable(false);
        order.setMenuDisabled(true);

        List<ColumnConfig<Argument, ?>> cmList = Lists.newArrayList();
        cmList.add(order);
        cmList.add(ordName);
        cm = new ColumnModel<Argument>(cmList);
    }

    @UiFactory
    public ListStore<Argument> createListStore() {
        ListStore<Argument> listStore = new ListStore<Argument>(argProps.id());
        orderStoreSortInfo = new StoreSortInfo<Argument>(argProps.order(), SortDir.ASC);

        // Set store sort info
        listStore.addSortInfo(orderStoreSortInfo);

        // JDS Immediately clear sort info. Otherwise, sorts will be applied when items are added to the
        // store during DnD.
        listStore.clearSortInfo();
        return listStore;
    }

    /**
     * Updates the ordering of the given list
     */
    private void updateArgumentOrdering() {
        for (Argument arg : orderedStore.getAll()) {
            arg.setOrder(orderedStore.indexOf(arg) + 1);
        }
        orderedStore.addSortInfo(orderStoreSortInfo);
        // JDS Immediately clear sort info. Otherwise, sorts will be applied when items are added to the
        // store during DnD.
        orderedStore.clearSortInfo();

    }

}
