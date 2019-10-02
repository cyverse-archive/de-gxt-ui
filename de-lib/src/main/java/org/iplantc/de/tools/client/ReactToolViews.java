package org.iplantc.de.tools.client;

import org.iplantc.de.admin.desktop.client.toolAdmin.ToolAdminView;
import org.iplantc.de.admin.desktop.client.toolRequest.ToolRequestView;
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
    public static class BaseEditToolProps extends BaseProps {
        public String[] toolTypes;
        public double maxCPUCore;
        public long maxMemory;
        public long maxDiskSpace;
        public String parentId;
        public boolean isAdmin;
        public boolean isAdminPublishing;
        public boolean open;
        public boolean loading;
        public Splittable tool;
    }

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    public static class EditToolProps extends BaseEditToolProps {
        public ManageToolsView.Presenter presenter;
    }

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    public static class AdminEditToolProps extends BaseEditToolProps {
        public ToolAdminView.Presenter presenter;
    }

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    public static class AdminPublishingToolProps extends BaseEditToolProps {
        public ToolRequestView.Presenter presenter;
    }

    @JsProperty public static ComponentConstructorFn<EditToolProps> ManageTools;

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    public static class ManageToolsProps extends BaseProps {
        public boolean loading;
        public String parentId;
        public Splittable toolList;
        public ManageToolsView.Presenter presenter;
        public Boolean isPublic;
        public String order;
        public String orderBy;
        public int page;
        public int rowsPerPage;
        public String searchTerm;
    }
}
