package org.iplantc.de.resources.client.uiapps.integration;

import com.google.gwt.safehtml.shared.SafeHtml;

public interface AppIntegrationErrorMessages {

    String unableToSave();

    String appContainsErrorsUnableToSave();

    String appContainsErrorsPromptToContinue();

    String appUsesDeprecatedTools();

    SafeHtml cannotDeleteLastArgumentGroup();

    String workflowAddingDeprecatedTask();

    String workflowUsesDeprecatedTools();
}
