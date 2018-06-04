package org.iplantc.de.desktop.client.views;

import org.iplantc.de.client.models.UserSettings;
import org.iplantc.de.client.services.MessageServiceFacade;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.util.CyVerseReactComponents;
import org.iplantc.de.desktop.client.DesktopView;
import org.iplantc.de.desktop.client.views.widgets.DEFeedbackDialog;
import org.iplantc.de.desktop.client.views.windows.WindowBase;
import org.iplantc.de.resources.client.messages.IplantNewUserTourStrings;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.WindowManager;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by jstroot on 7/6/14.
 * @author jstroot
 */
public class DesktopViewImpl implements DesktopView {

    public static final String DESKTOP_CONTAINER = "desktopContainer";
    @Inject AsyncProviderWrapper<DEFeedbackDialog> deFeedbackDialogProvider;
    @Inject UserSettings userSettings;
    @Inject
    MessageServiceFacade messageServiceFacade;

    DesktopView.Presenter presenter;
    HTMLPanel panel;
    DesktopView.DesktopAppearance appearance;

    private final WindowManager windowManager;

    @Inject
    DesktopViewImpl(final IplantNewUserTourStrings tourStrings,
                    final WindowManager windowManager,
                    DesktopView.DesktopAppearance appearance) {
        this.windowManager = windowManager;
        this.appearance = appearance;
        panel = new HTMLPanel("<div></div>");
   }

    @Override
    public Element getDesktopContainer() {
        return Document.get().getElementById(DESKTOP_CONTAINER);
    }


    @Override
    public void setPresenter(final DesktopView.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void renderView(Map<Splittable, WindowBase> windowConfigMap) {
        Scheduler.get().scheduleFinally(()-> {
            Splittable[] sp = new Splittable[windowConfigMap.size()];
            Iterator it = windowConfigMap.keySet().iterator();
            int count = 0;
            while (it.hasNext()) {
                sp[count++] = (Splittable)it.next();
            }

            ReactDesktop.DesktopProps props = new ReactDesktop.DesktopProps();
            props.presenter = DesktopViewImpl.this.presenter;
            props.messageServiceFacade = DesktopViewImpl.this.messageServiceFacade;
            props.windowConfigList = sp;
            props.desktopContainerId = DESKTOP_CONTAINER;

            CyVerseReactComponents.render(ReactDesktop.desktopProps, props, panel.getElement());
            presenter.setDesktopContainer(getDesktopContainer());
        });
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

    @Override
    public Widget asWidget() {
        return panel;
    }
}

