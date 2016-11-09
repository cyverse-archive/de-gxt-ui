package org.iplantc.de.desktop.client.presenter;

import org.iplantc.de.client.gin.ServicesInjector;
import org.iplantc.de.client.models.CommonModelAutoBeanFactory;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.UserSession;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.desktop.client.DesktopView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.Splittable;

import java.util.List;

public class SaveSessionPeriodic implements Runnable {

    private final DesktopView.Presenter presenter;
    private final CommonModelAutoBeanFactory factory = GWT.create(CommonModelAutoBeanFactory.class);
    private IplantAnnouncer announcer;
    private final DesktopView.Presenter.DesktopPresenterAppearance appearance;
    private final int maxRetries;
    private int count;

    public SaveSessionPeriodic(DesktopView.Presenter presenter,
                               DesktopView.Presenter.DesktopPresenterAppearance appearance,
                               int maxRetries) {
        this.presenter = presenter;
        this.appearance = appearance;
        this.maxRetries = maxRetries;
        this.announcer = IplantAnnouncer.getInstance();
    }

    @Override
    public void run() {
        AutoBean<UserSession> userSession = factory.userSession();
        final List<WindowState> orderedWindowStates = presenter.getOrderedWindowStates();
        userSession.as().setWindowStates(orderedWindowStates);
        Splittable spl = AutoBeanCodex.encode(userSession);
        if (isStateChanged(orderedWindowStates, spl)) {
            GWT.log("saving periodic...");
            ServicesInjector.INSTANCE.getUserSessionServiceFacade().saveUserSession(userSession.as().getWindowStates(), new AsyncCallback<Void>() {

                @Override
                public void onSuccess(Void result) {
                    // cache the update
                    presenter.setUserSessionConnection(true);
                    count = 0;
                    UserInfo info = UserInfo.getInstance();
                    info.setSavedOrderedWindowStates(orderedWindowStates);
                }

                @Override
                public void onFailure(Throwable caught) {
                    GWT.log("Session periodic save failed");
                    count++;
                    if (count >= maxRetries) {
                        count = 0;
                        announcer.schedule(new ErrorAnnouncementConfig(appearance.saveSessionFailed(),
                                                                       true,
                                                                       5000));
                        presenter.setUserSessionConnection(false);
                        presenter.doPeriodicSessionSave();
                    }
                }
            });
        }
    }

    private boolean isStateChanged(List<WindowState> orderedWindowStates, Splittable splOws) {
        if (splOws == null || orderedWindowStates == null) {
            return false;
        }

        UserInfo info = UserInfo.getInstance();
        List<WindowState> savedStates = info.getSavedOrderedWindowStates();

        if (savedStates == null) {
            if (orderedWindowStates.size() == 0) {
                return false;
            } else {
                return true;
            }
        }

        AutoBean<UserSession> savedUserSession = factory.userSession();
        savedUserSession.as().setWindowStates(savedStates);
        Splittable savedSpl = AutoBeanCodex.encode(savedUserSession);
        return !savedSpl.getPayload().equals(splOws.getPayload());
    }
}
