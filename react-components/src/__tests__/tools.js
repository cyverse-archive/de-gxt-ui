import React from "react";
import ReactDOM from "react-dom";

import { getDefaultTheme, MuiThemeProvider } from "../lib";
import EditToolTest from "../../stories/tools/EditTool.stories";
import NewToolRequestFormTest from "../../stories/tools/requests/NewToolRequestsForm.stories";

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
