import React, { Fragment } from "react";

import ids from "../ids";
import styles from "../styles";
import { options } from "./Operators";
import SelectOperator from "./SelectOperator";
import { minValue } from "./Validations";

import {
    build,
    getMessage,
    FormSelectField,
    FormNumberField,
} from "@cyverse-de/ui-lib";
import { Field } from "formik";
import MenuItem from "@material-ui/core/MenuItem";
import { withStyles } from "@material-ui/core/styles";

/**
 * A component which allows users to specify a file size range in QueryBuilder
 */
const SIZE_DEFAULT = {
    from: { value: "", unit: "KB" },
    to: { value: "", unit: "KB" },
};

const FileSize = withStyles(styles)(FileSizeClause);

function FileSizeClause(props) {
    const operators = [options.Between, options.BetweenNot];

    const {
        parentId,
        field: { name },
        classes,
    } = props;

    return (
        <Fragment>
            <SelectOperator
                operators={operators}
                parentId={parentId}
                name={name}
            />
            <div className={classes.fileSize}>
                <Field
                    name={`${name}.from.value`}
                    label={getMessage("fileSizeGreater")}
                    id={build(parentId, ids.fileSizeGreaterVal)}
                    validate={minValue}
                    fullWidth={false}
                    component={FormNumberField}
                />
                <Field
                    name={`${name}.from.unit`}
                    id={build(parentId, ids.fileSizeGreaterUnit)}
                    label=" "
                    render={SizeUnit}
                />
            </div>
            <div className={classes.fileSize}>
                <Field
                    name={`${name}.to.value`}
                    label={getMessage("fileSizeLessThan")}
                    id={build(parentId, ids.fileSizeLessThanVal)}
                    validate={minValue}
                    fullWidth={false}
                    component={FormNumberField}
                />
                <Field
                    name={`${name}.to.unit`}
                    id={build(parentId, ids.fileSizeLessThanUnit)}
                    label=" "
                    render={SizeUnit}
                />
            </div>
        </Fragment>
    );
}

const sizesList = ["KB", "MB", "GB", "TB"];

function SizeUnit(props) {
    const {
        field,
        form: { setFieldValue, ...form },
        ...rest
    } = props;
    return (
        <FormSelectField form={form} field={field} fullWidth={false} {...rest}>
            {sizesList.map((item, index) => {
                return (
                    <MenuItem key={index} value={item} id={item}>
                        {item}
                    </MenuItem>
                );
            })}
        </FormSelectField>
    );
}

export { FileSize, SIZE_DEFAULT };
