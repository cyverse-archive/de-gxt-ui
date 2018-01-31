package org.iplantc.de.desktop.client.views.windows;

import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.commons.client.CommonUiConstants;
import org.iplantc.de.commons.client.views.window.configs.WindowConfig;
import org.iplantc.de.desktop.client.presenter.util.WindowStateStorageWrapper;
import org.iplantc.de.desktop.client.views.widgets.ServiceDownPanel;
import org.iplantc.de.desktop.shared.DeModule;
import org.iplantc.de.intercom.client.IntercomFacade;
import org.iplantc.de.intercom.client.TrackingEventType;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.debug.client.DebugInfo;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Rectangle;
import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.widget.core.client.Header;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.IconButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.MaximizeEvent;
import com.sencha.gxt.widget.core.client.event.MaximizeEvent.MaximizeHandler;
import com.sencha.gxt.widget.core.client.event.RestoreEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

/**
 * @author jstroot
 * FIXME REFACTOR Rename to AbstractIplantWindow
 */
public abstract class WindowBase extends Window implements WindowInterface {
    private class HeaderDoubleClickHandler implements DoubleClickHandler {

        @Override
        public void onDoubleClick(DoubleClickEvent event) {
            if (!isMaximized()) {
                maximize();
            } else {
                restore();
            }
        }
    }

    private class MaximizeRestoreHandler implements MaximizeHandler, RestoreEvent.RestoreHandler {
        @Override
        public void onMaximize(MaximizeEvent event) {
            replaceMaximizeIcon();
        }

        @Override
        public void onRestore(RestoreEvent event) {
            replaceRestoreIcon();
        }
    }

    public interface IplantWindowAppearance {

        IconButton.IconConfig closeBtnConfig();

        String closeBtnToolTip();

        IconButton.IconConfig layoutBtnConfig();

        String layoutBtnToolTip();

        IconButton.IconConfig maximizeBtnConfig();

        String maximizeBtnToolTip();

        IconButton.IconConfig minimizeBtnConfig();

        String minimizeBtnToolTip();

        IconButton.IconConfig restoreBtnConfig();

        String restoreBtnToolTip();

        /**
         * sets header text style and sets header icon
         * @param header
         */
        void setHeaderStyle(Header header);

        String snapLeftMenuItem();

        String snapRightMenuItem();

        IconButton.IconConfig helpBtnConfig() ;

        String helpBtnToolTip() ;

    }
    public interface WindowStateFactory extends AutoBeanFactory {
        AutoBean<WindowState> windowState();
    }

    protected WindowConfig config;
    protected boolean isMaximizable;
    protected boolean maximized;
    protected boolean minimized;
    ToolButton btnRestore;
    WindowStateFactory wsf = GWT.create(WindowStateFactory.class);
    private String baseDebugID;
    private ToolButton btnClose;
    private ToolButton btnLayout;
    private ToolButton btnMaximize;
    private ToolButton btnMinimize;
    protected ToolButton btnHelp;
    private IplantWindowAppearance windowAppearance;
    protected CommonUiConstants constants = GWT.create(CommonUiConstants.class);
    protected Widget currentWidget;
    protected ServiceDownPanel serviceDownPanel;
    protected WindowState ws;
    UserInfo userInfo = UserInfo.getInstance();

    public WindowBase() {
        this(GWT.<IplantWindowAppearance> create(IplantWindowAppearance.class));
    }

    public WindowBase(final IplantWindowAppearance appearance) {
        // Let normal window appearance go through
        windowAppearance = appearance;
        // Turn off default window buttons.
        setMaximizable(false);
        setMinimizable(false);
        setClosable(false);

        setShadow(false);
        setBodyBorder(false);
        setBorders(false);

        windowAppearance.setHeaderStyle(getHeader());
       // Add help, Layout, minimize, and close buttons
        btnLayout = createLayoutButton();
        btnMinimize = createMinimizeButton();
        btnClose = createCloseButton();
        getHeader().addTool(btnLayout);
        getHeader().addTool(btnMinimize);
        getHeader().addTool(btnClose);

        final MaximizeRestoreHandler maximizeRestoreHandler = new MaximizeRestoreHandler();
        addRestoreHandler(maximizeRestoreHandler);
        addMaximizeHandler(maximizeRestoreHandler);
    }

    @Override
    public boolean isMaximized() {
        return maximized;
    }

    void setMaximized(boolean maximize) {
        if (isMaximizable) {
            this.maximized = maximize;

            if (maximize) {
                maximize();
                minimized = false;
            } else {
                restore();
            }
        }
    }

    @Override
    public boolean isMinimized() {
        return minimized;
    }

    @Override
    public void minimize() {
        super.minimize();
        minimized = true;

        // Do not call the overridden hide() method
        super.hide();
        manager.register(this);
    }

