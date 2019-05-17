import React, { Fragment } from "react";
import { Field } from "redux-form";

import { build, getMessage } from "@cyverse-de/ui-lib";
import ids from "../ids";
import { options } from "./Operators";
import ReduxTextField from "./ReduxTextField";
import SelectOperator from "./SelectOperator";

/**
 * A component that will allow users to fill out a starting and ending date, currently used
 * to choose either a Creation date range or Modified date range
 */
function Date(props) {
    const operators = [options.Between, options.BetweenNot];

    const { parentId } = props;

    return (
        <Fragment>
            <SelectOperator operators={operators} parentId={parentId} />
            <Field
                name="from"
                type="date"
                helperText={getMessage("startDate")}
                id={build(parentId, ids.fromDate)}
                validate={[]}
                component={ReduxTextField}
            />
            <Field
                name="to"
                type="date"
                helperText={getMessage("endDate")}
                id={build(parentId, ids.toDate)}
                validate={[]}
                component={ReduxTextField}
            />
        </Fragment>
    );
}

export default Date;
