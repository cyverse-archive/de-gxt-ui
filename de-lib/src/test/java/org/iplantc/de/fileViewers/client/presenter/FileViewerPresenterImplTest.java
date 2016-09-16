package org.iplantc.de.fileViewers.client.presenter;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.events.FileSavedEvent;
import org.iplantc.de.client.models.CommonModelAutoBeanFactory;
import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.diskResources.File;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.models.viewer.FileViewerAutoBeanFactory;
import org.iplantc.de.client.models.viewer.Manifest;
import org.iplantc.de.client.models.viewer.MimeType;
import org.iplantc.de.client.models.viewer.StructuredText;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.services.FileEditorServiceFacade;
import org.iplantc.de.client.services.UserSessionServiceFacade;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.client.util.JsonUtil;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.diskResource.client.views.dialogs.SaveAsDialog;
import org.iplantc.de.fileViewers.client.FileViewer;
import org.iplantc.de.fileViewers.client.events.DirtyStateChangedEvent;
import org.iplantc.de.fileViewers.client.views.SaveAsDialogCancelSelectHandler;
import org.iplantc.de.fileViewers.client.views.SaveAsDialogOkSelectHandler;
import org.iplantc.de.fileViewers.client.views.StructuredTextViewer;
import org.iplantc.de.fileViewers.client.views.TextViewerImpl;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwtmockito.GwtMockitoTestRunner;

import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.PlainTabPanel;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author aramsey 
 */
@RunWith(GwtMockitoTestRunner.class)
public class FileViewerPresenterImplTest {

    @Mock MimeTypeViewerResolverFactory mimeFactoryMock;
    @Mock CommonModelAutoBeanFactory factoryMock;
    @Mock FileViewerAutoBeanFactory viewerFactoryMock;
    @Mock FileEditorServiceFacade fileEditorServiceMock;
    @Mock FileViewer.FileViewerPresenterAppearance appearanceMock;
    @Mock AsyncProviderWrapper<SaveAsDialog> saveAsDialogProviderMock;
    @Mock UserSessionServiceFacade userSessionServiceMock;
    @Mock DiskResourceServiceFacade diskResourceServiceFacadeMock;
    @Mock DiskResourceUtil diskResourceUtilMock;
    @Mock JsonUtil jsonUtilMock;
    @Mock IplantAnnouncer announcerMock;
    @Mock File fileMock;
    @Mock Folder parentFolderMock;
    @Mock PlainTabPanel tabPanelMock;
    @Mock SimpleContainer simpleContainerMock;
    @Mock List<FileViewer> viewersMock;
    @Mock Iterator<FileViewer> viewerIteratorMock;
    @Mock PlainTabPanel plainTabPanelMock;
    @Mock AsyncCallback<String> stringCallback;
    @Mock Manifest manifestMock;
    @Mock Logger logMock;
    @Mock StructuredText structuredTextMock;
    @Mock StructuredTextViewer structuredTextViewerMock;
    @Mock FileViewer fileViewerMock;
    @Mock TextViewerImpl textViewerMock;
    @Mock IsMaskable maskableMock;
    @Mock SaveAsDialog saveAsDialogMock;
    @Mock MimeType contentTypeMock;
    @Mock OngoingStubbing<? extends FileViewer> extendedViewersMock;

    @Captor ArgumentCaptor<FileViewerPresenterImpl.GetManifestCallback> manifestCaptor;
    @Captor ArgumentCaptor<AsyncCallback<String>> stringCaptor;
    @Captor ArgumentCaptor<AsyncCallback<SaveAsDialog>> saveAsDialogCaptor;
    @Captor ArgumentCaptor<AsyncCallback<File>> fileCaptor;

    private FileViewerPresenterImpl uut;

