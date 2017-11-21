package org.iplantc.de.collaborators.client.util;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.collaborators.CollaboratorAutoBeanFactory;
import org.iplantc.de.client.models.collaborators.Subject;

import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.web.bindery.autobean.shared.AutoBean;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.Iterator;
import java.util.List;

@RunWith(GwtMockitoTestRunner.class)
public class CollaboratorsUtilTest {

    @Mock CollaboratorAutoBeanFactory factoryMock;
    @Mock Subject subjectMock;
    @Mock Subject diffSubjectMock;
    @Mock List<Subject> subjectListMock;
    @Mock AutoBean<Subject> subjectAutoBeanMock;
    @Mock Iterator<Subject> subjectIteratorMock;

    CollaboratorsUtil uut;

    @Before
    public void setUp() {
        when(factoryMock.getSubject()).thenReturn(subjectAutoBeanMock);
        when(subjectAutoBeanMock.as()).thenReturn(subjectMock);
        when(subjectListMock.iterator()).thenReturn(subjectIteratorMock);
        uut = CollaboratorsUtil.getInstance();

        uut.factory = factoryMock;
    }


    @Test
    public void getDummySubject() {
        uut.getDummySubject("username");

        verify(subjectMock).setId(eq("username"));
        verify(subjectMock).setName(eq("username"));
    }

    @Test
    public void isCurrentCollaborator_true() {
        when(subjectIteratorMock.hasNext()).thenReturn(true, false);
        when(subjectIteratorMock.next()).thenReturn(subjectMock);
        when(subjectMock.getId()).thenReturn("id");
        assertTrue(uut.isCurrentCollaborator(subjectMock, subjectListMock));
    }

    @Test
    public void isCurrentCollaborator_false() {
        when(subjectIteratorMock.hasNext()).thenReturn(true, false);
        when(subjectIteratorMock.next()).thenReturn(diffSubjectMock);
        when(diffSubjectMock.getId()).thenReturn("diffId");
        assertFalse(uut.isCurrentCollaborator(subjectMock, subjectListMock));
    }

}
