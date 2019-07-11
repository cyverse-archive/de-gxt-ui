import React from "react";
import ReactDOM from "react-dom";

import { getDefaultTheme, MuiThemeProvider } from "../lib";

import { EditToolTest } from "../../stories/tools/EditTool.stories";

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
