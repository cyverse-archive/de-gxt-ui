package org.iplantc.de.diskResource.client.views.metadata.dialogs;

/**
 * Created by sriram on 3/16/17.
 */

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.avu.Avu;
import org.iplantc.de.client.models.diskResources.MetadataTemplateAttribute;
import org.iplantc.de.diskResource.client.MetadataView;

import com.google.gwtmockito.GxtMockitoTestRunner;

import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.Field;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


@RunWith(GxtMockitoTestRunner.class)
public class MetadataTemplateViewDialogTest {

    @Mock
    MetadataView.Appearance appearanceMock;
    @Mock
    MetadataView.Presenter presenterMock;
    @Mock
    VerticalLayoutContainer widgetMock;
    @Mock
    FastMap<Avu> templateTagAvuMapMock;
    @Mock
    FastMap<MetadataTemplateAttribute> templateTagAtrrMapMock;
    @Mock
    FastMap<Field<?>> templateTagFieldMapMock;
    @Mock
    List<MetadataTemplateAttribute> attributesMock;
    @Mock
    FastMap<VerticalLayoutContainer> templateAttrVLCMapMock;
    @Mock
    List<Avu> templateMdMock;


    private MetadataTemplateViewDialog dialog;

    MetadataTemplateAttribute attr1 = mock(MetadataTemplateAttribute.class);
    MetadataTemplateAttribute attr2 = mock(MetadataTemplateAttribute.class);
    MetadataTemplateAttribute attr3 = mock(MetadataTemplateAttribute.class);

    String tag1 = "1";
    String tag2 = "2";
    String tag3 = "3";

    Avu md1 = mock(Avu.class);
    Avu md2 = mock(Avu.class);
    Avu md3 = mock(Avu.class);


    @Before
    public void setUp() throws Exception {
        when(attr1.getName()).thenReturn("name1");
        when(attr2.getName()).thenReturn("name2");
        when(attr3.getName()).thenReturn("name3");

        Iterator attrIteMock = mock(Iterator.class);
        when(attributesMock.iterator()).thenReturn(attrIteMock);
        when(attrIteMock.next()).thenReturn(attr1, attr2, attr3);


        dialog = new MetadataTemplateViewDialog(presenterMock, templateMdMock, true, attributesMock);
        dialog.templateTagAtrrMap = templateTagAtrrMapMock;
        dialog.templateTagAvuMap = templateTagAvuMapMock;
        dialog.templateTagFieldMap = templateTagFieldMapMock;
    }

    @Test
    public void testGetMetadataFromTemplate() {

        Field f1 = mock(Field.class);
        Field f2 = mock(Field.class);
        Field f3 = mock(Field.class);

        HashSet<String> tagSet = new HashSet<>();
        tagSet.add(tag1);
        tagSet.add(tag2);
        tagSet.add(tag3);

        when(templateTagAtrrMapMock.keySet()).thenReturn(tagSet);

        when(templateTagAtrrMapMock.get(tag1)).thenReturn(attr1);
        when(templateTagAtrrMapMock.get(tag2)).thenReturn(attr2);
        when(templateTagAtrrMapMock.get(tag3)).thenReturn(attr3);

        when(templateTagAvuMapMock.get(tag1)).thenReturn(md1);
        when(templateTagAvuMapMock.get(tag2)).thenReturn(md2);
        when(templateTagAvuMapMock.get(tag3)).thenReturn(md3);

        when(templateTagFieldMapMock.get(tag1)).thenReturn(f1);
        when(templateTagFieldMapMock.get(tag2)).thenReturn(f2);
        when(templateTagFieldMapMock.get(tag3)).thenReturn(f3);

        Assert.assertEquals(3, dialog.getMetadataFromTemplate().size());
    }

}