    @Override
    public Window asWindow() {
        return this;
    }

    @Override
    public <C extends WindowConfig> void show(final C windowConfig,
                                              final String tag,
                                              final boolean isMaximizable) {
        this.config = windowConfig;
        this.isMaximizable = isMaximizable;
        setStateId(tag);
        ws = getWindowStateFromLocalStorage(tag);
        if (isMaximizable) {
            btnMaximize = createMaximizeButton();
            // SRI: if a window is maximizable, then it is restorable.
            btnRestore = createRestoreButton();
            getHeader().insertTool(btnMaximize, getHeader().getToolCount() - 1);

            getHeader().addDomHandler(new HeaderDoubleClickHandler(), DoubleClickEvent.getType());
        }
        restoreWindowState();
        show();
    }

    @Override
    public void show() {
        if (!hidden || !fireCancellableEvent(new BeforeShowEvent())) {
            return;
        }


        // remove hide style, else layout fails
        removeStyleName(getHideMode().value());
        getElement().makePositionable(true);
        if (!isAttached()) {
            RootPanel.get().add(this);
        }


        onShow();

        //Prevent minimized windows from being duplicated in the window manager
        if (!manager.getWindows().contains(this)) {
            manager.register(this);
        }


        afterShow();
        notifyShow();
    }

    @Override
    public <C extends org.iplantc.de.commons.client.views.window.configs.WindowConfig> void update(C config) {
    }

    @Override
    public WindowState createWindowState() {
        WindowState ws = wsf.windowState().as();
        ws.setWindowType(getWindowType());
        ws.setWinLeft(getAbsoluteLeft());
        ws.setWinTop(getAbsoluteTop());
        ws.setWidth(getElement().getWidth(true) + "");
        ws.setHeight(getElement().getHeight(true) + "");
        ws.setMaximized(isMaximized());
        ws.setMinimized(isMinimized());
        ws.setAdditionalWindowStates(getAdditionalWindowStates());
        ws.setTag(getStateId());
        return ws;
    }

    /**
     * This method is reserved for actually closing a window.  Do not call this method
     * to minimize a window, call minimize()
     */
    @Override
    public void hide() {
        if (isMinimized()) {
            minimized = false;
            manager.unregister(this);
        }
        logWindowCloseToIntercom(config);
        super.hide();
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        this.baseDebugID = baseID;

        if(btnMaximize != null){
            btnMaximize.ensureDebugId(baseID + DeModule.Ids.WIN_MAX_BTN);
        }
        if(btnRestore != null) {
            btnRestore.ensureDebugId(baseID + DeModule.Ids.WIN_RESTORE_BTN);
        }
        btnMinimize.ensureDebugId(baseID + DeModule.Ids.WIN_MIN_BTN);
        btnClose.ensureDebugId(baseID + DeModule.Ids.WIN_CLOSE_BTN);
        btnLayout.ensureDebugId(baseID + DeModule.Ids.WIN_LAYOUT_BTN);
    }

    @Override
    protected void onShow() {
        super.onShow();
        minimized = false;
    }

