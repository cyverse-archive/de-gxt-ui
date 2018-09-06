/**
 * @author psarando
 */
import React from "react";

import { getIn } from 'formik';

import Checkbox from "@material-ui/core/Checkbox";
import FormControl from "@material-ui/core/FormControl";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import InputLabel from "@material-ui/core/InputLabel";
import Select from "@material-ui/core/Select";
import TableCell from "@material-ui/core/TableCell";
import TextField from "@material-ui/core/TextField";

const getFormikErrorMessage = (name, touched, errors) => {
    const error = getIn(errors, name);
    const touch = getIn(touched, name);

    return touch && error ? error : null;
};

const FormikTextField = ({
    field,
    label,
    required,
    form: {touched, errors},
    ...custom
}) => (
    <TextField
        label={label}
        error={!!getFormikErrorMessage(field.name, touched, errors)}
        helperText={getFormikErrorMessage(field.name, touched, errors)}
        required={required}
        fullWidth
        {...field}
        {...custom}
    />
);

const FormikCheckbox = ({
    field: {value, ...field},
    label,
    ...custom
}) => (
    <FormControlLabel
        control={
            <Checkbox checked={!!value}
                      {...field}
                      {...custom}
            />
        }
        label={label}
    />
);

const onNumberChange = onChange => (event) => {
    const newValue = event.target.value;
    let intVal = Number(newValue);
    if (!isNaN(intVal)) {
        onChange(event);
    }
};

const onIntegerChange = onChange => (event) => {
    const newValue = event.target.value;
    let intVal = Number(newValue);
    if (!isNaN(intVal) && Number.isInteger(intVal)) {
        onChange(event);
    }
};

const FormikNumberField = ({
    field: {onChange, ...field},
    ...props
}) => (
    <FormikTextField type="number"
                     step="any"
                     onChange={onNumberChange(onChange)}
                     field={field}
                     {...props}
    />
);

const FormikIntegerField = ({
    field: {onChange, ...field},
    ...props
}) => (
    <FormikTextField type="number"
                     step={1}
                     onChange={onIntegerChange(onChange)}
                     field={field}
                     {...props}
    />
);

const FormTextField = ({
    input,
    label,
    required,
    meta: {touched, error},
    ...custom
}) => (
    <TextField
        label={label}
        error={touched && !!error}
        helperText={touched && error}
        required={required}
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

const FormCheckboxTableCell = ({ input, ...custom }) => (
    <TableCell padding="checkbox">
        <Checkbox color="primary"
                  checked={!!input.value}
                  onClick={event => event.stopPropagation()}
                  onChange={input.onChange}
                  {...custom}
        />
    </TableCell>
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
    FormCheckboxTableCell,
    FormSelectField,
    FormTextField,
    FormikCheckbox,
    FormikTextField,
    FormikNumberField,
    FormikIntegerField,
};
