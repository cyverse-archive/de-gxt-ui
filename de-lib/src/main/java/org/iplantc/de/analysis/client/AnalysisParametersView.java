package org.iplantc.de.analysis.client;

import org.iplantc.de.client.models.diskResources.File;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.shared.Splittable;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;

/**
 * Created by jstroot on 2/23/15.
 * @author jstroot
 */
@JsType
public interface AnalysisParametersView {

    interface Appearance {

        String diskResourceDoesNotExist(String name);

        String fileUploadSuccess(String name);

        String importFailed(String path);

        String importRequestSubmit(String name);

        String retrieveParametersLoadingMask();

        String viewParameters(String name);

        String parametersDialogWidth();

        String parametersDialogHeight();
    }

    @JsType
    interface Presenter extends org.iplantc.de.commons.client.presenter.Presenter {



        interface BeanFactory extends AutoBeanFactory {
            @JsIgnore
            AutoBean<File> file();
        }

        void fetchAnalysisParameters(String analysis_id,
                                            ReactSuccessCallback callback,
                                            ReactErrorCallback errorCallback);

        void onAnalysisParamValueSelected(Splittable param);

        void onRequestSaveAnalysisParameters(String path, String contents);
    }
}
