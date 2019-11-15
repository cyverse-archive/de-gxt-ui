import React from "react";
import ReactDOM from "react-dom";

import { getDefaultTheme, MuiThemeProvider } from "../lib";
import EditToolTest from "../../stories/tools/EditTool.stories";
import ManageToolsTest from "../../stories/tools/ManageTools.stories";
import NewToolRequestFormTest from "../../stories/tools/requests/NewToolRequestsForm.stories";
import TooInfoDialogTest from "../../stories/tools/ToolInfoDialog.stories";

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

it("renders NewToolRequestForm without crashing", () => {
    const div = document.createElement("div");
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <NewToolRequestFormTest />
        </MuiThemeProvider>,
        div
    );
    ReactDOM.unmountComponentAtNode(div);
});

it("renders Tool Details without crashing", () => {
    const div = document.createElement("div");
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <TooInfoDialogTest />
        </MuiThemeProvider>,
        div
    );
    ReactDOM.unmountComponentAtNode(div);
});
