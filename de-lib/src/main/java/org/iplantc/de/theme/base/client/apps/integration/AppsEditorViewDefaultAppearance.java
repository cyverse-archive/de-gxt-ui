package org.iplantc.de.theme.base.client.apps.integration;

import org.iplantc.de.apps.integration.client.view.AppsEditorView;
import org.iplantc.de.apps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.resources.client.messages.IplantErrorStrings;
import org.iplantc.de.resources.client.uiapps.integration.AppIntegrationMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;

import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.IconButton;

/**
 * @author aramsey
 */
public class AppsEditorViewDefaultAppearance implements AppsEditorView.AppsEditorViewAppearance {

    private IplantDisplayStrings iplantDisplayStrings;
    private IplantErrorStrings errorStrings;
    private AppIntegrationMessages appIntegrationMessages;
    private AppTemplateWizardAppearance.Resources style;

    public AppsEditorViewDefaultAppearance() {
        this((IplantDisplayStrings)GWT.create(IplantDisplayStrings.class),
             (IplantErrorStrings)GWT.create(IplantErrorStrings.class),
             (AppIntegrationMessages)GWT.create(AppIntegrationMessages.class),
             (AppTemplateWizardAppearance.Resources)GWT.create(AppTemplateWizardAppearance.Resources.class));
    }

    public AppsEditorViewDefaultAppearance(IplantDisplayStrings iplantDisplayStrings,
                                           IplantErrorStrings errorStrings,
                                           AppIntegrationMessages appIntegrationMessages,
                                           AppTemplateWizardAppearance.Resources style) {

        this.iplantDisplayStrings = iplantDisplayStrings;
        this.errorStrings = errorStrings;
        this.appIntegrationMessages = appIntegrationMessages;
        this.style = style;

        this.style.css().ensureInjected();
    }

    @Override
    public SafeHtml cannotDeleteLastArgumentGroup() {
        return errorStrings.cannotDeleteLastArgumentGroup();
    }

    @Override
    public String appHeaderSelect() {
        return style.css().appHeaderSelect();
    }

    @Override
    public IconButton getArgListDeleteButton() {
        IconButton argDeleteBtn =
                new IconButton(new IconButton.IconConfig(style.css().delete(), style.css().deleteHover()));
        argDeleteBtn.addStyleName(style.css().deleteBtn());
        return argDeleteBtn;
    }

    @Override
    public String argumentSelect() {
        return style.css().argumentSelect();
    }

    @Override
    public String warning() {
        return iplantDisplayStrings.warning();
    }

    @Override
    public String appContainsErrorsPromptToContinue() {
        return errorStrings.appContainsErrorsPromptToContinue();
    }

    @Override
    public String save() {
        return iplantDisplayStrings.save();
    }

    @Override
    public String unsavedChanges() {
        return iplantDisplayStrings.unsavedChanges();
    }

    @Override
    public String done() {
        return iplantDisplayStrings.done();
    }

    @Override
    public String appContainsErrorsUnableToSave() {
        return errorStrings.appContainsErrorsUnableToSave();
    }

    @Override
    public String unableToSave() {
        return errorStrings.unableToSave();
    }

    @Override
    public ImageResource questionIcon() {
        return MessageBox.ICONS.question();
    }

    @Override
    public ImageResource errorIcon() {
        return MessageBox.ICONS.error();
    }

    @Override
    public String commandLineOrder() {
        return appIntegrationMessages.commandLineOrder();
    }

    @Override
    public SafeHtml argumentLabel() {
        return appIntegrationMessages.argumentLabel();
    }

    @Override
    public SafeHtml orderLabel() {
        return appIntegrationMessages.orderLabel();
    }

    @Override
    public String previewJSON() {
        return appIntegrationMessages.previewJSON();
    }

    @Override
    public String saveSuccessful() {
        return appIntegrationMessages.saveSuccessful();
    }
}
