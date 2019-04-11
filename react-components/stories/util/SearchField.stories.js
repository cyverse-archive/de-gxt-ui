import SearchField from "../../src/util/SearchField";

import React, { Component } from "react";

class SearchFieldTest extends Component {
    render() {
        const handleSearch =
            this.props.logger ||
            ((selection) => {
                console.log(selection);
            });

        return (
            <div>
                <label>Type at least 3 characters, then wait a second</label>
                <SearchField handleSearch={handleSearch} />
            </div>
        );
    }
}

export default SearchFieldTest;
