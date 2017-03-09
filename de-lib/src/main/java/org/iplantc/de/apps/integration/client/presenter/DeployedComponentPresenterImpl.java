/**
 *
 */
package org.iplantc.de.apps.integration.client.presenter;

import org.iplantc.de.apps.integration.client.gin.factory.DeployedComponentListingViewFactory;
import org.iplantc.de.apps.integration.client.view.deployedComponents.proxy.ToolSearchRPCProxy;
import org.iplantc.de.apps.integration.client.view.tools.DeployedComponentsListingView;
import org.iplantc.de.apps.integration.shared.AppIntegrationModule;
import org.iplantc.de.client.models.tool.Tool;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfigBean;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;

/**
 * @author sriram
 *
 */
public class DeployedComponentPresenterImpl implements DeployedComponentsListingView.Presenter {

    class DCKeyProvider implements ModelKeyProvider<Tool> {

        @Override
        public String getKey(Tool item) {
            return item.getId();
        }

    }
    private final DeployedComponentsListingView view;
    PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Tool>> loader;
    ToolSearchRPCProxy searchProxy;

    @Inject
    public DeployedComponentPresenterImpl(DeployedComponentListingViewFactory viewFactory) {
        ListStore<Tool> listStore = getToolListStore();
        searchProxy = getToolSearchRPCProxy();
        loader = buildLoader();
        view = viewFactory.createDcListingView(listStore, loader);
        loader.addLoadHandler(new LoadResultListStoreBinding<FilterPagingLoadConfig, Tool, PagingLoadResult<Tool>>(listStore));
        loader.load(new FilterPagingLoadConfigBean());
    }


    /* (non-Javadoc)
     * @see org.iplantc.core.appsIntegration.client.view.DeployedComponentsListingView.Presenter#getSelectedDC()
     */
    @Override
    public Tool getSelectedDC() {
        return view.getSelectedDC();
    }

    @Override
    public void go(HasOneWidget container) {

        view.asWidget().ensureDebugId(AppIntegrationModule.Ids.INSTALLED_TOOLS_DLG);
        container.setWidget(view.asWidget());
    }

    PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Tool>> buildLoader() {
        final PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Tool>> loader = new PagingLoader<>(searchProxy);
        loader.useLoadConfig(new FilterPagingLoadConfigBean());
        return loader;
    }

    ToolSearchRPCProxy getToolSearchRPCProxy() {
        return new ToolSearchRPCProxy();
    }

    ListStore<Tool> getToolListStore() {
        return new ListStore<>(new DCKeyProvider());
    }

    @Override
    public HandlerRegistration addSelectionChangedHandler(SelectionChangedEvent.SelectionChangedHandler<Tool> handler) {
        return view.addSelectionChangedHandler(handler);
    }
}
