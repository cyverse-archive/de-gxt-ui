package org.iplantc.de.tools.client;

import static org.iplantc.de.tools.client.views.manage.ManageToolsView.Presenter;

import gwt.react.client.components.ComponentConstructorFn;
import gwt.react.client.proptypes.BaseProps;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * Created by sriram on 02/26/193
 */
@JsType(isNative = true, namespace = "CyVerseReactComponents", name = "tools")
public class ReactTools {

    @JsProperty
    public  static ComponentConstructorFn<ToolRequestProps> NewToolRequestForm;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name="Object")
    public static class ToolRequestProps extends BaseProps {
        public Presenter presenter;
        public boolean dialogOpen;
    }
    

}
