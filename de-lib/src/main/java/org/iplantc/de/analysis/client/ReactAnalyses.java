package org.iplantc.de.analysis.client;


import org.iplantc.de.client.util.DiskResourceUtil;

import gwt.react.client.components.ComponentConstructorFn;
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
    public static ComponentConstructorFn<AnalysesProps> AnalysesView;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    public static class AnalysesProps extends BaseProps {
        public AnalysesView.Presenter presenter;
        public String username;
        public String email;
        public String name;
        public AnalysisParametersView.Presenter paramPresenter;
        public DiskResourceUtil diskResourceUtil;
        public String baseDebugId;
        public String viewFilter;
        public String appTypeFilter;
        public String parentId;
        public String appNameFilter;
        public String nameFilter;
        public String idFilter;
    }

    @JsProperty
    public static ComponentConstructorFn<ViceLogsProps> ViceLogsViewer;


    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    public static class ViceLogsProps extends BaseProps {
        public String logs;
        public boolean loading;
        public boolean dialogOpen;
        public String analysisName;
        public AnalysesView.Presenter presenter;
    }
}

