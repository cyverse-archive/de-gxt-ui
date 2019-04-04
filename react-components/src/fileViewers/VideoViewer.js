import PropTypes from "prop-types";
import React from "react";
import ReactPlayer from "react-player";

/**
 * @author aramsey
 * A simple component that will play the video at the given URL
 */
function VideoViewer(props) {
    return <ReactPlayer url={props.url} controls={true} />;
}

VideoViewer.propTypes = {
    url: PropTypes.string.isRequired,
};

export default VideoViewer;
