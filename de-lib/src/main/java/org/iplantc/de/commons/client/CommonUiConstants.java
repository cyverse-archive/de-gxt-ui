package org.iplantc.de.commons.client;

import com.google.gwt.i18n.client.Constants;

/**
 * Configurable constants shared by apps
 * @author jstroot
 */
public interface CommonUiConstants extends Constants {

    /**
     * URL to forums
     * 
     * @return a string representing the URL.
     */
    String learningCenterUrl();

    /**
     * URL for landing page.
     * 
     * @return a string representing the URL.
     */
    String cyverseHome();

    /**
     * URL to iplant services
     * 
     * @return a string representing the URL.
     */
    String supportUrl();

    /**
     * Characters that are not allowed in App names.
     * 
     * @return string representing the character set.
     */
    String appNameRestrictedChars();

    /**
     * Characters that are not allowed at the beginning of App names.
     * 
     * @return string representing the character set.
     */
    String appNameRestrictedStartingChars();

    /**
     * key board short cuts
     */
    String dataKeyShortCut();

    String appsKeyShortCut();

    String analysisKeyShortCut();

    String notifyKeyShortCut();

    String closeKeyShortCut();
    
    /**
     * iDrop download URL
     */
    String iDropDesktopClientInstructionsUrl();

    String faqUrl();

    String emailSupport();

    /**
     * The path to the wiki page "Creating Documentation for Tools"
     *
     * @return path to help file
     */
    String publishDocumentationUrl();

    /**
     * Logout url for the application
     *
     */
    String logoutUrl();

    String documentationTemplateUrl();
}
