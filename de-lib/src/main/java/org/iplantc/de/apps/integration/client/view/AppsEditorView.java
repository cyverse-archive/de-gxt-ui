package org.iplantc.de.apps.integration.client.view;

import org.iplantc.de.apps.integration.client.events.ArgumentOrderSelected;
import org.iplantc.de.apps.integration.client.events.DeleteArgumentGroupEvent;
import org.iplantc.de.apps.integration.client.events.DeleteArgumentGroupEvent.DeleteArgumentGroupEventHandler;
import org.iplantc.de.apps.integration.client.events.PreviewAppSelected;
import org.iplantc.de.apps.integration.client.events.PreviewJsonSelected;
import org.iplantc.de.apps.integration.client.events.SaveAppSelected;
import org.iplantc.de.apps.integration.client.events.UpdateCommandLinePreviewEvent;
import org.iplantc.de.apps.integration.client.events.UpdateCommandLinePreviewEvent.UpdateCommandLinePreviewEventHandler;
import org.iplantc.de.apps.integration.client.view.widgets.AppTemplatePropertyEditor;
import org.iplantc.de.apps.widgets.client.events.AppTemplateSelectedEvent.AppTemplateSelectedEventHandler;
import org.iplantc.de.apps.widgets.client.events.ArgumentGroupSelectedEvent.ArgumentGroupSelectedEventHandler;
import org.iplantc.de.apps.widgets.client.events.ArgumentSelectedEvent.ArgumentSelectedEventHandler;
import org.iplantc.de.apps.widgets.client.view.AppLaunchView.RenameWindowHeaderCommand;
import org.iplantc.de.apps.widgets.client.view.AppTemplateForm;
import org.iplantc.de.apps.widgets.client.view.HasLabelOnlyEditMode;
import org.iplantc.de.client.models.apps.integration.AppTemplate;
import org.iplantc.de.client.models.apps.integration.Argument;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import com.sencha.gxt.widget.core.client.button.IconButton;
import com.sencha.gxt.widget.core.client.event.BeforeHideEvent.BeforeHideHandler;

/**
 * @author jstroot
 *
 */
public interface AppsEditorView extends IsWidget,
                                        Editor<AppTemplate>,
                                        ArgumentSelectedEventHandler,
                                        ArgumentGroupSelectedEventHandler,
                                        AppTemplateSelectedEventHandler,
                                        DeleteArgumentGroupEvent.HasDeleteArgumentGroupEventHandlers,
                                        UpdateCommandLinePreviewEvent.HasUpdateCommandLinePreviewEventHandlers {
    
    interface EditorDriver extends SimpleBeanEditorDriver<AppTemplate, AppsEditorView> {
    }

    interface AppsEditorViewAppearance {

        SafeHtml cannotDeleteLastArgumentGroup();

        String appHeaderSelect();

        IconButton getArgListDeleteButton();

        String argumentSelect();

        String warning();

        String appContainsErrorsPromptToContinue();

        String save();

        String unsavedChanges();

        String done();

        String appContainsErrorsUnableToSave();

        String unableToSave();

        ImageResource questionIcon();

        ImageResource errorIcon();

        String commandLineOrder();

        SafeHtml argumentLabel();

        SafeHtml orderLabel();

        String previewJSON();

        String saveSuccessful();

        String contextualHelp();

        SafeHtml appCategorySection();

        String detailsPanelDefaultText();

        String detailsPanelHeader(String s);

        String paletteHeader();

        String cmdLinePreviewHeader();

        SafeHtml appCategoryFileInput();

        SafeHtml appCategoryLists();

        SafeHtml appCategoryTextInput();

        SafeHtml appCategoryOutput();

        SafeHtml appCategoryReferenceGenome();

        ImageResource inputSection();

        ImageResource inputFileMulti();

        ImageResource inputFile();

        ImageResource inputFolder();

        ImageResource generalInfoText();

        ImageResource inputTextSingle();

        ImageResource inputTextMulti();

        ImageResource inputCheckBox();

        ImageResource inputEnvVar();

        ImageResource inputNumberInteger();

        ImageResource inputNumberDouble();

        ImageResource inputSelectSingle();

        ImageResource inputSelectInteger();

        ImageResource inputSelectDouble();

        ImageResource inputSelectGrouped();

        ImageResource outputFileName();

        ImageResource outputFolderName();

        ImageResource outputMultiFile();

        ImageResource referenceGenome();

        ImageResource referenceSequence();

        ImageResource referenceAnnotation();

        String fileFolderCategoryTitle();

        String textNumericalInputCategoryTitle();

        String listsCategoryTitle();

        String outputCategoryTitle();

        String referenceGenomeCategoryTitle();

        String grab();

        String commandLineDialogWidth();

        String commandLineDialogHeight();

        int argumentNameColumnWidth();

        SafeHtml argumentNameColumnLabel();

        int argumentOrderColumnWidth();

        SafeHtml argumentOrderColumnLabel();

        int getAutoScrollDelay();

        int getAutoScrollRegionHeight();

        int getAutoScrollRepeatDelay();
    }
    
    public interface Presenter extends BeforeHideHandler,
                                       UpdateCommandLinePreviewEventHandler,
                                       HasLabelOnlyEditMode,
                                       DeleteArgumentGroupEventHandler,
                                       ArgumentOrderSelected.ArgumentOrderSelectedHandler,
                                       PreviewJsonSelected.PreviewJsonSelectedHandler,
                                       PreviewAppSelected.PreviewAppSelectedHandler,
                                       SaveAppSelected.SaveAppSelectedHandler {

        /**
         * This constant is used to key into an Autobean's tag map
         */
        String HANDLERS = "autobean_handlers_tag_key";
        void go(final HasOneWidget container, final AppTemplate appTemplate, final RenameWindowHeaderCommand renameCmd);

        boolean isEditorDirty();

        /**
         * Checks if the given argument should be ordered in order to be used by an App at launch.
         * 
         * @param arg
         * @return true if the property can be used at analysis execution but needs an order.
         */
        boolean orderingRequired(Argument arg);

        void setBeforeHideHandlerRegistration(HandlerRegistration hr);

        AppTemplate getAppTemplate();

        void go(final HasOneWidget container, final AppTemplate appTemplate);

        void go(final HasOneWidget container);

        void setViewDebugId(String baseID);

    }

    AppTemplate flush();

    /**
     * Exposed to satisfy Editor contract
     * 
     * @return
     */
    @Path("")
    AppTemplateForm getAppTemplateForm();

    /**
     * Exposed to satisfy Editor contract
     * 
     * @return
     */
    @Path("")
    AppTemplatePropertyEditor getAppTemplatePropertyEditor();

    EditorDriver getEditorDriver();

    AppEditorToolbar getToolbar();

    boolean hasErrors();

    void setCmdLinePreview(String cmdLinePreview);

    void setEastWidget(IsWidget widget);

    void setOnlyLabelEditMode(boolean onlyLabelEditMode);

}
