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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.event.SelectEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author aramsey
 */
public class DECallbackWrapper<T> implements AsyncCallback<T>, SelectEvent.SelectHandler{

    private DiscEnvApiService deServiceFacade;
    private DECallback<T> callback;
    Logger LOG = Logger.getLogger(DECallbackWrapper.class.getName());
    private ServiceCallWrapper wrapper;
    boolean retry = false;
    private DECallbackWrapper<String> callbackWrapper;


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

        if (caught instanceof StatusCodeException) {
            StatusCodeException statusCodeException = (StatusCodeException)caught;
            statusCode = statusCodeException.getStatusCode();
            response = statusCodeException.getEncodedResponse();
        } else if (caught instanceof HttpRedirectException) {
            HttpRedirectException redirectException = (HttpRedirectException)caught;
            statusCode = redirectException.getStatusCode();
            response = redirectException.getResponseBody();
//            Window.Location.assign(redirectException.getLocation());
        } else if (caught instanceof HttpException) {
            HttpException exception = (HttpException)caught;
            statusCode = exception.getStatusCode();
            response = exception.getResponseBody();
        } else {
            callback.onFailure(null, caught);
            return;
        }

        LOG.log(Level.SEVERE, "Status code: " + statusCode + "; Response : " + response, caught);

        if (statusCode >= 300 && statusCode <= 399) {
            //TODO FIX ME!
            return;
        }
        if (statusCode == 502 || statusCode == 503 || statusCode == 403) {
            retry = true;
            EventBus.getInstance().fireEvent(new ServiceDown(callback.getWindowType(), this));
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
        this.wrapper = wrapper;
        this.callbackWrapper = callbackWrapper;
    }

    @Override
    public void onSelect(SelectEvent event) {
        deServiceFacade.getServiceData(wrapper, callbackWrapper);
    }
}
