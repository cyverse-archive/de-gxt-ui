package org.iplantc.de.client.services.impl;

import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.GET;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.POST;

import org.iplantc.de.client.models.CommonModelAutoBeanFactory;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.UserSession;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.client.models.notifications.Notification;
import org.iplantc.de.client.models.userSettings.UserSetting;
import org.iplantc.de.client.models.userSettings.UserSettingAutoBeanFactory;
import org.iplantc.de.client.services.UserSessionServiceFacade;
import org.iplantc.de.client.services.converters.AsyncCallbackConverter;
import org.iplantc.de.client.services.converters.StringToVoidCallbackConverter;
import org.iplantc.de.shared.DEProperties;
import org.iplantc.de.shared.services.DiscEnvApiService;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.gwt.http.client.Request;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import java.util.List;

/**
 * A service facade to save and retrieve user session
 * 
 * @author sriram
 * 
 */
public class UserSessionServiceFacadeImpl implements UserSessionServiceFacade {

    private final String BOOTSTRAP = "org.iplantc.services.bootstrap";
    private final String LOGOUT = "org.iplantc.services.logout";
    private final DEProperties deProperties;
    private final UserInfo userInfo;
    private final CommonModelAutoBeanFactory factory;
    private final DiscEnvApiService deServiceFacade;
    private final UserSettingAutoBeanFactory settingFactory;

    @Inject
    public UserSessionServiceFacadeImpl(final DiscEnvApiService deServiceFacade,
                                        final DEProperties deProperties,
                                        final UserInfo userInfo,
                                        final CommonModelAutoBeanFactory factory,
                                        final UserSettingAutoBeanFactory settingFactory) {
        this.deServiceFacade = deServiceFacade;
        this.deProperties = deProperties;
        this.userInfo = userInfo;
        this.factory = factory;
        this.settingFactory = settingFactory;
    }

    @Override
    public Request getUserSession(AsyncCallback<List<WindowState>> callback) {
        String address = deProperties.getMuleServiceBaseUrl() + "sessions"; //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        return deServiceFacade.getServiceData(wrapper, new AsyncCallbackConverter<String, List<WindowState>>(callback) {

            @Override
            protected List<WindowState> convertFrom(String object) {
                final AutoBean<UserSession> decode = AutoBeanCodex.decode(factory, UserSession.class, object);
                return decode.as().getWindowStates();
            }
        });
    }

    @Override
    public Request saveUserSession(final List<WindowState> windowStates, AsyncCallback<Void> callback) {
        String address = deProperties.getMuleServiceBaseUrl() + "sessions"; //$NON-NLS-1$
        final AutoBean<UserSession> userSessionAutoBean = factory.userSession();
        userSessionAutoBean.as().setWindowStates(windowStates);
        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, AutoBeanCodex.encode(userSessionAutoBean).getPayload());
        return deServiceFacade.getServiceData(wrapper, new StringToVoidCallbackConverter(callback));
    }

    @Override
    public Request getUserPreferences(AsyncCallback<UserSetting> callback) {
        String address = deProperties.getMuleServiceBaseUrl() + "preferences"; //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        return deServiceFacade.getServiceData(wrapper, new AsyncCallbackConverter<String, UserSetting>(callback) {
            @Override
            protected UserSetting convertFrom(String object) {
                return AutoBeanCodex.decode(settingFactory, UserSetting.class, object).as();
            }
        });
    }

    @Override
    public void saveUserPreferences(UserSetting setting, AsyncCallback<Void> callback) {
        String address = deProperties.getMuleServiceBaseUrl() + "preferences"; //$NON-NLS-1$
        final Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(setting));
        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, encode.getPayload());
        deServiceFacade.getServiceData(wrapper, new StringToVoidCallbackConverter(callback));
    }

    @Override
    public void postClientNotification(JSONObject notification, AsyncCallback<String> callback) {
        String address = deProperties.getUnproctedMuleServiceBaseUrl() + "send-notification";

        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, notification.toString());

        deServiceFacade.getServiceData(wrapper, callback);

    }

    @Override
    public void postClientNotification(Notification notification, AsyncCallback<String> callback) {
        String address = deProperties.getUnproctedMuleServiceBaseUrl() + "send-notification";
        final Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(notification));
        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, encode.getPayload());

        deServiceFacade.getServiceData(wrapper, callback);

    }

    @Override
    public Request bootstrap(AsyncCallback<String> callback) {
        String address = BOOTSTRAP;
        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        return deServiceFacade.getServiceData(wrapper, callback);
    }

    @Override
    public void logout(AsyncCallback<String> callback) {
        String address = LOGOUT + "?login-time=" + userInfo.getLoginTime();
        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deServiceFacade.getServiceData(wrapper, callback);
    }

}
