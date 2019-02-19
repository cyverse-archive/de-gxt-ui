import React from "react";
import ReactDOM from "react-dom";

import {getDefaultTheme, MuiThemeProvider} from "../lib";

import ManageCommunitiesViewTest from "../../stories/communities/ManageCommunitiesView.stories";

it('renders ManageCommunitiesView without crashing', () => {
    const div = document.createElement('div');
    ReactDOM.render(<MuiThemeProvider theme={getDefaultTheme()}><ManageCommunitiesViewTest /></MuiThemeProvider>, div);
    ReactDOM.unmountComponentAtNode(div);
});
