package org.iplantc.de.fileViewers.client.views;

import org.iplantc.de.client.models.diskResources.File;
import org.iplantc.de.commons.client.util.CyVerseReactComponents;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author aramsey
 */
public class VideoViewerImpl extends AbstractFileViewer {

    private HTMLPanel panel;

    public VideoViewerImpl(final File file,
                           final String videoUrl) {
        super(file, null);
        panel = new HTMLPanel("<div></div>");

        showVideo(videoUrl);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    private void showVideo(String url) {
        ReactVideoViewer.VideoViewerProps props = new ReactVideoViewer.VideoViewerProps();
        props.url = url;

        CyVerseReactComponents.render(ReactVideoViewer.VideoViewer,
                                      props,
                                      panel.getElement());
    }

    @Override
    public String getEditorContent() {
        return null;
    }

    @Override
    public void setData(Object data) { /* Do nothing intentionally */ }

    @Override
    public boolean isDirty() {
        return false;
    }
}
