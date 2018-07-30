import build from "../../../util/DebugIDUtil";
import ids from "../ids";
import { options } from "./Operators";
import ReduxTextField from "./ReduxTextField";
import SelectOperator from "./SelectOperator";

import { Field } from 'redux-form';
import React, { Fragment } from 'react';

/**
 * A component which allows users to specify an owner in QueryBuilder
 */

function Owner(props) {
    let operators = [
        options.Is,
        options.IsNot,
        options.Contains,
        options.ContainsNot
    ];

    let {parentId} = props;

    return (
        <Fragment>
            <SelectOperator operators={operators}
                            parentId={parentId}/>
            <Field name='owner'
                   operators={operators}
                   id={build(parentId, ids.owner)}
                   component={ReduxTextField}/>
        </Fragment>
    )
}

export default Owner;