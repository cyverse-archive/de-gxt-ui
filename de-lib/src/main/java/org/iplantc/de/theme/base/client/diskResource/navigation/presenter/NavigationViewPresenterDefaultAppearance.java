package org.iplantc.de.theme.base.client.diskResource.navigation.presenter;

import org.iplantc.de.diskResource.client.NavigationView;
import org.iplantc.de.resources.client.messages.IplantErrorStrings;
import org.iplantc.de.theme.base.client.desktop.DesktopErrorMessages;
import org.iplantc.de.theme.base.client.diskResource.navigation.NavigationDisplayStrings;

import com.google.gwt.core.client.GWT;

/**
 * @author jstroot
 */
public class NavigationViewPresenterDefaultAppearance implements NavigationView.Presenter.Appearance {
    private final NavigationDisplayStrings displayStrings;
    private final IplantErrorStrings iplantErrorStrings;
    private DesktopErrorMessages desktopErrorMessages;

    public NavigationViewPresenterDefaultAppearance() {
        this(GWT.<NavigationDisplayStrings> create(NavigationDisplayStrings.class),
             GWT.<IplantErrorStrings> create(IplantErrorStrings.class),
             GWT.<DesktopErrorMessages> create(DesktopErrorMessages.class));
    }

    NavigationViewPresenterDefaultAppearance(final NavigationDisplayStrings displayStrings,
                                             final IplantErrorStrings iplantErrorStrings,
                                             DesktopErrorMessages desktopErrorMessages) {
        this.displayStrings = displayStrings;
        this.iplantErrorStrings = iplantErrorStrings;
        this.desktopErrorMessages = desktopErrorMessages;
    }

    @Override
    public String diskResourceDoesNotExist(String folderName) {
        return iplantErrorStrings.diskResourceDoesNotExist(folderName);
    }

    @Override
    public String retrieveFolderInfoFailed() {
        return displayStrings.retrieveFolderInfoFailed();
    }

    @Override
    public String savedFiltersRetrievalFailure() {
        return displayStrings.savedFiltersRetrievalFailure();
    }

    @Override
    public String permissionErrorMessage() {
        return desktopErrorMessages.permissionErrorMessage();
    }

    @Override
    public String permissionErrorTitle() {
        return desktopErrorMessages.permissionErrorTitle();
    }


    @Override
    public String fileExist() {
        return iplantErrorStrings.fileExist();
    }

    @Override
    public String uploadFailErrorMessage() {
        return displayStrings.uploadFailErrorMessage();
    }
}