    @Before
    public void setUp() {
        when(appearanceMock.retrieveFileManifestMask()).thenReturn("mask");
        when(appearanceMock.retrievingFileContentsMask()).thenReturn("mask");
        when(appearanceMock.savingMask()).thenReturn("mask");
        when(appearanceMock.retrieveTreeUrlsMask()).thenReturn("mask");
        when(fileMock.getName()).thenReturn("fileName");
        when(viewersMock.size()).thenReturn(3);
        when(viewersMock.iterator()).thenReturn(viewerIteratorMock);
        when(viewerIteratorMock.hasNext()).thenReturn(true, true, true, false);
        when(viewerIteratorMock.next()).thenReturn(fileViewerMock, textViewerMock, structuredTextViewerMock);
        when(viewersMock.get(0)).thenReturn(fileViewerMock);
        when(viewersMock.get(1)).thenReturn(textViewerMock);
        when(viewersMock.get(2)).thenReturn(structuredTextViewerMock);

        uut = new FileViewerPresenterImpl() {
            @Override
            PlainTabPanel getPlainTabPanel() {
                return tabPanelMock;
            }

            @Override
            SimpleContainer getSimpleContainer() {
                return simpleContainerMock;
            }

            @Override
            List<FileViewer> getFileViewers() {
                return viewersMock;
            }

            @Override
            StructuredText getStructuredText(String result) {
                return structuredTextMock;
            }

            @Override
            FileViewer getActiveFileViewer() {
                return fileViewerMock;
            }

            @Override
            IsMaskable asMaskable(Component component) {
                return maskableMock;
            }
        };
        uut.mimeFactory = mimeFactoryMock;
        uut.factory = factoryMock;
        uut.viewerFactory = viewerFactoryMock;
        uut.fileEditorService = fileEditorServiceMock;
        uut.appearance = appearanceMock;
        uut.saveAsDialogProvider = saveAsDialogProviderMock;
        uut.userSessionService = userSessionServiceMock;
        uut.diskResourceServiceFacade = diskResourceServiceFacadeMock;
        uut.diskResourceUtil = diskResourceUtilMock;
        uut.announcer = announcerMock;
        uut.parentFolder = parentFolderMock;
        uut.file = fileMock;
        uut.title = "title";
        uut.LOG = logMock;

        verifyConstructor();
    }

    private void verifyConstructor() {
        verify(simpleContainerMock).setWidget(eq(tabPanelMock));
    }

    @Test
    public void testGo() {
        when(tabPanelMock.isAttached()).thenReturn(false);
        when(manifestMock.getInfoType()).thenReturn("info-type");
        when(manifestMock.getContentType()).thenReturn("content-type");

        HasOneWidget containerMock = mock(HasOneWidget.class);

        /** CALL METHOD UNDER TEST **/
        uut.go(containerMock, fileMock, parentFolderMock, true, true, stringCallback);
        verify(containerMock).setWidget(eq(simpleContainerMock));
        verify(simpleContainerMock).mask(eq(appearanceMock.retrieveFileManifestMask()));

        verify(fileEditorServiceMock).getManifest(eq(fileMock), manifestCaptor.capture());
    }

    @Test
    public void testLoadStructuredData() {

        /** CALL METHOD UNDER TEST **/
        uut.loadStructuredData(0, 123456789L, "separator");
        verify(simpleContainerMock).mask(eq(appearanceMock.retrievingFileContentsMask()));
        verify(fileEditorServiceMock).readCsvChunk(eq(fileMock),
                                                   anyString(),
                                                   anyInt(),
                                                   anyLong(),
                                                   stringCaptor.capture());

        stringCaptor.getValue().onSuccess("result");
        verify(structuredTextViewerMock).setData(structuredTextMock);
        verify(simpleContainerMock).unmask();
    }

    @Test
    public void testLoadTextData() {

        /** CALL METHOD UNDER TEST **/
        uut.loadTextData(0, 123456789L);

        verify(simpleContainerMock).mask(eq(appearanceMock.retrievingFileContentsMask()));
        verify(fileEditorServiceMock).readChunk(eq(fileMock),
                                                anyLong(),
                                                anyInt(),
                                                stringCaptor.capture());

        stringCaptor.getValue().onSuccess("{chunk:test}");
        verify(textViewerMock).setData(anyString());
        verify(simpleContainerMock).unmask();
    }

    @Test
    public void testNewFileGo() {

    }

    @Test
    public void testOnFileSaved_fileNotNull() {
        FileSavedEvent eventMock = mock(FileSavedEvent.class);
        Widget widgetMock = mock(Widget.class);
        when(eventMock.getFile()).thenReturn(fileMock);
        when(tabPanelMock.getActiveWidget()).thenReturn(widgetMock);
        when(tabPanelMock.getWidgetCount()).thenReturn(2);
        when(tabPanelMock.getWidget(0)).thenReturn(widgetMock);
        when(tabPanelMock.getWidget(1)).thenReturn(widgetMock);

        /** CALL METHOD UNDER TEST **/
        uut.onFileSaved(eventMock);

        verify(textViewerMock).refresh();
        verify(simpleContainerMock).unmask();
    }

