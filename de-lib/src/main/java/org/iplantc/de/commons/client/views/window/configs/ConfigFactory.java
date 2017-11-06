package org.iplantc.de.commons.client.views.window.configs;

import org.iplantc.de.client.models.HasQualifiedId;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.client.models.WindowType;
import org.iplantc.de.client.models.diskResources.File;
import org.iplantc.de.client.models.notifications.NotificationCategory;
import org.iplantc.de.client.models.viewer.MimeType;
import org.iplantc.de.client.services.FileEditorServiceFacade;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

import java.util.Date;

/**
 * @author jstroot
 */
public class ConfigFactory {
    private static ConfigAutoBeanFactory factory = GWT.create(ConfigAutoBeanFactory.class);
    private static int dataWindowCount = 0;
    private static int appEditorWindowCount = 0;

    public static AboutWindowConfig aboutWindowConfig() {
        AboutWindowConfig awc = applyWindowType(WindowType.ABOUT, factory.aboutWindowConfig()).as();
        return awc;
    }

    public static AnalysisWindowConfig analysisWindowConfig() {
        AnalysisWindowConfig awc = applyWindowType(WindowType.ANALYSES, factory.analysisWindowConfig())
                .as();
        return awc;
    }

    public static CollaborationWindowConfig collaborationWindowConfig() {
        CollaborationWindowConfig collabWindowConfig = applyWindowType(WindowType.COLLABORATION, factory.collaborationWindowConfig())
                .as();
        return collabWindowConfig;
    }

    public static AppsIntegrationWindowConfig appsIntegrationWindowConfig(HasQualifiedId app) {
        AutoBean<AppsIntegrationWindowConfig> aiwc = applyWindowType(WindowType.APP_INTEGRATION,
                factory.appsIntegrationWindowConfig());
        String systemId = app == null ? "" : app.getSystemId();
        String appId = app == null ? "" : app.getId();
        aiwc.as().setSystemId(systemId);
        aiwc.as().setAppId(appId);
        String tag;
        if (Strings.isNullOrEmpty(systemId) && Strings.isNullOrEmpty(appId)) {
            tag = Integer.toString(appEditorWindowCount++);
        } else {
            tag = systemId + ":" + appId;
        }
        applyTag(tag, aiwc);
        aiwc.as().setOnlyLabelEditMode(false);
        return aiwc.as();
    }


    public static AppsWindowConfig appsWindowConfig() {
        AppsWindowConfig awc = applyWindowType(WindowType.APPS, factory.appsWindowConfig()).as();
        return awc;
    }

    public static AppWizardConfig appWizardConfig(String systemId, String appId) {
        AutoBean<AppWizardConfig> awc = applyWindowType(WindowType.APP_WIZARD, factory.appWizardConfig());
        applyTag(systemId + ":" + appId, awc);
        awc.as().setSystemId(systemId);
        awc.as().setAppId(appId);
        return awc.as();
    }

    public static AppWizardConfig appWizardConfig(String appId) {
        return appWizardConfig(null, appId);
    }

    public static AppWizardConfig appWizardConfig(HasQualifiedId qualifiedId) {
        return appWizardConfig(qualifiedId.getSystemId(), qualifiedId.getId());
    }

    public static DiskResourceWindowConfig diskResourceWindowConfig(boolean newWindowRequested) {
        AutoBean<DiskResourceWindowConfig> drwc = applyWindowType(WindowType.DATA,
                factory.diskResourceWindowConfig());

        if (newWindowRequested) {
            applyTag(Integer.toString(dataWindowCount++), drwc);
        }

        return drwc.as();
    }

    public static FileViewerWindowConfig fileViewerWindowConfig(File file) {
        AutoBean<FileViewerWindowConfig> fvwc = applyWindowType(WindowType.DATA_VIEWER,
                factory.fileViewerWindowConfig());
        fvwc.as().setFile(file);
        if (file != null) {
            applyTag(file.getId(), fvwc);
        } else {
            applyTag(
                    "Untitled-"
                            + DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_FULL).format(new Date()),
                    fvwc);
        }

