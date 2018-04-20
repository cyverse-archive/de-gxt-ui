/**
 *
 * @author sriram
 *
 */
import React, {Component} from "react";
import {getDefaultTheme, NewMuiThemeProvider, getCyVerseTheme} from "../../../src/lib";
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import DesktopView from '../../../src/desktop/view/DesktopView';

class DesktopViewTest extends  Component {
    render() {
        return (
            <NewMuiThemeProvider muiTheme={getDefaultTheme()}>
                <MuiThemeProvider muiTheme={getCyVerseTheme()}>
                    <DesktopView/>
                </MuiThemeProvider>
            </NewMuiThemeProvider>
        )
    }
}

export default DesktopViewTest;
