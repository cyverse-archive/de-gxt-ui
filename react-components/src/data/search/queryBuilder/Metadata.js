import React, { Fragment } from "react";

import ids from "../ids";
import { options } from "./Operators";
import SelectOperator from "./SelectOperator";

import { build, FormTextField, getMessage } from "@cyverse-de/ui-lib";
import { Field } from "formik";

/**
 * A component which allows users to specify metadata attribute and values in QueryBuilder
 */
const METADATA_DEFAULT = { attribute: "", value: "" };

function Metadata(props) {
    const operators = [options.Is, options.IsNot];

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
                name={`${name}.attribute`}
                label={getMessage("attribute")}
                id={build(parentId, ids.metadataAttr)}
                fullWidth={false}
                component={FormTextField}
            />
            <Field
                name={`${name}.value`}
                label={getMessage("value")}
                id={build(parentId, ids.metadataVal)}
                fullWidth={false}
                component={FormTextField}
            />
        </Fragment>
    );
}

export { Metadata, METADATA_DEFAULT };
