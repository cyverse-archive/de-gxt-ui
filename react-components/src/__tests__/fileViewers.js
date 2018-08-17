import React from "react";
import ReactDOM from "react-dom";

import {getDefaultTheme, MuiThemeProvider} from "../lib";

import VideoViewerTest from "../../stories/fileViewers/VideoViewer.stories";

it('renders VideoViewer without crashing', () => {
    const div = document.createElement('div');
    ReactDOM.render(<MuiThemeProvider theme={getDefaultTheme()}><VideoViewerTest /></MuiThemeProvider>, div);
    ReactDOM.unmountComponentAtNode(div);
});
