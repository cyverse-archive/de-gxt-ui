import React, {Component} from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

import {getCyVerseTheme} from '../../../src/lib';
import Tag from '../../../src/tags/tagSearch/Tag';

class TagTest extends Component {
    render() {

        const editLogger = this.props.editLogger || ((selection) => {
            console.log(selection);
        });

        const deleteLogger = this.props.deleteLogger || ((selection) => {
            console.log(selection);
        });

        const tag = {
            value: 'This is a Tag',
            description: 'A description of some sort'
        };

        return (
            <MuiThemeProvider muiTheme={getCyVerseTheme()}>
                <Tag tag={tag}
                     onClick={editLogger}
                     onRequestDelete={deleteLogger}/>
            </MuiThemeProvider>);
    }
}

export default TagTest;
