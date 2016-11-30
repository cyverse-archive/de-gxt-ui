package org.iplantc.de.shared;

import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.shared.events.ServiceDown;
import org.iplantc.de.shared.events.ServiceRestored;
import org.iplantc.de.shared.events.UserLoggedOutEvent;
import org.iplantc.de.shared.exceptions.AuthenticationException;
import org.iplantc.de.shared.exceptions.HttpException;
import org.iplantc.de.shared.exceptions.HttpRedirectException;
import org.iplantc.de.shared.services.DiscEnvApiService;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.common.base.Strings;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.event.SelectEvent;

import org.apache.commons.httpclient.HttpStatus;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author aramsey
 */
public class DECallbackWrapper<T> implements AsyncCallback<T>, SelectEvent.SelectHandler {

    private DiscEnvApiService deServiceFacade;
    private DECallback<T> callback;
    Logger LOG = Logger.getLogger(DECallbackWrapper.class.getName());
    private ServiceCallWrapper wrapper;
    boolean retry = false;
    private DECallbackWrapper<String> callbackWrapper;
    private HashMap<String, String> mdcMap;


    @Inject
    public DECallbackWrapper(DECallback<T> callback, DiscEnvApiService deServiceFacade) {
        this.callback = callback;
        this.deServiceFacade = deServiceFacade;
    }

    @Override
    public void onFailure(Throwable caught) {
        if (caught instanceof AuthenticationException) {
            EventBus.getInstance().fireEvent(new UserLoggedOutEvent());
            LOG.log(Level.SEVERE, "Auth error!!!!!", caught);
            return;
        }

        Integer statusCode;
        String response;
        String uri = null;

        if (caught instanceof StatusCodeException) {
            StatusCodeException statusCodeException = (StatusCodeException)caught;
            statusCode = statusCodeException.getStatusCode();
            response = statusCodeException.getEncodedResponse();
        } else if (caught instanceof HttpRedirectException) {
            //DEServiceImpl currently only throws this if status == 302
            HttpRedirectException redirectException = (HttpRedirectException)caught;
            statusCode = redirectException.getStatusCode();
            response = redirectException.getResponseBody();
            uri = redirectException.getLocation();
        } else if (caught instanceof HttpException) {
            HttpException exception = (HttpException)caught;
            statusCode = exception.getStatusCode();
            response = exception.getResponseBody();
        } else {
            LOG.log(Level.SEVERE, "Unexpected error", caught);
            callback.onFailure(null, caught);
            return;
        }

        LOG.log(Level.SEVERE, "Status code: " + statusCode + "; Response : " + response, caught);

        if (statusCode == HttpStatus.SC_BAD_GATEWAY ||
            statusCode == HttpStatus.SC_SERVICE_UNAVAILABLE) {
            retry = true;
            EventBus.getInstance().fireEvent(new ServiceDown(callback.getWindowType(), this));
            return;
        }

        EventBus.getInstance().fireEvent(new ServiceRestored(callback.getWindowType()));

        if (statusCode >= 300 && statusCode <= 399 && !Strings.isNullOrEmpty(uri)) {
            if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                EventBus.getInstance().fireEvent(new UserLoggedOutEvent());
                return;
            }
            Window.Location.replace(uri);
            return;
        }
        if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
            EventBus.getInstance().fireEvent(new UserLoggedOutEvent());
            return;
        }

        callback.onFailure(statusCode, caught);

    }

    @Override
    public void onSuccess(T result) {
        if (retry) {
            EventBus.getInstance().fireEvent(new ServiceRestored(callback.getWindowType()));
        }
        callback.onSuccess(result);
    }

    public void setRetryVars(ServiceCallWrapper wrapper, DECallbackWrapper<String> callbackWrapper) {
        setRetryVars(wrapper, null, callbackWrapper);
    }

    public void setRetryVars(ServiceCallWrapper wrapper,
                             HashMap<String, String> mdcMap,
                             DECallbackWrapper<String> callbackWrapper) {
        this.wrapper = wrapper;
        this.mdcMap = mdcMap;
        this.callbackWrapper = callbackWrapper;
    }

    @Override
    public void onSelect(SelectEvent event) {
        if (mdcMap == null) {
            deServiceFacade.getServiceData(wrapper, callbackWrapper);
        } else {
            deServiceFacade.getServiceData(wrapper, mdcMap, callbackWrapper);
        }
    }
}
