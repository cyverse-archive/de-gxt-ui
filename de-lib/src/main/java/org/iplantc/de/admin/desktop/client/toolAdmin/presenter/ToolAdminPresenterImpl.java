package org.iplantc.de.admin.desktop.client.toolAdmin.presenter;

import org.iplantc.de.admin.desktop.client.toolAdmin.ToolAdminView;
import org.iplantc.de.admin.desktop.client.toolAdmin.events.AddToolSelectedEvent;
import org.iplantc.de.admin.desktop.client.toolAdmin.events.DeleteToolSelectedEvent;
import org.iplantc.de.admin.desktop.client.toolAdmin.events.ToolSelectedEvent;
import org.iplantc.de.admin.desktop.client.toolAdmin.gin.factory.ToolAdminViewFactory;
import org.iplantc.de.admin.desktop.client.toolAdmin.model.ToolProperties;
import org.iplantc.de.admin.desktop.client.toolAdmin.service.ToolAdminServiceFacade;
import org.iplantc.de.admin.desktop.client.toolAdmin.view.dialogs.DeleteToolDialog;
import org.iplantc.de.admin.desktop.client.toolAdmin.view.dialogs.OverwriteToolDialog;
import org.iplantc.de.admin.desktop.shared.Belphegor;
import org.iplantc.de.client.models.errorHandling.ServiceErrorCode;
import org.iplantc.de.client.models.errorHandling.SimpleServiceError;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.models.tool.ToolAutoBeanFactory;
import org.iplantc.de.client.models.tool.ToolContainer;
import org.iplantc.de.client.models.tool.ToolList;
import org.iplantc.de.client.models.tool.ToolType;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.shared.DEProperties;
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

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aramsey
 */
