import React, {Component} from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

import {getCyVerseTheme} from '../../../src/lib';
import TagSearchField from '../../../src/tags/tagSearch/TagSearchField';

class TagSearchTest extends Component {
    render() {

        const editLogger = this.props.editLogger || ((selection) => {
            console.log(selection);
        });

        const searchLogger = this.props.searchLogger || ((selection) => {
            console.log(selection);
        });

        const addLogger = this.props.addLogger || ((selection) => {
            console.log(selection);
        });

        const deleteTag = (tag) => {
            deleteLogger(tag);
            let index = tags.indexOf(tag);
            tags.splice(index, 1);
        };

        const deleteLogger = this.props.deleteLogger || ((selection) => {
            console.log(selection);
        });

        const suggestedTags = [
            {
                value: 'apples'
            },
            {
                value: 'oranges'
            },
            {
                value: 'tangerines'
            },
            {
                value: 'kiwis'
            }
        ];

        const taggedWith = (tag) => {
            if (tag)
                return tag;
            return '';
        };

        const tags = [
            {
                value: 'some'
            },
            {
                value: 'tags',
                description: 'MOAR TAGS'
            }
        ];

        return (
            <MuiThemeProvider muiTheme={getCyVerseTheme()}>
                <TagSearchField label='Tagged with'
                                onEditTagSelected={editLogger}
                                fetchTagSuggestions={searchLogger}
                                onDeleteTagSelected={deleteTag}
                                suggestedTags={suggestedTags}
                                onTagSelected={addLogger}
                                onTagValueChange={taggedWith}
                                taggedWith={taggedWith()}
                                tags={tags}/>
            </MuiThemeProvider>);
    }
}

export default TagSearchTest;
