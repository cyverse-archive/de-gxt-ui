import ids from "../ids";
import { options } from "./Operators";
import SearchFormTagPanel from "../SearchFormTagPanel";
import SelectOperator from "./SelectOperator";

import { Field } from "redux-form";
import Grid from "@material-ui/core/Grid";
import React, { Fragment } from "react";

/**
 * A component which allows users to specify tags in QueryBuilder
 */
function Tags(props) {
    let operators = [
        options.Are,
        options.AreNot
    ];

    let {
        helperProps: {
            array,
            presenter
        }
    } = props;

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

function renderTagSearchField(props) {
    let {
        input,
        array,
        parentId,
        placeholder,
        presenter
    } = props;
    return (
        <Grid item>
            <SearchFormTagPanel parentId={parentId}
                                placeholder={placeholder}
                                presenter={presenter}
                                array={array}
                                tagQuery={input}/>
        </Grid>
    )
}

export default Tags;