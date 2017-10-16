package org.iplantc.de.theme.base.client.collaborators.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class MaskUtilTest {

    private UserSearchFieldDefaultAppearance.MaskUtil maskUtil;

    @Before
    public void setUp() {
        maskUtil = new UserSearchFieldDefaultAppearance.MaskUtil();
    }

    @Test
    public void testMask() {
        maskUtil.setSearchTerm("foo*bar");
        assertEquals("", maskUtil.mask("noMatch").toString());

        assertEquals("***foo***bar***", maskUtil.mask("foosecretbar").toString());
        assertEquals(3, maskUtil.getMatchStartIndex());
        assertEquals(12, maskUtil.getMatchEndIndex());

        assertEquals("***foo***bar***", maskUtil.mask("secretfoobarsecret").toString());
        assertEquals(3, maskUtil.getMatchStartIndex());
        assertEquals(12, maskUtil.getMatchEndIndex());


        maskUtil.setSearchTerm("foobar");
        assertEquals("", maskUtil.mask("noMatch").toString());

        assertEquals("***foobar***", maskUtil.mask("secretfoobarsecret").toString());
        assertEquals(3, maskUtil.getMatchStartIndex());
        assertEquals(9, maskUtil.getMatchEndIndex());

        assertEquals("***foobar***", maskUtil.mask("foobarsecretfoobar").toString());
        assertEquals(3, maskUtil.getMatchStartIndex());
        assertEquals(9, maskUtil.getMatchEndIndex());

        assertEquals("***foobar***", maskUtil.mask("foobar").toString());
        assertEquals(3, maskUtil.getMatchStartIndex());
        assertEquals(9, maskUtil.getMatchEndIndex());

        maskUtil.setSearchTerm("**foo**bar**");
        assertEquals("", maskUtil.mask("noMatch").toString());

        assertEquals("***foo***bar***", maskUtil.mask("foosecretbar").toString());
        assertEquals(3, maskUtil.getMatchStartIndex());
        assertEquals(12, maskUtil.getMatchEndIndex());

        assertEquals("***foo***bar***", maskUtil.mask("secretfoobarsecret").toString());
        assertEquals(3, maskUtil.getMatchStartIndex());
        assertEquals(12, maskUtil.getMatchEndIndex());
    }

    @Test
    public void testMask_upperCase() {
        maskUtil.setSearchTerm("FOO*BAR");

        assertEquals("", maskUtil.mask("noMatch").toString());
        assertEquals("***foo***bar***", maskUtil.mask("foosecretbar").toString());
        assertEquals(3, maskUtil.getMatchStartIndex());
        assertEquals(12, maskUtil.getMatchEndIndex());

        maskUtil.setSearchTerm("FOO*BAR");
        assertEquals("***foo***bar***@gmail.com", maskUtil.maskEmail("secretfoosecretbarsecret@gmail.com").toString());
        assertEquals(3, maskUtil.getMatchStartIndex());
        assertEquals(12, maskUtil.getMatchEndIndex());
    }

    @Test
    public void testMask_badregex() {
        maskUtil.setSearchTerm("[badregex");
        assertEquals("", maskUtil.mask("noMatch").toString());

        assertEquals("***[badregex***", maskUtil.mask("[badregexcompleted]").toString());
        assertEquals(3, maskUtil.getMatchStartIndex());
        assertEquals(12, maskUtil.getMatchEndIndex());

        assertEquals("***[badregex***", maskUtil.mask("secret[badregexsecret").toString());
        assertEquals(3, maskUtil.getMatchStartIndex());
        assertEquals(12, maskUtil.getMatchEndIndex());
    }

    @Test
    public void testMaskEmail() {
        maskUtil.setSearchTerm("foo*bar");
        assertEquals("***foo***bar***@gmail.com", maskUtil.maskEmail("secretfoosecretbarsecret@gmail.com").toString());
        assertEquals(3, maskUtil.getMatchStartIndex());
        assertEquals(12, maskUtil.getMatchEndIndex());

        assertEquals("***@blahfooblahbarblah.com", maskUtil.maskEmail("secret@blahfooblahbarblah.com").toString());
        assertEquals(8, maskUtil.getMatchStartIndex());
        assertEquals(18, maskUtil.getMatchEndIndex());

        assertEquals("***@foobar.com", maskUtil.maskEmail("secret@foobar.com").toString());
        assertEquals(4, maskUtil.getMatchStartIndex());
        assertEquals(10, maskUtil.getMatchEndIndex());
        assertEquals("***foo***bar***@gmail.com", maskUtil.maskEmail("secretfoosecretbar@gmail.com").toString());


        maskUtil.setSearchTerm("foo*bar@gmail.com");
        assertEquals("***foo***bar@gmail.com", maskUtil.maskEmail("secretfoosecretbar@gmail.com").toString());
        assertEquals(3, maskUtil.getMatchStartIndex());
        assertEquals(22, maskUtil.getMatchEndIndex());

        maskUtil.setSearchTerm("foo*r@g");
        assertEquals("***foo***r@gmail.com", maskUtil.maskEmail("secretfoosecretbar@gmail.com").toString());
        assertEquals(3, maskUtil.getMatchStartIndex());
        assertEquals(12, maskUtil.getMatchEndIndex());


        maskUtil.setSearchTerm("mail");
        assertEquals("***@gmail.com", maskUtil.maskEmail("secret@gmail.com").toString());
        assertEquals(5, maskUtil.getMatchStartIndex());
        assertEquals(9, maskUtil.getMatchEndIndex());
    }
}
