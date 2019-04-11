import SearchFormTagPanel from "../../../src/data/search/SearchFormTagPanel";

import React, { Component } from "react";

class SearchFormTagPanelTest extends Component {
    render() {
        const editTagLogger =
            this.props.editTagLogger ||
            ((selection) => {
                console.log(selection);
            });

        const addTagLogger =
            this.props.addTagLogger ||
            ((selection) => {
                console.log(selection);
            });

        const removeTagLogger =
            this.props.removeTagLogger ||
            ((selection) => {
                console.log(selection);
            });

        const appendTagLogger =
            this.props.appendTagLogger ||
            ((selection) => {
                console.log(selection);
            });

        const dataSource = [
            {
                id: "1",
                value: "apples",
                description: "old apples",
            },
            {
                id: "2",
                value: "oranges",
                description: "old oranges",
            },
            {
                id: "3",
                value: "tangerines",
                description: "old tangerines",
            },
            {
                id: "4",
                value: "kiwis",
                description: "old kiwis",
            },
        ];

        const array = {
            insert: appendTagLogger,
            remove: removeTagLogger,
        };

        const tagQuery = {
            input: {
                value: [
                    {
                        id: "10",
                        value: "cucumber",
                        description: "old cucumber",
                    },
                    {
                        id: "20",
                        value: "tomato",
                        description: "old tomato",
                    },
                    {
                        id: "30",
                        value: "carrot",
                        description: "old carrot",
                    },
                    {
                        id: "40",
                        value: "lettuce",
                        description: "old lettuce",
                    },
                ],
            },
        };

        const parentId = "searchForm";

        const placeholder = "Tagged With";

        const presenter = {
            onAddTagSelected: addTagLogger,
            onEditTagSelected: editTagLogger,
            fetchTagSuggestions: (data, fn) => fn(dataSource),
        };

        return (
            <SearchFormTagPanel
                parentId={parentId}
                placeholder={placeholder}
                presenter={presenter}
                array={array}
                tagQuery={tagQuery}
            />
        );
    }
}

export default SearchFormTagPanelTest;
