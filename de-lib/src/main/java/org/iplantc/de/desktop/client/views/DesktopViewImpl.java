package org.iplantc.de.desktop.client.views;

import org.iplantc.de.client.models.UserSettings;
import org.iplantc.de.client.models.notifications.NotificationMessage;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.widgets.IPlantAnchor;
import org.iplantc.de.desktop.client.DesktopView;
import org.iplantc.de.desktop.client.views.widgets.DEFeedbackDialog;
import org.iplantc.de.desktop.client.views.widgets.DesktopIconButton;
import org.iplantc.de.desktop.client.views.widgets.TaskBar;
import org.iplantc.de.desktop.client.views.widgets.TaskButton;
import org.iplantc.de.desktop.client.views.widgets.UnseenNotificationsView;
import org.iplantc.de.desktop.client.views.windows.WindowInterface;
import org.iplantc.de.desktop.shared.DeModule;
import org.iplantc.de.intercom.client.IntercomFacade;
import org.iplantc.de.intercom.client.TrackingEventType;
import org.iplantc.de.resources.client.messages.IplantNewUserTourStrings;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.common.base.Preconditions;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.WindowManager;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.button.IconButton;
import com.sencha.gxt.widget.core.client.event.RegisterEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.ShowContextMenuEvent;
import com.sencha.gxt.widget.core.client.event.UnregisterEvent;

/**
 * Created by jstroot on 7/6/14.
 * @author jstroot
 */
public class DesktopViewImpl implements DesktopView, UnregisterEvent.UnregisterHandler<Widget>, RegisterEvent.RegisterHandler<Widget> {

    interface DesktopViewImplUiBinder extends UiBinder<Widget, DesktopViewImpl> { }

    @UiField IconButton analysisWinBtn;
    @UiField IconButton appsWinBtn;
    @UiField IconButton dataWinBtn;
    @UiField DesktopIconButton helpBtn;
    @UiField IconButton notificationsBtn;
    @UiField TaskBar taskBar;
    @UiField DesktopIconButton userSettingsBtn;
    @UiField IPlantAnchor preferencesLink;
    @UiField IPlantAnchor collaboratorsLink;
    @UiField IPlantAnchor systemMsgsLink;
    @UiField IPlantAnchor documentationLink;
    @UiField IPlantAnchor introBtn;
    @UiField IPlantAnchor aboutBtn;
    @UiField IPlantAnchor logoutBtn;
    @UiField(provided = true) UnseenNotificationsView notificationsListView;
    @UiField DivElement desktopContainer;
    @UiField DesktopAppearance appearance;
    @UiField IPlantAnchor faqLink;
    @UiField IPlantAnchor forumsLink;
    @UiField IPlantAnchor feedbackLink;

    @Inject AsyncProviderWrapper<DEFeedbackDialog> deFeedbackDialogProvider;
    @Inject UserSettings userSettings;

    private static DesktopViewImplUiBinder ourUiBinder = GWT.create(DesktopViewImplUiBinder.class);
    private final Widget widget;
    private final SpanElement notificationCountElement;
    private final WindowManager windowManager;
    int unseenNotificationCount;
    private DesktopView.Presenter presenter;


