import React from 'react';
import ReactDOM from 'react-dom';

import {getDefaultTheme, MuiThemeProvider} from "../lib";

import CategoryTreeTest from '../../stories/apps/details/CategoryTree.stories';
import ToolDetailsTest from '../../stories/apps/details/ToolDetails.stories';
import CopyTextAreaTest from '../../stories/util/CopyTextArea.stories';
import AppStatsTest from '../../stories/apps/admin/AppStats.stories';

it('renders CopyTextArea without crashing', () => {
  const div = document.createElement('div');
  ReactDOM.render(<MuiThemeProvider theme={getDefaultTheme()}><CopyTextAreaTest /></MuiThemeProvider>, div);
  ReactDOM.unmountComponentAtNode(div);
});

it('renders CategoryTree without crashing', () => {
  const div = document.createElement('div');
  ReactDOM.render(<MuiThemeProvider theme={getDefaultTheme()}><CategoryTreeTest /></MuiThemeProvider>, div);
  ReactDOM.unmountComponentAtNode(div);
});

it('renders ToolDetails without crashing', () => {
    const div = document.createElement('div');
    ReactDOM.render(<MuiThemeProvider theme={getDefaultTheme()}><ToolDetailsTest /></MuiThemeProvider>, div);
    ReactDOM.unmountComponentAtNode(div);
});

it('renders ToolDetails without crashing', () => {
    const div = document.createElement('div');
    ReactDOM.render(<MuiThemeProvider theme={getDefaultTheme()}><AppStatsTest /></MuiThemeProvider>, div);
    ReactDOM.unmountComponentAtNode(div);
});



