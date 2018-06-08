import React from 'react';
import {SearchForm} from "../../../src/data/search";

class SearchFormTest extends React.Component {

    render() {
        const searchLogger = this.props.logger('Search Submitted') || ((selection) => {
            console.log(selection);
        });

        const editTagLogger = this.props.logger('Edit Tag') || ((selection) => {
            console.log(selection);
        });

        const suggestionsLogger = this.props.logger('Fetch Suggestions') || ((selection) => {
            console.log(selection);
        });

        const saveSearchLogger = this.props.logger('Save Search') || ((selection) => {
            console.log(selection);
        });

        const addTagLogger = this.props.logger('Add Tag') || ((selection) => {
            console.log(selection);
        });

        const id = 'dataSearchForm';

        const presenter = {
            onSearchBtnClicked: searchLogger,
            onAddTagSelected: addTagLogger,
            onEditTagSelected: editTagLogger,
            fetchTagSuggestions: suggestionsLogger,
            onSaveSearch: saveSearchLogger
        };

        const suggestedTags = [
            {
                id: '1',
                value: 'apples',
                description: 'old apples'
            },
            {
                id: '2',
                value: 'oranges',
                description: 'old oranges'
            },
            {
                id: '3',
                value: 'tangerines',
                description: 'old tangerines'
            },
            {
                id: '4',
                value: 'kiwis',
                description: 'old kiwis'
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
            <SearchForm presenter={presenter}
                        dateIntervals={dateIntervals}
                        suggestedTags={suggestedTags}
                        id={id}
                        initialValues={template}/>
        )
    }
}

export default SearchFormTest;