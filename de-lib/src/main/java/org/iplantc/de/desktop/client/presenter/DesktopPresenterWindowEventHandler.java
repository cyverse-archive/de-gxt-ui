package org.iplantc.de.desktop.client.presenter;

import static com.google.common.base.Preconditions.checkNotNull;

import org.iplantc.de.analysis.client.events.OpenAppForRelaunchEvent;
import org.iplantc.de.apps.client.events.CreateNewAppEvent;
import org.iplantc.de.apps.client.events.CreateNewWorkflowEvent;
import org.iplantc.de.apps.client.events.EditAppEvent;
import org.iplantc.de.apps.client.events.EditWorkflowEvent;
import org.iplantc.de.apps.client.events.QuickLaunchEvent;
import org.iplantc.de.apps.client.events.RequestCreateQuickLaunchEvent;
import org.iplantc.de.apps.client.events.RunAppEvent;
import org.iplantc.de.client.DEClientConstants;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.events.diskResources.OpenFolderEvent;
import org.iplantc.de.client.models.HasPath;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.apps.integration.AppTemplate;
import org.iplantc.de.client.models.apps.integration.AppTemplateAutoBeanFactory;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.File;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.models.diskResources.TYPE;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.util.CommonModelUtils;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.util.WindowUtil;
import org.iplantc.de.commons.client.views.dialogs.SimpleFileUploadDialog;
import org.iplantc.de.commons.client.views.window.configs.AppWizardConfig;
import org.iplantc.de.commons.client.views.window.configs.AppsIntegrationWindowConfig;
import org.iplantc.de.commons.client.views.window.configs.ConfigFactory;
import org.iplantc.de.commons.client.views.window.configs.DiskResourceWindowConfig;
import org.iplantc.de.commons.client.views.window.configs.FileViewerWindowConfig;
import org.iplantc.de.commons.client.views.window.configs.PipelineEditorWindowConfig;
import org.iplantc.de.commons.client.views.window.configs.SimpleDownloadWindowConfig;
import org.iplantc.de.desktop.client.DesktopView;
import org.iplantc.de.diskResource.client.events.CreateNewFileEvent;
import org.iplantc.de.diskResource.client.events.RequestSendToCoGeEvent;
import org.iplantc.de.diskResource.client.events.RequestSendToEnsemblEvent;
import org.iplantc.de.diskResource.client.events.RequestSimpleDownloadEvent;
import org.iplantc.de.diskResource.client.events.RequestSimpleUploadEvent;
import org.iplantc.de.diskResource.client.events.ShowFilePreviewEvent;
import org.iplantc.de.fileViewers.client.callbacks.GenomeBrowserUtil;
import org.iplantc.de.notifications.client.events.WindowShowRequestEvent;
import org.iplantc.de.tools.client.events.UseToolInNewAppEvent;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.widget.core.client.box.AlertMessageBox;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Event handling for all DE managed windows. Event handlers are not registered until after the
 * presenter is set.
 *
 * @author jstroot
 */
