package org.iplantc.de.commons.client.util;

import com.google.gwt.dom.client.Element;
import com.google.web.bindery.autobean.shared.Splittable;

import gwt.react.client.api.React;
import gwt.react.client.api.ReactDOM;
import gwt.react.client.components.ComponentConstructorFn;
import gwt.react.client.elements.ReactElement;
import gwt.react.client.proptypes.BaseProps;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class CyVerseReactComponents {

    public static native Splittable getDefaultTheme();

    public static ComponentConstructorFn<MuiThemeProviderProps> MuiThemeProvider;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    static class MuiThemeProviderProps extends BaseProps {
        public Splittable theme;
    }

    @JsOverlay
    public static void render(ComponentConstructorFn reactClass, BaseProps props, Element el) {
        MuiThemeProviderProps themeProps = new MuiThemeProviderProps();
        themeProps.theme = getDefaultTheme();
        final ReactElement element = React.createElement(reactClass, props);
        ReactElement elementWithTheme =
                React.createElement(MuiThemeProvider, themeProps, element);
        ReactDOM.render(elementWithTheme,
                        el);
    }
}

