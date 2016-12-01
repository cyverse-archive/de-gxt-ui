package org.iplantc.de.client.services.impl;

import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.GET;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.POST;

import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.sysMsgs.IdList;
import org.iplantc.de.client.models.sysMsgs.MessageFactory;
import org.iplantc.de.client.models.sysMsgs.MessageList;
import org.iplantc.de.client.models.sysMsgs.User;
import org.iplantc.de.client.services.SystemMessageServiceFacade;
import org.iplantc.de.client.services.converters.DECallbackConverter;
import org.iplantc.de.shared.DECallback;
import org.iplantc.de.shared.services.DiscEnvApiService;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 * Provides access to remote services to acquire system messages.
 */
public final class SystemMessageServiceFacadeImpl implements SystemMessageServiceFacade {

    private class StringToVoidDECallbackConverter extends DECallbackConverter<String, Void> {

        public StringToVoidDECallbackConverter(DECallback<Void> callback) {
            super(callback);
        }

        @Override
        protected Void convertFrom(String object) {
            return null;
        }
    }

    private static final class MsgListCB extends DECallbackConverter<String, MessageList> {
        private final MessageFactory msgFactory;

        public MsgListCB(final DECallback<MessageList> callback, final MessageFactory msgFactory) {
            super(callback);
            this.msgFactory = msgFactory;
        }

        @Override
        protected MessageList convertFrom(final String json) {
            return AutoBeanCodex.decode(msgFactory, MessageList.class, json).as();
        }
    }

    private final String NOTIFICATIONS = "org.iplantc.services.notifications";
    private final MessageFactory factory;
    private final DiscEnvApiService deServiceFacade;
    private final UserInfo userInfo;

    @Inject
    public SystemMessageServiceFacadeImpl(final DiscEnvApiService deServiceFacade,
                                          final MessageFactory factory,
                                          final UserInfo userInfo) {
        this.deServiceFacade = deServiceFacade;
        this.factory = factory;
        this.userInfo = userInfo;
    }

    /**
     * @see SystemMessageServiceFacade#getAllMessages(DECallback)
     */
    @Override
    public final void getAllMessages(final DECallback<MessageList> callback) {
        getMessages("/messages", callback); //$NON-NLS-1$
    }

    /**
     * @see SystemMessageServiceFacade#getNewMessages(DECallback)
     */
    @Override
    public final void getNewMessages(final DECallback<MessageList> callback) {
        getMessages("/new-messages", callback); //$NON-NLS-1$
    }

    /**
     * @see SystemMessageServiceFacade#getUnseenMessages(DECallback)
     */
    @Override
    public final void getUnseenMessages(final DECallback<MessageList> callback) {
        getMessages("/unseen-messages", callback); //$NON-NLS-1$
    }

    /**
     * @see SystemMessageServiceFacade#markAllReceived(DECallback)
     */
    @Override
    public void markAllReceived(final DECallback<Void> callback) {
        final String address = makeAddress("/mark-all-received");  //$NON-NLS-1$
        final AutoBean<User> user = factory.makeUser();
        user.as().setUser(userInfo.getUsername());
        final String payload = AutoBeanCodex.encode(user).getPayload();
        final ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, payload);
        final DECallback<String> voidedCB = new StringToVoidDECallbackConverter(callback);
        deServiceFacade.getServiceData(wrapper, voidedCB);
    }

    /**
     * @see SystemMessageServiceFacade#markReceived(IdList, DECallback)
     */
    @Override
    public void markReceived(final IdList msgIds, final DECallback<Void> callback) {
        final String address = makeAddress("/received");  //$NON-NLS-1$
        final Splittable split = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(msgIds));
        final ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, split.getPayload());
        final DECallback<String> voidedCB = new StringToVoidDECallbackConverter(callback);
        deServiceFacade.getServiceData(wrapper, voidedCB);
    }

    /**
     * @see SystemMessageServiceFacade#acknowledgeMessages(IdList, DECallback)
     */
    @Override
    public void acknowledgeMessages(final IdList msgIds, final DECallback<Void> callback) {
        final String address = makeAddress("/seen"); //$NON-NLS-1$
        final Splittable split = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(msgIds));
        final ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, split.getPayload());
        final DECallback<String> voidedCB = new StringToVoidDECallbackConverter(callback);
        deServiceFacade.getServiceData(wrapper, voidedCB);
    }

    /**
     * @see SystemMessageServiceFacade#hideMessages(IdList, DECallback)
     */
    @Override
    public void hideMessages(final IdList msgIds, final DECallback<Void> callback) {
        final String address = makeAddress("/delete");  //$NON-NLS-1$
        final Splittable split = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(msgIds));
        final ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, split.getPayload());
        final DECallback<String> voidedCB = new StringToVoidDECallbackConverter(callback);
        deServiceFacade.getServiceData(wrapper, voidedCB);
    }

    private void getMessages(final String relSvcPath, final DECallback<MessageList> callback) {
        final String address = makeAddress(relSvcPath);
        final ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deServiceFacade.getServiceData(wrapper, new MsgListCB(callback, factory));
    }

    private String makeAddress(final String relPath) {
        final String base = NOTIFICATIONS + "/system";
        return base + relPath;  //$NON-NLS-1$
    }

}
