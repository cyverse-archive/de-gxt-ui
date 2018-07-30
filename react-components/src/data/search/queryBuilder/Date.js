import { options } from "./Operators";
import ReduxTextField from "./ReduxTextField";
import SelectOperator from "./SelectOperator";

import { Field } from "redux-form";
import React, { Fragment } from "react";

/**
 * A component that will allow users to fill out a starting and ending date, currently used
 * to choose either a Creation date range or Modified date range
 */
function Date() {
    let operators = [
        options.Between,
        options.BetweenNot,
    ];

    return (
        <Fragment>
            <SelectOperator operators={operators}/>
            <Field name='from'
                   type='date'
                   component={ReduxTextField}/>
            <Field name='to'
                   type='date'
                   component={ReduxTextField}/>
        </Fragment>
    )
}

export default Date;