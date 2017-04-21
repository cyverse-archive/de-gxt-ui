package org.iplantc.de.apps.client.views.tools;

import org.iplantc.de.apps.client.views.ManageToolsToolbarView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;

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

    @UiField
    ToolSearchField toolSearch;

    private static final ManageToolsViewToolbarUiBinder uiBinder = GWT.create(ManageToolsViewToolbarUiBinder.class);

    public ManageToolsViewToolbarImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }
    
}
