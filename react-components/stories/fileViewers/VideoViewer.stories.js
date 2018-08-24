import React, { Component } from "react";
import VideoViewer from "../../src/fileViewers/VideoViewer";

class VideoViewerTest extends Component {

    render() {

        const url = 'https://youtu.be/Tx1XIm6q4r4';

        return (
            <VideoViewer url={url}/>
        )
    }
}

export default VideoViewerTest;