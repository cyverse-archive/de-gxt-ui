import React, { Fragment } from "react";

import ids from "../ids";
import { options } from "./Operators";
import ReduxTextField from "./ReduxTextField";
import SelectOperator from "./SelectOperator";
import Validations from "./Validations";

import { build } from "@cyverse-de/ui-lib";

import { Field } from "redux-form";

/**
 * A component which allows users to specify a path prefix in QueryBuilder
 */
function Path(props) {
    const operators = [options.Begins, options.BeginsNot];

    const { parentId } = props;

    return (
        <Fragment>
            <SelectOperator operators={operators} parentId={parentId} />
            <Field
                name="prefix"
                operators={operators}
                id={build(parentId, ids.path)}
                validate={Validations.nonEmptyField}
                component={ReduxTextField}
            />
        </Fragment>
    );
}

export default Path;