    @Test
    public void testOnFileSaved_fileNull() {
        uut.file = null;
        FileSavedEvent eventMock = mock(FileSavedEvent.class);
        File newFileMock = mock(File.class);
        Widget widgetMock = mock(Widget.class);

        when(newFileMock.getName()).thenReturn("name");
        when(eventMock.getFile()).thenReturn(newFileMock);
        when(tabPanelMock.getActiveWidget()).thenReturn(widgetMock);
        when(tabPanelMock.getWidgetCount()).thenReturn(2);
        when(tabPanelMock.getWidget(0)).thenReturn(widgetMock);
        when(tabPanelMock.getWidget(1)).thenReturn(widgetMock);
        when(structuredTextViewerMock.getViewName("name")).thenReturn("viewName");
        when(fileViewerMock.getViewName("name")).thenReturn("viewName");
        when(textViewerMock.getViewName("name")).thenReturn("viewName");

        /** CALL METHOD UNDER TEST **/
        uut.onFileSaved(eventMock);

        verify(eventMock).getFile();
        verify(tabPanelMock, times(2)).update(eq(widgetMock), Matchers.<TabItemConfig> any());
        verify(fileViewerMock).refresh();
        verify(textViewerMock).refresh();
        verify(structuredTextViewerMock).refresh();
    }

    @Test
    public void testSaveFile() {
        when(structuredTextViewerMock.isDirty()).thenReturn(false);
        when(fileViewerMock.isDirty()).thenReturn(false);
        when(textViewerMock.isDirty()).thenReturn(true);

        FileViewerPresenterImpl spy = spy(uut);

        /** CALL METHOD UNDER TEST **/
        spy.saveFile();
        verify(spy, times(1)).saveFile(Matchers.<FileViewer> any());
    }

    @Test
    public void testSaveFile1_fileNotNull() {
        when(fileMock.getPath()).thenReturn("path");
        when(fileViewerMock.getEditorContent()).thenReturn("content");

        /** CALL METHOD UNDER TEST **/
        uut.saveFile(fileViewerMock);

        verify(simpleContainerMock).mask(eq(appearanceMock.savingMask()));
        verify(fileEditorServiceMock).uploadTextAsFile(eq("path"),
                                                       eq("content"),
                                                       eq(false),
                                                       fileCaptor.capture());
    }

    @Test
    public void testSaveFile1_fileNull() {
        uut.file = null;
        when(fileMock.getPath()).thenReturn("path");
        when(fileViewerMock.getEditorContent()).thenReturn("content");

        /** CALL METHOD UNDER TEST **/
        uut.saveFile(fileViewerMock);

        verify(saveAsDialogProviderMock).get(saveAsDialogCaptor.capture());

        saveAsDialogCaptor.getValue().onSuccess(saveAsDialogMock);
        verify(saveAsDialogMock).addOkButtonSelectHandler(isA(SaveAsDialogOkSelectHandler.class));
        verify(saveAsDialogMock).addCancelButtonSelectHandler(isA(SaveAsDialogCancelSelectHandler.class));
        verify(saveAsDialogMock).show(eq(parentFolderMock));
        verify(saveAsDialogMock).toFront();
    }

    @Test
    public void testSaveFileWithExtension() {
        when(fileMock.getPath()).thenReturn("path");

        /** CALL METHOD UNDER TEST **/
        uut.saveFileWithExtension(fileViewerMock, "content", "extension");

        verify(fileEditorServiceMock).uploadTextAsFile(eq("pathextension"),
                                                       eq("content"),
                                                       eq(true),
                                                       fileCaptor.capture());
    }

    @Test
    public void testSetViewDirtyState() {
        uut.isDirty = true;

        /** CALL METHOD UNDER TEST **/
        uut.setViewDirtyState(false, fileViewerMock);

        verify(simpleContainerMock).fireEvent(isA(DirtyStateChangedEvent.class));
    }

    @Test
    public void testCallTreeCreateService() {
        when(fileMock.getPath()).thenReturn("path");

        /** CALL METHOD UNDER TEST **/
        uut.callTreeCreateService(fileViewerMock, fileMock);

        verify(simpleContainerMock).mask(eq(appearanceMock.retrieveTreeUrlsMask()));
        verify(fileEditorServiceMock).getTreeUrl(eq("path"), eq(false), stringCaptor.capture());
    }

    @Test
    @Ignore
    public void testComposeView() {

        uut.composeView(fileMock,
                        parentFolderMock,
                        manifestMock,
                        contentTypeMock,
                        "intoType",
                        true,
                        true);


    }
}