    @Inject
    DesktopViewImpl(final IplantNewUserTourStrings tourStrings,
                    final WindowManager windowManager) {
        this.windowManager = windowManager;
        notificationsListView = new UnseenNotificationsView();
        widget = ourUiBinder.createAndBindUi(this);
        notificationCountElement = Document.get().createSpanElement();
        notificationCountElement.addClassName(appearance.styles().notificationCount());
        notificationCountElement.setAttribute("hidden", "");
        notificationsBtn.getElement().appendChild(notificationCountElement);

        windowManager.addRegisterHandler(this);
        windowManager.addUnregisterHandler(this);
        initIntroAttributes(tourStrings);
        feedbackLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                  onFeedbackBtnSelect();
            }
        });
        forumsLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                 onForumsSelect();
            }
        });
        faqLink.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onFaqSelect();
            }
        });
    }

    @UiHandler("notificationsBtn")
    void onNotificationMenuClicked(ShowContextMenuEvent event){
        if(unseenNotificationCount < 10){
            presenter.doMarkAllSeen(false);
        }
    }

    public void hideNotificationMenu() {
        ((DesktopIconButton)notificationsBtn).hideMenu();
    }

    @Override
    public void setNotificationConnection(boolean connected) {
        notificationsListView.setNotificationConnection(connected);
    }

    @Override
    public void onRegister(RegisterEvent<Widget> event) {
        final Widget eventItem = event.getItem();

        if(eventItem instanceof WindowInterface) {
            com.sencha.gxt.widget.core.client.Window iplantWindow = (com.sencha.gxt.widget.core.client.Window) eventItem;
            // If it already exists, mark button active
            for(TaskButton btn : taskBar.getButtons()){
                if(btn.getWindow() == iplantWindow){
                    // If it already exists, do not re-add
                    return;
                }
            }

            // If it is new, add task button and mark active
            taskBar.addTaskButton(iplantWindow);
        }
    }

    @Override
    public void onUnregister(UnregisterEvent<Widget> event) {

        final Widget eventItem = event.getItem();
        if(eventItem instanceof WindowInterface) {
            WindowInterface iplantWindow = (WindowInterface) eventItem;
            if (iplantWindow.isMinimized()) {
                return;
            }
            TaskButton taskButton = null;
            for(TaskButton btn : taskBar.getButtons()){
                if(btn.getWindow() == iplantWindow){
                   taskButton = btn;
                    break;
                }
            }

            Preconditions.checkNotNull(taskButton, "TaskButton should not be null");
            // remove corresponding task button
            taskBar.removeTaskButton(taskButton);
        }
    }

    private void initIntroAttributes(IplantNewUserTourStrings tourStrings) {
        // FIXME Need to move intro to themes
        // Window Btns
        dataWinBtn.getElement().setAttribute("data-intro", tourStrings.introDataWindow());
        dataWinBtn.getElement().setAttribute("data-position", "right");
        dataWinBtn.getElement().setAttribute("data-step", "1");
        appsWinBtn.getElement().setAttribute("data-intro", tourStrings.introAppsWindow());
        appsWinBtn.getElement().setAttribute("data-position", "right");
        appsWinBtn.getElement().setAttribute("data-step", "2");
        analysisWinBtn.getElement().setAttribute("data-intro", tourStrings.introAnalysesWindow());
        analysisWinBtn.getElement().setAttribute("data-position", "right");
        analysisWinBtn.getElement().setAttribute("data-step", "3");

        // User Menu Btns
        notificationsBtn.getElement().setAttribute("data-intro", tourStrings.introNotifications());
        notificationsBtn.getElement().setAttribute("data-position", "left");
        notificationsBtn.getElement().setAttribute("data-step", "4");

        userSettingsBtn.getElement().setAttribute("data-intro", tourStrings.introSettings());
        userSettingsBtn.getElement().setAttribute("data-position", "left");
        userSettingsBtn.getElement().setAttribute("data-step", "5");

        helpBtn.getElement().setAttribute("data-intro", tourStrings.introHelp());
        helpBtn.getElement().setAttribute("data-position", "left");
        helpBtn.getElement().setAttribute("data-step", "6");
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public void ensureDebugId(String baseID) {
        notificationsBtn.ensureDebugId(baseID + DeModule.Ids.NOTIFICATION_BUTTON);
        userSettingsBtn.ensureDebugId(baseID + DeModule.Ids.USER_PREF_MENU);
        helpBtn.ensureDebugId(baseID + DeModule.Ids.HELP_MENU);
        dataWinBtn.ensureDebugId(baseID + DeModule.Ids.DATA_BTN);
        appsWinBtn.ensureDebugId(baseID + DeModule.Ids.APPS_BTN);
        analysisWinBtn.ensureDebugId(baseID + DeModule.Ids.ANALYSES_BTN);
        taskBar.ensureDebugId(baseID + DeModule.Ids.TASK_BAR);


        // User Settings Menu Items
        preferencesLink.ensureDebugId(baseID + DeModule.Ids.USER_PREF_MENU + DeModule.Ids.PREFERENCES_LINK);
        collaboratorsLink.ensureDebugId(baseID + DeModule.Ids.USER_PREF_MENU + DeModule.Ids.COLLABORATORS_LINK);
        systemMsgsLink.ensureDebugId(baseID + DeModule.Ids.USER_PREF_MENU + DeModule.Ids.SYS_MSGS_LINK);
        documentationLink.ensureDebugId(baseID + DeModule.Ids.USER_PREF_MENU + DeModule.Ids.USER_MANUAL_LINK);
        introBtn.ensureDebugId(baseID + DeModule.Ids.USER_PREF_MENU + DeModule.Ids.INTRO_LINK);
        aboutBtn.ensureDebugId(baseID + DeModule.Ids.USER_PREF_MENU + DeModule.Ids.ABOUT_LINK);
        logoutBtn.ensureDebugId(baseID + DeModule.Ids.USER_PREF_MENU + DeModule.Ids.LOGOUT_LINK);

        //Help Menu Items
        faqLink.ensureDebugId(baseID + DeModule.Ids.HELP_MENU + DeModule.Ids.FAQS_LINK);
        forumsLink.ensureDebugId(baseID + DeModule.Ids.HELP_MENU + DeModule.Ids.FORUMS_LINK);
        feedbackLink.ensureDebugId(baseID + DeModule.Ids.HELP_MENU + DeModule.Ids.FEEDBACK_LINK);

    }

    @Override
    public Element getDesktopContainer() {
        return desktopContainer;
    }

    @Override
    public ListStore<NotificationMessage> getNotificationStore() {
        return notificationsListView.getStore();
    }

    @Override
    public int getUnseenNotificationCount() {
        return unseenNotificationCount;
    }

    @Override
    public void setPresenter(final DesktopView.Presenter presenter) {
        this.presenter = presenter;
        notificationsListView.setPresenter(presenter);
    }

    @Override
    public void setUnseenNotificationCount(int count) {
        this.unseenNotificationCount = count;
        if(count > 0){
            notificationCountElement.setInnerText(Integer.toString(count));
            notificationCountElement.removeAttribute("hidden");
            Window.setTitle(appearance.rootApplicationTitle(count));
        }else {
            notificationCountElement.setAttribute("hidden", "");
            notificationCountElement.setInnerText(null);
            Window.setTitle(appearance.rootApplicationTitle());
        }
        notificationsListView.onUnseenCountUpdated(count);
    }

    @Override
    public void setUnseenSystemMessageCount(int count) {
        String labelText = appearance.systemMessagesLabel();
        if(count > 0) {
            labelText += " (" + count + ")";
        }
        systemMsgsLink.setText(labelText);
    }

    @UiHandler("notificationsListView")
    void onUnseenNotificationSelected(SelectionEvent<NotificationMessage> event){
        presenter.onNotificationSelected(event.getSelectedItem());
    }

    @UiHandler("analysisWinBtn")
    void onAnalysesWinBtnSelect(SelectEvent event) {
        presenter.onAnalysesWinBtnSelect();
    }

    @UiHandler("appsWinBtn")
    void onAppsWinBtnSelect(SelectEvent event) {
        presenter.onAppsWinBtnSelect();
    }

    @UiHandler("dataWinBtn")
    void onDataWinBtnSelect(SelectEvent event) {
        presenter.onDataWinBtnSelect();
    }


    void onFeedbackBtnSelect() {
        helpBtn.hideMenu();
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


    void onFaqSelect() {
        helpBtn.hideMenu();
        presenter.onFaqSelect();
    }

    void onForumsSelect() {
        helpBtn.hideMenu();
        presenter.onForumsBtnSelect();
    }

    @UiHandler({ "preferencesLink", "collaboratorsLink", "systemMsgsLink",
                 "documentationLink", "introBtn", /*"contactSupportBtn",*/ "aboutBtn", "logoutBtn"})
    void onAnyUserSettingsItemClick(ClickEvent event){
        userSettingsBtn.hideMenu();
    }

    @UiHandler("preferencesLink")
    void onPreferencesClick(ClickEvent event){
        presenter.onPreferencesClick();
    }

    @UiHandler("collaboratorsLink")
    void onCollaboratorsClick(ClickEvent event){
        presenter.onCollaboratorsClick();
    }

    @UiHandler("systemMsgsLink")
    void onSystemMessagesClick(ClickEvent event){
        presenter.onSystemMessagesClick();
    }

    @UiHandler("documentationLink")
    void onDocumentationClick(ClickEvent event){
        IntercomFacade.trackEvent(TrackingEventType.HELP_BUTTON_CLICKED,null);
        presenter.onDocumentationClick();
    }

    @UiHandler("introBtn")
    void onIntroClick(ClickEvent event){
        presenter.onIntroClick();
    }

    @UiHandler("aboutBtn")
    void onAboutClick(ClickEvent event){
        presenter.onAboutClick();
    }

    @UiHandler("logoutBtn")
    void onLogoutClick(ClickEvent event){
        presenter.doLogout(false);
    }

}
