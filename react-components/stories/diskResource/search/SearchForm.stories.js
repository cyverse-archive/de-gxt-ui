import React from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import {getCyVerseTheme} from "../../../src/lib";
import {SearchForm} from "../../../src/diskResource/search";

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
                            dateIntervals={dateIntervals}
                            suggestedTags={suggestedTags}
                            id={id}
                            template={template}/>
            </MuiThemeProvider>
        )
    }
}

export default SearchFormTest;