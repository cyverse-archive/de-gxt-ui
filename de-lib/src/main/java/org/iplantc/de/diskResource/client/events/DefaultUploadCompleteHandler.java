package org.iplantc.de.diskResource.client.events;

import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.diskResources.File;
import org.iplantc.de.client.models.notifications.Notification;
import org.iplantc.de.client.models.notifications.NotificationCategory;
import org.iplantc.de.client.models.notifications.payload.PayloadData;
import org.iplantc.de.client.services.UserSessionServiceFacade;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.diskResource.client.DiskResourceView;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 * General handler for file upload. Expects to be called after form submission is complete.
 *
 * @author lenards, jstroot
 */
public class DefaultUploadCompleteHandler extends UploadCompleteHandler {

    interface DefaultUploadAutoBeanFactory extends AutoBeanFactory {
        AutoBean<File> file();

        AutoBean<PayloadData> payloadData();

        AutoBean<Notification> notification();
    }

    private final UserInfo userInfo;
    private final UserSessionServiceFacade userSessionService;
    private final DefaultUploadAutoBeanFactory factory = GWT.create(DefaultUploadAutoBeanFactory.class);
    private IplantAnnouncer announcer = IplantAnnouncer.getInstance();
    DiskResourceView.Presenter.Appearance appearance = GWT.create(DiskResourceView.Presenter.Appearance.class);


    /**
     * Construct a new instance of the default handler.
     *
     * @param idParent the parent identifier to upload the file
     */
    public DefaultUploadCompleteHandler(final UserSessionServiceFacade userSessionService,
                                        String idParent) {
        super(idParent,
              GWT.<DiskResourceView.Presenter.Appearance> create(DiskResourceView.Presenter.Appearance.class));
        this.userSessionService = userSessionService;
        userInfo = UserInfo.getInstance();
    }

    /**
     * Invoked immediately following onCompletion().
     * <p/>
     * Provides a manner for handlers to do cleanup or post-completion operations.
     */
    @Override
    public void onAfterCompletion() {
        // Let the specific instance provide an implementation. This is not abstract
        // because then the class would to be abstract and there might be a case
        // when you want to use this without defining this action.
    }

    /**
     * Invoked on completion of file upload form submission.
     *
     * @param sourceUrl the URL the file is being imported from, or the filename if uploading a local
     *                  file
     * @param response  the server response in JSON format
     */
    @Override
    public void onCompletion(String sourceUrl, String response) {
        File file = AutoBeanCodex.decode(factory, File.class, response).as();
        if (file == null) {
            announcer.schedule(new ErrorAnnouncementConfig(appearance.fileUploadsFailed(Lists.newArrayList(sourceUrl)) + ": " + response));
            return;
        }

        Notification notification = createNotification(file, sourceUrl);

        userSessionService.postClientNotification(notification, new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                // do nothing intentionally

            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }
        });
    }

    Notification createNotification(File file, String sourceUrl) {
        Notification notification = factory.notification().as();
        PayloadData payload = factory.payloadData().as();
        payload.setAction(PayloadData.ACTION_UPLOAD_COMPLETE);
        // since our current file info objects don't have a parent folder id, we have to add it in.
        // This is further complicated by the fact the messages we receive will have this field.
        // The following code is to transform our file info retrieved from upload completion into the
        // form
        // we expect in the message.
        file.setParentFolderId(getParentId());
        file.setSourceUrl(sourceUrl);
        payload.setData(file);

        notification.setCategory(NotificationCategory.DATA.toString());
        notification.setSubject(buildMessageText(file));
        Splittable payloadSplittable = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(payload));

        notification.setNotificationPayload(payloadSplittable);
        notification.setUser(userInfo.getUsername());
        notification.setEmail(false);
        return notification;
    }

    private String buildMessageText(File file) {
        String filename = file.getName();

        if (!filename.isEmpty()) {
            return appearance.fileUploadSuccess(filename);
        }

        String sourceUrl = file.getSourceUrl();

        return appearance.importFailed(sourceUrl);
    }
}
