package org.iplantc.de.desktop.client.views;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.WindowManager;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.event.RegisterEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.UnregisterEvent;
import org.iplantc.de.client.models.UserSettings;
import org.iplantc.de.client.services.MessageServiceFacade;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.util.CyVerseReactComponents;
import org.iplantc.de.desktop.client.DesktopView;
import org.iplantc.de.desktop.client.views.widgets.DEFeedbackDialog;
import org.iplantc.de.resources.client.messages.IplantNewUserTourStrings;
import org.iplantc.de.shared.AsyncProviderWrapper;

/**
 * Created by jstroot on 7/6/14.
 * @author jstroot
 */
public class DesktopViewImpl extends Composite implements DesktopView, UnregisterEvent.UnregisterHandler<Widget>, RegisterEvent.RegisterHandler<Widget> {

    @Inject AsyncProviderWrapper<DEFeedbackDialog> deFeedbackDialogProvider;
    @Inject UserSettings userSettings;
    @Inject
    MessageServiceFacade messageServiceFacade;



    private final WindowManager windowManager;
    DesktopView.Presenter presenter;
    HTMLPanel panel;
    DesktopView.DesktopAppearance appearance;

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
    }

    @Override
    public void onUnregister(UnregisterEvent<Widget> event) {

    }

    @Override
    public Element getDesktopContainer() {
           return  panel.getElement();
    }


    @Override
    public void setPresenter(final DesktopView.Presenter presenter) {
        this.presenter = presenter;
        Scheduler.get().scheduleFinally(()-> {
          ReactDesktop.DesktopProps props = new ReactDesktop.DesktopProps();
          props.presenter = DesktopViewImpl.this.presenter;
          props.messageServiceFacade = DesktopViewImpl.this.messageServiceFacade;
          CyVerseReactComponents.render(ReactDesktop.desktopProps, props, panel.getElement());
          
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
                feedbackDialog.getButton(PredefinedButton.OK).addSelectHandler(new SelectEvent.SelectHandler() {
                    @Override
                    public void onSelect(SelectEvent event) {
                        if(feedbackDialog.validate()){
                            presenter.submitUserFeedback(feedbackDialog.toJson(), feedbackDialog);
                        } else {
                            AlertMessageBox amb = new AlertMessageBox(appearance.feedbackAlertValidationWarning(),
                                                                      appearance.completeRequiredFieldsError());
                            amb.setModal(true);
                            amb.show();
                        }
                    }
                });

                feedbackDialog.getButton(PredefinedButton.CANCEL).addSelectHandler(new SelectEvent.SelectHandler() {
                    @Override
                    public void onSelect(SelectEvent event) {
                        feedbackDialog.hide();
                    }
                });
            }
        });
   }
}

