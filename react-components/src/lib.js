import * as apps from "./apps";
import * as data from "./data";
import React from "react";
import ReactDOM from "react-dom";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import getMuiTheme from "material-ui/styles/getMuiTheme";
import cyverseTheme from "cyverse-ui/lib/styles/cyverseTheme";
import { MuiThemeProvider as NewMuiThemeProvider, createMuiTheme } from 'material-ui-next/styles';

// gwt-react needs React and ReactDOM on the global object
window.React = React;
window.ReactDOM = ReactDOM;

const getCyVerseTheme = () => getMuiTheme(cyverseTheme);

const getDefaultTheme = () => createMuiTheme();

export {apps, data, getDefaultTheme, getCyVerseTheme, MuiThemeProvider, NewMuiThemeProvider};
