import React from 'react';
import ReactDOM from 'react-dom';

import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

import { getCyVerseTheme } from '../lib';

import CategoryTreeTest from '../../stories/apps/details/CategoryTree.stories';
import ToolDetailsTest from '../../stories/apps/details/ToolDetails.stories';
import CopyTextAreaTest from '../../stories/util/CopyTextArea.stories';
import AppStatsTest from '../../stories/apps/admin/AppStats.stories';

it('renders CopyTextArea without crashing', () => {
  const div = document.createElement('div');
  ReactDOM.render(<MuiThemeProvider muiTheme={getCyVerseTheme()}><CopyTextAreaTest /></MuiThemeProvider>, div);
  ReactDOM.unmountComponentAtNode(div);
});

it('renders CategoryTree without crashing', () => {
  const div = document.createElement('div');
  ReactDOM.render(<MuiThemeProvider muiTheme={getCyVerseTheme()}><CategoryTreeTest /></MuiThemeProvider>, div);
  ReactDOM.unmountComponentAtNode(div);
});

it('renders ToolDetails without crashing', () => {
    const div = document.createElement('div');
    ReactDOM.render(<MuiThemeProvider muiTheme={getCyVerseTheme()}><ToolDetailsTest /></MuiThemeProvider>, div);
    ReactDOM.unmountComponentAtNode(div);
});

it('renders ToolDetails without crashing', () => {
    const div = document.createElement('div');
    ReactDOM.render(<MuiThemeProvider muiTheme={getCyVerseTheme()}><AppStatsTest /></MuiThemeProvider>, div);
    ReactDOM.unmountComponentAtNode(div);
});

