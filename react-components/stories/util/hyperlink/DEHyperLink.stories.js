/**
 * @author sriram
 *
 */
import React, {Component} from "react";
import {  text } from '@storybook/addon-knobs';
import DEHyperLink from "../../../src/util/hyperlink/DEHyperLink";

class DEHyperLinkTest extends Component {
    render() {
        let linkText = "Test links";

        return(
                <DEHyperLink text={text('Link Text', linkText)}/>
        );
    }
}

export default DEHyperLinkTest;

