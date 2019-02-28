package org.iplantc.de.tools.client.views.requests;

import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.Splittable;

import jsinterop.annotations.JsType;

/**
 *  Modified by Sriram 02/26/2019
 *
 */

@JsType
public interface NewToolRequestFormView extends IsWidget {

    interface NewToolRequestFormViewAppearance  {
        String newToolRequest();

        String makePublicRequest();

        String newToolInstruction();

        String makePublicInstruction();

        SafeHtml buildRequiredFieldLabel(String label);

        SafeHtml sameFileError(String filename);

        SafeHtml alert();

        SafeHtml fileSizeViolation(String filename);

        SafeHtml maxFileSizeExceed();

        SafeHtml fileExistTitle();

        SafeHtml fileExists(String dupeFiles);

        String invalidFileName();

        String fileNameValidationMsg();

        String getFileName(String filename);
    }

    void load(Presenter presenter);

    @JsType
    interface Presenter extends org.iplantc.de.commons.client.presenter.Presenter {
        void submitRequest(Splittable toolRequest,
                           ReactSuccessCallback callback,
                           ReactErrorCallback errorCallback);

    }


}

