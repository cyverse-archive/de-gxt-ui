package org.iplantc.de.apps.client.views.tools;

import org.iplantc.de.apps.client.views.ManageToolsToolbarView;
import org.iplantc.de.apps.integration.client.presenter.ToolSearchRPCProxy;
import org.iplantc.de.client.models.tool.Tool;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

/**
 * Created by sriram on 4/21/17.
 */
public class ManageToolsViewToolbarImpl extends Composite implements ManageToolsToolbarView {

    @UiTemplate("ManageToolsToolbar.ui.xml")
    interface  ManageToolsViewToolbarUiBinder extends UiBinder<Widget, ManageToolsViewToolbarImpl> {
        
    }

    @UiField
    TextButton shareMenuButton;

    @UiField
    MenuItem  shareCollab;

    @UiField
    MenuItem sharePublic;

    @UiField
    TextButton refreshButton;

    @UiField(provided=true)
    ToolSearchField toolSearch;

    private static final ManageToolsViewToolbarUiBinder uiBinder = GWT.create(ManageToolsViewToolbarUiBinder.class);

    final PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Tool>> loader;

    @Inject
    public ManageToolsViewToolbarImpl(final ManageToolsToolbarView.ManageToolsToolbarApperance apperance) {
        loader = new PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Tool>>(new ToolSearchRPCProxy()) ;
        toolSearch = new ToolSearchField(loader);
        initWidget(uiBinder.createAndBindUi(this));
    }
    
}
