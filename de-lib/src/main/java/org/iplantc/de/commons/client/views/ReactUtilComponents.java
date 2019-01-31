package org.iplantc.de.commons.client.views;

import gwt.react.client.components.ComponentConstructorFn;
import gwt.react.client.proptypes.BaseProps;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * @author aramsey
 */
@JsType(isNative = true, namespace = "CyVerseReactComponents", name = "util")
public class ReactUtilComponents {
    @JsProperty
    public static ComponentConstructorFn<ErrorHandlerProps> ErrorHandler;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    public static class ErrorHandlerProps extends BaseProps {
        public String errorSummary;
        public String errorDetails;
    }
}
