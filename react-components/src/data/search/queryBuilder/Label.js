import { options } from "./Operators";
import ReduxTextField from "./ReduxTextField";
import SelectOperator from "./SelectOperator";

import { Field } from "redux-form";
import React, { Fragment } from "react";

/**
 * A component which allows users to specify file names in QueryBuilder
 */
function Label() {
    let operators = [
        options.Is,
        options.IsNot,
        options.Contains,
        options.ContainsNot
    ];

    return (
        <Fragment>
            <SelectOperator operators={operators}/>
            <Field name='label'
                   operators={operators}
                   component={ReduxTextField}/>
        </Fragment>
    )
}

export default Label;