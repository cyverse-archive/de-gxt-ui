package org.iplantc.de.client.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.search.DateInterval;
import org.iplantc.de.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.de.client.models.search.FileSizeRange;
import org.iplantc.de.client.models.tags.Tag;

import com.google.gwtmockito.GwtMockitoTestRunner;

import com.sencha.gxt.core.client.util.DateWrapper;
import com.sencha.gxt.core.client.util.Format;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import org.skyscreamer.jsonassert.JSONAssert;
import org.json.JSONException;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 
 * @author jstroot
 * 
 */
@RunWith(GwtMockitoTestRunner.class)
public class DataSearchQueryBuilderTest {
    
    @Mock DiskResourceQueryTemplate dsf;
    @Mock UserInfo userInfoMock;

    @Before public void setUp() {
        when(dsf.isIncludeTrashItems()).thenReturn(true);
        when(userInfoMock.getUsername()).thenReturn("test_user");
    }

    @Test public void testApplyOROperator_case1() {
        DataSearchQueryBuilder uut = new DataSearchQueryBuilder(dsf, userInfoMock);
        String searchText = "one two three";
        final String applyOROperator = uut.applyOROperator(searchText);
        final String expected = "one" + DataSearchQueryBuilder.OR_OPERATOR + "two"
                + DataSearchQueryBuilder.OR_OPERATOR + "three";
        assertEquals("Verify OR operator applied", expected, applyOROperator);
    }

    @Test public void testApplyOROperator_case2() {
        DataSearchQueryBuilder uut = new DataSearchQueryBuilder(dsf, userInfoMock);
        String searchText = "one ";
        final String applyOROperator = uut.applyOROperator(searchText);
        final String expected = "one";
        assertEquals("Verify OR operator applied", expected, applyOROperator);
    }

    /**
     * when asterisks needed
     */
    @Test public void testApplyImplicitAsteriskSearchText_Case1() {
        DataSearchQueryBuilder uut = new DataSearchQueryBuilder(dsf, userInfoMock);
        String searchText = "one" + DataSearchQueryBuilder.OR_OPERATOR + "two"
                + DataSearchQueryBuilder.OR_OPERATOR + "three";
        final String applyImplicitAsteriskSearchText = uut.applyImplicitAsteriskSearchText(searchText);
        final String expected = "*one*" + DataSearchQueryBuilder.OR_OPERATOR + "*two*"
                + DataSearchQueryBuilder.OR_OPERATOR + "*three*";
        assertEquals("Verify application of implicit asterisk(*)", expected, applyImplicitAsteriskSearchText);
    }

    /**
     * when string contains '*'
     */
    @Test public void testApplyImplicitAsteriskSearchText_Case2() {
        DataSearchQueryBuilder uut = new DataSearchQueryBuilder(dsf, userInfoMock);
        String searchText = "one*" + DataSearchQueryBuilder.OR_OPERATOR + "two"
                + DataSearchQueryBuilder.OR_OPERATOR + "three";
        final String applyImplicitAsteriskSearchText = uut.applyImplicitAsteriskSearchText(searchText);
        assertEquals("Verify that implicit asterisk NOT applied", searchText, applyImplicitAsteriskSearchText);
    }

    /**
     * when string contains '?'
     */
    @Test public void testApplyImplicitAsteriskSearchText_Case3() {
        DataSearchQueryBuilder uut = new DataSearchQueryBuilder(dsf, userInfoMock);
        String searchText = "one" + DataSearchQueryBuilder.OR_OPERATOR + "?two"
                + DataSearchQueryBuilder.OR_OPERATOR + "three";
        final String applyImplicitAsteriskSearchText = uut.applyImplicitAsteriskSearchText(searchText);
        assertEquals("Verify that implicit asterisk NOT applied", searchText, applyImplicitAsteriskSearchText);
    }

