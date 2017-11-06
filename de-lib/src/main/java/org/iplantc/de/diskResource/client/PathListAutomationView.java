package org.iplantc.de.diskResource.client;

import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.diskResources.PathListRequest;
import org.iplantc.de.client.models.viewer.InfoType;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.List;

/**
 * An interface for the HT path list automation view in the Data window
 */
public interface PathListAutomationView extends IsWidget,
                                                IsMaskable {

    /**
     * Appearance class to handle all the text, icons, etc. for the HTPathListAutomationView
     */
    interface PathListAutomationAppearance {
        String inputLbl();

        String folderPathOnlyLbl();

        String selectorEmptyText();

        SafeHtml patternMatchLbl();

        String infoTypeLbl();

        String destLbl();

        String patternMatchEmptyText();

        String dialogHTHeading();

        String dialogMultiInputHeading();

        String loading();

        String processing();

        String requestSuccess();

        String requestFailed();

        SafeHtml formatRequiredFieldLbl(String label);

        String folderPathOnlyPrompt();

        String validationMessage();

        String dialogHeight();

        String dialogWidth();

        String listHeight();

        String destSelectorWidth();

        String select();
    }

    PathListRequest getRequest();

    boolean isValid();

    void addInfoTypes(List<InfoType> infoTypes);
}
