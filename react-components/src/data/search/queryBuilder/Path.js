import React, { Fragment } from "react";

import ids from "../ids";
import { options } from "./Operators";
import SelectOperator from "./SelectOperator";

import { build, FormTextField } from "@cyverse-de/ui-lib";
import { Field } from "formik";

/**
 * A component which allows users to specify a path prefix in QueryBuilder
 */
const PATH_DEFAULT = { prefix: "" };
function Path(props) {
    const operators = [options.Begins, options.BeginsNot];

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
                name={`${name}.prefix`}
                fullWidth={false}
                id={build(parentId, ids.path)}
                component={FormTextField}
            />
        </Fragment>
    );
}

export { Path, PATH_DEFAULT };
