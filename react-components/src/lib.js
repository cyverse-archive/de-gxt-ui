import * as apps from "./apps";
import * as data from "./data";
import * as util from './util/util';
import * as desktop from "./desktop";
import palette from "./util/CyVersePalette";
import React from "react";
import ReactDOM from "react-dom";
import { MuiThemeProvider, createMuiTheme } from "@material-ui/core/styles"; // v1.x


// gwt-react needs React and ReactDOM on the global object
window.React = React;
window.ReactDOM = ReactDOM;


const getDefaultTheme = () => createMuiTheme({
    palette: {
        primary: {
            main: palette.blue,
        },
        secondary: {
            main: palette.lightBlue
        }
    }
});


export {apps, data, desktop, getDefaultTheme, MuiThemeProvider};