public class DesktopPresenterWindowEventHandler implements EditAppEvent.EditAppEventHandler,
                                                           CreateNewAppEvent.CreateNewAppEventHandler,
                                                           UseToolInNewAppEvent.UseToolInNewAppEventHandler,
                                                           CreateNewWorkflowEvent.CreateNewWorkflowEventHandler,
                                                           EditWorkflowEvent.EditWorkflowEventHandler,
                                                           ShowFilePreviewEvent.ShowFilePreviewEventHandler,
                                                           CreateNewFileEvent.CreateNewFileEventHandler,
                                                           WindowShowRequestEvent.WindowShowRequestEventHandler,
                                                           RunAppEvent.RunAppEventHandler,
                                                           OpenFolderEvent.OpenFolderEventHandler,
                                                           OpenAppForRelaunchEvent.OpenAppForRelaunchEventHandler,
                                                           RequestSendToCoGeEvent.RequestSendToCoGeEventHandler,
                                                           RequestSendToEnsemblEvent.RequestSendToEnsemblEventHandler,
                                                           RequestSimpleDownloadEvent.RequestSimpleDownloadEventHandler,
                                                           RequestSimpleUploadEvent.RequestSimpleUploadEventHandler,
                                                           QuickLaunchEvent.QuickLaunchEventHandler,
                                                           RequestCreateQuickLaunchEvent.RequestCreateQuickLaunchEventHandler {

    @Inject DEClientConstants clientConstants;
    @Inject Provider<DiskResourceServiceFacade> diskResourceServiceProvider;
    @Inject EventBus eventBus;
    @Inject UserInfo userInfo;
    @Inject IplantAnnouncer announcer;
    @Inject DiskResourceUtil diskResourceUtil;
    @Inject DiskResourceServiceFacade diskResourceServiceFacade;
    @Inject DesktopView.Presenter.DesktopPresenterAppearance appearance;
    @Inject
    AppTemplateAutoBeanFactory templateAutoBeanFactory;

    @Inject
    GenomeBrowserUtil genomeBrowserUtil;

    private DesktopWindowManager desktopWindowManager;
    private DesktopPresenterImpl presenter;

    @Inject
    public DesktopPresenterWindowEventHandler() {

    }

    @Override
    public void createNewApp(CreateNewAppEvent event) {
        presenter.show(ConfigFactory.appsIntegrationWindowConfig(null), true);
    }

    @Override
    public void createNewWorkflow() {
        presenter.show(ConfigFactory.workflowIntegrationWindowConfig());
    }

    @Override
    public void onCreateNewFile(CreateNewFileEvent event) {
        if (event.geFileViewerWindowConfig() != null) {
            presenter.show(event.geFileViewerWindowConfig());
        } else {
            FileViewerWindowConfig fileViewerWindowConfig = ConfigFactory.fileViewerWindowConfig(null);
            fileViewerWindowConfig.setEditing(true);
            fileViewerWindowConfig.setParentFolder(event.getParentFolder());
            presenter.show(fileViewerWindowConfig);
        }
    }

    @Override
    public void onEditApp(EditAppEvent event) {
        AppsIntegrationWindowConfig config = ConfigFactory.appsIntegrationWindowConfig(event.getAppToEdit());
        config.setOnlyLabelEditMode(event.isUserIntegratorAndAppPublic());
        if (desktopWindowManager.isOpen(config.getWindowType())) {
            announcer.schedule(new ErrorAnnouncementConfig(appearance.cannotEditTwoApps()));
        } else {
            presenter.show(config, false);
        }
    }

    @Override
    public void onEditWorkflow(EditWorkflowEvent event) {
        PipelineEditorWindowConfig config = ConfigFactory.workflowIntegrationWindowConfig();
        Splittable serviceWorkflowJson = event.getServiceWorkflowJson();
        config.setServiceWorkflowJson(serviceWorkflowJson);
        presenter.show(config);
    }

    @Override
    public void onRequestOpenAppForRelaunch(OpenAppForRelaunchEvent event) {
        final String systemId = event.getSystemId();
        final String appId = event.getAppId();
        AppWizardConfig config = ConfigFactory.appWizardConfig("", systemId, appId, "");
        config.setAnalysisId(event.getAnalysisId());
        config.setRelaunchAnalysis(true);

        presenter.show(config);
    }

    @Override
    public void onRequestOpenFolder(OpenFolderEvent event) {
        final HasPath path = CommonModelUtils.getInstance().createHasPathFromString(event.getFolderPath());
        DiskResourceWindowConfig config = ConfigFactory.diskResourceWindowConfig(event.newViewRequested());
        config.setSelectedFolder(path);

        presenter.show(config, !event.newViewRequested());
    }

    @Override
    public void onRequestSendToCoGe(RequestSendToCoGeEvent event) {
        checkNotNull(event.getFile());
        showFile(event.getFile());
        presenter.doViewGenomes(event.getFile());
    }

    @Override
    public void onRequestSendToEnsembl(RequestSendToEnsemblEvent event) {
        final List<DiskResource> resourcesToSend = event.getResourcesToSend();

        checkNotNull(resourcesToSend);
        if (resourcesToSend.size() == 1) {
            showFile((File)resourcesToSend.iterator().next());
        }

        genomeBrowserUtil.sendToGenomeBrowser(resourcesToSend, null);
    }

    @Override
    public void onRequestSimpleDownload(RequestSimpleDownloadEvent event) {
        List<DiskResource> resources = Lists.newArrayList(event.getRequestedResources());
        if (isDownloadable(resources)) {
            if (resources.size() == 1) {
                // Download now. No folders possible here....
                final String encodedSimpleDownloadURL =
                    diskResourceServiceProvider.get().getEncodedSimpleDownloadURL(resources.get(0).getPath());

                WindowUtil.open(encodedSimpleDownloadURL, "width=100,height=100");
            } else {
                SimpleDownloadWindowConfig sdwc = ConfigFactory.simpleDownloadWindowConfig();
                sdwc.setResourcesToDownload(filterFolders(resources));
                presenter.show(sdwc);
            }
        } else {
            showErrorMsg();
        }
    }

    @Override
    public void onRequestSimpleUpload(RequestSimpleUploadEvent event) {
        Folder uploadDest = event.getDestinationFolder();
        if (canUpload(uploadDest)) {
            SimpleFileUploadDialog dialog = new SimpleFileUploadDialog(uploadDest,
                                                                        diskResourceServiceProvider.get(),
                                                                        eventBus,
                                                                        diskResourceUtil,
                                                                        UriUtils.fromTrustedString(
                                                                                clientConstants.fileUploadServlet()),
                                                                        userInfo.getUsername());
            dialog.show();
        }
    }

    @Override
    public void onRunAppActionInitiated(RunAppEvent event) {
        AppWizardConfig config = ConfigFactory.appWizardConfig(event.getAppToRun());
        presenter.show(config);
    }

    @Override
    public void onWindowShowRequest(WindowShowRequestEvent event) {
        presenter.show(event.getWindowConfig(), event.updateWithConfig());
    }

    public void setPresenter(final DesktopPresenterImpl presenter,
                             final DesktopWindowManager desktopWindowManager) {
        this.presenter = presenter;
        this.desktopWindowManager = desktopWindowManager;
        init(eventBus);
    }

    @Override
    public void showFilePreview(ShowFilePreviewEvent event) {
        FileViewerWindowConfig fileViewerWindowConfig;
        if (event.getConfig() == null) {
            fileViewerWindowConfig = ConfigFactory.fileViewerWindowConfig(event.getFile());
        } else {
            fileViewerWindowConfig = event.getConfig();
        }
        fileViewerWindowConfig.setEditing(diskResourceUtil.isWritable(event.getFile()));
        presenter.show(fileViewerWindowConfig);
    }

    Map<String, String> buildTypeMap(List<DiskResource> resources) {
        Map<String, String> map = Maps.newHashMap();
        for (DiskResource dr : resources) {
            map.put(dr.getId(), dr instanceof Folder ? TYPE.FOLDER.toString() : TYPE.FILE.toString());
        }

        return map;
    }

    boolean canUpload(Folder uploadDest) {
        if (uploadDest != null && diskResourceUtil.canUploadTo(uploadDest)) {
            return true;
        } else {
            showErrorMsg();
            return false;
        }
    }

    // remove folders from list to be displayed for simple download
    List<DiskResource> filterFolders(List<DiskResource> listToFilter) {
        List<DiskResource> filteredList = Lists.newArrayList();
        for (DiskResource dr : listToFilter) {
            if (!(dr instanceof Folder)) {
                filteredList.add(dr);
            }
        }
        return filteredList;
    }

    boolean isDownloadable(List<DiskResource> resources) {
        if ((resources == null) || resources.isEmpty()) {
            return false;
        }

        for (DiskResource dr : resources) {
            if (!diskResourceUtil.isReadable(dr)) {
                return false;
            }
        }
        return true;
    }

    void showErrorMsg() {
        new AlertMessageBox(appearance.permissionErrorTitle(),
                            appearance.permissionErrorMessage()).show();
    }

    void showFile(final File file) {
        FileViewerWindowConfig fileViewerConfig = ConfigFactory.fileViewerWindowConfig(file);
        fileViewerConfig.setVizTabFirst(true);
        fileViewerConfig.setEditing(diskResourceUtil.isWritable(file));
        presenter.show(fileViewerConfig);
    }

    private void init(EventBus eventBus) {
        List<HandlerRegistration> registrations = Lists.newArrayList();
        HandlerRegistration handlerRegistration = eventBus.addHandler(EditAppEvent.TYPE, this);
        registrations.add(handlerRegistration);

        handlerRegistration = eventBus.addHandler(CreateNewAppEvent.TYPE, this);
        registrations.add(handlerRegistration);
        handlerRegistration = eventBus.addHandler(CreateNewWorkflowEvent.TYPE, this);
        registrations.add(handlerRegistration);
        handlerRegistration = eventBus.addHandler(EditWorkflowEvent.TYPE, this);
        registrations.add(handlerRegistration);
        handlerRegistration = eventBus.addHandler(ShowFilePreviewEvent.TYPE, this);
        registrations.add(handlerRegistration);
        handlerRegistration = eventBus.addHandler(CreateNewFileEvent.TYPE, this);
        registrations.add(handlerRegistration);
        handlerRegistration = eventBus.addHandler(WindowShowRequestEvent.TYPE, this);
        registrations.add(handlerRegistration);
        handlerRegistration = eventBus.addHandler(RunAppEvent.TYPE, this);
        registrations.add(handlerRegistration);
        handlerRegistration = eventBus.addHandler(OpenAppForRelaunchEvent.TYPE, this);
        registrations.add(handlerRegistration);
        handlerRegistration = eventBus.addHandler(OpenFolderEvent.TYPE, this);
        registrations.add(handlerRegistration);
        handlerRegistration = eventBus.addHandler(RequestSendToCoGeEvent.TYPE, this);
        registrations.add(handlerRegistration);
        handlerRegistration = eventBus.addHandler(RequestSendToEnsemblEvent.TYPE, this);
        registrations.add(handlerRegistration);
        handlerRegistration = eventBus.addHandler(UseToolInNewAppEvent.TYPE, this);
        registrations.add(handlerRegistration);
        handlerRegistration = eventBus.addHandler(RequestSimpleDownloadEvent.TYPE, this);
        registrations.add(handlerRegistration);
        handlerRegistration = eventBus.addHandler(RequestSimpleUploadEvent.TYPE, this);
        registrations.add(handlerRegistration);
        handlerRegistration = eventBus.addHandler(QuickLaunchEvent.TYPE, this);
        registrations.add(handlerRegistration);
        handlerRegistration = eventBus.addHandler(RequestCreateQuickLaunchEvent.TYPE, this);
        registrations.add(handlerRegistration);

    }

    @Override
    public void useToolInNewApp(UseToolInNewAppEvent event) {
        AppsIntegrationWindowConfig aiwc = ConfigFactory.appsIntegrationWindowConfig(null);
        AppTemplate newAppTemplate = templateAutoBeanFactory.defaultAppTemplate().as();
        newAppTemplate.setName(appearance.newApp());
        newAppTemplate.getArgumentGroups().get(0).setLabel(appearance.sectionOne());
        newAppTemplate.setTools(Arrays.asList(event.getTool()));
        aiwc.setAppTemplate(AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(newAppTemplate)));

        presenter.show(aiwc, true);
    }

    @Override
    public void onQuickLaunch(QuickLaunchEvent event) {
        AppWizardConfig config =
                ConfigFactory.appWizardConfig("", "", event.getAppId(), event.getQuickLaunchId());
        presenter.show(config);
    }

    @Override
    public void onRequestCreateQuickLaunch(RequestCreateQuickLaunchEvent event) {
        AppWizardConfig config =
                ConfigFactory.appWizardConfig(event.getAppId());
        presenter.show(config);
    }
}
