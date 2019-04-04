import React from "react";
import ReactDOM from "react-dom";
import { getDefaultTheme, MuiThemeProvider } from "../lib";
import DesktopViewTest from "../../stories/desktop/view/DesktopView.stories";
import TaskbarTest from "../../stories/desktop/view/Taskbar.stories";
import TaskButtonTest from "../../stories/desktop/view/Taskbar.stories";

/*
// This test fails because of WebSocket object not being available during tests.
//  ReferenceError: WebSocket is not defined
it('renders DesktopViewTest without crashing', () => {
    const div = document.createElement('div');
    ReactDOM.render(<MuiThemeProvider
        theme={getDefaultTheme()}><DesktopViewTest /></MuiThemeProvider>, div);
    ReactDOM.unmountComponentAtNode(div);
});
*/

it("renders TaskbarTest without crashing", () => {
    const div = document.createElement("div");
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <TaskbarTest />
        </MuiThemeProvider>,
        div
    );
    ReactDOM.unmountComponentAtNode(div);
});

it("renders TaskButtonTest without crashing", () => {
    const div = document.createElement("div");
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <TaskButtonTest />
        </MuiThemeProvider>,
        div
    );
    ReactDOM.unmountComponentAtNode(div);
});
