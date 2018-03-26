import React from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import {getCyVerseTheme} from "../../../src/lib";
import SearchForm from "../../../src/diskResource/search/SearchForm";

class SearchFormTest extends React.Component {

    render() {
        const logger = this.props.logger || ((selection) => {
            console.log(selection);
        });

        const id = 'dataSearchForm';

        const presenter = {
            onSearchBtnClicked: logger
        };

        const appearance = {
            nameHas: () => 'File/Folder name has the words',
            createdWithin: () => 'Created within',
            createdWithinItems: () => '1 day, 3 days, 1 week, 2 weeks, 1 month, 2 months, 6 months, 1 year',
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
            searchBtn: () => 'Search'
        };

        return (
            <MuiThemeProvider muiTheme={getCyVerseTheme()}>
                <SearchForm presenter={presenter}
                            appearance={appearance}
                            id={id}/>
            </MuiThemeProvider>
        )
    }
}

export default SearchFormTest;