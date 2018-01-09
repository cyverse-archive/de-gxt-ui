package org.iplantc.de.theme.base.client.fileViewers;

import org.iplantc.de.fileViewers.client.FileViewer;

/**
 * Created by sriram on 1/8/18.
 */
public class FileViewerDefaultAppearance implements FileViewer.FileViewerAppearance {
    @Override
    public String windowWidth() {
        return "800";
    }

    @Override
    public String windowHeight() {
        return "480";
    }
}
