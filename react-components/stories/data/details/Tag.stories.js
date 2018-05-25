/**
 @author sriram
 */
import React, {Component} from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import {getCyVerseTheme} from "../../../src/lib";
import Tag from "../../../src/data/details/Tag";

class TagTest extends Component {

    render() {
        const logger = this.props.logger || ((tag) => {
                console.log(tag);
            });

        const tag = {id: 1, value: 'testag'};

        return (
            <MuiThemeProvider muiTheme={getCyVerseTheme()}>
                <Tag tag={tag} removeTag={logger} onClick={logger}/>
            </MuiThemeProvider>
        );
    }
}

export default TagTest;
