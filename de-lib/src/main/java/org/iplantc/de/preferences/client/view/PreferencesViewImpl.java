package org.iplantc.de.preferences.client.view;

import org.iplantc.de.apps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.de.apps.widgets.client.view.editors.validation.AnalysisOutputValidator;
import org.iplantc.de.client.KeyBoardShortcutConstants;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.UserSettings;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.models.webhooks.Webhook;
import org.iplantc.de.client.models.webhooks.WebhookType;
import org.iplantc.de.client.models.webhooks.WebhookTypeList;
import org.iplantc.de.client.models.webhooks.WebhooksAutoBeanFactory;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.validators.UrlValidator;
import org.iplantc.de.diskResource.client.gin.factory.DiskResourceSelectorFieldFactory;
import org.iplantc.de.diskResource.client.views.widgets.FolderSelectorField;
import org.iplantc.de.preferences.client.PreferencesView;
import org.iplantc.de.preferences.client.events.PrefDlgRetryUserSessionClicked;
import org.iplantc.de.preferences.client.events.ResetHpcTokenClicked;
import org.iplantc.de.preferences.client.events.TestWebhookClicked;
import org.iplantc.de.preferences.shared.Preferences;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.HTML;
import com.google.inject.Inject;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldSet;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sriram, jstroot
 */
