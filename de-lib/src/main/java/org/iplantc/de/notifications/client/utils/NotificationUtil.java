package org.iplantc.de.notifications.client.utils;

import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.HasPath;
import org.iplantc.de.client.models.QualifiedId;
import org.iplantc.de.client.models.analysis.AnalysesAutoBeanFactory;
import org.iplantc.de.client.models.analysis.Analysis;
import org.iplantc.de.client.models.diskResources.DiskResourceAutoBeanFactory;
import org.iplantc.de.client.models.diskResources.File;
import org.iplantc.de.client.models.notifications.Notification;
import org.iplantc.de.client.models.notifications.NotificationAutoBeanFactory;
import org.iplantc.de.client.models.notifications.NotificationCategory;
import org.iplantc.de.client.models.notifications.NotificationMessage;
import org.iplantc.de.client.models.notifications.payload.PayloadAnalysis;
import org.iplantc.de.client.models.notifications.payload.PayloadApps;
import org.iplantc.de.client.models.notifications.payload.PayloadAppsList;
import org.iplantc.de.client.models.notifications.payload.PayloadData;
import org.iplantc.de.client.models.notifications.payload.PayloadRequest;
import org.iplantc.de.client.models.notifications.payload.PayloadTeam;
import org.iplantc.de.client.models.requestStatus.RequestHistory;
import org.iplantc.de.client.util.CommonModelUtils;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.collaborators.client.CollaborationView;
import org.iplantc.de.commons.client.views.window.configs.AnalysisWindowConfig;
import org.iplantc.de.commons.client.views.window.configs.AppsWindowConfig;
import org.iplantc.de.commons.client.views.window.configs.CollaborationWindowConfig;
import org.iplantc.de.commons.client.views.window.configs.ConfigFactory;
import org.iplantc.de.commons.client.views.window.configs.DiskResourceWindowConfig;
import org.iplantc.de.notifications.client.events.NotificationClickedEvent;
import org.iplantc.de.notifications.client.events.WindowShowRequestEvent;
import org.iplantc.de.notifications.client.views.dialogs.DenyJoinRequestDetailsDialog;
import org.iplantc.de.notifications.client.views.dialogs.JoinTeamRequestDialog;
import org.iplantc.de.notifications.client.views.dialogs.RequestHistoryDialog;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sriram on 6/16/16.
 */
public class NotificationUtil {

    @Inject NotificationAutoBeanFactory notificationFactory;
    @Inject DiskResourceAutoBeanFactory drFactory;
    @Inject AnalysesAutoBeanFactory analysesFactory;
    @Inject DiskResourceUtil diskResourceUtil;
    @Inject EventBus eventBus;
    @Inject AsyncProviderWrapper<JoinTeamRequestDialog> joinRequestDlgProvider;
    @Inject AsyncProviderWrapper<DenyJoinRequestDetailsDialog> denyDetailsDlgProvider;

    @Inject
    public NotificationUtil() {
    }

    /**
     * A utility method for preparing NotificationMessages to be viewed/handled properly in
     * the NotificationView
     * @param notification
     * @param notFactory
     * @return
     */
    public NotificationMessage getMessage(Notification notification,
                                          final NotificationAutoBeanFactory notFactory) {

        NotificationMessage msg = notification.getMessage();
        msg.setSeen(notification.isSeen());
        msg.setCategory(NotificationCategory.fromTypeString(notification.getCategory()));
        Splittable payload = notification.getNotificationPayload();

        if (payload == null) {
            return msg;
        }

        switch (msg.getCategory()) {
            case ALL:
                GWT.log("ALL Analysis category");
                break;
            case APPS:
                PayloadAppsList appPList =
                        AutoBeanCodex.decode(notFactory, PayloadAppsList.class, payload).as();
                if ("share".equalsIgnoreCase(appPList.getAction())) {
                    msg.setContext(payload.getPayload());
                } else {
                    GWT.log("Unhandled apps action type!!");
                }
                break;
            case ANALYSIS:
                PayloadAnalysis analysisPayload =
                        AutoBeanCodex.decode(notFactory, PayloadAnalysis.class, payload).as();
                String analysisAction = analysisPayload.getAction();

                if ("job_status_change".equals(analysisAction) || "share".equals(analysisAction)) {
                    msg.setContext(payload.getPayload());
                } else {
                    GWT.log("Unhandled Analysis action type!!");
                }
                break;

            case DATA:
                PayloadData dataPayload =
                        AutoBeanCodex.decode(notFactory, PayloadData.class, payload).as();
                String dataAction = dataPayload.getAction();

                if ("file_uploaded".equals(dataAction)) {
                    AutoBean<File> fileAb = AutoBeanUtils.getAutoBean(dataPayload.getData());
                    msg.setContext(AutoBeanCodex.encode(fileAb).getPayload());
                } else if ("share".equals(dataAction) || "unshare".equals(dataAction)) {
                    List<String> paths = dataPayload.getPaths();
                    if (paths != null && !paths.isEmpty()) {
                        String path = paths.get(0);
                        Splittable file =
                                CommonModelUtils.getInstance().createHasPathSplittableFromString(path);
                        msg.setContext(file.getPayload());
                    }
                }
                break;

            case NEW:
                GWT.log("NEW  category");
                break;

            case SYSTEM:
                GWT.log("SYSTEM  category");
                break;

            case PERMANENTIDREQUEST:
                GWT.log("Permanent Id request category");
                msg.setContext(payload.getPayload());
                break;
            case TOOLREQUEST:
                GWT.log("TOOLREQUEST  category");
                msg.setContext(payload.getPayload());
                break;
            case TOOLS:
                GWT.log("TOOL category");
                msg.setContext(payload.getPayload());
                break;
            case TEAM:
                GWT.log("TEAM category");
                PayloadTeam payloadTeam = AutoBeanCodex.decode(notFactory, PayloadTeam.class, payload).as();
                String action = notification.getEmailTemplate();
                payloadTeam.setAction(action);
                if (action.equals(PayloadTeam.ACTION_ADD)) {
                    msg.setMessage(msg.getMessage() + " " + payloadTeam.getTeamName());
                }
                msg.setContext(payload.getPayload());
                break;

            default:
                break;
        }

        return msg;
    }