public class ToolAdminPresenterImpl implements ToolAdminView.Presenter,
                                               AddToolSelectedEvent.AddToolSelectedEventHandler,
                                               ToolSelectedEvent.ToolSelectedEventHandler,
                                               DeleteToolSelectedEvent.DeleteToolSelectedEventHandler {

    private final ToolAdminView view;
    private final ToolAdminServiceFacade toolAdminServiceFacade;
    private final ToolAutoBeanFactory factory;
    private final ToolAdminView.ToolAdminViewAppearance appearance;
    private final ListStore<Tool> listStore;
    EditToolView editToolView;
    @Inject IplantAnnouncer announcer;
    @Inject DEProperties deProperties;
    @Inject AsyncProviderWrapper<OverwriteToolDialog> overwriteAppDialog;
    @Inject AsyncProviderWrapper<DeleteToolDialog> deleteAppDialog;

    @Inject
    public ToolAdminPresenterImpl(final ToolAdminViewFactory viewFactory,
                                  ToolAdminServiceFacade toolAdminServiceFacade,
                                  ToolAutoBeanFactory factory,
                                  ToolProperties toolProperties,
                                  ToolAdminView.ToolAdminViewAppearance appearance,
                                  EditToolViewFactory editToolViewFactory) {
        this.listStore = createListStore(toolProperties);
        this.view = viewFactory.create(listStore);
        view.addAddToolSelectedEventHandler(this);
        view.addToolSelectedEventHandler(this);
        view.addDeleteToolSelectedEventHandler(this);
        this.editToolView = editToolViewFactory.create(getBaseProps());
        this.factory = factory;
        this.appearance = appearance;
        this.toolAdminServiceFacade = toolAdminServiceFacade;
    }

    @Override
    public void go(HasOneWidget container) {
        toolAdminServiceFacade.getToolTypes(new AsyncCallback<List<ToolType>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(List<ToolType> result) {
                List<String> toolTypes = Lists.newArrayList();
                result.forEach(toolType -> toolTypes.add(toolType.getName()));
                editToolView.setToolTypes(toolTypes.toArray(new String[0]));
            }
        });
        container.setWidget(view);
        updateView();

    }

    @Override
    public void setViewDebugId(String baseId) {
        view.asWidget().ensureDebugId(baseId + Belphegor.ToolAdminIds.VIEW);
    }

    ListStore<Tool> createListStore(final ToolProperties toolProps) {
        final ListStore<Tool> listStore = new ListStore<>(toolProps.id());
        listStore.setEnableFilters(true);
        return listStore;
    }

    @Override
    public void onAddToolSelected(AddToolSelectedEvent event) {
        editToolView.edit(null);
    }

    @Override
    @SuppressWarnings("unusable-by-js")
    public void addTool(Splittable toolSpl) {
        //The UI handles creating a single tool request, but the admin/tools POST endpoint requires
        // an array of requests.  Wrapping the request inside an array.
        Tool tool = convertSplittableToTool(toolSpl);
        checkForViceTool(tool);
        ToolList toolList = factory.getToolList().as();
        List<Tool> listTool = new ArrayList<>();
        listTool.add(tool);
        toolList.setToolList(listTool);
        editToolView.mask();

        toolAdminServiceFacade.addTool(toolList, new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                editToolView.unmask();
                ErrorHandler.postReact(caught);
            }

            @Override
            public void onSuccess(Void result) {
                editToolView.close();
                announcer.schedule(new SuccessAnnouncementConfig(appearance.addToolSuccessText()));
                updateView();
            }
        });
    }

    @Override
    public void closeEditToolDlg() {
        editToolView.close();
    }

    @Override
    public void onDeleteToolSelected(final DeleteToolSelectedEvent event) {
        toolAdminServiceFacade.deleteTool(event.getTool().getId(), new AsyncCallback<Void>() {
            @Override
            public void onFailure(final Throwable caught) {
                String serviceError = getServiceError(caught);
                if (serviceError.equals(ServiceErrorCode.ERR_NOT_WRITEABLE.toString())) {
                    deleteAppDialog.get(new AsyncCallback<DeleteToolDialog>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            ErrorHandler.post(caught);
                        }

                        @Override
                        public void onSuccess(DeleteToolDialog result) {
                            result.setText(caught);
                            result.show();
                        }
                    });
                } else {
                    ErrorHandler.post(caught);
                }
            }

            @Override
            public void onSuccess(Void result) {
                announcer.schedule(new SuccessAnnouncementConfig(appearance.deleteToolSuccessText()));
                listStore.remove(listStore.findModelWithKey(event.getTool().getId()));
            }
        });
    }

    @Override
    public void onToolSelected(ToolSelectedEvent event) {
        view.mask(appearance.loadingMask());
        toolAdminServiceFacade.getToolDetails(event.getTool().getId(), new AsyncCallback<Tool>() {
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
    public void updateTool(Splittable toolSpl) {
        Tool tool = convertSplittableToTool(toolSpl);
        updateTool(tool, false);
    }

    void updateTool(final Tool tool, final boolean overwrite) {
        editToolView.mask();

        checkForViceTool(tool);
        toolAdminServiceFacade.updateTool(tool, overwrite, new AsyncCallback<Void>() {
            @Override
            public void onFailure(final Throwable caught) {
                String serviceError = getServiceError(caught);
                if (ServiceErrorCode.ERR_NOT_WRITEABLE.toString().equals(serviceError)) {
                    overwriteAppDialog.get(new AsyncCallback<OverwriteToolDialog>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            ErrorHandler.post(caught);
                        }

                        @Override
                        public void onSuccess(OverwriteToolDialog result) {
                            result.setText(caught);
                            result.show();
                            result.addOkButtonSelectHandler(new SelectEvent.SelectHandler() {
                                @Override
                                public void onSelect(SelectEvent event) {
                                    updateTool(tool, true);
                                }
                            });
                        }
                    });

                } else {
                    editToolView.unmask();
                    ErrorHandler.postReact(caught);
                }
            }

            @Override
            public void onSuccess(Void result) {
                editToolView.close();
                announcer.schedule(new SuccessAnnouncementConfig(appearance.updateToolSuccessText()));
                updateView();
            }
        });
    }

    void checkForViceTool(Tool tool) {
        if ("interactive".equals(tool.getType())) {
            if (tool.getContainer().getInteractiveApps() == null) {
                factory.appendDefaultInteractiveAppValues(tool, deProperties);
            }
            tool.setInteractive(true);
            tool.getContainer().setSkipTmpMount(true);
        } else {
            tool.getContainer().setInteractiveApps(null);
            tool.setInteractive(false);
            tool.getContainer().setSkipTmpMount(false);
        }
    }

    String getServiceError(Throwable caught) {
        try {
            SimpleServiceError simpleServiceError =
                    AutoBeanCodex.decode(factory, SimpleServiceError.class, caught.getMessage()).as();
            return simpleServiceError.getErrorCode();
        }
        catch (Exception e) {
            return null;
        }
    }

    void updateView() {
        String searchTerm = "*";
        updateView(searchTerm);
    }

    void updateView(String searchTerm) {
        toolAdminServiceFacade.getTools(searchTerm, new AsyncCallback<List<Tool>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(List<Tool> result) {
                listStore.replaceAll(result);
            }
        });
    }

    ReactToolViews.AdminEditToolProps getBaseProps() {
        ReactToolViews.AdminEditToolProps baseProps = new ReactToolViews.AdminEditToolProps();
        baseProps.presenter = this;
        baseProps.parentId = Belphegor.ToolAdminIds.TOOL_ADMIN_DIALOG;
        baseProps.isAdmin = true;
        baseProps.isAdminPublishing = false;

        return baseProps;
    }

    Tool convertSplittableToTool(Splittable toolSpl) {
        return AutoBeanCodex.decode(factory, Tool.class, toolSpl.getPayload()).as();
    }

    Splittable convertToolToSplittable(Tool tool) {
        return AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(tool));
    }
}
