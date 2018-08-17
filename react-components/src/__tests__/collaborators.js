import React from "react";
import ReactDOM from "react-dom";

import {getDefaultTheme, MuiThemeProvider} from "../lib";

import SubjectSearchFieldTest from "../../stories/collaborators/SubjectSearchField.stories";

it('renders SubjectSearchField without crashing', () => {
    const div = document.createElement('div');
    ReactDOM.render(<MuiThemeProvider theme={getDefaultTheme()}><SubjectSearchFieldTest /></MuiThemeProvider>, div);
    ReactDOM.unmountComponentAtNode(div);
});