    /**
     * when string contains '\'
     */
    @Test public void testApplyImplicitAsteriskSearchText_Case4() {
        DataSearchQueryBuilder uut = new DataSearchQueryBuilder(dsf, userInfoMock);
        String searchText = "one" + DataSearchQueryBuilder.OR_OPERATOR + "two"
                + DataSearchQueryBuilder.OR_OPERATOR + "\\three";
        final String applyImplicitAsteriskSearchText = uut.applyImplicitAsteriskSearchText(searchText);
        assertEquals("Verify that implicit asterisk NOT applied", searchText, applyImplicitAsteriskSearchText);
    }

    // @Test public void testBuildQuery() {
    //     final String expectedFileQuery = setFileQuery("some* file* query*");
    //     final String expectedModifiedWithin = setModifiedWithin(new Date(), new DateWrapper().addDays(1)
    //             .asDate());
    //     final String expectedCreatedWithin = setCreatedWithin(new Date(), new DateWrapper().addMonths(1)
    //             .asDate());
    //     final String expectedNegatedFile = setNegatedFileQuery("term1*" + " " + "term2*" + " "
    //                                                                + "term3*");
    //     final String expectedMetadataAttributeQuery = setMetadataAttributeQuery(
    //             "some* metadata* query*");
    //     final String expectedMetadataValueQuery = setMetadataValueQuery("some* metadata* query*");
    //     final String expectedOwnedBy = setOwnedBy("someuser");
    //     final String expectedFileSizeRange = setFileSizeRange(0.1, 100.78763);
    //     final String expectedSharedWith = setSharedWith("some users who were shared with");

    //     String result = new DataSearchQueryBuilder(dsf, userInfoMock).buildFullQuery();

    //     assertTrue(result.contains(expectedFileQuery));
    //     assertTrue(result.contains(expectedModifiedWithin));
    //     assertTrue(result.contains(expectedCreatedWithin));
    //     assertTrue(result.contains(expectedNegatedFile));
    //     assertTrue(result.contains(expectedMetadataAttributeQuery));
    //     assertTrue(result.contains(expectedMetadataValueQuery));
    //     assertTrue(result.contains(expectedOwnedBy));
    //     assertTrue(result.contains(expectedFileSizeRange));
    //     assertTrue(result.contains(expectedSharedWith));
    // }

    @Test public void testOwnedBy() throws JSONException {
        final String expectedValue = setOwnedBy("someuser");

        String result = new DataSearchQueryBuilder(dsf, userInfoMock).ownedBy().toString();
        JSONAssert.assertEquals(wrappedQuery(expectedValue), result, true);
    }

    @Test public void testCreatedWithin() throws JSONException {
        final String expectedValue = setCreatedWithin(new Date(), new DateWrapper().addDays(1).asDate());

        String result = new DataSearchQueryBuilder(dsf, userInfoMock).createdWithin().toString();
        JSONAssert.assertEquals(wrappedQuery(expectedValue), result, true);
    }
    
    @Test public void testCreatedWithinFromNull() throws JSONException {
        final String expectedValue = setCreatedWithin(null, new DateWrapper().addDays(1).asDate());

        String result = new DataSearchQueryBuilder(dsf, userInfoMock).createdWithin().toString();
        JSONAssert.assertEquals(wrappedQuery(expectedValue), result, true);
    }

    @Test public void testCreatedWithinToNull() throws JSONException {
        final String expectedValue = setCreatedWithin(new Date(), null);

        String result = new DataSearchQueryBuilder(dsf, userInfoMock).createdWithin().toString();
        JSONAssert.assertEquals(wrappedQuery(expectedValue), result, true);
    }

    @Test public void testFile() throws JSONException {
        final String expectedValue = setFileQuery("some* words* in* query*");

        String result = new DataSearchQueryBuilder(dsf, userInfoMock).file().toString();
        JSONAssert.assertEquals(wrappedQuery(expectedValue), result, true);
    }

    @Test public void testFileSizeRange() throws JSONException {
        final String expectedValue = setFileSizeRange(1.0, 100.0);

        String result = new DataSearchQueryBuilder(dsf, userInfoMock).fileSizeRange().toString();
        JSONAssert.assertEquals(wrappedQuery(expectedValue), result, true);
    }

