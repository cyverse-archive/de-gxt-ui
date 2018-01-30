package org.iplantc.de.desktop.client.views.windows;

import org.iplantc.de.client.models.IsHideable;
import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.WindowType;
import org.iplantc.de.client.models.diskResources.File;
import org.iplantc.de.commons.client.views.window.configs.FileViewerWindowConfig;
import org.iplantc.de.commons.client.views.window.configs.HTPathListWindowConfig;
import org.iplantc.de.commons.client.views.window.configs.MultiInputPathListWindowConfig;
import org.iplantc.de.commons.client.views.window.configs.TabularFileViewerWindowConfig;
import org.iplantc.de.commons.client.views.window.configs.WindowConfig;
import org.iplantc.de.fileViewers.client.FileViewer;
import org.iplantc.de.fileViewers.client.events.DirtyStateChangedEvent;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.common.base.Strings;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;

import java.util.logging.Logger;

/**
 * @author sriram, jstroot
 */
public class FileViewerWindow extends WindowBase
        implements IsMaskable, DirtyStateChangedEvent.DirtyStateChangedEventHandler, IsHideable {
    private class CriticalPathCallback implements AsyncCallback<String> {
        @Override
        public void onFailure(Throwable caught) {
            FileViewerWindow.this.hide();
        }

        @Override
        public void onSuccess(String result) {
        }
    }

    protected File file;
    protected JSONObject manifest;
    Logger LOG = Logger.getLogger(FileViewerWindow.class.getName());
    private FileViewerWindowConfig configAB;
    private final IplantDisplayStrings displayStrings;
    private final FileViewer.Presenter presenter;
    private final FileViewer.FileViewerAppearance appearance;

    @Inject
    FileViewerWindow(final IplantDisplayStrings displayStrings,
                     final FileViewer.Presenter presenter,
                     final FileViewer.FileViewerAppearance appearance) {
        this.displayStrings = displayStrings;
        this.presenter = presenter;
        this.presenter.addDirtyStateChangedEventHandler(this);
        this.appearance = appearance;

        setMinHeight(appearance.windowMinHeight());
        setMinWidth(appearance.windowMinWidth());
   }

    @Override
    public <C extends org.iplantc.de.commons.client.views.window.configs.WindowConfig> void show(C windowConfig,
                                                                                                 String tag,
                                                                                                 boolean isMaximizable) {

        super.show(windowConfig, tag, isMaximizable);
        final FileViewerWindowConfig fileViewerWindowConfig = (FileViewerWindowConfig) windowConfig;
        this.configAB = fileViewerWindowConfig;
        this.file = configAB.getFile();
        if (file != null) {
            setHeading(file.getName());
            presenter.go(this,
                         file,
                         configAB.getParentFolder(),
                         fileViewerWindowConfig.isEditing(),
                         fileViewerWindowConfig.isVizTabFirst(),
                         new CriticalPathCallback());
        } else {
            String title = "Untitled-" + Math.random();
            setHeading(title);
            boolean isTabularFile = windowConfig instanceof TabularFileViewerWindowConfig;
            boolean isPathListFile = windowConfig instanceof HTPathListWindowConfig;
            boolean isMultiInputFile = windowConfig instanceof MultiInputPathListWindowConfig;
            String delimiter = isTabularFile ? ((TabularFileViewerWindowConfig) windowConfig).getSeparator() : "";
            Integer columns = isTabularFile ? ((TabularFileViewerWindowConfig) windowConfig).getColumns() : null;
            presenter.newFileGo(this,
                                title,
                                fileViewerWindowConfig.getContentType(),
                                fileViewerWindowConfig.getParentFolder(),
                                fileViewerWindowConfig.isEditing(),
                                fileViewerWindowConfig.isVizTabFirst(),
                                isTabularFile,
                                isPathListFile,
                                isMultiInputFile,
                                columns,
                                delimiter);
        }

    }

    @Override
    public void hide() {
        if (presenter.isDirty() && configAB.isEditing()) {
            final MessageBox cmb = new MessageBox(displayStrings.save(), displayStrings.unsavedChanges());
            cmb.setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO, PredefinedButton.CANCEL);
            cmb.addDialogHideHandler(new DialogHideEvent.DialogHideHandler() {
                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (PredefinedButton.YES.equals(event.getHideButton())) {
                        cmb.hide();
                        presenter.saveFileAndClose(FileViewerWindow.this);
                    } else if (PredefinedButton.NO.equals(event.getHideButton())) {
                        FileViewerWindow.super.hide();
                    }

                }
            });
            cmb.show();
        } else {
            super.hide();
        }
    }

    @Override
    public String getWindowType() {
        return WindowType.FILE_VIEWER.toString();
    }

    @Override
    public FastMap<String> getAdditionalWindowStates() {
        return null;
    }

    @Override
    public void restoreWindowState() {
        if (getStateId().equals(ws.getTag())) {
            super.restoreWindowState();
            String width = ws.getWidth();
            String height = ws.getHeight();
            setSize((Strings.isNullOrEmpty(width)) ? appearance.windowWidth() : width,
                    (Strings.isNullOrEmpty(height)) ? appearance.windowHeight() : height);
        }

    }

    @Override
    public WindowConfig getWindowConfig() {
        return configAB;
    }

    @Override
    public void onEditorDirtyStateChanged(DirtyStateChangedEvent event) {
        if (event.isDirty()) {
            setHeading(SafeHtmlUtils.fromTrustedString(getHeader().getHTML()
                                     + "<span style='color:red; vertical-align: super'> * </span>"));
        } else {
            setHeading(presenter.getTitle());
        }
    }

}
