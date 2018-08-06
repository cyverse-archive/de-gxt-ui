import build from "../../../util/DebugIDUtil";
import ids from "../ids";
import { options } from "./Operators";
import ReduxTextField from "./ReduxTextField";
import SelectOperator from "./SelectOperator";
import Validations from "./Validations";

import { Field } from "redux-form";
import React, { Fragment } from "react";

/**
 * A component which allows users to specify file names in QueryBuilder
 */
function Label(props) {
    let operators = [
        options.Contains,
        options.Is,
        options.ContainsNot,
        options.IsNot,
    ];

    let {parentId} = props;

    return (
        <Fragment>
            <SelectOperator operators={operators}
                            parentId={parentId}/>
            <Field name='label'
                   id={build(parentId, ids.fileName)}
                   validate={Validations.nonEmptyField}
                   operators={operators}
                   component={ReduxTextField}/>
        </Fragment>
    )
}

export default Label;