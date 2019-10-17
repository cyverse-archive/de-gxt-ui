import React from "react";
import ReactDOM from "react-dom";

import { getDefaultTheme, MuiThemeProvider } from "../lib";

import TeamsTest from "../../stories/teams/Teams.stories";

it("renders TeamsTest without crashing", () => {
    const div = document.createElement("div");
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <TeamsTest />
        </MuiThemeProvider>,
        div
    );
    ReactDOM.unmountComponentAtNode(div);
});
