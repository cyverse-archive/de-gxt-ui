import Autocomplete from "../../src/util/Autocomplete";

import React, { Component } from "react";

/**
 * @author aramsey
 */
class AutocompleteTest extends Component {
    render() {
        const selectOptionLogger = this.props.selectOptionLogger || ((selection) => {
            console.log(selection);
        });

        let placeholder = 'Search for Fruit';

        let options = [
            {
                id: '1',
                value: 'apples',
                display: 'Apples - The Staple',
                description: 'old apples'
            },
            {
                id: '2',
                value: 'oranges',
                display: 'Oranges - Meh',
                description: 'old oranges'
            },
            {
                id: '3',
                value: 'tangerines',
                display: 'Tangerines - The Best',
                description: 'old tangerines'
            },
            {
                id: '4',
                value: 'kiwis',
                display: 'Kiwis - The Fuzzy',
                description: 'old kiwis'
            }
        ];

        return (
            <Autocomplete variant='creatable'
                          placeholder={placeholder}
                          labelKey='display'
                          valueKey='value'
                          onChange={selectOptionLogger}
                          options={options}
            />
        );
    }
}

export default AutocompleteTest;
