import SearchField from '../../src/util/SearchField';

import React, { Component } from 'react';

class SearchFieldTest extends Component {
    render() {
        const handleSearch = this.props.logger('Search') || ((selection) => {
            console.log(selection);
        });

        return (
            <SearchField handleSearch={handleSearch} />
        )
    }
}

export default SearchFieldTest;