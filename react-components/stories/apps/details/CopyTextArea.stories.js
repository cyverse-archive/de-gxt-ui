/**
 * @author psarando
 */
import React, { Component } from 'react';
import CopyTextArea from '../../../src/apps/details/CopyTextArea';

class CopyTextAreaTest extends Component {
    render () {
        let textToCopy =
`The Dark Arts better be worried,
oh boy!`;

        return ( <CopyTextArea btnText='Copy' text={ textToCopy } /> );
    }
}

export default CopyTextAreaTest;
