import ids from "../ids";
import { options } from "./Operators";
import SearchFormTagPanel from "../SearchFormTagPanel";
import SelectOperator from "./SelectOperator";

import { Fields } from "redux-form";
import React, { Component, Fragment } from "react";


class Tags extends Component {
    render() {
        let operators = [
            options.Is,
            options.IsNot
        ];

        let {
            array,
            presenter
        } = this.props;

        return (
            <Fragment>
                <SelectOperator operators={operators}/>
                <Fields names={['taggedWith', 'tags']}
                        parentId={ids.form}
                        placeholder={''}
                        array={array}
                        presenter={presenter}
                        component={renderTagSearchField}/>
            </Fragment>

        )
    }
}

function renderTagSearchField(props) {
    let {
        taggedWith,
        tags,
        array,
        parentId,
        placeholder,
        presenter
    } = props;
    return (
        <SearchFormTagPanel parentId={parentId}
                            placeholder={placeholder}
                            presenter={presenter}
                            array={array}
                            tagQuery={tags}
                            taggedWith={taggedWith}/>
    )
}

export default Tags;