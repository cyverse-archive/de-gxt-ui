import build from "../../../util/DebugIDUtil";
import ids from "../ids";
import { options } from "./Operators";
import ReduxTextField from "./ReduxTextField";
import SelectOperator from "./SelectOperator";

import { Field } from "redux-form";
import React, { Fragment } from "react";

/**
 * A component that will allow users to fill out a starting and ending date, currently used
 * to choose either a Creation date range or Modified date range
 */
function Date(props) {
    let operators = [
        options.Between,
        options.BetweenNot,
    ];

    let {parentId} = props;

    return (
        <Fragment>
            <SelectOperator operators={operators}
                            parentId={parentId}/>
            <Field name='from'
                   type='date'
                   id={build(parentId, ids.fromDate)}
                   component={ReduxTextField}/>
            <Field name='to'
                   type='date'
                   id={build(parentId, ids.toDate)}
                   component={ReduxTextField}/>
        </Fragment>
    )
}

export default Date;