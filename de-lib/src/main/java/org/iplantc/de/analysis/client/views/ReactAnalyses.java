package org.iplantc.de.analysis.client.views;


import org.iplantc.de.analysis.client.AnalysesView;

import com.google.web.bindery.autobean.shared.Splittable;

import gwt.react.client.components.ReactClass;
import gwt.react.client.proptypes.BaseProps;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * Created by sriram on 08/27/18.
 */
@JsType(isNative = true, namespace = "CyVerseReactComponents.analysis", name = "AnalysesView")
public class ReactAnalyses {

    @JsProperty(namespace = "CyVerseReactComponents.analysis", name = "AnalysesView")
    public static ReactClass<AnalysesProps> analysesProps;

    static class AnalysesProps extends BaseProps {
        AnalysesView.Presenter presenter;
        Splittable analysesList;
        String username;
    }

}
