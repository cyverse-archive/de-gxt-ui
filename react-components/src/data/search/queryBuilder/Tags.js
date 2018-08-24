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
    const operators = [
        options.Are,
        options.AreNot
    ];

    const {
        parentId,
        helperProps: {
            array,
            presenter,
            classes
        }
    } = props;

    return (
        <Fragment>
            <SelectOperator operators={operators}
                            parentId={parentId}/>
            <Field name='tags'
                   parentId={parentId}
                   placeholder={''}
                   array={array}
                   classes={classes}
                   presenter={presenter}
                   validate={[]}
                   component={renderTagSearchField}/>
        </Fragment>
    )
}

function renderTagSearchField(props) {
    const {
        input,
        array,
        parentId,
        placeholder,
        presenter,
        classes
    } = props;
    return (
        <Grid item className={classes.autocompleteField}>
            <SearchFormTagPanel parentId={parentId}
                                placeholder={placeholder}
                                presenter={presenter}
                                array={array}
                                tagQuery={input}/>
        </Grid>
    )
}

export default Tags;