import React from "react";
import ReactDOM from "react-dom";

import {getDefaultTheme, MuiThemeProvider} from "../lib";

import CommunitiesViewTest from "../../stories/communities/CommunitiesView.stories";

it('renders CommunitiesView without crashing', () => {
    const div = document.createElement('div');
    ReactDOM.render(<MuiThemeProvider theme={getDefaultTheme()}><CommunitiesViewTest /></MuiThemeProvider>, div);
    ReactDOM.unmountComponentAtNode(div);
});
