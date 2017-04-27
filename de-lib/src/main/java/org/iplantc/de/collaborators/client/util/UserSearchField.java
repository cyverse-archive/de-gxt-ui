/**
 *
 */
package org.iplantc.de.collaborators.client.util;

import org.iplantc.de.client.models.collaborators.Collaborator;
import org.iplantc.de.collaborators.client.events.UserSearchResultSelected;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.data.shared.loader.BeforeLoadEvent;
import com.sencha.gxt.data.shared.loader.BeforeLoadEvent.BeforeLoadHandler;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadConfigBean;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.form.ComboBox;

/**
 * @author sriram
 *
 */
public class UserSearchField implements IsWidget,
                                        UserSearchResultSelected.HasUserSearchResultSelectedEventHandlers {

    public class UsersLoadConfig extends PagingLoadConfigBean {
        private String query;

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }
    }

    public interface UserSearchFieldAppearance {
        void render(Cell.Context context, Collaborator collaborator, SafeHtmlBuilder sb);

        String searchCollab();

        String collaboratorDisplayName(Collaborator c);
    }

    private final UserSearchRPCProxy searchProxy;
    private ComboBox<Collaborator> combo;
    private ListView<Collaborator, Collaborator> view;
    private UserSearchFieldAppearance appearance;
    private HandlerManager handlerManager;

    @Inject
    public UserSearchField(UserSearchFieldAppearance appearance,
                           UserSearchRPCProxy searchProxy) {
        this.appearance = appearance;
        this.searchProxy = searchProxy;
        PagingLoader<UsersLoadConfig, PagingLoadResult<Collaborator>> loader = buildLoader();

        ListStore<Collaborator> store = buildStore();
        loader.addLoadHandler(new LoadResultListStoreBinding<UsersLoadConfig, Collaborator, PagingLoadResult<Collaborator>>(
                store));

        view = buildView(store);

        ComboBoxCell<Collaborator> cell = buildComboCell(store, view);
        initCombo(loader, cell);
    }

    private void initCombo(PagingLoader<UsersLoadConfig, PagingLoadResult<Collaborator>> loader,
            ComboBoxCell<Collaborator> cell) {
        combo = new ComboBox<Collaborator>(cell);
        combo.setLoader(loader);
        combo.setMinChars(3);
        combo.setWidth(250);
        combo.setHideTrigger(true);
        combo.setEmptyText(appearance.searchCollab());
        combo.addSelectionHandler(new SelectionHandler<Collaborator>() {

            @Override
            public void onSelection(SelectionEvent<Collaborator> event) {
                Collaborator collaborator = combo.getListView().getSelectionModel().getSelectedItem();
                ensureHandlers().fireEvent(new UserSearchResultSelected(collaborator));
            }
        });
    }

    private ComboBoxCell<Collaborator> buildComboCell(ListStore<Collaborator> store,
            ListView<Collaborator, Collaborator> view) {
        ComboBoxCell<Collaborator> cell = new ComboBoxCell<Collaborator>(store,
                new StringLabelProvider<Collaborator>() {

                    @Override
                    public String getLabel(Collaborator c) {
                        return appearance.collaboratorDisplayName(c);
                    }

                }, view) {
            @Override
            protected void onEnterKeyDown(Context context, Element parent, Collaborator value,
                    NativeEvent event, ValueUpdater<Collaborator> valueUpdater) {
                if (isExpanded()) {
                    super.onEnterKeyDown(context, parent, value, event, valueUpdater);
                }
            }

        };

        return cell;
    }

    private ListView<Collaborator, Collaborator> buildView(ListStore<Collaborator> store) {
        ListView<Collaborator, Collaborator> view = new ListView<Collaborator, Collaborator>(store,
                new IdentityValueProvider<Collaborator>());

        view.setCell(new AbstractCell<Collaborator>() {

            @Override
            public void render(com.google.gwt.cell.client.Cell.Context context, Collaborator value,
                    SafeHtmlBuilder sb) {
                appearance.render(context, value, sb);
            }

        });
        return view;
    }

    private PagingLoader<UsersLoadConfig, PagingLoadResult<Collaborator>> buildLoader() {
        PagingLoader<UsersLoadConfig, PagingLoadResult<Collaborator>> loader = new PagingLoader<UsersLoadConfig, PagingLoadResult<Collaborator>>(
                searchProxy);
        loader.useLoadConfig(new UsersLoadConfig());
        loader.addBeforeLoadHandler(new BeforeLoadHandler<UsersLoadConfig>() {

            @Override
            public void onBeforeLoad(BeforeLoadEvent<UsersLoadConfig> event) {
                String query = combo.getText();
                if (query != null && !query.equals("")) {
                    event.getLoadConfig().setQuery(query);
                }

            }
        });
        return loader;
    }

    private ListStore<Collaborator> buildStore() {
        ListStore<Collaborator> store = new ListStore<Collaborator>(item -> item.getUserName());
        return store;
    }

    @Override
    public Widget asWidget() {
        return combo;
    }

    public void setViewDebugId(String debugId) {
        view.ensureDebugId(debugId);
    }

    @Override
    public HandlerRegistration addUserSearchResultSelectedEventHandler(UserSearchResultSelected.UserSearchResultSelectedEventHandler handler) {
        return ensureHandlers().addHandler(UserSearchResultSelected.TYPE, handler);
    }

    protected HandlerManager ensureHandlers() {
        if (handlerManager == null) {
            handlerManager = new HandlerManager(this);
        }
        return handlerManager;
    }
}
