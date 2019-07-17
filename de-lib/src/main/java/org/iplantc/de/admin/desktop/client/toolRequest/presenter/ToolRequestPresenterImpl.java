package org.iplantc.de.admin.desktop.client.toolRequest.presenter;

import org.iplantc.de.admin.desktop.client.toolAdmin.service.ToolAdminServiceFacade;
import org.iplantc.de.admin.desktop.client.toolRequest.ToolRequestView;
import org.iplantc.de.admin.desktop.client.toolRequest.events.AdminMakeToolPublicSelectedEvent;
import org.iplantc.de.admin.desktop.client.toolRequest.service.ToolRequestServiceFacade;
import org.iplantc.de.admin.desktop.shared.Belphegor;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.models.tool.ToolAutoBeanFactory;
import org.iplantc.de.client.models.tool.ToolType;
import org.iplantc.de.client.models.toolRequests.ToolRequest;
import org.iplantc.de.client.models.toolRequests.ToolRequestDetails;
import org.iplantc.de.client.models.toolRequests.ToolRequestUpdate;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.tools.client.ReactToolViews;
import org.iplantc.de.tools.client.gin.factory.EditToolViewFactory;
import org.iplantc.de.tools.client.views.manage.EditToolView;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import java.util.List;

/**
 * @author jstroot sriram
 */
public class ToolRequestPresenterImpl implements ToolRequestView.Presenter {

    ToolRequestView view;
    ToolRequestServiceFacade toolReqService;
    UserInfo userInfo;
    ToolRequestPresenterAppearance appearance;
    EditToolViewFactory editToolViewFactory;
    EditToolView editToolView;
    ToolAutoBeanFactory factory;
    ToolAdminServiceFacade toolAdminServiceFacade;
    @Inject IplantAnnouncer announcer;

    @Inject
    ToolRequestPresenterImpl(final ToolRequestView view,
                             final ToolRequestServiceFacade toolReqService,
                             final UserInfo userInfo,
                             final ToolRequestPresenterAppearance appearance,
                             final EditToolViewFactory editToolViewFactory,
                             final ToolAutoBeanFactory factory,
                             final ToolAdminServiceFacade toolAdminServiceFacade) {
        this.view = view;
        this.toolReqService = toolReqService;
        this.userInfo = userInfo;
        this.appearance = appearance;
        this.editToolViewFactory = editToolViewFactory;
        this.factory = factory;
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
        editToolView = editToolViewFactory.create(getBaseProps());

        toolAdminServiceFacade.getToolTypes(new AsyncCallback<List<ToolType>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.postReact(caught);
            }

            @Override
            public void onSuccess(List<ToolType> result) {
                List<String> toolTypes = Lists.newArrayList();
                result.forEach(toolType -> toolTypes.add(toolType.getName()));
                editToolView.setToolTypes(toolTypes.toArray(new String[0]));

                getToolDetails(event);
            }
        });
    }

    protected void getToolDetails(AdminMakeToolPublicSelectedEvent event) {
        view.mask(appearance.getToolRequestsLoadingMask());
        toolAdminServiceFacade.getToolDetails(event.getToolId(), new AsyncCallback<Tool>() {
            @Override
            public void onFailure(Throwable caught) {
                view.unmask();
                ErrorHandler.postReact(caught);
            }

            @Override
            public void onSuccess(Tool result) {
                view.unmask();
                Splittable tool = convertToolToSplittable(result);
                editToolView.edit(tool);
            }
        });
    }

    @Override
    @SuppressWarnings("unusable-by-js")
    public void onPublish(Splittable toolSpl) {
        Tool tool = convertSplittableToTool(toolSpl);
        editToolView.mask();
        toolAdminServiceFacade.publishTool(tool, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                editToolView.unmask();
                ErrorHandler.postReact(appearance.publishFailed(), throwable);
            }

            @Override
            public void onSuccess(Void aVoid) {
                editToolView.close();
                announcer.schedule(new SuccessAnnouncementConfig(appearance.publishSuccess()));
            }
        });
    }

    @Override
    public void closeEditToolDlg() {
        editToolView.close();
    }

    ReactToolViews.AdminPublishingToolProps getBaseProps() {
        ReactToolViews.AdminPublishingToolProps baseProps = new ReactToolViews.AdminPublishingToolProps();
        baseProps.presenter = this;
        baseProps.parentId = Belphegor.ToolAdminIds.TOOL_ADMIN_DIALOG;
        baseProps.isAdmin = true;
        baseProps.isAdminPublishing = true;

        return baseProps;
    }

    Tool convertSplittableToTool(Splittable toolSpl) {
        return AutoBeanCodex.decode(factory, Tool.class, toolSpl.getPayload()).as();
    }

    Splittable convertToolToSplittable(Tool tool) {
        return AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(tool));
    }
}
