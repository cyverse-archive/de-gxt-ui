import * as apps from './apps';

import React from "react";
import ReactDOM from "react-dom";

import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import cyverseTheme from 'cyverse-ui/lib/styles/cyverseTheme';

// gwt-react needs React and ReactDOM on the global object
window.React = React;
window.ReactDOM = ReactDOM;

const getCyVerseTheme = () => getMuiTheme(cyverseTheme);

export { apps, getCyVerseTheme, MuiThemeProvider };
