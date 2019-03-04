package org.iplantc.de.tools.client.views.requests;

import org.iplantc.de.tools.client.views.manage.ManageToolsView;

import com.google.gwt.user.client.ui.IsWidget;

import jsinterop.annotations.JsType;

/**
 *  Modified by Sriram 02/26/2019
 *
 */

@JsType
public interface NewToolRequestFormView extends IsWidget {

    void load(ManageToolsView.Presenter presenter);

    void onClose();


}