    @Test public void testFileSizeRangeMinNull() throws JSONException {
        final String expectedValue = setFileSizeRange(null, 100.0);

        String result = new DataSearchQueryBuilder(dsf, userInfoMock).fileSizeRange().toString();
        JSONAssert.assertEquals(wrappedQuery(expectedValue), result, true);
    }

    @Test public void testFileSizeRangeMaxNul() throws JSONException {
        final String expectedValue = setFileSizeRange(1.0, null);

        String result = new DataSearchQueryBuilder(dsf, userInfoMock).fileSizeRange().toString();
        JSONAssert.assertEquals(wrappedQuery(expectedValue), result, true);
    }

    @Test public void testMetadataAttribute() throws JSONException {
        final String expectedValue = setMetadataAttributeQuery("some* metadata* to* search* for*");

        String result = new DataSearchQueryBuilder(dsf, userInfoMock).metadataAttribute().toString();
        JSONAssert.assertEquals(wrappedQuery(expectedValue), result, true);
    }

    @Test public void testMetadataValue() throws JSONException {
        final String expectedValue = setMetadataValueQuery("some* metadata* to* search* for*");

        String result = new DataSearchQueryBuilder(dsf, userInfoMock).metadataValue().toString();
        JSONAssert.assertEquals(wrappedQuery(expectedValue), result, true);
    }

    @Test public void testModifiedWithin() throws JSONException {
        final Date fromDate = new Date();
        final Date toDate = new DateWrapper(fromDate).addDays(2).asDate();
        final String expectedValue = setModifiedWithin(fromDate, toDate);

        String result = new DataSearchQueryBuilder(dsf, userInfoMock).modifiedWithin().toString();
        JSONAssert.assertEquals(wrappedQuery(expectedValue), result, true);
    }

    @Test public void testModifiedWithinFromNull() throws JSONException {
        final Date fromDate = null;
        final Date toDate = new DateWrapper(new Date()).addDays(2).asDate();
        final String expectedValue = setModifiedWithin(fromDate, toDate);

        String result = new DataSearchQueryBuilder(dsf, userInfoMock).modifiedWithin().toString();
        JSONAssert.assertEquals(wrappedQuery(expectedValue), result, true);
    }

    @Test public void testModifiedWithinToNull() throws JSONException {
        final Date fromDate = new Date();
        final Date toDate = null;
        final String expectedValue = setModifiedWithin(fromDate, toDate);

        String result = new DataSearchQueryBuilder(dsf, userInfoMock).modifiedWithin().toString();
        JSONAssert.assertEquals(wrappedQuery(expectedValue), result, true);
    }

    @Test public void testNegatedFile() throws JSONException {
        final String term1 = "term1*";
        final String term2 = "term2*";
        final String term3 = "term3*";
        final String expectedValue = setNegatedFileQuery(term1 + " " + term2 + " " + term3);

        String result = new DataSearchQueryBuilder(dsf, userInfoMock).negatedFile().toString();
        JSONAssert.assertEquals(wrappedNegatedQuery(expectedValue), result, true);
    }

    @Test public void testSharedWith() throws JSONException {
        final String retVal = "user that are shared with";
        final String expectedValue = setSharedWith(retVal);

        String result = new DataSearchQueryBuilder(dsf, userInfoMock).sharedWith().toString();
        JSONAssert.assertEquals(wrappedQuery(expectedValue), result, true);
    }

    @Test public void testFileExcludingTrash() throws JSONException {
        when(dsf.isIncludeTrashItems()).thenReturn(false);

        final String expectedValue = setFileQuery("*query*");

        String result = new DataSearchQueryBuilder(dsf, userInfoMock).file().toString();
        JSONAssert.assertEquals(wrappedQueryExcludingTrash(expectedValue), result, true);
    }

