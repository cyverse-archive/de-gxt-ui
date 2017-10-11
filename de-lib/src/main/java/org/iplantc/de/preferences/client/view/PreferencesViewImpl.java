package org.iplantc.de.preferences.client.view;

import org.iplantc.de.apps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.de.apps.widgets.client.view.editors.validation.AnalysisOutputValidator;
import org.iplantc.de.client.KeyBoardShortcutConstants;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.UserSettings;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.diskResource.client.gin.factory.DiskResourceSelectorFieldFactory;
import org.iplantc.de.diskResource.client.views.widgets.FolderSelectorField;
import org.iplantc.de.preferences.client.PreferencesView;
import org.iplantc.de.preferences.client.events.PrefDlgRetryUserSessionClicked;
import org.iplantc.de.preferences.client.events.ResetHpcTokenClicked;
import org.iplantc.de.preferences.shared.Preferences;

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

import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sriram, jstroot
 */
public class PreferencesViewImpl extends Composite implements PreferencesView,
                                                              Editor<UserSettings> {


    @UiTemplate("PreferencesView.ui.xml")
    interface MyUiBinder extends UiBinder<VerticalLayoutContainer, PreferencesViewImpl> { }

    interface EditorDriver extends SimpleBeanEditorDriver<UserSettings, PreferencesViewImpl>{}

    @UiField @Ignore VerticalLayoutContainer container;
    @UiField @Ignore VerticalLayoutContainer kbContainer;
    @UiField @Ignore VerticalLayoutContainer prefContainer;

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

    private final KeyBoardShortcutConstants KB_CONSTANTS;
    private final Map<TextField, String> kbMap;

    private final EditorDriver editorDriver = GWT.create(EditorDriver.class);
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    private UserSettings flushedValue;
    private UserSettings usValue;

    @Inject UserSettings us;

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
        initWidget(uiBinder.createAndBindUi(this));

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


    @Ignore
    public UserSettings getValue() {
        return flushedValue;
    }

    @Override
    public void initAndShow(final UserSettings userSettings) {
        this.usValue = userSettings;
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
                        }
                    }
                }
            }
        }
        return valid;
    }

    @Override
    public void flush() {
        UserSettings value = editorDriver.flush();
        if (!editorDriver.hasErrors() && isValid()) {
            this.flushedValue = value;
        } else {
            IplantAnnouncer.getInstance()
                           .schedule(new ErrorAnnouncementConfig(appearance.completeRequiredFieldsError()));
        }
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
}
