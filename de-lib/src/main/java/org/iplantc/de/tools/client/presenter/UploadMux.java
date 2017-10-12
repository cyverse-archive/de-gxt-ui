package org.iplantc.de.tools.client.presenter;

import org.iplantc.de.tools.client.views.requests.UploadForm;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.core.client.Callback;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;
import com.google.web.bindery.event.shared.HandlerRegistration;

import com.sencha.gxt.core.client.util.Format;

import java.util.List;

// TODO move this to ui-commons

/**
 * This class manages a bunch of UploadForm uploads happening in parallel. It provides a single class
 * method, startUpload, that manages the uploads.
 */
public final class UploadMux {

    private static final class Upload {
        private final Callback<Void, UploadForm> onComplete;
        private final UploadForm UploadForm;

        private HandlerRegistration handlerReg = null;

        Upload(final UploadForm UploadForm, final Callback<Void, UploadForm> onComplete) {
            this.UploadForm = UploadForm;
            this.onComplete = onComplete;
        }

        boolean isActive() {
            return handlerReg != null;
        }

        void start() {
            handlerReg = UploadForm.addSubmitCompleteHandler(event -> complete(event.getResults()));
            UploadForm.submit();
        }

        private void complete(final String jsonResults) {
            handlerReg.removeHandler();
            handlerReg = null;

            final Splittable split = StringQuoter.split(Format.stripTags(jsonResults));
            if (split.isNull("file")) {
                onComplete.onFailure(UploadForm);
            } else {
                onComplete.onSuccess(null);
            }
        }
    }

    /**
     * Given a collection of UploadForm objects, it calls submit on each one. When all of the uploads
     * are complete, it executes the provided callback, passing a list of the UploadForms that failed
     * back if any of them failed.
     *
     * @param UploadForms The list of UploadForms to call submit on.
     * @param onAllComplete the callback to execute upon completion.
     */
    public static void startUploads(final Iterable<UploadForm> UploadForms,
                                    final Callback<Void, Iterable<UploadForm>> onAllComplete) {
        final UploadMux mux = new UploadMux(UploadForms, onAllComplete);
        mux.startUploads();
    }

    private final Callback<Void, Iterable<UploadForm>> onAllComplete;
    private final List<UploadForm> failedUploadForms;
    private final List<Upload> uploads;

    private UploadMux(final Iterable<UploadForm> UploadForms,
                      final Callback<Void, Iterable<UploadForm>> onAllComplete) {
        this.onAllComplete = onAllComplete;
        failedUploadForms = Lists.newArrayList();
        uploads = Lists.newArrayList();
        for (UploadForm UploadForm : Sets.newHashSet(UploadForms)) {
            uploads.add(new Upload(UploadForm, new Callback<Void, UploadForm>() {
                @Override
                public void onFailure(final UploadForm failure) {
                    failedUploadForms.add(failure);
                    handleUploadComplete();
                }
                @Override
                public void onSuccess(Void unused) {
                    handleUploadComplete();
                }
            }));
        }
    }

    private void startUploads() {
        failedUploadForms.clear();
        if (uploads.isEmpty()) {
            onAllComplete.onSuccess(null);
        } else {
            for (Upload upload : uploads) {
                upload.start();
            }
        }
    }

    private void handleUploadComplete() {
        for (Upload upload : uploads) {
            if (upload.isActive()) {
                return;
            }
        }
        if (failedUploadForms.isEmpty()) {
            onAllComplete.onSuccess(null);
        } else {
            onAllComplete.onFailure(failedUploadForms);
        }
    }

}