    @Test public void testTaggedWith() {
        when(dsf.isIncludeTrashItems()).thenReturn(false);
        Set<Tag> tags = new LinkedHashSet<>();

        final String expectedValue = setTaggedWithQuery(tags, dsf);
        assertEquals("", expectedValue);

        Tag mock1 = mock(Tag.class);
        mock1.setId("id1");
        mock1.setValue("tag1");
        
        when(mock1.getId()).thenReturn("id1");

        Tag mock2 = mock(Tag.class);
        mock1.setId("id2");
        mock1.setValue("tag2");
        
        when(mock2.getId()).thenReturn("id2");

        tags.add(mock1);
        final String expectedValue1 = setTaggedWithQuery(tags, dsf);
        assertEquals("id1", expectedValue1);

        tags.add(mock2);

        final String expectedValue2 = setTaggedWithQuery(tags, dsf);
        assertEquals("id1,id2", expectedValue2);

    }

    /**
     * @return the expected value
     */
    private String setOwnedBy(final String givenValue) {
        when(dsf.getOwnedBy()).thenReturn(givenValue);
        return "{\"nested\":{\"query\":{\"bool\":{\"must\":[{\"term\":{\"userPermissions.permission\":\"own\"}},{\"wildcard\":{\"userPermissions.user\":\""
                + givenValue + "#*\"}}]}},\"path\":\"userPermissions\"}}";
    }

    /**
     * @return the expected value
     */
    private String setCreatedWithin(final Date fromDate, final Date toDate) {
        DateInterval di = mock(DateInterval.class);
        when(di.getFrom()).thenReturn(fromDate);
        when(di.getTo()).thenReturn(toDate);

        when(dsf.getCreatedWithin()).thenReturn(di);

        if (fromDate == null && toDate == null) {
            return null;
        }

        if (fromDate == null) {
            return "{\"range\":{\"dateCreated\":{\"lte\":\"" + toDate.getTime() + "\"}}}";
        } else if (toDate == null) {
            return "{\"range\":{\"dateCreated\":{\"gte\":\"" + fromDate.getTime() + "\"}}}";
        }

        return "{\"range\":{\"dateCreated\":{\"gte\":\"" + fromDate.getTime() + "\",\"lte\":\""
                + toDate.getTime() + "\"}}}";
    }

    /**
     * @return the expected value
     */
    private String setFileQuery(final String givenQuery) {
        when(dsf.getFileQuery()).thenReturn(givenQuery);
        DataSearchQueryBuilder uut = new DataSearchQueryBuilder(dsf, userInfoMock);
        return uut.getSimpleQuery(DataSearchQueryBuilder.LABEL, givenQuery).getPayload();
    }

    /**
     * @return the expected value
     */
    private String setFileSizeRange(final Double min, final Double max) {
        FileSizeRange fsr = mock(FileSizeRange.class);
        when(fsr.getMin()).thenReturn(min);
        when(fsr.getMax()).thenReturn(max);

        when(dsf.getFileSizeRange()).thenReturn(fsr);
        if (min == null && max == null) {
            return null;
        }
        if (min == null) {
            return "{\"range\":{\"fileSize\":{\"lte\":\"" + max.longValue() + "\"}}}";
        } else if (max == null) {
            return "{\"range\":{\"fileSize\":{\"gte\":\"" + min.longValue() + "\"}}}";
        }
        return "{\"range\":{\"fileSize\":{\"gte\":\"" + min.longValue() + "\",\"lte\":\""
                + max.longValue() + "\"}}}";
    }

    /**
     * @return the expected value
     */
    private String setMetadataAttributeQuery(final String givenQuery) {
        when(dsf.getMetadataAttributeQuery()).thenReturn(givenQuery);
        DataSearchQueryBuilder uut = new DataSearchQueryBuilder(dsf, userInfoMock);
        String nestedQuery = "{\"nested\":{\"query\":"
                + uut.getSimpleQuery(DataSearchQueryBuilder.METADATA_ATTRIBUTE, givenQuery).getPayload()
                + ",\"path\":\"metadata\"}}";
        String fileQuery = "{\"has_child\":{\"query\":"+nestedQuery+",\"score_mode\":\"max\",\"type\":\"file_metadata\"}}";
        String folderQuery = "{\"has_child\":{\"query\":"+nestedQuery+",\"score_mode\":\"max\",\"type\":\"folder_metadata\"}}";
        return "{\"bool\":{\"should\":[" + nestedQuery + "," + fileQuery + "," + folderQuery + "]}}";
    }

