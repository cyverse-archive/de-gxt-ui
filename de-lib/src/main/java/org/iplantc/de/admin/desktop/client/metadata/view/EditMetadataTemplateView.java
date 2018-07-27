package org.iplantc.de.admin.desktop.client.metadata.view;

import org.iplantc.de.admin.desktop.client.metadata.presenter.callbacks.SaveTemplateError;
import org.iplantc.de.admin.desktop.client.metadata.presenter.callbacks.SaveTemplateSuccess;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.Splittable;

import jsinterop.annotations.JsType;

public interface EditMetadataTemplateView extends IsWidget {

    @JsType
    interface Presenter {
        @SuppressWarnings("unusable-by-js")
        void onSaveTemplate(Splittable metadataTemplate, SaveTemplateSuccess resolve, SaveTemplateError reject);
        void closeTemplateInfoDialog();
    }

    void edit(Presenter presenter, Splittable metadataTemplate);

    void closeDialog();
}
