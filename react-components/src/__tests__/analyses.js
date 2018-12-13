import React from 'react';
import ReactDOM from 'react-dom';

import {getDefaultTheme, MuiThemeProvider} from "../lib";

import AnalysisCommentsDialogTest from "../../stories/analysis/view/dialogs/AnalysisCommentsDialog.stories";
import AnalysisInfoDialogTest from "../../stories/analysis/view/dialogs/AnalysisInfoDialog.stories";
import AnalysisParametersDialogTest from "../../stories/analysis/view/dialogs/AnalysisParametersDialog.stories";
import ShareWithSupportDialogTest from "../../stories/analysis/view/dialogs/ShareWithSupportDialog.stories";
import AnalysesViewTest from "../../stories/analysis/view/AnalysesView.stories";


it("renders AnalysisCommentsDialog without crashing", () => {
    const div = document.createElement('div');
    ReactDOM.render(<MuiThemeProvider theme={getDefaultTheme()}><AnalysisCommentsDialogTest /></MuiThemeProvider>, div);
    ReactDOM.unmountComponentAtNode(div);
});

it("renders AnalysisInfoDialog without crashing", () => {
    const div = document.createElement('div');
    ReactDOM.render(<MuiThemeProvider theme={getDefaultTheme()}><AnalysisInfoDialogTest /></MuiThemeProvider>, div);
    ReactDOM.unmountComponentAtNode(div);
});

it("renders AnalysisParametersDialog without crashing", () => {
    const div = document.createElement('div');
    ReactDOM.render(<MuiThemeProvider theme={getDefaultTheme()}><AnalysisParametersDialogTest /></MuiThemeProvider>, div);
    ReactDOM.unmountComponentAtNode(div);
});

it("renders ShareWithSupportDialog without crashing", () => {
    const div = document.createElement('div');
    ReactDOM.render(<MuiThemeProvider theme={getDefaultTheme()}><ShareWithSupportDialogTest /></MuiThemeProvider>, div);
    ReactDOM.unmountComponentAtNode(div);
});

it("renders AnalysesView without crashing", () => {
    const div = document.createElement('div');
    ReactDOM.render(<MuiThemeProvider theme={getDefaultTheme()}><AnalysesViewTest /></MuiThemeProvider>, div);
    ReactDOM.unmountComponentAtNode(div);
});