    /**
     * @return the expected value
     */
    private String setMetadataValueQuery(final String givenQuery) {
        when(dsf.getMetadataValueQuery()).thenReturn(givenQuery);
        DataSearchQueryBuilder uut = new DataSearchQueryBuilder(dsf, userInfoMock);
        String nestedQuery = "{\"nested\":{\"query\":"
                + uut.getSimpleQuery(DataSearchQueryBuilder.METADATA_VALUE, givenQuery).getPayload()
                + ",\"path\":\"metadata\"}}";
        String fileQuery = "{\"has_child\":{\"query\":"+nestedQuery+",\"score_mode\":\"max\",\"type\":\"file_metadata\"}}";
        String folderQuery = "{\"has_child\":{\"query\":"+nestedQuery+",\"score_mode\":\"max\",\"type\":\"folder_metadata\"}}";
        return "{\"bool\":{\"should\":[" + nestedQuery + "," + fileQuery + "," + folderQuery + "]}}";
    }

    /**
     * @return the expected value
     */
    private String setModifiedWithin(final Date from, final Date to) {
        DateInterval di = mock(DateInterval.class);
        when(di.getFrom()).thenReturn(from);
        when(di.getTo()).thenReturn(to);

        when(dsf.getModifiedWithin()).thenReturn(di);
        if(from == null && to == null) {
            return null;
        }
        if (from == null) {
            return "{\"range\":{\"dateModified\":{\"lte\":\""
                    + to.getTime() + "\"}}}";
        } else if (to == null) {
            return "{\"range\":{\"dateModified\":{\"gte\":\"" + from.getTime() + "\"}}}";
        }
        return "{\"range\":{\"dateModified\":{\"gte\":\"" + from.getTime() + "\",\"lte\":\""
                + to.getTime() + "\"}}}";
    }

    /**
     * @return the expected value
     */
    private String setNegatedFileQuery(final String givenSearchTerms) {
        when(dsf.getNegatedFileQuery()).thenReturn(givenSearchTerms);

        DataSearchQueryBuilder uut = new DataSearchQueryBuilder(dsf, userInfoMock);
        return uut.getSimpleQuery(DataSearchQueryBuilder.LABEL, givenSearchTerms).getPayload();
    }

    /**
     * @return the expected value
     */
    private String setSharedWith(final String givenValue) {
        when(dsf.getSharedWith()).thenReturn(givenValue);
        return "{\"bool\":{\"must\":[{\"nested\":{\"query\":{\"bool\":{\"must\":[{\"term\":{\"userPermissions.permission\":\"own\"}},{\"wildcard\":{\"userPermissions.user\":\""
                + userInfoMock.getUsername()
                + "#*\"}}]}},\"path\":\"userPermissions\"}},{\"nested\":{\"query\":{\"wildcard\":{\"userPermissions.user\":\""
                + givenValue + "#*\"}},\"path\":\"userPermissions\"}}]}}";
    }

    private String setTaggedWithQuery(final Set<Tag> tags, final DiskResourceQueryTemplate drqt) {
        when(dsf.getTagQuery()).thenReturn(tags);
        DataSearchQueryBuilder uut = new DataSearchQueryBuilder(dsf, userInfoMock);
        return uut.taggedWith();
    }

    private String wrappedQuery(String query) {
        return Format.substitute("{\"bool\":{\"must_not\":[],\"must\":[{0}]}}", query);
    }

    private String wrappedNegatedQuery(String query) {
        return Format.substitute("{\"bool\":{\"must_not\":[{0}],\"must\":[]}}", query);
    }

    private String wrappedQueryExcludingTrash(String query) {
        return Format.substitute("{\"bool\":{\"must_not\":[],\"must\":[{0}]}}", query);
    }
}
