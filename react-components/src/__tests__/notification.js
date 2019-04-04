import React from "react";
import ReactDOM from "react-dom";
import { getDefaultTheme, MuiThemeProvider } from "../lib";
import NotificationViewTest from "../../stories/notifications/view/NotificationView.stories";
import DenyJoinRequestDetailsDialogTest from "../../stories/notifications/view/dialogs/DenyJoinRequestDetailsDialog.stories";
import JoinTeamRequestDialogTest from "../../stories/notifications/view/dialogs/JoinTeamRequestDialog.stories";
import RequestHistoryDialogTest from "../../stories/notifications/view/dialogs/RequestHistoryDialog.stories";

it("renders NotificationViewTest without crashing", () => {
    const div = document.createElement("div");
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <NotificationViewTest />
        </MuiThemeProvider>,
        div
    );
    ReactDOM.unmountComponentAtNode(div);
});
it("renders DenyJoinRequestDetailsDialogTest without crashing", () => {
    const div = document.createElement("div");
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <DenyJoinRequestDetailsDialogTest />
        </MuiThemeProvider>,
        div
    );
    ReactDOM.unmountComponentAtNode(div);
});
it("renders JoinTeamRequestDialogTest without crashing", () => {
    const div = document.createElement("div");
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <JoinTeamRequestDialogTest />
        </MuiThemeProvider>,
        div
    );
    ReactDOM.unmountComponentAtNode(div);
});
it("renders RequestHistoryDialogTest without crashing", () => {
    const div = document.createElement("div");
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <RequestHistoryDialogTest />
        </MuiThemeProvider>,
        div
    );
    ReactDOM.unmountComponentAtNode(div);
});
