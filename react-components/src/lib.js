import * as apps from "./apps";
import * as data from "./data";
import React from "react";
import ReactDOM from "react-dom";
import {MuiThemeProvider, createMuiTheme} from "@material-ui/core/styles"; // v1.x


// gwt-react needs React and ReactDOM on the global object
window.React = React;
window.ReactDOM = ReactDOM;


const getDefaultTheme = () => createMuiTheme({});

export {
    apps,
    data,
    getDefaultTheme,
    MuiThemeProvider
};
