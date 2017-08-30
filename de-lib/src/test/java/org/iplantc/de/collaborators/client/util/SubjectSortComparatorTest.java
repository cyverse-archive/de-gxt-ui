package org.iplantc.de.collaborators.client.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;

import com.google.gwtmockito.GwtMockitoTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

@RunWith(GwtMockitoTestRunner.class)
public class SubjectSortComparatorTest {

    @Mock Group g1;
    @Mock Group g2;
    @Mock Subject s1;
    @Mock Subject s2;

    UserSearchField.SubjectSortComparator uut;

    @Before
    public void setUp() {
        when(g1.getSourceId()).thenReturn(Group.GROUP_IDENTIFIER);
        when(g2.getSourceId()).thenReturn(Group.GROUP_IDENTIFIER);
        when(s1.getSourceId()).thenReturn("notGroup");
        when(s2.getSourceId()).thenReturn("notGroup");
        uut = new UserSearchField.SubjectSortComparator();
    }

    @Test
    public void compare_twoEqualGroups() {
        when(g1.getSubjectDisplayName()).thenReturn("name");
        when(g2.getSubjectDisplayName()).thenReturn("name");
        when(g1.getName()).thenReturn("name");
        when(g2.getName()).thenReturn("name");

        assertEquals(0, uut.compare(g1, g2));
    }

    @Test
    public void compare_twoGroups() {
        when(g1.getSubjectDisplayName()).thenReturn("aname");
        when(g2.getSubjectDisplayName()).thenReturn("bname");
        when(g1.getName()).thenReturn("aname");
        when(g2.getName()).thenReturn("bname");

        assertEquals(-1, uut.compare(g1, g2));
    }

    @Test
    public void compare_oneGroup() {
        when(g2.getSubjectDisplayName()).thenReturn("bname");
        when(s1.getName()).thenReturn("aname");
        when(g2.getName()).thenReturn("bname");

        assertEquals(1, uut.compare(s1, g2));
    }

    @Test
    public void compare_twoSubjectsWithEqualLastNames() {
        when(s1.getLastName()).thenReturn("lastName");
        when(s2.getLastName()).thenReturn("lastName");

        assertEquals(0, uut.compare(s1, s2));
    }

    @Test
    public void compare_twoSubjectsWithLastNames() {
        when(s1.getLastName()).thenReturn("aLastName");
        when(s2.getLastName()).thenReturn("bLastName");

        assertEquals(-1, uut.compare(s1, s2));
    }

    @Test
    public void compare_oneSubjectWithOneLastName() {
        when(s1.getLastName()).thenReturn(null);
        when(s2.getLastName()).thenReturn("bLastName");

        assertEquals(-1, uut.compare(s1, s2));
    }

    @Test
    public void compare_noLastNames() {
        when(s1.getLastName()).thenReturn(null);
        when(s2.getLastName()).thenReturn(null);

        assertEquals(0, uut.compare(s1, s2));
    }

    @Test
    public void compare_twoSubjectsWithEqualFirstNames() {
        when(s1.getLastName()).thenReturn("lastName");
        when(s2.getLastName()).thenReturn("lastName");
        when(s1.getFirstName()).thenReturn("firstName");
        when(s2.getFirstName()).thenReturn("firstName");

        assertEquals(0, uut.compare(s1, s2));
    }

    @Test
    public void compare_twoSubjectsWithNoFirstNames() {
        when(s1.getLastName()).thenReturn("lastName");
        when(s2.getLastName()).thenReturn("lastName");
        when(s1.getFirstName()).thenReturn(null);
        when(s2.getFirstName()).thenReturn(null);

        assertEquals(0, uut.compare(s1, s2));
    }

    @Test
    public void compare_twoSubjectsFirstNames() {
        when(s1.getLastName()).thenReturn("lastName");
        when(s2.getLastName()).thenReturn("lastName");
        when(s1.getFirstName()).thenReturn("aFirstName");
        when(s2.getFirstName()).thenReturn("bFirstName");

        assertEquals(-1, uut.compare(s1, s2));
    }

    @Test
    public void compare_twoSubjectsOneFirstName() {
        when(s1.getLastName()).thenReturn("lastName");
        when(s2.getLastName()).thenReturn("lastName");
        when(s1.getFirstName()).thenReturn(null);
        when(s2.getFirstName()).thenReturn("bFirstName");

        assertEquals(-1, uut.compare(s1, s2));
    }

