package org.iplantc.de.fileViewers.client.views;

import org.iplantc.de.client.models.diskResources.File;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.media.client.Video;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

/**
 * @author aramsey
 */
public class VideoViewerImpl extends AbstractFileViewer {

    interface VideoViewerUiBinder extends UiBinder<Widget, VideoViewerImpl> { }

    @UiField VerticalLayoutContainer con;
    @UiField(provided = true) Video video;

    private static VideoViewerUiBinder uiBinder = GWT.create(VideoViewerUiBinder.class);

    public VideoViewerImpl(final File file,
                           final String videoUrl) {
        super(file, null);
        video = Video.createIfSupported();
        video.setSrc(videoUrl);
        video.setAutoplay(true);
        video.setControls(true);
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        con.fireEvent(event);
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