    private ToolButton createCloseButton() {
        final ToolButton newCloseBtn = new ToolButton(windowAppearance.closeBtnConfig());
        newCloseBtn.setToolTip(windowAppearance.closeBtnToolTip());
        newCloseBtn.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                hide();
            }
        });
        return newCloseBtn;
    }


    protected ToolButton createHelpButton() {
        final ToolButton helpBtn = new ToolButton(windowAppearance.helpBtnConfig());
        // Remove tool tip, it gets in the way of the menu.
        helpBtn.setToolTip(windowAppearance.helpBtnToolTip());
        return helpBtn;
    }

    private ToolButton createLayoutButton() {
        final ToolButton layoutBtn = new ToolButton(windowAppearance.layoutBtnConfig());
        // Remove tool tip, it gets in the way of the menu.
        layoutBtn.setToolTip(windowAppearance.layoutBtnToolTip());
        final Menu m = new Menu();
        MenuItem left = new MenuItem(windowAppearance.snapLeftMenuItem());
        MenuItem right = new MenuItem(windowAppearance.snapRightMenuItem());
        left.addSelectionHandler(new SelectionHandler<Item>() {
            @Override
            public void onSelection(SelectionEvent<Item> event) {
                doSnapLeft(WindowBase.this.getContainer().<XElement>cast());
            }
        });
        right.addSelectionHandler(new SelectionHandler<Item>() {
            @Override
            public void onSelection(SelectionEvent<Item> event) {
                doSnapRight(WindowBase.this.getContainer().<XElement>cast());
            }
        });

        m.add(left);
        m.add(right);

        layoutBtn.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                m.showAt(layoutBtn.getAbsoluteLeft() + 10, layoutBtn.getAbsoluteTop() + 15);
            }
        });

        return layoutBtn;
    }

    void doSnapLeft(XElement xElement) {
        setMaximized(false);
        Rectangle bounds = xElement.getBounds();
        setPagePosition(bounds.getX(), bounds.getY());
        setPixelSize(bounds.getWidth()/2, bounds.getHeight());
    }

    void doSnapRight(XElement xElement) {
        setMaximized(false);
        Rectangle bounds = xElement.getBounds();
        setPagePosition(bounds.getWidth()/2, bounds.getY());
        setPixelSize(bounds.getWidth()/2, bounds.getHeight());
    }

    private ToolButton createMaximizeButton() {

        final ToolButton maxBtn = new ToolButton(windowAppearance.maximizeBtnConfig());
        maxBtn.setToolTip(windowAppearance.maximizeBtnToolTip());

        maxBtn.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                setMaximized(true);
            }
        });

        if(DebugInfo.isDebugIdEnabled()
            && !Strings.isNullOrEmpty(baseDebugID)){
            maxBtn.ensureDebugId(baseDebugID + DeModule.Ids.WIN_MAX_BTN);
        }
        return maxBtn;
    }

    private ToolButton createMinimizeButton() {
        final ToolButton newMinBtn = new ToolButton(windowAppearance.minimizeBtnConfig());
        newMinBtn.setToolTip(windowAppearance.minimizeBtnToolTip());

        newMinBtn.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                minimize();
            }
        });

        return newMinBtn;
    }

    private ToolButton createRestoreButton() {
        final ToolButton btnRestore = new ToolButton(windowAppearance.restoreBtnConfig());
        btnRestore.setToolTip(windowAppearance.restoreBtnToolTip());

        btnRestore.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                setMaximized(false);
            }
        });

        if(DebugInfo.isDebugIdEnabled()
            && !Strings.isNullOrEmpty(baseDebugID)){
            btnRestore.ensureDebugId(baseDebugID + DeModule.Ids.WIN_RESTORE_BTN);
        }
        return btnRestore;
    }


    /**
     * Replaces the maximize icon with the restore icon.
     * <p/>
     * The restore icon is only visible to the user when a window is in maximized state.
     */
    private void replaceMaximizeIcon() {
        final int indexOf = getHeader().getTools().indexOf(btnMaximize);
        getHeader().removeTool(btnMaximize);
        getHeader().insertTool(btnRestore, indexOf);
    }

    /**
     * Replaces the restore icon with the maximize icon.
     */
    private void replaceRestoreIcon() {
        final int indexOf = getHeader().getTools().indexOf(btnRestore);
        getHeader().removeTool(btnRestore);
        getHeader().insertTool(btnMaximize, indexOf);
    }

    @Override
    public void serviceDown(SelectEvent.SelectHandler handler) {
        if (currentWidget == null) {
            currentWidget = getWidget();
            serviceDownPanel = new ServiceDownPanel();
            this.setWidget(serviceDownPanel);
        }

        serviceDownPanel.addHandler(handler);
        serviceDownPanel.unmask();
    }

    @Override
    public void serviceUp() {
        if (currentWidget != null) {
            this.setWidget(currentWidget);
            currentWidget = null;
        }
    }

    /**
     * Log window opening event to Intercom
     *
     * @param config Config of the window that was opened.
     */
    @Override
    public void logWindowOpenToIntercom(org.iplantc.de.commons.client.views.window.configs.WindowConfig config) {
        if (config != null) {
            switch (config.getWindowType()) {

                case DATA:
                    IntercomFacade.trackEvent(TrackingEventType.DATA_WINDOW_OPEN,
                                              AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(config)));
                    break;

                case ANALYSES:
                    IntercomFacade.trackEvent(TrackingEventType.ANALYSIS_WINDOW_OPEN,
                                              AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(config)));
                    break;
                case APPS:
                    IntercomFacade.trackEvent(TrackingEventType.APPS_WINDOW_OPEN,
                                              AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(config)));
                    break;

                case MANAGETOOLS:
                    IntercomFacade.trackEvent(TrackingEventType.MANAGE_TOOLS_WINDOW_OPEN,
                                              AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(config)));
                    break;

                case APP_INTEGRATION:
                    IntercomFacade.trackEvent(TrackingEventType.APP_INT_WINDOW_OPEN,
                                              AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(config)));
                    break;
                case ABOUT:
                    IntercomFacade.trackEvent(TrackingEventType.ABOUT_WINDOW_OPEN,
                                              AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(config)));
                    break;

                case APP_WIZARD:
                    IntercomFacade.trackEvent(TrackingEventType.APP_WIZARD_OPEN,
                                              AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(config)));
                    break;

                case HELP:
                    IntercomFacade.trackEvent(TrackingEventType.HELP_BUTTON_CLICKED,
                                              AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(config)));
                    break;

                case NOTIFICATIONS:
                    IntercomFacade.trackEvent(TrackingEventType.NOTIFICATION_WINDOW_OPEN,
                                              AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(config)));
                    break;

                case SIMPLE_DOWNLOAD:
                    IntercomFacade.trackEvent(TrackingEventType.SIMPLE_DOWNLOAD_WINDOW_OPEN,
                                              AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(config)));
                    break;

                case WORKFLOW_INTEGRATION:
                    IntercomFacade.trackEvent(TrackingEventType.WORKFLOW_INT_WINDOW_OPEN,
                                              AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(config)));
                    break;

                case SYSTEM_MESSAGES:
                    IntercomFacade.trackEvent(TrackingEventType.SYS_MESSAGE_WINDOW_OPEN,
                                              AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(config)));
                    break;

                case COLLABORATION:
                    IntercomFacade.trackEvent(TrackingEventType.COLLAB_WINDOW_OPEN,
                                              AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(config)));
                    break;

                case FILE_VIEWER:
                    IntercomFacade.trackEvent(TrackingEventType.DATA_VIEWER_WINDOW_OPEN,
                                              AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(config)));
                    break;


            }
        }
    }


    /**
     * Log window closing event to Intercom
     *
     * @param config Config of the window that was closed.
     */
    @Override
    public void logWindowCloseToIntercom(org.iplantc.de.commons.client.views.window.configs.WindowConfig config) {
        if (config != null) {
            switch (config.getWindowType()) {

                case DATA:
                    IntercomFacade.trackEvent(TrackingEventType.DATA_WINDOW_CLOSED,
                                              AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(config)));
                    break;

                case ANALYSES:
                    IntercomFacade.trackEvent(TrackingEventType.ANALYSIS_WINDOW_CLOSE,
                                              AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(config)));
                    break;
                case APPS:
                    IntercomFacade.trackEvent(TrackingEventType.APPS_WINDOW_CLOSED,
                                              AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(config)));
                    break;

                case MANAGETOOLS:
                    IntercomFacade.trackEvent(TrackingEventType.MANAGE_TOOLS_WINDOW_CLOSED,
                                              AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(config)));
                    break;

                case APP_INTEGRATION:
                    IntercomFacade.trackEvent(TrackingEventType.APP_INT_WINDOW_CLOSED,
                                              AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(config)));
                    break;
                case ABOUT:
                    IntercomFacade.trackEvent(TrackingEventType.ABOUT_WINDOW_CLOSED,
                                              AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(config)));
                    break;

                case APP_WIZARD:
                    IntercomFacade.trackEvent(TrackingEventType.APP_WIZARD_CLOSED,
                                              AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(config)));
                    break;

                case NOTIFICATIONS:
                    IntercomFacade.trackEvent(TrackingEventType.NOTIFICATION_WINDOW_CLOSED,
                                              AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(config)));
                    break;

                case SIMPLE_DOWNLOAD:
                    IntercomFacade.trackEvent(TrackingEventType.SIMPLE_DOWNLOAD_WINDOW_CLOSED,
                                              AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(config)));
                    break;

                case WORKFLOW_INTEGRATION:
                    IntercomFacade.trackEvent(TrackingEventType.WORKFLOW_INT_WINDOW_CLOSED,
                                              AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(config)));
                    break;

                case SYSTEM_MESSAGES:
                    IntercomFacade.trackEvent(TrackingEventType.SYS_MESSAGE_WINDOW_CLOSED,
                                              AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(config)));
                    break;

                case COLLABORATION:
                    IntercomFacade.trackEvent(TrackingEventType.COLLAB_WINDOW_CLOSED,
                                              AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(config)));
                    break;

                case FILE_VIEWER:
                    IntercomFacade.trackEvent(TrackingEventType.DATA_VIEWER_WINDOW_CLOSED,
                                              AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(config)));
                    break;


            }
        }
    }

    public abstract String getWindowType();

    public abstract FastMap<String> getAdditionalWindowStates();

    public void restoreWindowState() {
        if (ws.isMaximized()) {
            Scheduler.get().scheduleDeferred((Command)() -> maximize());
        } else if (ws.isMinimized()) {
            Scheduler.get().scheduleDeferred((Command)() -> minimize());
        } else {
            int left = ws.getWinLeft();
            int top = ws.getWinTop();
            if (left != 0 && top != 0) {
                setPagePosition(left, top);
            }
        }
    }

    @Override
    public WindowState getWindowStateFromLocalStorage(String tag) {
        WindowStateStorageWrapper wssw = new WindowStateStorageWrapper(userInfo, getWindowType(), tag);
        return wssw.retrieveWindowState();
    }
}

