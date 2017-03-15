package org.iplantc.de.apps.integration.client.view;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.iplantc.de.apps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.de.client.models.apps.integration.AppTemplateAutoBeanFactory;
import org.iplantc.de.client.models.apps.integration.Argument;
import org.iplantc.de.client.models.apps.integration.ArgumentType;
import org.iplantc.de.client.models.apps.integration.FileParameters;
import org.iplantc.de.client.util.AppTemplateUtils;
import org.iplantc.de.resources.client.IplantContextualHelpAccessStyle;
import org.iplantc.de.resources.client.uiapps.widgets.AppsWidgetsDefaultLabels;

import com.google.gwt.user.client.ui.Image;
import com.google.gwtmockito.GxtMockitoTestRunner;
import com.google.web.bindery.autobean.shared.AutoBean;

import com.sencha.gxt.dnd.core.client.DragSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

@RunWith(GxtMockitoTestRunner.class)
public class AppIntegrationItemDNDTest {

    @Mock AppIntegrationPalette viewMock;
    @Mock AppTemplateWizardAppearance mockAppearance;
    @Mock AppsWidgetsDefaultLabels mockLabels; 
    @Mock AppTemplateAutoBeanFactory mockFactory; 
    @Mock IplantContextualHelpAccessStyle mockStyle; 
    @Mock AutoBean<Argument> mockAbArgument; 
    @Mock AutoBean<FileParameters> mockAbDataObject;
    @Mock DragSource mockDragSource;
    @Mock Image widgetMock;
    @Mock ArgumentType typeMock;

    private AppIntegrationItemDND uut;

    @Before public void setUp() {
        uut = new AppIntegrationItemDND(mockAppearance,
                                        AppTemplateUtils.getInstance(),
                                        mockFactory,
                                        mockLabels, 
                                        viewMock,
                                        widgetMock,
                                        typeMock);
    }

    @Test public void testCreateNewArgument_FileOutput() {
        when(mockFactory.argument()).thenReturn(mockAbArgument);
        when(mockFactory.fileParameters()).thenReturn(mockAbDataObject);

        Argument mockArgument = mock(Argument.class);
        FileParameters mockDataObject = mock(FileParameters.class);
        when(mockAbArgument.as()).thenReturn(mockArgument);
        when(mockAbDataObject.as()).thenReturn(mockDataObject);

        // FileOutput
        uut.createNewArgument(ArgumentType.FileOutput);
        verify(mockFactory).argument();
        verify(mockFactory).fileParameters();
        verify(mockArgument).setFileParameters(eq(mockDataObject));
        verify(mockLabels).defFileOutput();

        verifyNoMoreInteractions(mockFactory, mockLabels);
    }

    @Test public void testCreateNewArgument_FolderOutput() {
        when(mockFactory.argument()).thenReturn(mockAbArgument);
        when(mockFactory.fileParameters()).thenReturn(mockAbDataObject);

        Argument mockArgument = mock(Argument.class);
        FileParameters mockDataObject = mock(FileParameters.class);
        when(mockAbArgument.as()).thenReturn(mockArgument);
        when(mockAbDataObject.as()).thenReturn(mockDataObject);

        // FolderOutput
        uut.createNewArgument(ArgumentType.FolderOutput);
        verify(mockFactory).argument();
        verify(mockFactory).fileParameters();
        verify(mockArgument).setFileParameters(eq(mockDataObject));
        verify(mockLabels).defFolderOutput();

        verifyNoMoreInteractions(mockFactory, mockLabels);
    }

    @Test public void testCreateNewArgument_MultiFileOutput() {
        when(mockFactory.argument()).thenReturn(mockAbArgument);
        when(mockFactory.fileParameters()).thenReturn(mockAbDataObject);


        Argument mockArgument = mock(Argument.class);
        FileParameters mockDataObject = mock(FileParameters.class);
        when(mockAbArgument.as()).thenReturn(mockArgument);
        when(mockAbDataObject.as()).thenReturn(mockDataObject);

        // MultiFileOutput
        uut.createNewArgument(ArgumentType.MultiFileOutput);
        verify(mockFactory).argument();
        verify(mockFactory).fileParameters();
        verify(mockArgument).setFileParameters(eq(mockDataObject));
        verify(mockLabels).defMultiFileOutput();

        verifyNoMoreInteractions(mockFactory, mockLabels);
    }

}
