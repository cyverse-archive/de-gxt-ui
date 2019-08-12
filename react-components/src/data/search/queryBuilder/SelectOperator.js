/**
 * @author aramsey
 *
 * A Select field which enables the user to toggle different parameters such as "exact", "negated", and
 * "permission_recurse".
 *
 * An operator is a way of mapping English terms such as "contains" or "is" to values for those
 * parameters.  For example, "contains" would mean "exact" is set to false whereas "is" would mean
 * "exact" is set to true.
 *
 * This enables each rendered Clause in a search query to be readable in English.
 * For example, "File Name Contains ___" or "Path Begins With ___".
 */
import React from "react";

import ids from "../ids";
import { operatorMap } from "./Operators";

import { build, FormSelectField } from "@cyverse-de/ui-lib";
import { Field } from "formik";
import { MenuItem } from "@material-ui/core";

function SelectOperator(props) {
    const { operators, parentId, name } = props;

    return (
        <Field
            name={`${name}.opLabel`}
            render={({ field, form: { setFieldValue, ...form }, ...props }) => {
                if (!field.value) {
                    handleChange(operators[0].value, name, setFieldValue);
                }
                return (
                    <FormSelectField
                        {...props}
                        fullWidth={false}
                        id={build(parentId, ids.selectOperator, ids.inputField)}
                        field={field}
                        form={form}
                        onChange={(event) => {
                            handleChange(
                                event.target.value,
                                name,
                                setFieldValue
                            );
                        }}
                    >
                        {operators.map((item, index) => {
                            return (
                                <MenuItem key={index} value={item.value}>
                                    {item.label}
                                </MenuItem>
                            );
                        })}
                    </FormSelectField>
                );
            }}
        />
    );
}

// Handles setting `opLabel`, `exact`, `permission_recurse`, and `negated`
// based on the singular drop down selection they made e.g. Contains, Is, Is Not...
function handleChange(operatorName, field, setFieldValue) {
    let operator = operatorMap[operatorName];
    setFieldValue(`${field}.opLabel`, operatorName);

    Object.keys(operator).forEach((key) => {
        let value = operator[key];
        setFieldValue(`${field}.${key}`, value);
    });
}

export default SelectOperator;
