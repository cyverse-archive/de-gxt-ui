import React from 'react';
import ReactDOM from 'react-dom';

import {getDefaultTheme, MuiThemeProvider} from "../lib";

import BasicDetailsTest from '../../stories/data/details/BasicDetails.stories';
import EditTagDialogTest from '../../stories/data/search/EditTagDialog.stories';
import InfoTypeSelectionListTest from '../../stories/data/details/InfoTypeSelectionList.stories';
import SaveSearchButtonTest from '../../stories/data/search/SaveSearchButton.stories';
import SearchFormTagPanelTest from '../../stories/data/search/SearchFormTagPanel.stories';
import TagPanelTest from '../../stories/data/details/TagPanel.stories';
import TagTest from '../../stories/data/details/Tag.stories';

it('renders BasicDetailsTest without crashing', () => {
    const div = document.createElement('div');
    ReactDOM.render(<MuiThemeProvider theme={getDefaultTheme()}><BasicDetailsTest /></MuiThemeProvider>, div);
    ReactDOM.unmountComponentAtNode(div);
});

it('renders EditTagDialog without crashing', () => {
    const div = document.createElement('div');
    ReactDOM.render(<MuiThemeProvider theme={getDefaultTheme()}><EditTagDialogTest /></MuiThemeProvider>, div);
    ReactDOM.unmountComponentAtNode(div);
});

it('renders InfoTypeSelectionListTest without crashing', () => {
    const div = document.createElement('div');
    ReactDOM.render(<MuiThemeProvider theme={getDefaultTheme()}><InfoTypeSelectionListTest /></MuiThemeProvider>, div);
    ReactDOM.unmountComponentAtNode(div);
});

it('renders SaveSearchButton without crashing', () => {
    const div = document.createElement('div');
    ReactDOM.render(<MuiThemeProvider theme={getDefaultTheme()}><SaveSearchButtonTest /></MuiThemeProvider>, div);
    ReactDOM.unmountComponentAtNode(div);
});

it('renders SearchFormTagPanel without crashing', () => {
    const div = document.createElement('div');
    ReactDOM.render(<MuiThemeProvider theme={getDefaultTheme()}><SearchFormTagPanelTest /></MuiThemeProvider>, div);
    ReactDOM.unmountComponentAtNode(div);
});

it('renders TagPanelTest without crashing', () => {
    const div = document.createElement('div');
    ReactDOM.render(<MuiThemeProvider theme={getDefaultTheme()}><TagPanelTest /></MuiThemeProvider>, div);
    ReactDOM.unmountComponentAtNode(div);
});

it('renders TagTest without crashing', () => {
    const div = document.createElement('div');
    ReactDOM.render(<MuiThemeProvider theme={getDefaultTheme()}><TagTest /></MuiThemeProvider>, div);
    ReactDOM.unmountComponentAtNode(div);
});

