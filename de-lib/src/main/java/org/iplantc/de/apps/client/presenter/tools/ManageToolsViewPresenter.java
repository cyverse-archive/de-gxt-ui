package org.iplantc.de.apps.client.presenter.tools;

import org.iplantc.de.apps.client.views.ManageToolsView;
import org.iplantc.de.client.gin.ServicesInjector;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.services.ToolServices;
import org.iplantc.de.commons.client.ErrorHandler;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

import java.util.List;

/**
 * Created by sriram on 4/24/17.
 */
public class ManageToolsViewPresenter implements ManageToolsView.Presenter {

    @Inject
    ManageToolsView toolsView;

    ManageToolsView.ManageToolsViewAppearance appearance;

    ToolServices dcService = ServicesInjector.INSTANCE.getDeployedComponentServices();
    
    @Inject
    public ManageToolsViewPresenter(ManageToolsView.ManageToolsViewAppearance appearance) {
        this.appearance = appearance;
    }

    @Override
    public void go(HasOneWidget container) {
       container.setWidget(toolsView.asWidget());
       toolsView.mask(appearance.mask());
       loadTools();
    }

    @Override
    public void loadTools() {
            dcService.getTools(new AsyncCallback<List<Tool>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    ErrorHandler.post(throwable);
                }

                @Override
                public void onSuccess(List<Tool> tools) {
                    toolsView.unmask();
                    toolsView.loadTools(tools);
                }
            });
    }
}
