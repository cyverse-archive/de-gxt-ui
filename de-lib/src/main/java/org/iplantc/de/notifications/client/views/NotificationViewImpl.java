/**
 *
 */
package org.iplantc.de.notifications.client.views;

import org.iplantc.de.client.models.notifications.NotificationMessage;
import org.iplantc.de.commons.client.util.CyVerseReactComponents;
import org.iplantc.de.notifications.client.events.NotificationGridRefreshEvent;
import org.iplantc.de.notifications.client.events.NotificationSelectionEvent;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.button.TextButton;

import java.util.List;

/**
 * 
 * Notification View as grid
 * 
 * @author sriram
 * 
 */
public class NotificationViewImpl implements NotificationView {


    HTMLPanel panel;

    NotificationView.Presenter presenter;

    @Inject
    public NotificationViewImpl() {
        panel = new HTMLPanel("<div></div>");
     /*   this.listStore = listStore;
        this.messageCell = messageCell;
        this.appearance = appearance;
        toolBar.getElement().getStyle().setProperty("borderBottom", "none");
        grid.setSelectionModel(checkBoxModel);
        grid.getSelectionModel().setSelectionMode(SelectionMode.MULTI);
        addGridSelectionHandler();
        addGridRefreshHandler();*/
    }

    @Override
    public void setPresenter(NotificationView.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public HandlerRegistration addNotificationGridRefreshEventHandler(NotificationGridRefreshEvent.NotificationGridRefreshEventHandler handler) {
        //  return addHandler(handler, NotificationGridRefreshEvent.TYPE);
        return null;
    }

    @Override
    public HandlerRegistration addNotificationSelectionEventHandler(NotificationSelectionEvent.NotificationSelectionEventHandler handler) {
        //  return addHandler(handler, NotificationSelectionEvent.TYPE);
        return null;
    }

    /* private void addGridRefreshHandler() {
         grid.addRefreshHandler(new RefreshEvent.RefreshHandler() {
             @Override
             public void onRefresh(RefreshEvent event) {
                 fireEvent(new NotificationGridRefreshEvent());
             }
         });
     }

     private void addGridSelectionHandler() {
         grid.getSelectionModel().addSelectionChangedHandler(
                 new SelectionChangedHandler<NotificationMessage>() {

                     @Override
                     public void onSelectionChanged(SelectionChangedEvent<NotificationMessage> event) {
                         fireEvent(new NotificationSelectionEvent(event.getSelection()));
                     }
                 });
     }
 */
    /*
     * (non-Javadoc)
     * 
     * @see com.google.gwt.user.client.ui.IsWidget#asWidget()
     */
    @Override
    public Widget asWidget() {
        return panel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.iplantc.de.client.gxt3.views.NotificationView#getSelectedItems()
     */
    @Override
    public List<NotificationMessage> getSelectedItems() {
        //  return grid.getSelectionModel().getSelectedItems();
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loadNotifications(FilterPagingLoadConfig config) {
/*        listStore.clear();
        ((PagingLoader<FilterPagingLoadConfig, PagingLoadResult<NotificationMessage>>)grid.getLoader())
                .load(config);*/
        Scheduler.get().scheduleFinally(() -> {
            ReactNotifications.NotificationsProps props = new ReactNotifications.NotificationsProps();
            props.presenter = presenter;
            CyVerseReactComponents.render(ReactNotifications.notifiProps, props, panel.getElement());

        });
    }

    @Override
    public void setLoader(
            PagingLoader<FilterPagingLoadConfig, PagingLoadResult<NotificationMessage>> loader) {
/*        grid.setLoader(loader);
        toolBar.bind(loader);*/
    }

    @Override
    public void setNorthWidget(IsWidget widget) {
        // con.setNorthWidget(widget, northData);
    }

    @Override
    public FilterPagingLoadConfig getCurrentLoadConfig() {
/*        FilterPagingLoadConfig lastConfig = (FilterPagingLoadConfig)grid.getLoader().getLastLoadConfig();
        return lastConfig;*/
        return null;
    }

    @Override
    public void mask() {
        //mainPanel.mask(I18N.DISPLAY.loadingMask());

    }

    @Override
    public void unmask() {
        // mainPanel.unmask();
    }

    @Override
    public TextButton getRefreshButton() {
        //return toolBar.getRefreshButton();
        return null;
    }

    @Override
    public void updateStore(NotificationMessage nm) {
/*        if(listStore.findModel(nm)!=null) {
            listStore.update(nm);
        }*/
    }


  /*  @UiFactory
    ColumnModel<NotificationMessage> createColumnModel() {
        NotificationMessageProperties props = GWT.create(NotificationMessageProperties.class);
        List<ColumnConfig<NotificationMessage, ?>> configs = new LinkedList<>();

        checkBoxModel =
                new CheckBoxSelectionModel<>(new IdentityValueProvider<NotificationMessage>());
        @SuppressWarnings("rawtypes")
        ColumnConfig colCheckBox = checkBoxModel.getColumn();
        configs.add(colCheckBox);

        ColumnConfig<NotificationMessage, NotificationCategory> colCategory =
                new ColumnConfig<>(props.category(),
                                   appearance.categoryColumnWidth(),
                                   appearance.category());
        configs.add(colCategory);
        colCategory.setSortable(false);

        ColumnConfig<NotificationMessage, NotificationMessage> colMessage =
                new ColumnConfig<>(new IdentityValueProvider<NotificationMessage>(),
                                   appearance.messagesColumnWidth(),
                                   appearance.messagesGridHeader());
        colMessage.setCell(messageCell);
        configs.add(colMessage);
        colMessage.setSortable(false);

        ColumnConfig<NotificationMessage, Date> colTimestamp = new ColumnConfig<>(new ValueProvider<NotificationMessage, Date>() {

            @Override
            public Date getValue(NotificationMessage object) {
                return new Date(object.getTimestamp());
            }

            @Override
            public void setValue(NotificationMessage object,
                                 Date value) {
                // do nothing
            }

            @Override
            public String getPath() {
                return "timestamp";
            }
        }, appearance.createdDateColumnWidth(), appearance.createdDateGridHeader());
        colTimestamp.setCell(new DateCell(DateTimeFormat
                                                  .getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM)));

        configs.add(colTimestamp);
        return new ColumnModel<>(configs);
    }*/
}
