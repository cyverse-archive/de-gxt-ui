/**
 * @author psarando
 */
import React, {Component} from "react";
import CopyTextArea from "../../src/util/CopyTextArea";

class CopyTextAreaTest extends Component {
    render () {
        let textToCopy = `The Dark Arts better be worried,
         oh boy!`;

        return (
                <CopyTextArea debugIdPrefix="test.id.prefix" btnText="Copy" copiedText="Copied!" text={ textToCopy } />
        );
    }
}

export default CopyTextAreaTest;
