import React from "react";
import ReactDOM from "react-dom";

import { getDefaultTheme, MuiThemeProvider } from "../lib";

import EditToolTest from "../../stories/tools/EditTool.stories";
import ManageToolsTest from "../../stories/tools/ManageTools.stories";

it("renders EditToolTest without crashing", () => {
    const div = document.createElement("div");
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <EditToolTest />
        </MuiThemeProvider>,
        div
    );
    ReactDOM.unmountComponentAtNode(div);
});

it("renders ManageToolsTest without crashing", () => {
    const div = document.createElement("div");
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <ManageToolsTest />
        </MuiThemeProvider>,
        div
    );
    ReactDOM.unmountComponentAtNode(div);
});
