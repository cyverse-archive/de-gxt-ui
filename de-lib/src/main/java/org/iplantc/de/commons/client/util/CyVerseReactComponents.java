package org.iplantc.de.commons.client.util;

import com.google.gwt.dom.client.Element;
import com.google.web.bindery.autobean.shared.Splittable;
import gwt.react.client.api.React;
import gwt.react.client.api.ReactDOM;
import gwt.react.client.components.ReactClass;
import gwt.react.client.proptypes.BaseProps;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class CyVerseReactComponents {
    public static native Splittable getCyVerseTheme();

    public static ReactClass<MuiThemeProviderProps> MuiThemeProvider;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    static class MuiThemeProviderProps extends BaseProps {
        public Splittable muiTheme;
    }

    @JsOverlay
    public static void render(ReactClass reactClass, BaseProps props, Element el) {
        MuiThemeProviderProps themeProps = new MuiThemeProviderProps();
        themeProps.muiTheme = getCyVerseTheme();
        ReactDOM.render(React.createElement(MuiThemeProvider, themeProps, React.createElement(reactClass, props)), el);
    }
}