public class PreferencesViewImpl extends Composite implements PreferencesView,
                                                              Editor<UserSettings> {


    @UiTemplate("PreferencesView.ui.xml")
    interface MyUiBinder extends UiBinder<TabPanel, PreferencesViewImpl> { }

    interface EditorDriver extends SimpleBeanEditorDriver<UserSettings, PreferencesViewImpl>{}

    @UiField TextField analysesShortCut;
    @UiField TextField appsShortCut;
    @UiField CheckBox rememberLastPath;
    @UiField CheckBox enableAnalysisEmailNotification;
    @UiField CheckBox enableImportEmailNotification;
    @UiField CheckBox enableWaitTimeMessage;
    @UiField CheckBox saveSession;
    @UiField @Ignore HTML savedSessionFailed;
    @UiField @Ignore TextButton retrySession;
    @UiField TextField closeShortCut;
    @UiField TextField dataShortCut;
    @UiField(provided = true) FolderSelectorField defaultOutputFolder;
    @UiField TextField notifyShortCut;
    @UiField(provided = true) PreferencesViewAppearance appearance;

    @UiField(provided = true)
    @Ignore
    HTML resetHpcfield;

    @UiField
    @Ignore
    TextButton hpcResetBtn;

    @UiField(provided = true)
    @Ignore
    HTML webhooksfield;

    @UiField
    @Ignore
    TextButton testBtn;

    @UiField
    @Ignore
    TextField hookUrl;

    @UiField
    @Ignore
    TextButton hookDelBtn;

    @UiField(provided = true)
    @Ignore
    ComboBox<WebhookType> typeCombo;

    @UiField
    @Ignore
    CheckBox dataNotification, appsNotification, analysesNotification, toolsNotification,
            permIdNotification, teamNotification;

    @Ignore
    @UiField
    FieldSet hookFieldSet;

    private final KeyBoardShortcutConstants KB_CONSTANTS;
    private final Map<TextField, String> kbMap;

    private final EditorDriver editorDriver = GWT.create(EditorDriver.class);
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    private UserSettings flushedValue;
    private UserSettings usValue;

    @Inject UserSettings us;
    @Inject
    WebhooksAutoBeanFactory wabFactory;
    @Inject
    IplantAnnouncer announcer;

    @Inject
    PreferencesViewImpl(final DiskResourceSelectorFieldFactory folderSelectorFieldFactory,
                        final PreferencesViewAppearance appearance,
                        final KeyBoardShortcutConstants kbConstants,
                        AppTemplateWizardAppearance wizardAppearance,
                        final UserInfo userInfo) {
        this.appearance = appearance;
        this.defaultOutputFolder = folderSelectorFieldFactory.defaultFolderSelector();
        this.defaultOutputFolder.hideResetButton();
        this.KB_CONSTANTS = kbConstants;
        this.resetHpcfield = new HTML(appearance.resetHpcPrompt());
        this.webhooksfield = new HTML(appearance.webhooksPrompt());

        ListStore<WebhookType> typeListStore = new ListStore<>(item -> item.getId());
        typeCombo = new ComboBox<>(typeListStore, item -> item.getType());
        typeCombo.setEditable(false);

        initWidget(uiBinder.createAndBindUi(this));

        hookUrl.addValidator(new UrlValidator());
        hookUrl.setValidateOnBlur(true);
        
        kbMap = new HashMap<>();
        appsShortCut.addValidator(new MaxLengthValidator(1));
        dataShortCut.addValidator(new MaxLengthValidator(1));
        analysesShortCut.addValidator(new MaxLengthValidator(1));
        notifyShortCut.addValidator(new MaxLengthValidator(1));
        closeShortCut.addValidator(new MaxLengthValidator(1));
        defaultOutputFolder.addValidator(new AnalysisOutputValidator(wizardAppearance));

        defaultOutputFolder.addValueChangeHandler(new ValueChangeHandler<Folder>() {

            @Override
            public void onValueChange(ValueChangeEvent<Folder> event) {
                defaultOutputFolder.validate(false);
            }
        });
        populateKbMap();
        savedSessionFailed.setHTML(appearance.sessionConnectionFailed());

        editorDriver.initialize(this);

        if (userInfo.hasAgaveRedirect()) {
            hpcResetBtn.disable();  //User has not yet authenticated to HPC yet. So cannot reset
        } else {
            hpcResetBtn.enable();
        }
    }

    @Override
    public HandlerRegistration addPrefDlgRetryUserSessionClickedHandlers(PrefDlgRetryUserSessionClicked.PrefDlgRetryUserSessionClickedHandler handler) {
        return addHandler(handler, PrefDlgRetryUserSessionClicked.TYPE);
    }

    @Override
    public HandlerRegistration addResetHpcTokenClickedHandlers(ResetHpcTokenClicked.ResetHpcTokenClickedHandler handler) {
        return addHandler(handler, ResetHpcTokenClicked.TYPE);
    }

    @Override
    public HandlerRegistration addTestWebhookClickedHandlers(TestWebhookClicked.TestWebhookClickedHandler handler) {
        return addHandler(handler, TestWebhookClicked.TYPE);
    }


    @Ignore
    public UserSettings getValue() {
        return flushedValue;
    }

    @Override
    public void initAndShow(final UserSettings userSettings, WebhookTypeList typeList) {
        this.usValue = userSettings;
        if (typeList!=null) {
            typeCombo.getStore().addAll(typeList.getTypes());
        }
        if (userSettings.getWebhooks() != null && userSettings.getWebhooks().size() > 0) {
            Webhook webhook = userSettings.getWebhooks().get(0);
            List<String> topics = webhook.getTopics();
            this.hookUrl.setValue(webhook.getUrl());
            this.typeCombo.setValue(webhook.getType());
            List<IsField<?>> fields = FormPanelHelper.getFields(hookFieldSet);
            for (IsField f : fields) {
                if (f instanceof CheckBox && (topics.contains(((CheckBox)f).getName()))) {
                    ((CheckBox)f).setValue(true);
                }
            }
        }
        editorDriver.edit(userSettings);
        if (!userSettings.hasUserSessionConnection()) {
            userSessionFail();
        }
        show();
        ensureDebugId(Preferences.Ids.PREFERENCES_DLG);
    }

    public boolean isValid() {
        boolean valid = defaultOutputFolder.validate(false) && appsShortCut.isValid() && dataShortCut.isValid()
                            && analysesShortCut.isValid() && notifyShortCut.isValid() && closeShortCut.isValid();

        if (valid) {
            populateKbMap();
            resetKbFieldErrors();
            for (TextField ks : kbMap.keySet()) {
                for (TextField sc : kbMap.keySet()) {
                    if (ks != sc) {
                        if (kbMap.get(ks).equals(kbMap.get(sc))) {
                            ks.markInvalid(appearance.duplicateShortCutKey(kbMap.get(ks)));
                            sc.markInvalid(appearance.duplicateShortCutKey(kbMap.get(ks)));
                            valid = false;
                            announcer.schedule(new ErrorAnnouncementConfig(appearance.completeRequiredFieldsError()));
                        }
                    }
                }
            }
        }
        if (!Strings.isNullOrEmpty(hookUrl.getValue())) {
            List<String> topics = getSelectedTopics();
            if (topics == null || topics.size() == 0) {
                announcer.schedule(new ErrorAnnouncementConfig(appearance.mustSelectATopic()));
                valid = false;
            }
        }

        if (editorDriver.hasErrors()) {
            announcer.schedule(new ErrorAnnouncementConfig(appearance.completeRequiredFieldsError()));
            valid = false;
        }
        return valid;
    }

    @Override
    public void flush() {
        UserSettings value = editorDriver.flush();
        List<String> topics = getSelectedTopics();
        if (!Strings.isNullOrEmpty(hookUrl.getValue())) {
            if (topics == null || topics.size() == 0) {
                return;
            }
            Webhook hook = wabFactory.getWebhook().as();
            hook.setUrl(hookUrl.getValue());
            hook.setType(typeCombo.getValue());
            hook.setTopics(topics);
            value.setWebhooks(Arrays.asList(hook));
        } else {
            value.setWebhooks(new ArrayList<>());
        }

        if (!editorDriver.hasErrors() && isValid()) {
            this.flushedValue = value;
        }
    }

    private List<String> getSelectedTopics() {
        List<IsField<?>> fields = FormPanelHelper.getFields(hookFieldSet);
        return fields.stream()
                     .filter(f -> f instanceof CheckBox && ((CheckBox)f).getValue())
                     .map(f -> (((CheckBox)f).getName()))
                     .collect(Collectors.toList());
    }

    @Override
    public void saveUserSettings() {

    }

    @Override
    public void setDefaultValues() {
        enableAnalysisEmailNotification.setValue(true);
        enableImportEmailNotification.setValue(true);
        enableWaitTimeMessage.setValue(true);
        rememberLastPath.setValue(true);
        saveSession.setValue(true);
        appsShortCut.setValue(KB_CONSTANTS.appsKeyShortCut());
        dataShortCut.setValue(KB_CONSTANTS.dataKeyShortCut());
        analysesShortCut.setValue(KB_CONSTANTS.analysisKeyShortCut());
        notifyShortCut.setValue(KB_CONSTANTS.notifyKeyShortCut());
        closeShortCut.setValue(KB_CONSTANTS.closeKeyShortCut());
        defaultOutputFolder.setValue(us.getSystemDefaultOutputFolder());
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        enableAnalysisEmailNotification.ensureDebugId(baseID + Preferences.Ids.EMAIL_ANALYSIS_NOTIFICATION);
        enableImportEmailNotification.ensureDebugId(baseID + Preferences.Ids.EMAIL_IMPORT_NOTIFICATION);
        rememberLastPath.ensureDebugId(baseID + Preferences.Ids.REMEMBER_LAST_PATH);
        saveSession.ensureDebugId(baseID + Preferences.Ids.SAVE_SESSION);
        defaultOutputFolder.ensureDebugId(baseID + Preferences.Ids.DEFAULT_OUTPUT_FOLDER);
        defaultOutputFolder.setInputFieldId(baseID + Preferences.Ids.DEFAULT_OUTPUT_FOLDER + Preferences.Ids.DEFAULT_OUTPUT_FIELD);
        defaultOutputFolder.setBrowseButtonId(baseID + Preferences.Ids.DEFAULT_OUTPUT_FOLDER + Preferences.Ids.BROWSE_OUTPUT_FOLDER);

        appsShortCut.ensureDebugId(baseID + Preferences.Ids.APPS_SC);
        dataShortCut.ensureDebugId(baseID + Preferences.Ids.DATA_SC);
        analysesShortCut.ensureDebugId(baseID + Preferences.Ids.ANALYSES_SC);
        notifyShortCut.ensureDebugId(baseID + Preferences.Ids.NOTIFICATION_SC);
        closeShortCut.ensureDebugId(baseID + Preferences.Ids.CLOSE_SC);

        hpcResetBtn.ensureDebugId(baseID + Preferences.Ids.RESET_HPC);

        dataNotification.ensureDebugId(baseID + Preferences.Ids.DATA_NOTIFICATION);
        appsNotification.ensureDebugId(baseID + Preferences.Ids.APPS_NOTIFICATION);
        analysesNotification.ensureDebugId(baseID + Preferences.Ids.ANALYSES_NOTIFICATION);
        toolsNotification.ensureDebugId(baseID + Preferences.Ids.TOOLS_NOTIFICATION);
        permIdNotification.ensureDebugId(baseID + Preferences.Ids.PERMS_NOTIFICATION);
        teamNotification.ensureDebugId(baseID + Preferences.Ids.TEAM_NOTIFICATION);

    }

    @UiHandler({"appsShortCut", "dataShortCut", "analysesShortCut", "notifyShortCut", "closeShortCut"})
    void onKeyPress(KeyPressEvent event) {
        TextField fld = (TextField) event.getSource();
        int code = event.getNativeEvent().getCharCode();
        if ((code > 96 && code <= 122)) {
            fld.setValue((event.getCharCode() + "").toUpperCase());
            fld.setText((event.getCharCode() + "").toUpperCase());
            fld.setCursorPos(1);
            fld.focus();
        } else if ((code > 47 && code <= 57) || (code > 64 && code <= 90)) {
            fld.setValue(event.getCharCode() + "");
            fld.setText(event.getCharCode() + "");
            fld.setCursorPos(1);
            fld.focus();
        }
        if (code != 0) {
            event.preventDefault();
        }

    }

    @UiHandler("retrySession")
    void onRetrySessionClicked(SelectEvent event) {
        savedSessionFailed.setVisible(false);
        fireEvent(new PrefDlgRetryUserSessionClicked());
    }

    private void populateKbMap() {
        kbMap.put(appsShortCut, appsShortCut.getValue());
        kbMap.put(dataShortCut, dataShortCut.getValue());
        kbMap.put(analysesShortCut, analysesShortCut.getValue());
        kbMap.put(notifyShortCut, notifyShortCut.getValue());
        kbMap.put(closeShortCut, closeShortCut.getValue());
    }

    private void resetKbFieldErrors() {
        for (TextField ks : kbMap.keySet()) {
            ks.clearInvalid();
        }
    }

    @Override
    public void userSessionSuccess() {
        saveSession.setVisible(true);
        savedSessionFailed.setVisible(false);
        retrySession.setVisible(false);
    }

    @Override
    public void userSessionFail() {
        saveSession.setVisible(false);
        savedSessionFailed.setVisible(true);
        retrySession.setVisible(true);
    }

    @UiHandler("hpcResetBtn")
    void onResetHpcToken(SelectEvent event) {
        fireEvent(new ResetHpcTokenClicked());
    }

    @UiHandler("testBtn")
    void onTest(SelectEvent event) {
        if (!Strings.isNullOrEmpty(hookUrl.getValue())) {
            fireEvent(new TestWebhookClicked(hookUrl.getValue()));
        } else {
            hookUrl.markInvalid(appearance.validUrl());
        }

    }

    @UiHandler("hookDelBtn")
    public void onDeleteHook(SelectEvent event) {
        hookUrl.clear();
    }
}
