/**
 * @author sriram
 *
 */
import React, {Component} from "react";
import DEHyperLink from "../../../src/util/hyperlink/DEHyperLink";

class DEHyperLinkTest extends Component {
    render() {
        let linkText = "Test links";

        return(
                <DEHyperLink text={linkText}/>
        );
    }
}

export default DEHyperLinkTest;
