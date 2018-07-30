import { options } from "./Operators";
import ReduxTextField from "./ReduxTextField";
import SelectOperator from "./SelectOperator";

import { Field } from 'redux-form';
import React, { Fragment } from 'react';

/**
 * A component which allows users to specify an owner in QueryBuilder
 */

function Owner() {
    let operators = [
        options.Is,
        options.IsNot,
        options.Contains,
        options.ContainsNot
    ];

    return (
        <Fragment>
            <SelectOperator operators={operators}/>
            <Field name='owner'
                   operators={operators}
                   component={ReduxTextField}/>
        </Fragment>
    )
}

export default Owner;