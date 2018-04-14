/**
 * @author psarando
 */
import React, { Component } from 'react';

import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

import { getCyVerseTheme } from '../../src/lib';
import CopyTextArea from '../../src/util/CopyTextArea';

class CopyTextAreaTest extends Component {
    render () {
        let textToCopy =
`The Dark Arts better be worried,
oh boy!`;

        return (
            <MuiThemeProvider muiTheme={getCyVerseTheme()}>
                <CopyTextArea debugIdPrefix="test.id.prefix" btnText="Copy" copiedText="Copied!" text={ textToCopy } />
            </MuiThemeProvider>
        );
    }
}

export default CopyTextAreaTest;
