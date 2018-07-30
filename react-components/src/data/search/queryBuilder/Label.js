import build from "../../../util/DebugIDUtil";
import ids from "../ids";
import { options } from "./Operators";
import ReduxTextField from "./ReduxTextField";
import SelectOperator from "./SelectOperator";

import { Field } from "redux-form";
import React, { Fragment } from "react";

/**
 * A component which allows users to specify file names in QueryBuilder
 */
function Label(props) {
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
            <Field name='label'
                   id={build(parentId, ids.fileName)}
                   operators={operators}
                   component={ReduxTextField}/>
        </Fragment>
    )
}

export default Label;