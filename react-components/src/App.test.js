import React from 'react';
import ReactDOM from 'react-dom';

import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

import { getCyVerseTheme } from './lib';
import App from './apps/details/CopyTextArea';

it('renders without crashing', () => {
  const div = document.createElement('div');
  ReactDOM.render(<MuiThemeProvider muiTheme={getCyVerseTheme()}><App /></MuiThemeProvider>, div);
  ReactDOM.unmountComponentAtNode(div);
});
