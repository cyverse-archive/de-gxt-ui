import * as apps from "./apps";
import * as collaborators from "./collaborators";
import * as data from "./data";
import * as desktop from "./desktop";
import * as fileViewers from "./fileViewers";
import * as metadata from "./metadata";
import * as util from "./util/util";
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


export {
    apps,
    collaborators,
    data,
    desktop,
    fileViewers,
    metadata,
    util,
    getDefaultTheme,
    MuiThemeProvider
};
