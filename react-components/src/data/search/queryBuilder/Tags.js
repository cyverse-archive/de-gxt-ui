import ids from "../ids";
import { options } from "./Operators";
import SearchFormTagPanel from "../SearchFormTagPanel";
import SelectOperator from "./SelectOperator";

import { Field } from "redux-form";
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
                <Field name='tags'
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
        input,
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
                            tagQuery={input}/>
    )
}

export default Tags;