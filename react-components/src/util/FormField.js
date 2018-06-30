/**
 * @author psarando
 */
import React from "react";

import Checkbox from "@material-ui/core/Checkbox";
import FormControl from "@material-ui/core/FormControl";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import InputLabel from "@material-ui/core/InputLabel";
import Select from "@material-ui/core/Select";
import TextField from "@material-ui/core/TextField";

const FormTextField = ({
    input,
    label,
    meta: {touched, error},
    ...custom
}) => (
    <TextField
        label={label}
        error={touched && !!error}
        helperText={touched && error}
        fullWidth
        {...input}
        {...custom}
    />
);

const FormCheckbox = ({ input, label, ...custom }) => (
    <FormControlLabel
        control={
            <Checkbox checked={!!input.value}
                      onChange={input.onChange}
                      {...custom}
            />
        }
        label={label}
    />
);

const FormSelectField = ({
    input,
    label,
    id,
    children,
    ...custom
}) => (
    <FormControl fullWidth>
        <InputLabel htmlFor={id}>{label}</InputLabel>
        <Select id={id}
                value={input.value || ""}
                {...input}
                {...custom}
        >
            {children}
        </Select>
    </FormControl>
);

export {
    FormCheckbox,
    FormSelectField,
    FormTextField,
};
