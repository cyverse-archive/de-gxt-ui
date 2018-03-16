import React from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import {getCyVerseTheme} from "../../../src/lib";
import SearchForm from "../../../src/diskResource/search/SearchForm";

class SearchFormTest extends React.Component {
    render() {
        return (
                <SearchForm/>
            <MuiThemeProvider muiTheme={getCyVerseTheme()}>
            </MuiThemeProvider>
        )
    }
}

export default SearchFormTest;