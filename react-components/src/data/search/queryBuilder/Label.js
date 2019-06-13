import React, { Fragment } from "react";

import ids from "../ids";
import { options } from "./Operators";
import SelectOperator from "./SelectOperator";

import { build, FormTextField } from "@cyverse-de/ui-lib";
import { Field, getIn } from "formik";

/**
 * A component which allows users to specify file names in QueryBuilder
 */
const LABEL_DEFAULT = { label: "" };

function Label(props) {
    const operators = [
        options.Contains,
        options.Is,
        options.ContainsNot,
        options.IsNot,
    ];

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
                name={`${name}.label`}
                fullWidth={false}
                id={build(parentId, ids.fileName)}
                component={FormTextField}
            />
        </Fragment>
    );
}

export { Label, LABEL_DEFAULT };
