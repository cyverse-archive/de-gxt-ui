package org.iplantc.de.diskResource.client.presenters.metadata.proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.ontologies.OntologyLookupServiceQueryParams;
import org.iplantc.de.client.models.ontologies.OntologyLookupServiceQueryParams.EntityTypeFilterValue;

import com.google.common.collect.Lists;
import com.google.gwtmockito.GwtMockitoTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

@RunWith(GwtMockitoTestRunner.class)
public class OntologyLookupServiceLoadConfigTest {
    @Mock OntologyLookupServiceQueryParams queryParamsMock;

    OntologyLookupServiceLoadConfig loadConfigUnderTest;

    @Test
    public void loadConfigNoSettings() {
        loadConfigUnderTest = new OntologyLookupServiceLoadConfig();

        assertNull(loadConfigUnderTest.getEntityTypeFilter());
        assertNull(loadConfigUnderTest.getOntologyListFilter());
        assertNull(loadConfigUnderTest.getChildrenFilter());
        assertNull(loadConfigUnderTest.getAllChildrenFilter());
    }

    @Test
    public void loadConfigSettings() {
        when(queryParamsMock.getEntityType()).thenReturn(EntityTypeFilterValue.CLASS);
        when(queryParamsMock.getOntologies()).thenReturn(Lists.newArrayList("ontology1", "ontology2"));
        when(queryParamsMock.getChildren()).thenReturn(Lists.newArrayList("child"));
        when(queryParamsMock.getAllChildren()).thenReturn(Lists.newArrayList("children1", "children2", "children3"));

        loadConfigUnderTest = new OntologyLookupServiceLoadConfig(queryParamsMock);

        assertEquals(EntityTypeFilterValue.CLASS, loadConfigUnderTest.getEntityTypeFilter());
        assertEquals("ontology1,ontology2", loadConfigUnderTest.getOntologyListFilter());
        assertEquals("child", loadConfigUnderTest.getChildrenFilter());
        assertEquals("children1,children2,children3", loadConfigUnderTest.getAllChildrenFilter());
    }

    @Test
    public void loadConfigEmptyStringSettings() {
        when(queryParamsMock.getOntologies()).thenReturn(Lists.newArrayList("ontology1", null, ""));
        when(queryParamsMock.getChildren()).thenReturn(Lists.newArrayList("", "child", null));
        when(queryParamsMock.getAllChildren())
                .thenReturn(Lists.newArrayList("children1", "", "children2", "", "children3"));

        loadConfigUnderTest = new OntologyLookupServiceLoadConfig(queryParamsMock);

        assertEquals("ontology1", loadConfigUnderTest.getOntologyListFilter());
        assertEquals("child", loadConfigUnderTest.getChildrenFilter());
        assertEquals("children1,children2,children3", loadConfigUnderTest.getAllChildrenFilter());
    }
}
