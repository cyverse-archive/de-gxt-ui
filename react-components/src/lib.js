import * as analysis from "./analysis";
import * as apps from "./apps";
import * as collaborators from "./collaborators";
import * as communities from "./communities";
import * as data from "./data";
import * as desktop from "./desktop";
import * as fileViewers from "./fileViewers";
import * as metadata from "./metadata";
import * as notifications from "./notifications";
import { palette } from "@cyverse-de/ui-lib";
import React from "react";
import ReactDOM from "react-dom";
import { MuiThemeProvider, createMuiTheme } from "@material-ui/core/styles"; // v1.x

// gwt-react needs React and ReactDOM on the global object
window.React = React;
window.ReactDOM = ReactDOM;

const adjustZIndexForGXT = 888888;

const getDefaultTheme = () =>
    createMuiTheme({
        palette: {
            primary: {
                main: palette.blue,
            },
            secondary: {
                main: palette.lightBlue,
            },
        },
        typography: {
            button: {
                textTransform: "none",
            },
            useNextVariants: true,
        },
        zIndex: {
            mobileStepper: 1000 + adjustZIndexForGXT,
            // Not sure about appBar, but MUI docs recommend customizing all zIndex values.
            // So far appBars are only used in modal dialogs...
            appBar: 1100 + adjustZIndexForGXT,
            drawer: 1200 + adjustZIndexForGXT,
            modal: 1300 + adjustZIndexForGXT,
            snackbar: 1400 + adjustZIndexForGXT,
            tooltip: 1500 + adjustZIndexForGXT,
        },
    });

export {
    analysis,
    apps,
    collaborators,
    communities,
    data,
    desktop,
    fileViewers,
    metadata,
    notifications,
    getDefaultTheme,
    MuiThemeProvider,
};
