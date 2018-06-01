package org.iplantc.de.desktop.client.views;

import org.iplantc.de.client.models.UserSettings;
import org.iplantc.de.client.services.MessageServiceFacade;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.util.CyVerseReactComponents;
import org.iplantc.de.commons.client.views.window.configs.WindowConfig;
import org.iplantc.de.desktop.client.DesktopView;
import org.iplantc.de.desktop.client.views.widgets.DEFeedbackDialog;
import org.iplantc.de.desktop.client.views.windows.WindowBase;
import org.iplantc.de.desktop.client.views.windows.WindowInterface;
import org.iplantc.de.resources.client.messages.IplantNewUserTourStrings;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.WindowManager;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.event.RegisterEvent;
import com.sencha.gxt.widget.core.client.event.UnregisterEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by jstroot on 7/6/14.
 * @author jstroot
 */
public class DesktopViewImpl extends Composite implements DesktopView,
                                                          UnregisterEvent.UnregisterHandler<Widget>,
                                                          RegisterEvent.RegisterHandler<Widget> {

    @Inject AsyncProviderWrapper<DEFeedbackDialog> deFeedbackDialogProvider;
    @Inject UserSettings userSettings;
    @Inject
    MessageServiceFacade messageServiceFacade;

    DesktopView.Presenter presenter;
    HTMLPanel panel;
    DesktopView.DesktopAppearance appearance;

    private final WindowManager windowManager;
    private Map<Splittable, WindowBase> windowConfigMap = new HashMap<>();

    @Inject
    DesktopViewImpl(final IplantNewUserTourStrings tourStrings,
                    final WindowManager windowManager,
                    DesktopView.DesktopAppearance appearance) {
        this.windowManager = windowManager;
        this.appearance = appearance;
        windowManager.addRegisterHandler(this);
        windowManager.addUnregisterHandler(this);
        panel = new HTMLPanel("<div></div>");
        initWidget(panel);
   }

    @Override
    public void onRegister(RegisterEvent<Widget> event) {
        if (event.getItem() instanceof WindowInterface) {
            WindowBase wb = (WindowBase)event.getItem();
            buildWindowConfigList();
            renderView();
        }

    }

    @Override
    public void onUnregister(UnregisterEvent<Widget> event) {
        if (event.getItem() instanceof WindowInterface) {
            buildWindowConfigList();
            renderView();
        }
    }

    @Override
    public Element getDesktopContainer() {
        return Document.get().getElementById("desktopContainer");
    }


    @Override
    public void setPresenter(final DesktopView.Presenter presenter) {
        this.presenter = presenter;
        renderView();
    }

    protected void renderView() {
        Scheduler.get().scheduleFinally(()-> {
            ReactDesktop.DesktopProps props = new ReactDesktop.DesktopProps();
            props.presenter = DesktopViewImpl.this.presenter;
            props.messageServiceFacade = DesktopViewImpl.this.messageServiceFacade;
            Splittable[] sp = new Splittable[windowConfigMap.size()];
            Iterator it = windowConfigMap.keySet().iterator();
            int count = 0;
            while (it.hasNext()) {
                sp[count++] = (Splittable)it.next();
            }
            props.windowConfigList = sp;
            CyVerseReactComponents.render(ReactDesktop.desktopProps, props, panel.getElement());
            presenter.setDesktopContainer(getDesktopContainer());
        });
    }


    @Override
    public void onTaskButtonClick(Splittable windowConfig) {
        WindowBase win = getWindowFromConfig(windowConfig);
        if (win != null) {
            if (win.isMinimized()) {
                win.show();
            } else {
                win.toFront();
            }
        }
    }

    private void buildWindowConfigList() {
        List<Widget> widgets = windowManager.getWindows();
        windowConfigMap.clear();
        if (widgets.size() == 0) {
            return;
        }
        for (Widget w : widgets) {
            if (w instanceof WindowInterface) {
                WindowBase cyverseWin = (WindowBase)w;
                windowConfigMap.put(getConfigAsSplittable(cyverseWin), cyverseWin);
            }
        }
    }

    private Splittable getConfigAsSplittable(WindowBase win) {
        WindowConfig config = win.getWindowConfig();
        config.setMinimized(win.isMinimized());
        config.setWindowTitle(win.getHeading().asString());
        Splittable sp = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(config));
        return sp;
    }

    private WindowBase getWindowFromConfig(Splittable config) {
        Iterator it = windowConfigMap.keySet().iterator();
        while (it.hasNext()) {
            Splittable sp = (Splittable)it.next();
            if (sp.get("tag") != null && sp.get("tag").asString().equals(config.get("tag").asString())) {
                return windowConfigMap.get(sp);
            }
        }
        return null;
    }


    void onFeedbackBtnSelect() {
        deFeedbackDialogProvider.get(new AsyncCallback<DEFeedbackDialog>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(final DEFeedbackDialog feedbackDialog) {
                feedbackDialog.show();
                feedbackDialog.getButton(PredefinedButton.OK).addSelectHandler(event -> {
                    if (feedbackDialog.validate()) {
                        presenter.submitUserFeedback(feedbackDialog.toJson(), feedbackDialog);
                    } else {
                        AlertMessageBox amb =
                                new AlertMessageBox(appearance.feedbackAlertValidationWarning(),
                                                    appearance.completeRequiredFieldsError());
                        amb.setModal(true);
                        amb.show();
                    }
                });

                feedbackDialog.getButton(PredefinedButton.CANCEL)
                              .addSelectHandler(event -> feedbackDialog.hide());
            }
        });
   }
}

