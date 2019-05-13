package org.iplantc.de.commons.client.views.window.configs;

import org.iplantc.de.client.models.HasQualifiedId;
import org.iplantc.de.client.models.WindowType;
import org.iplantc.de.client.models.apps.App;
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
        AutoBean<AboutWindowConfig> awc = applyWindowType(WindowType.ABOUT, factory.aboutWindowConfig());
        applyTag("About", awc);
        return awc.as();
    }

    public static AnalysisWindowConfig analysisWindowConfig() {
        AutoBean<AnalysisWindowConfig> awc = applyWindowType(WindowType.ANALYSES, factory.analysisWindowConfig());
        applyTag("Analysis", awc);
        return awc.as();
    }

    public static CollaboratorsWindowConfig collaboratorsWindowConfig() {
        AutoBean<CollaboratorsWindowConfig> collabWindowConfig = applyWindowType(WindowType.COLLABORATORS, factory.collaboratorsWindowConfig());
        applyTag("Collab", collabWindowConfig);
        return collabWindowConfig.as();
    }

    public static TeamsWindowConfig teamsWindowConfig() {
        AutoBean<TeamsWindowConfig> teamsWindowConfig = applyWindowType(WindowType.TEAMS, factory.teamsWindowConfig());
        applyTag("Teams", teamsWindowConfig);
        return teamsWindowConfig.as();
    }

    public static CommunitiesWindowConfig communitiesWindowConfig() {
        AutoBean<CommunitiesWindowConfig> communitiesWindowConfig = applyWindowType(WindowType.COMMUNITIES, factory.communitiesWindowConfig());
        applyTag("Communities", communitiesWindowConfig);
        return communitiesWindowConfig.as();
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
        AutoBean<AppsWindowConfig> awc = applyWindowType(WindowType.APPS, factory.appsWindowConfig());
        applyTag("Apps", awc);
        return awc.as();
    }

    public static AppWizardConfig appWizardConfig(String appIntegratorEmail,
                                                  String systemId,
                                                  String appId,
                                                  String quickLaunchId) {
        AutoBean<AppWizardConfig> awc = applyWindowType(WindowType.APP_WIZARD, factory.appWizardConfig());
        applyTag(systemId + ":" + appId, awc);
        awc.as().setSystemId(systemId);
        awc.as().setAppId(appId);
        awc.as().setAppIntegratorEmail(appIntegratorEmail);
        awc.as().setQuickLaunchId(quickLaunchId);
        return awc.as();
    }

    public static AppWizardConfig appWizardConfig(String appId) {
        return appWizardConfig(null, null, appId, "");
    }

    public static AppWizardConfig appWizardConfig(App app) {
        return appWizardConfig(app.getIntegratorEmail(), app.getSystemId(), app.getId(), "");
    }

    public static AppWizardConfig appWizardConfig(String appId, String quickLaunchId) {
        return appWizardConfig(null, null, appId, quickLaunchId);
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
        AutoBean<FileViewerWindowConfig> fvwc = applyWindowType(WindowType.FILE_VIEWER,
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
        AutoBean<TabularFileViewerWindowConfig> ab = applyWindowType(WindowType.FILE_VIEWER, factory.newTabularFileViewerWindowConfig());
        applyTag("Tabular File-" + DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_FULL).format(new Date()),
                 ab);
        return ab.as();
    }

    public static HTPathListWindowConfig newHTPathListWindowConfig() {
        AutoBean<HTPathListWindowConfig> ab = applyWindowType(WindowType.FILE_VIEWER, factory.pathListWindowConfig());
        applyTag("Path List-" + DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_FULL).format(new Date()),
                 ab);
        HTPathListWindowConfig config = ab.as();
        config.setContentType(MimeType.PLAIN);
        config.setVizTabFirst(true);
        config.setSeparator(FileEditorServiceFacade.COMMA_DELIMITER);
        config.setColumns(1);
        return config;
    }

    public static MultiInputPathListWindowConfig newMultiInputPathListWindowConfig() {
        AutoBean<MultiInputPathListWindowConfig> ab = applyWindowType(WindowType.FILE_VIEWER, factory.multiInputPathListWindowConfig());
        applyTag("Path List-" + DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_FULL).format(new Date()),
                 ab);
        MultiInputPathListWindowConfig config = ab.as();
        config.setContentType(MimeType.PLAIN);
        config.setVizTabFirst(true);
        config.setSeparator(FileEditorServiceFacade.COMMA_DELIMITER);
        config.setColumns(1);
        return config;
    }

    public static NotifyWindowConfig notifyWindowConfig(NotificationCategory category) {
        AutoBean<NotifyWindowConfig> nwc = applyWindowType(WindowType.NOTIFICATIONS, factory.notifyWindowConfig());
        applyTag("Notify", nwc);
        nwc.as().setFilter(category.toString());
        return nwc.as();
    }

    public static PipelineEditorWindowConfig workflowIntegrationWindowConfig() {
        AutoBean<PipelineEditorWindowConfig> config = applyWindowType(WindowType.WORKFLOW_INTEGRATION,
                factory.pipelineEditorWindowConfig());
        applyTag("workflow-" + DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_FULL).format(new Date()),
                 config);
        return config.as();
    }

    public static SimpleDownloadWindowConfig simpleDownloadWindowConfig() {
        AutoBean<SimpleDownloadWindowConfig> sdwc = applyWindowType(WindowType.SIMPLE_DOWNLOAD,
                factory.simpleDownloadWindowConfig());
        applyTag("simpleDown-" + DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_FULL).format(new Date()),
                 sdwc);
        return sdwc.as();
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

            case COLLABORATORS:
                config = collaboratorsWindowConfig();
                break;

            case TEAMS:
                config = teamsWindowConfig();
                break;

            case COMMUNITIES:
                config = communitiesWindowConfig();
                break;

            case APP_INTEGRATION:
            case APP_WIZARD:
            case FILE_VIEWER:
            case HELP:
            case WORKFLOW_INTEGRATION:
            case MANAGETOOLS:
                // Default unsupported
                break;
        }

        return config;
    }

    public static WindowConfig getConfig(SavedWindowConfig wc) {
        WindowConfig config = null;
        switch (wc.getWindowType()) {
            case ABOUT:
                config = AutoBeanCodex.decode(factory,
                                              AboutWindowConfig.class,
                                              wc.getWindowConfig()).as();
                break;
            case ANALYSES:
                config = AutoBeanCodex.decode(factory,
                                              AnalysisWindowConfig.class,
                                              wc.getWindowConfig()).as();
                break;

            case APP_INTEGRATION:
                config = AutoBeanCodex.decode(factory, AppsIntegrationWindowConfig.class,
                                              wc.getWindowConfig()).as();
                break;

            case APP_WIZARD:
                config = AutoBeanCodex.decode(factory,
                                              AppWizardConfig.class,
                                              wc.getWindowConfig()).as();
                break;

            case APPS:
                config = AutoBeanCodex.decode(factory,
                                              AppsWindowConfig.class,
                                              wc.getWindowConfig()).as();
                break;

            case DATA:
                config = AutoBeanCodex.decode(factory, DiskResourceWindowConfig.class,
                                              wc.getWindowConfig()).as();
                break;

            case FILE_VIEWER:
                config = AutoBeanCodex.decode(factory, FileViewerWindowConfig.class,
                                              wc.getWindowConfig()).as();
                break;

            case HELP:
                config = null;
                break;

            case NOTIFICATIONS:
                config = AutoBeanCodex.decode(factory, NotifyWindowConfig.class,
                                              wc.getWindowConfig()).as();
                break;

            case SIMPLE_DOWNLOAD:
                config = AutoBeanCodex.decode(factory, SimpleDownloadWindowConfig.class,
                                              wc.getWindowConfig()).as();
                break;

            case WORKFLOW_INTEGRATION:
                config = AutoBeanCodex.decode(factory, PipelineEditorWindowConfig.class,
                                              wc.getWindowConfig()).as();
                break;

            case MANAGETOOLS:
                config = AutoBeanCodex.decode(factory,
                                              ManageToolsWindowConfig.class,
                                              wc.getWindowConfig()).as();
                break;
            case COLLABORATORS:
                config = AutoBeanCodex.decode(factory,
                                              CollaboratorsWindowConfig.class,
                                              wc.getWindowConfig()).as();
                break;

            case TEAMS:
                config = AutoBeanCodex.decode(factory,
                                              TeamsWindowConfig.class,
                                              wc.getWindowConfig()).as();
                break;

            case COMMUNITIES:
                config = AutoBeanCodex.decode(factory,
                                              CommunitiesWindowConfig.class,
                                              wc.getWindowConfig()).as();
                break;
        }

        return config;
    }

    public static ManageToolsWindowConfig manageToolsWindowConfig() {
        AutoBean<ManageToolsWindowConfig> mtwc =
                applyWindowType(WindowType.MANAGETOOLS, factory.manageToolsWindowConfig());
        applyTag("manageTools",
                 mtwc);
        return mtwc.as();
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
