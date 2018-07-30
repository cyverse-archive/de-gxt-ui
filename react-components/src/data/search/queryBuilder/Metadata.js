import { getMessage } from "../../../util/I18NWrapper";
import { options } from "./Operators";
import ReduxTextField from "./ReduxTextField";
import SelectOperator from "./SelectOperator";

import { Field } from "redux-form";
import React, { Fragment } from "react";

/**
 * A component which allows users to specify metadata attribute and values in QueryBuilder
 */
function Metadata() {
    let operators = [
        options.Is,
        options.IsNot
    ];

    return (
        <Fragment>
            <SelectOperator operators={operators}/>
            <Field name='attribute'
                   label={getMessage('attribute')}
                   operators={operators}
                   component={ReduxTextField}/>
            <Field name='value'
                   label={getMessage('value')}
                   operators={operators}
                   component={ReduxTextField}/>
        </Fragment>
    )
}

export default Metadata;