package org.iplantc.de.tools.client;

import org.iplantc.de.tools.client.views.manage.ManageToolsView;

import com.google.web.bindery.autobean.shared.Splittable;

import gwt.react.client.components.ComponentConstructorFn;
import gwt.react.client.proptypes.BaseProps;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = "CyVerseReactComponents", name = "tools")
public class ReactToolViews {

    @JsProperty public static ComponentConstructorFn<EditToolProps> EditTool;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    public static class EditToolProps extends BaseProps {
        public ManageToolsView.Presenter presenter;
        public String[] toolTypes;
        public double maxCPUCore;
        public long maxMemory;
        public long maxDiskSpace;
        public String parentId;
        public boolean open;
        public boolean loading;
        public Splittable tool;
    }
}
