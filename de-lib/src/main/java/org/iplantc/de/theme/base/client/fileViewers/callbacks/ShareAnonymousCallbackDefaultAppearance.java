package org.iplantc.de.theme.base.client.fileViewers.callbacks;

import org.iplantc.de.fileViewers.client.callbacks.ShareAnonymousCallback;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.theme.base.client.fileViewers.FileViewerContextualHelpStrings;
import org.iplantc.de.theme.base.client.fileViewers.FileViewerStrings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * @author jstroot
 */
public class ShareAnonymousCallbackDefaultAppearance implements ShareAnonymousCallback.ShareAnonymousCallbackAppearance {

    private final FileViewerContextualHelpStrings helpStrings;
    private final IplantDisplayStrings displayStrings;
    private final IplantResources resources;
    private final FileViewerStrings fileViewerStrings;

    public ShareAnonymousCallbackDefaultAppearance() {
        this(GWT.<FileViewerStrings> create(FileViewerStrings.class),
             GWT.<FileViewerContextualHelpStrings> create(FileViewerContextualHelpStrings.class),
             GWT.<IplantDisplayStrings> create(IplantDisplayStrings.class),
             GWT.<IplantResources> create(IplantResources.class));
    }

    ShareAnonymousCallbackDefaultAppearance(final FileViewerStrings fileViewerStrings,
                                            final FileViewerContextualHelpStrings helpStrings,
                                            final IplantDisplayStrings displayStrings,
                                            final IplantResources resources) {
        this.fileViewerStrings = fileViewerStrings;
        this.helpStrings = helpStrings;
        this.displayStrings = displayStrings;
        this.resources = resources;
    }

    @Override
    public String copyPasteInstructions() {
        return displayStrings.copyPasteInstructions();
    }

    @Override
    public String ensemblUrl() {
        return fileViewerStrings.ensemblUrl();
    }

    @Override
    public SafeHtml notificationWithContextHelp() {
        return fileViewerStrings.sendToEnsemblePopupNote();
    }

    @Override
    public int notificationWithContextWidth() {
        return 600;
    }

    @Override
    public String sendToEnsemblMenuItem() {
        return displayStrings.sendToEnsemblMenuItem();
    }

    @Override
    public String sendToEnsemblUrlHelp() {
        return helpStrings.sendToEnsemblUrlHelp();
    }

    @Override
    public int sendToEnsemblUrlHelpPopupWidth() {
        return 480;
    }

    @Override
    public String ensemblUrlDialogWidth() {
        return "640";
    }

    @Override
    public String ensemblUrlDialogHeight() {
        return "320";
    }

    @Override
    public int ensemblUrlTextAreaWidth() {
        return 600;
    }

    @Override
    public int ensemblUrlTextAreaHeight() {
        return 180;
    }

}