    /**
     * A utility method for handling what should happen when a notification is clicked
     * @param message
     */
    public void onNotificationClick(NotificationMessage message) {

        if (message.getContext() != null
            && message.getCategory() != null) {
            NotificationCategory category = message.getCategory();
            String context1 = message.getContext();

            //mark this message as seen
            eventBus.fireEvent(new NotificationClickedEvent(message));
            message.setSeen(true);
            switch (category) {

                case APPS:
                    final AppsWindowConfig appsConfig = ConfigFactory.appsWindowConfig();
                    final PayloadAppsList pal = AutoBeanCodex.decode(notificationFactory,
                                                                     PayloadAppsList.class,
                                                                     context1).as();
                    if (pal != null && pal.getApps() != null && pal.getApps().size() > 0) {
                        PayloadApps payload = pal.getApps().get(0);
                        final String systemId = payload.getSystemId();
                        final String appCategoryId = payload.getCategoryId();
                        final String appId = payload.getId();
                        appsConfig.setSelectedAppCategory(new QualifiedId(systemId, appCategoryId));
                        appsConfig.setSelectedApp(new QualifiedId(systemId, appId));
                        eventBus.fireEvent(new WindowShowRequestEvent(appsConfig, true));
                    }

                    break;
                case DATA:
                    // execute data context
                    File file = AutoBeanCodex.decode(drFactory, File.class, context1).as();
                    ArrayList<HasId> selectedResources = Lists.newArrayList();
                    selectedResources.add(file);

                    DiskResourceWindowConfig dataWindowConfig =
                            ConfigFactory.diskResourceWindowConfig(false);
                    HasPath folder = diskResourceUtil.getFolderPathFromFile(file);
                    dataWindowConfig.setSelectedFolder(folder);
                    dataWindowConfig.setSelectedDiskResources(selectedResources);
                    eventBus.fireEvent(new WindowShowRequestEvent(dataWindowConfig, true));

                    break;

                case ANALYSIS:
                    AutoBean<Analysis> hAb =
                            AutoBeanCodex.decode(analysesFactory, Analysis.class, context1);

                    AnalysisWindowConfig analysisWindowConfig = ConfigFactory.analysisWindowConfig();
                    analysisWindowConfig.setSelectedAnalyses(Lists.newArrayList(hAb.as()));
                    eventBus.fireEvent(new WindowShowRequestEvent(analysisWindowConfig, true));

                    break;
                case PERMANENTIDREQUEST:
                    // fall through to ToolRequest logic
                case TOOLREQUEST:
                    PayloadRequest toolRequest =
                            AutoBeanCodex.decode(notificationFactory, PayloadRequest.class, context1)
                                         .as();

                    List<RequestHistory> history =
                            toolRequest.getHistory();

                    RequestHistoryDialog dlg =
                            new RequestHistoryDialog(toolRequest.getName(), history);
                    dlg.show();

                    break;
                case TEAM:
                    PayloadTeam payloadTeam = AutoBeanCodex.decode(notificationFactory, PayloadTeam.class, context1).as();

                    if (payloadTeam.getAction().equals(PayloadTeam.ACTION_JOIN)) {
                        joinRequestDlgProvider.get(new AsyncCallback<JoinTeamRequestDialog>() {
                            @Override
                            public void onFailure(Throwable caught) {
                            }

                            @Override
                            public void onSuccess(JoinTeamRequestDialog dialog) {
                                dialog.show(message, payloadTeam);
                            }
                        });
                    } else if (payloadTeam.getAction().equals(PayloadTeam.ACTION_DENY)) {
                        denyDetailsDlgProvider.get(new AsyncCallback<DenyJoinRequestDetailsDialog>() {
                            @Override
                            public void onFailure(Throwable throwable) { }

                            @Override
                            public void onSuccess(DenyJoinRequestDetailsDialog dialog) {
                                dialog.show(payloadTeam.getTeamName(), payloadTeam.getAdminMessage());
                            }
                        });
                    } else {
                        CollaborationWindowConfig window = ConfigFactory.collaborationWindowConfig();
                        window.setSelectedTab(CollaborationView.TAB.Teams);
                        eventBus.fireEvent(new WindowShowRequestEvent(window, true));
                    }
                    break;

                default:
                    break;
            }
        }
    }
}
