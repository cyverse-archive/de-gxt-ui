package org.iplantc.de.admin.desktop.client.toolRequest.presenter;

import org.iplantc.de.admin.desktop.client.toolAdmin.ToolAdminView;
import org.iplantc.de.admin.desktop.client.toolAdmin.events.PublishToolEvent;
import org.iplantc.de.admin.desktop.client.toolAdmin.gin.factory.ToolAdminViewFactory;
import org.iplantc.de.admin.desktop.client.toolAdmin.service.ToolAdminServiceFacade;
import org.iplantc.de.admin.desktop.client.toolAdmin.view.dialogs.ToolAdminDetailsDialog;
import org.iplantc.de.admin.desktop.client.toolRequest.ToolRequestView;
import org.iplantc.de.admin.desktop.client.toolRequest.events.AdminMakeToolPublicSelectedEvent;
import org.iplantc.de.admin.desktop.client.toolRequest.service.ToolRequestServiceFacade;
import org.iplantc.de.admin.desktop.shared.Belphegor;
import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.models.tool.ToolType;
import org.iplantc.de.client.models.toolRequests.ToolRequest;
import org.iplantc.de.client.models.toolRequests.ToolRequestDetails;
import org.iplantc.de.client.models.toolRequests.ToolRequestUpdate;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

import com.sencha.gxt.data.shared.ListStore;

import java.util.List;

/**
 * @author jstroot sriram
 */
public class ToolRequestPresenterImpl implements ToolRequestView.Presenter, PublishToolEvent.PublishToolEventHandler {

    ToolRequestView view;
    ToolRequestServiceFacade toolReqService;
    UserInfo userInfo;
    ToolRequestPresenterAppearance appearance;
    ToolAdminViewFactory adminFactory;
    ToolAdminView adminView;
    ToolAdminServiceFacade toolAdminServiceFacade;
    @Inject IplantAnnouncer announcer;

    @Inject
    ToolRequestPresenterImpl(final ToolRequestView view,
                             final ToolRequestServiceFacade toolReqService,
                             final UserInfo userInfo,
                             final ToolRequestPresenterAppearance appearance,
                             final ToolAdminViewFactory adminFactory,
                             final ToolAdminServiceFacade toolAdminServiceFacade) {
        this.view = view;
        this.toolReqService = toolReqService;
        this.userInfo = userInfo;
        this.appearance = appearance;
        this.adminFactory = adminFactory;
        this.toolAdminServiceFacade = toolAdminServiceFacade;
        view.setPresenter(this);
    }

    @Override
    public void updateToolRequest(String id, final ToolRequestUpdate update) {
        toolReqService.updateToolRequest(id, update, new AsyncCallback<ToolRequestDetails>() {

            @Override
            public void onSuccess(ToolRequestDetails result) {
                announcer.schedule(new SuccessAnnouncementConfig(appearance.toolRequestUpdateSuccessMessage()));
                view.update(update, result);
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }
        });

    }

    @Override
    public void fetchToolRequestDetails(ToolRequest toolRequest) {
        view.maskDetailsPanel(appearance.getToolRequestDetailsLoadingMask());
        toolReqService.getToolRequestDetails(toolRequest, new AsyncCallback<ToolRequestDetails>() {

            @Override
            public void onSuccess(ToolRequestDetails result) {
                view.unmaskDetailsPanel();
                view.setDetailsPanel(result);
            }

            @Override
            public void onFailure(Throwable caught) {
                view.unmaskDetailsPanel();
                ErrorHandler.post(caught);
            }
        });
    }

    @Override
    public void go(HasOneWidget container) {
        view.mask(appearance.getToolRequestsLoadingMask());
        view.getDetailsPanel().addAdminMakeToolPublicEventHandler(this);
        container.setWidget(view);
        toolReqService.getToolRequests(null, userInfo.getUsername(), new AsyncCallback<List<ToolRequest>>() {

            @Override
            public void onSuccess(List<ToolRequest> result) {
                view.unmask();
                view.setToolRequests(result);
            }

            @Override
            public void onFailure(Throwable caught) {
                view.unmask();
                ErrorHandler.post(caught);
            }
        });
    }

    @Override
    public void setViewDebugId(String baseId) {
        view.asWidget().ensureDebugId(baseId + Belphegor.ToolRequestIds.VIEW);
    }

    @Override
    public void onAdminMakeToolPublicSelected(AdminMakeToolPublicSelectedEvent event) {
        toolAdminServiceFacade.getToolTypes(new AsyncCallback<List<ToolType>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(List<ToolType> result) {
                List<String> toolTypes = Lists.newArrayList();
                result.forEach(toolType -> toolTypes.add(toolType.getName()));
                adminView = adminFactory.create(new ListStore<>(HasId::getId));
                adminView.setToolTypes(toolTypes);
                adminView.addPublishToolEventHandler(ToolRequestPresenterImpl.this);

                getToolDetails(event);
            }
        });
    }

    protected void getToolDetails(AdminMakeToolPublicSelectedEvent event) {
        toolAdminServiceFacade.getToolDetails(event.getToolId(), new AsyncCallback<Tool>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(Tool result) {
                adminView.editToolDetails(result, ToolAdminDetailsDialog.Mode.MAKEPUBLIC);
            }
        });
    }

    @Override
    public void onPublish(PublishToolEvent event) {
        toolAdminServiceFacade.publishTool(event.getTool(), new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                ErrorHandler.post(appearance.publishFailed(), throwable);
            }

            @Override
            public void onSuccess(Void aVoid) {
                announcer.schedule(new SuccessAnnouncementConfig(appearance.publishSuccess()));
            }
        });
    }
}