    @Test
    public void compare_twoSubjectsWithEqualNames() {
        when(s1.getLastName()).thenReturn("lastName");
        when(s2.getLastName()).thenReturn("lastName");
        when(s1.getFirstName()).thenReturn("firstName");
        when(s2.getFirstName()).thenReturn("firstName");
        when(s1.getName()).thenReturn("name");
        when(s2.getName()).thenReturn("name");

        assertEquals(0, uut.compare(s1, s2));
    }

    @Test
    public void compare_twoSubjectsNoNames() {
        when(s1.getLastName()).thenReturn("lastName");
        when(s2.getLastName()).thenReturn("lastName");
        when(s1.getFirstName()).thenReturn("firstName");
        when(s2.getFirstName()).thenReturn("firstName");
        when(s1.getName()).thenReturn(null);
        when(s2.getName()).thenReturn(null);

        assertEquals(0, uut.compare(s1, s2));
    }

    @Test
    public void compare_twoSubjectsWithNames() {
        when(s1.getLastName()).thenReturn("lastName");
        when(s2.getLastName()).thenReturn("lastName");
        when(s1.getFirstName()).thenReturn("firstName");
        when(s2.getFirstName()).thenReturn("firstName");
        when(s1.getName()).thenReturn("aOtherName");
        when(s2.getName()).thenReturn("bOtherName");

        assertEquals(-1, uut.compare(s1, s2));
    }

    @Test
    public void compare_twoSubjectsWithOneName() {
        when(s1.getLastName()).thenReturn("lastName");
        when(s2.getLastName()).thenReturn("lastName");
        when(s1.getFirstName()).thenReturn("firstName");
        when(s2.getFirstName()).thenReturn("firstName");
        when(s1.getName()).thenReturn(null);
        when(s2.getName()).thenReturn("bOtherName");

        assertEquals(-1, uut.compare(s1, s2));
    }

    @Test
    public void compare_twoSubjectsWithNoIds() {
        when(s1.getLastName()).thenReturn("lastName");
        when(s2.getLastName()).thenReturn("lastName");
        when(s1.getFirstName()).thenReturn("firstName");
        when(s2.getFirstName()).thenReturn("firstName");
        when(s1.getName()).thenReturn("name");
        when(s2.getName()).thenReturn("name");
        when(s1.getId()).thenReturn(null);
        when(s2.getId()).thenReturn(null);

        assertEquals(0, uut.compare(s1, s2));
    }

    @Test
    public void compare_twoSubjectsWithEqualIds() {
        when(s1.getLastName()).thenReturn("lastName");
        when(s2.getLastName()).thenReturn("lastName");
        when(s1.getFirstName()).thenReturn("firstName");
        when(s2.getFirstName()).thenReturn("firstName");
        when(s1.getName()).thenReturn("name");
        when(s2.getName()).thenReturn("name");
        when(s1.getId()).thenReturn("id");
        when(s2.getId()).thenReturn("id");

        assertEquals(0, uut.compare(s1, s2));
    }

    @Test
    public void compare_twoSubjectsWithIds() {
        when(s1.getLastName()).thenReturn("lastName");
        when(s2.getLastName()).thenReturn("lastName");
        when(s1.getFirstName()).thenReturn("firstName");
        when(s2.getFirstName()).thenReturn("firstName");
        when(s1.getName()).thenReturn("name");
        when(s2.getName()).thenReturn("name");
        when(s1.getId()).thenReturn("id1");
        when(s2.getId()).thenReturn("id2");

        assertEquals(-1, uut.compare(s1, s2));
    }

    @Test
    public void compare_twoSubjectsWithOneId() {
        when(s1.getLastName()).thenReturn("lastName");
        when(s2.getLastName()).thenReturn("lastName");
        when(s1.getFirstName()).thenReturn("firstName");
        when(s2.getFirstName()).thenReturn("firstName");
        when(s1.getName()).thenReturn("name");
        when(s2.getName()).thenReturn("name");
        when(s1.getId()).thenReturn(null);
        when(s2.getId()).thenReturn("id2");

        assertEquals(-1, uut.compare(s1, s2));
    }

}
