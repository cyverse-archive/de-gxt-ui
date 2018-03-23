import React from 'react';
import ReactDOM from 'react-dom';

import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

import { getCyVerseTheme } from '../lib';

import BasicDetailsTest from '../../stories/data/details/BasicDetails.stories';
import InfoTypeSelectionListTest from '../../stories/data/details/InfoTypeSelectionList.stories';
import TagPanelTest from '../../stories/data/details/TagPanel.stories';
import TagTest from '../../stories/data/details/Tag.stories';

it('renders BasicDetailsTest without crashing', () => {
    const div = document.createElement('div');
    ReactDOM.render(<MuiThemeProvider muiTheme={getCyVerseTheme()}><BasicDetailsTest /></MuiThemeProvider>, div);
    ReactDOM.unmountComponentAtNode(div);
});

it('renders InfoTypeSelectionListTest without crashing', () => {
    const div = document.createElement('div');
    ReactDOM.render(<MuiThemeProvider muiTheme={getCyVerseTheme()}><InfoTypeSelectionListTest /></MuiThemeProvider>, div);
    ReactDOM.unmountComponentAtNode(div);
});

it('renders TagPanelTest without crashing', () => {
    const div = document.createElement('div');
    ReactDOM.render(<MuiThemeProvider muiTheme={getCyVerseTheme()}><TagPanelTest /></MuiThemeProvider>, div);
    ReactDOM.unmountComponentAtNode(div);
});

it('renders TagTest without crashing', () => {
    const div = document.createElement('div');
    ReactDOM.render(<MuiThemeProvider muiTheme={getCyVerseTheme()}><TagTest /></MuiThemeProvider>, div);
    ReactDOM.unmountComponentAtNode(div);
});