        return fvwc.as();
    }

    public static TabularFileViewerWindowConfig newTabularFileViewerWindowConfig() {
        AutoBean<TabularFileViewerWindowConfig> ab = applyWindowType(WindowType.DATA_VIEWER, factory.newTabularFileViewerWindowConfig());
        applyTag("Tabular File-" + DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_FULL).format(new Date()),
                 ab);
        return ab.as();
    }

    public static HTPathListWindowConfig newHTPathListWindowConfig() {
        AutoBean<HTPathListWindowConfig> ab = applyWindowType(WindowType.DATA_VIEWER, factory.pathListWindowConfig());
        ab.as().setContentType(MimeType.PLAIN);
        ab.as().setVizTabFirst(true);
        ab.as().setSeparator(FileEditorServiceFacade.COMMA_DELIMITER);
        ab.as().setColumns(1);
        applyTag("Path List-" + DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_FULL).format(new Date()),
                 ab);
        return ab.as();
    }

    public static MultiInputPathListWindowConfig newMultiInputPathListWindowConfig() {
        AutoBean<MultiInputPathListWindowConfig> ab = applyWindowType(WindowType.DATA_VIEWER, factory.multiInputPathListWindowConfig());
        ab.as().setContentType(MimeType.PLAIN);
        ab.as().setVizTabFirst(true);
        ab.as().setSeparator(FileEditorServiceFacade.COMMA_DELIMITER);
        ab.as().setColumns(1);
        applyTag("Path List-" + DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_FULL).format(new Date()),
                 ab);
        return ab.as();
    }

    public static NotifyWindowConfig notifyWindowConfig(NotificationCategory category) {
        NotifyWindowConfig nwc = applyWindowType(WindowType.NOTIFICATIONS, factory.notifyWindowConfig())
                .as();
        nwc.setSortCategory(category);
        return nwc;
    }

    public static PipelineEditorWindowConfig workflowIntegrationWindowConfig() {
        PipelineEditorWindowConfig config = applyWindowType(WindowType.WORKFLOW_INTEGRATION,
                factory.pipelineEditorWindowConfig()).as();
        return config;
    }

    public static SimpleDownloadWindowConfig simpleDownloadWindowConfig() {
        SimpleDownloadWindowConfig sdwc = applyWindowType(WindowType.SIMPLE_DOWNLOAD,
                factory.simpleDownloadWindowConfig()).as();
        return sdwc;
    }

    public static SystemMessagesWindowConfig systemMessagesWindowConfig(final String selectedMsg) {
        final AutoBean<SystemMessagesWindowConfig> ab = applyWindowType(WindowType.SYSTEM_MESSAGES,
                factory.systemMessagesWindowConfig());
        ab.as().setSelectedMessage(selectedMsg);
        return ab.as();
    }

    public static WindowConfig getDefaultConfig(WindowType type) {
        WindowConfig config = null;
        switch (type) {
            case ABOUT:
                config = aboutWindowConfig();
                break;
            case ANALYSES:
                config = analysisWindowConfig();
                break;

            case APPS:
                config = appsWindowConfig();
                break;

            case DATA:
                config = diskResourceWindowConfig(true);
                break;

            case NOTIFICATIONS:
                config = notifyWindowConfig(NotificationCategory.ALL);
                break;

            case SIMPLE_DOWNLOAD:
                config = simpleDownloadWindowConfig();
                break;

            case SYSTEM_MESSAGES:
                config = systemMessagesWindowConfig(null);
                break;

            case COLLABORATION:
                config = collaborationWindowConfig();
                break;

            case APP_INTEGRATION:
            case APP_WIZARD:
            case DATA_VIEWER:
            case HELP:
            case WORKFLOW_INTEGRATION:
            case MANAGETOOLS:
                // Default unsupported
                break;
        }

        return config;
    }

    public static WindowConfig getConfig(WindowState ws) {
        WindowConfig config = null;
        switch (ws.getConfigType()) {
            case ABOUT:
                config = AutoBeanCodex.decode(factory, AboutWindowConfig.class, ws.getWindowConfig())
                        .as();
                break;
            case ANALYSES:
                config = AutoBeanCodex.decode(factory, AnalysisWindowConfig.class, ws.getWindowConfig())
                        .as();
                break;

            case APP_INTEGRATION:
                config = AutoBeanCodex.decode(factory, AppsIntegrationWindowConfig.class,
                        ws.getWindowConfig()).as();
                break;

            case APP_WIZARD:
                config = AutoBeanCodex.decode(factory, AppWizardConfig.class, ws.getWindowConfig()).as();
                break;

            case APPS:
                config = AutoBeanCodex.decode(factory, AppsWindowConfig.class, ws.getWindowConfig())
                        .as();
                break;

            case DATA:
                config = AutoBeanCodex.decode(factory, DiskResourceWindowConfig.class,
                        ws.getWindowConfig()).as();
                break;

            case DATA_VIEWER:
                config = AutoBeanCodex.decode(factory, FileViewerWindowConfig.class,
                        ws.getWindowConfig()).as();
                break;

            case HELP:
                config = null;
                break;

            case NOTIFICATIONS:
                config = AutoBeanCodex.decode(factory, NotifyWindowConfig.class, ws.getWindowConfig())
                        .as();
                break;

            case SIMPLE_DOWNLOAD:
                config = AutoBeanCodex.decode(factory, SimpleDownloadWindowConfig.class,
                        ws.getWindowConfig()).as();
                break;

            case WORKFLOW_INTEGRATION:
                config = AutoBeanCodex.decode(factory, PipelineEditorWindowConfig.class,
                        ws.getWindowConfig()).as();
                break;

            case SYSTEM_MESSAGES:
                config = AutoBeanCodex.decode(factory, SystemMessagesWindowConfig.class,
                        ws.getWindowConfig()).as();
                break;

            case MANAGETOOLS:
                config = AutoBeanCodex.decode(factory,
                                              ManageToolsWindowConfig.class,
                                              ws.getWindowConfig()).as();
                break;
            case COLLABORATION:
                config = AutoBeanCodex.decode(factory,
                                              CollaborationWindowConfig.class,
                                              ws.getWindowConfig()).as();
                break;
        }

        return config;
    }

    public static ManageToolsWindowConfig manageToolsWindowConfig() {
        ManageToolsWindowConfig mtwc =
                applyWindowType(WindowType.MANAGETOOLS, factory.manageToolsWindowConfig()).as();
        return mtwc;
    }

    private static <C extends WindowConfig> AutoBean<C> applyWindowType(WindowType type, AutoBean<C> wc) {
        Splittable data = StringQuoter.createSplittable();
        StringQuoter.create(type.toString()).assign(data, "windowType");
        AutoBeanCodex.decodeInto(data, wc);
        return wc;
    }

    private static <C extends WindowConfig> AutoBean<C> applyTag(String tag, AutoBean<C> wc) {
        Splittable data = StringQuoter.createSplittable();
        StringQuoter.create(tag).assign(data, "tag");
        AutoBeanCodex.decodeInto(data, wc);
        return wc;
    }

}
