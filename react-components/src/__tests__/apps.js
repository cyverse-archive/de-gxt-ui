import React from "react";
import ReactDOM from "react-dom";

import { getDefaultTheme, MuiThemeProvider } from "../lib";
import AppGridListingTest from "../../stories/apps/listing/AppGridListing.stories";
import AppTileListingTest from "../../stories/apps/listing/AppTileListing.stories";
import AppStatsTest from "../../stories/apps/admin/AppStats.stories";

import AppDetailsTest from "../../stories/apps/details/AppDetails.stories";
import AppDocTest from "../../stories/apps/details/AppDoc.stories";
import AppInfoDialogTest from "../../stories/apps/details/AppInfoDialog.stories";
import CategoryTreeTest from "../../stories/apps/details/CategoryTree.stories";
import ToolDetailsTest from "../../stories/apps/details/ToolDetails.stories";

import CreateQuickLaunchDialogTest from "../../stories/apps/quickLaunch/CreateQuickLaunchDialog.stories";
import QuickLaunchListingTest from "../../stories/apps/quickLaunch/QuickLaunchListing.stories";

it("renders CategoryTree without crashing", () => {
    const div = document.createElement("div");
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <CategoryTreeTest />
        </MuiThemeProvider>,
        div
    );
    ReactDOM.unmountComponentAtNode(div);
});

it("renders ToolDetails without crashing", () => {
    const div = document.createElement("div");
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <ToolDetailsTest />
        </MuiThemeProvider>,
        div
    );
    ReactDOM.unmountComponentAtNode(div);
});

it("renders AppStats without crashing", () => {
    const div = document.createElement("div");
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <AppStatsTest />
        </MuiThemeProvider>,
        div
    );
    ReactDOM.unmountComponentAtNode(div);
});

it("renders AppDetails without crashing", () => {
    const div = document.createElement("div");
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <AppDetailsTest />
        </MuiThemeProvider>,
        div
    );
    ReactDOM.unmountComponentAtNode(div);
});

it("renders AppDoc without crashing", () => {
    const div = document.createElement("div");
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <AppDocTest />
        </MuiThemeProvider>,
        div
    );
    ReactDOM.unmountComponentAtNode(div);
});

it("renders AppInfoDialog without crashing", () => {
    const div = document.createElement("div");
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <AppInfoDialogTest />
        </MuiThemeProvider>,
        div
    );
    ReactDOM.unmountComponentAtNode(div);
});

it("renders Quick launch without crashing", () => {
    const div = document.createElement("div");
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <QuickLaunchListingTest />
        </MuiThemeProvider>,
        div
    );
    ReactDOM.unmountComponentAtNode(div);
});

it("renders create Quick launch dialog without crashing", () => {
    const div = document.createElement("div");
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <CreateQuickLaunchDialogTest />
        </MuiThemeProvider>,
        div
    );
    ReactDOM.unmountComponentAtNode(div);
});

it("renders app grid listing without crashing", () => {
    const div = document.createElement("div");
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <AppGridListingTest />
        </MuiThemeProvider>,
        div
    );
    ReactDOM.unmountComponentAtNode(div);
});

it("renders app tile listing without crashing", () => {
    const div = document.createElement("div");
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <AppTileListingTest />
        </MuiThemeProvider>,
        div
    );
    ReactDOM.unmountComponentAtNode(div);
});
