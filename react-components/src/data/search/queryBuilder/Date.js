import React, { Fragment } from "react";

import ids from "../ids";
import { options } from "./Operators";
import SelectOperator from "./SelectOperator";

import { build, FormTimestampField, getMessage } from "@cyverse-de/ui-lib";
import { Field } from "formik";

/**
 * A component that will allow users to fill out a starting and ending date, currently used
 * to choose either a Creation date range or Modified date range
 */
const DATE_DEFAULT = { from: "", to: "" };

function Date(props) {
    const operators = [options.Between, options.BetweenNot];

    const {
        parentId,
        field: { name },
    } = props;

    return (
        <Fragment>
            <SelectOperator
                operators={operators}
                parentId={parentId}
                name={name}
            />
            <Field
                name={`${name}.from`}
                helperText={getMessage("startDate")}
                id={build(parentId, ids.fromDate)}
                component={FormTimestampField}
            />
            <Field
                name={`${name}.to`}
                helperText={getMessage("endDate")}
                id={build(parentId, ids.toDate)}
                component={FormTimestampField}
            />
        </Fragment>
    );
}

export { Date, DATE_DEFAULT };
