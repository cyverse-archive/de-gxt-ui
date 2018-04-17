import React from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import {getCyVerseTheme} from "../../../src/lib";
import SearchForm from "../../../src/diskResource/search/SearchForm";

class SearchFormTest extends React.Component {

    render() {
        const searchLogger = this.props.searchLogger || ((selection) => {
            console.log(selection);
        });

        const editTagLogger = this.props.editTagLogger || ((selection) => {
            console.log(selection);
        });

        const suggestionsLogger = this.props.suggestionsLogger || ((selection) => {
            console.log(selection);
        });

        const saveSearchLogger = this.props.saveSearchLogger || ((selection) => {
            console.log(selection);
        });

        const id = 'dataSearchForm';

        const presenter = {
            onSearchBtnClicked: searchLogger,
            onEditTagSelected: editTagLogger,
            fetchTagSuggestions: suggestionsLogger,
            onSaveSearch: saveSearchLogger
        };

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

        const appearance = {
            nameHas: () => 'File/Folder name has the words',
            createdWithin: () => 'Created within',
            nameHasNot: () => "File/Folder name doesn't have",
            modifiedWithin: () => 'Modified within',
            metadataAttributeHas: () => 'Metadata attribute has the words',
            ownedBy: () => 'Owned by',
            metadataValueHas: () => 'Metadata value has the words',
            sharedWith: () => 'Shared with',
            enterCyVerseUserName: () => 'Enter CyVerse user name',
            fileSizeGreater: () => 'File size is bigger than or equal to',
            fileSizeLessThan: () => 'File size is smaller than or equal to',
            includeTrash: () => 'Include items in Trash',
            taggedWith: () => 'Tagged with',
            fileSizes: () => 'KB, MB, GB, TB',
            searchBtn: () => 'Search',
            saveSearchBtnText: () => 'Save Search',
            filterName: () => 'Filter Name',
            requiredField: () => 'This field is required',
            saveSearchTitle: () => 'Save Filter',
            saveBtn: () => 'Save',
            cancelBtn: () => 'Cancel',
        };

        const dateIntervals = [
            {
                from: null,
                to: null,
                label: ''
            },
            {
                from: 1522738800000,
                to: 1522871293651,
                label: '1 day'
            },
            {
                from: 1522738800000,
                to: 1522871293651,
                label: '3 days'
            },
            {
                from: 1522738800000,
                to: 1522871293651,
                label: '1 week'
            },
            {
                from: 1522738800000,
                to: 1522871293651,
                label: '2 weeks'
            },
            {
                from: 1522738800000,
                to: 1522871293651,
                label: '1 month'
            },
            {
                from: 1522738800000,
                to: 1522871293651,
                label: '2 months'
            },
            {
                from: 1522738800000,
                to: 1522871293651,
                label: '6 months'
            },
            {
                from: 1522738800000,
                to: 1522871293651,
                label: '1 year'
            }
        ];

        const template = {
            label: 'oldtemplate',
            fileQuery: 'testzzzz',
            path: '/savedFilters/'
        };

        return (
            <MuiThemeProvider muiTheme={getCyVerseTheme()}>
                <SearchForm presenter={presenter}
                            appearance={appearance}
                            dateIntervals={dateIntervals}
                            suggestedTags={suggestedTags}
                            id={id}
                            template={template}/>
            </MuiThemeProvider>
        )
    }
}

export default SearchFormTest;