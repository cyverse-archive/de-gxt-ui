import React from "react";

import ids from "../ids";
import { operatorMap } from "./Operators";

import { build, FormSelectField } from "@cyverse-de/ui-lib";
import { Field } from "formik";
import MenuItem from "@material-ui/core/MenuItem";

/**
 * A Select which prompts the user to choose between different Operators
 */
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
