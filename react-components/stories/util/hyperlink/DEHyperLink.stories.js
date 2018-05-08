/**
 * @author sriram
 *
 */
import React, { Component } from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import { getCyVerseTheme } from '../../../src/lib';
import DEHyperLink from '../../../src/util/hyperlink/DEHyperLink';

class DEHyperLinkTest extends Component {
    render() {
        let linkText = "Test links";

        return(
            <MuiThemeProvider muiTheme={getCyVerseTheme()}>
                <DEHyperLink text={linkText}/>
            </MuiThemeProvider>
        );
    }
}

export default DEHyperLinkTest;