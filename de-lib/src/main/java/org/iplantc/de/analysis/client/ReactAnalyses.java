package org.iplantc.de.analysis.client;


import gwt.react.client.components.ReactClass;
import gwt.react.client.proptypes.BaseProps;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * Created by sriram on 08/27/18.
 */
@JsType(isNative = true, namespace = "CyVerseReactComponents", name = "analysis")
public class ReactAnalyses {

    @JsProperty
    public static ReactClass<AnalysesProps> AnalysesView;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    public static class AnalysesProps extends BaseProps {
        public AnalysesView.Presenter presenter;
        public String user;
        public String email;
        public String name;
    }
}

