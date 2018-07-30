import { options } from "./Operators";
import ReduxTextField from "./ReduxTextField";
import SelectOperator from "./SelectOperator";

import { Field } from "redux-form";
import React, { Fragment } from "react";

/**
 * A component which allows users to specify a path prefix in QueryBuilder
 */
function Path() {
    let operators = [
        options.Begins,
        options.BeginsNot,
    ];

    return (
        <Fragment>
            <SelectOperator operators={operators}/>
            <Field name='prefix'
                   operators={operators}
                   component={ReduxTextField}/>
        </Fragment>
    )
}

export default Path;