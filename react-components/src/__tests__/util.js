import React from "react";
import ReactDOM from "react-dom";

import { getDefaultTheme, MuiThemeProvider } from "../lib";

import AutocompleteTest from "../../stories/util/Autocomplete.stories";
import CopyTextAreaTest from "../../stories/util/CopyTextArea.stories";
import ErrorExpansionPanel from "../../stories/util/ErrorExpansionPanel.stories";
import ErrorHandlerTest from "../../stories/util/ErrorHandler.stories";
import SearchFieldTest from "../../stories/util/SearchField.stories";
import TriggerFieldTest from "../../stories/util/TriggerField.stories";

it("renders Autocomplete without crashing", () => {
    const div = document.createElement("div");
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <AutocompleteTest />
        </MuiThemeProvider>,
        div
    );
    ReactDOM.unmountComponentAtNode(div);
});

it("renders CopyTextArea without crashing", () => {
    const div = document.createElement("div");
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <CopyTextAreaTest />
        </MuiThemeProvider>,
        div
    );
    ReactDOM.unmountComponentAtNode(div);
});

it("renders ErrorExpansionPanel without crashing", () => {
    const div = document.createElement("div");
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <ErrorExpansionPanel />
        </MuiThemeProvider>,
        div
    );
    ReactDOM.unmountComponentAtNode(div);
});

it("renders ErrorHandler without crashing", () => {
    const div = document.createElement("div");
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <ErrorHandlerTest />
        </MuiThemeProvider>,
        div
    );
    ReactDOM.unmountComponentAtNode(div);
});

it("renders SearchField without crashing", () => {
    const div = document.createElement("div");
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <SearchFieldTest />
        </MuiThemeProvider>,
        div
    );
    ReactDOM.unmountComponentAtNode(div);
});

it("renders TriggerField without crashing", () => {
    const div = document.createElement("div");
    ReactDOM.render(
        <MuiThemeProvider theme={getDefaultTheme()}>
            <TriggerFieldTest />
        </MuiThemeProvider>,
        div
    );
    ReactDOM.unmountComponentAtNode(div);
});
