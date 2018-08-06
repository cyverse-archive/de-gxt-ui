import build from "../../../util/DebugIDUtil";
import { getMessage } from "../../../util/I18NWrapper";
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
                   helperText={getMessage('startDate')}
                   id={build(parentId, ids.fromDate)}
                   validate={[]}
                   component={ReduxTextField}/>
            <Field name='to'
                   type='date'
                   helperText={getMessage('endDate')}
                   id={build(parentId, ids.toDate)}
                   validate={[]}
                   component={ReduxTextField}/>
        </Fragment>
    )
}

export default Date;