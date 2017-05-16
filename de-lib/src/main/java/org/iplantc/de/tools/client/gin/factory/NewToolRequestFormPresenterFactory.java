package org.iplantc.de.tools.client.gin.factory;

import org.iplantc.de.tools.client.views.requests.NewToolRequestFormView;

import com.google.gwt.user.client.Command;

/**
 * @author jstroot
 */
public interface NewToolRequestFormPresenterFactory {
    NewToolRequestFormView.Presenter createPresenter(Command callbackCommand);
}
