/**
 * @author psarando
 */
import React from "react";

import { getIn } from "formik";
import moment from "moment";

import build from "./DebugIDUtil";
import Autocomplete from "./Autocomplete";

import Checkbox from "@material-ui/core/Checkbox";
import FormControl from "@material-ui/core/FormControl";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import FormHelperText from "@material-ui/core/FormHelperText";
import InputLabel from "@material-ui/core/InputLabel";
import Select from "@material-ui/core/Select";
import TableCell from "@material-ui/core/TableCell";
import TextField from "@material-ui/core/TextField";

const getFormError = (name, touched, errors) => {
    const error = getIn(errors, name);
    const touch = getIn(touched, name);

    return touch && error ? error : null;
};

const FormTextField = ({
    field,
    label,
    required,
    form: { touched, errors },
    ...custom
}) => {
    const errorMsg = getFormError(field.name, touched, errors);
    return (
        <TextField
            label={label}
            error={!!errorMsg}
            helperText={errorMsg}
            required={required}
            variant="outlined"
            margin="dense"
            fullWidth
            {...field}
            {...custom}
        />
    );
};

const FormSearchField = ({
    field: { value, onBlur, onChange, ...field },
    label,
    required,
    form: { touched, errors, setFieldTouched, setFieldValue },
    ...props
}) => {
    const errorMsg = getFormError(field.name, touched, errors);
    const onOptionSelected = (option) => {
        setFieldValue(field.name, option ? option[props.valueKey] : "");
    };
    const onSearchBlur = (event) => {
        setFieldTouched(field.name, true);
    };

    return (
        <FormControl fullWidth error={!!errorMsg}>
            <Autocomplete
                label={label}
                controlShouldRenderValue
                isClearable={!required}
                cacheOptions
                defaultInputValue={value}
                onChange={onOptionSelected}
                onBlur={onSearchBlur}
                {...field}
                {...props}
            />
            <FormHelperText>{errorMsg}</FormHelperText>
        </FormControl>
    );
};

// Apparently only necessary for FastField, but maybe more correct for "vanilla" Field as well.
const onCheckboxChange = (setFieldValue, fieldName) => (event, checked) => {
    setFieldValue(fieldName, checked);
};

const FormCheckbox = ({
    label,
    field: { value, onChange, ...field },
    form: { setFieldValue },
    ...custom
}) => (
    <FormControlLabel
        control={
            <Checkbox
                checked={!!value}
                onChange={onCheckboxChange(setFieldValue, field.name)}
                {...field}
                {...custom}
            />
        }
        label={label}
    />
);

const FormCheckboxTableCell = ({
    field: { value, onChange, ...field },
    form: { setFieldValue },
    ...custom
}) => (
    <TableCell padding="checkbox">
        <Checkbox
            color="primary"
            checked={!!value}
            onClick={(event) => event.stopPropagation()}
            onChange={onCheckboxChange(setFieldValue, field.name)}
            {...custom}
        />
    </TableCell>
);

const FormCheckboxStringValue = ({
    field: { value, onChange, ...field },
    form: { setFieldValue, ...form },
    ...custom
}) => (
    <FormCheckbox
        checked={value && value !== "false"}
        onChange={(event, checked) => {
            setFieldValue(field.name, checked ? "true" : "false");
        }}
        field={field}
        form={form}
        {...custom}
    />
);

const onNumberChange = (onChange) => (event) => {
    const newValue = event.target.value;
    let intVal = Number(newValue);
    if (!isNaN(intVal)) {
        onChange(event);
    }
};

const onIntegerChange = (onChange) => (event) => {
    const newValue = event.target.value;
    let intVal = Number(newValue);
    if (!isNaN(intVal) && Number.isInteger(intVal)) {
        onChange(event);
    }
};

const FormNumberField = ({ field: { onChange, ...field }, ...props }) => (
    <FormTextField
        type="number"
        step="any"
        onChange={onNumberChange(onChange)}
        field={field}
        {...props}
    />
);

const FormIntegerField = ({ field: { onChange, ...field }, ...props }) => (
    <FormTextField
        type="number"
        step={1}
        onChange={onIntegerChange(onChange)}
        field={field}
        {...props}
    />
);

const FormMultilineTextField = (props) => (
    <FormTextField multiline rows={3} {...props} />
);

const onDateChange = (prevDate, fieldName, setFieldValue) => (event) => {
    const newValue = event.target.value;
    const date = prevDate ? prevDate : new Date();
    const time = moment(date).format("HH:mm:ss");

    setFieldValue(fieldName, `${newValue} ${time}`);
};

const onTimeChange = (prevDate, fieldName, setFieldValue) => (event) => {
    const newValue = event.target.value;
    const date = prevDate ? prevDate : new Date();
    const dateStr = moment(date).format("YYYY-MM-DD");

    setFieldValue(fieldName, `${dateStr} ${newValue}`);
};

const FormTimestampField = ({
    id,
    label,
    required,
    field: { value, onChange, ...field },
    form: { touched, errors, setFieldValue },
    ...custom
}) => {
    const errorMsg = getFormError(field.name, touched, errors);
    const date = value && Date.parse(value);

    return (
        <FormControl error={!!errorMsg}>
            <TextField
                id={build(id, "date")}
                type="date"
                variant="outlined"
                label={label}
                error={!!errorMsg}
                required={required}
                value={date ? moment(date).format("YYYY-MM-DD") : ""}
                onChange={onDateChange(date, field.name, setFieldValue)}
                {...field}
                {...custom}
            />
            <TextField
                id={build(id, "time")}
                type="time"
                variant="outlined"
                error={!!errorMsg}
                required={required}
                value={date ? moment(date).format("HH:mm:ss") : ""}
                onChange={onTimeChange(date, field.name, setFieldValue)}
                {...field}
                {...custom}
            />
            <FormHelperText>{errorMsg}</FormHelperText>
        </FormControl>
    );
};

const FormSelectField = ({
    id,
    field: { value, ...field },
    label,
    required,
    form: { touched, errors },
    children,
    ...custom
}) => {
    const errorMsg = getFormError(field.name, touched, errors);
    return (
        <FormControl fullWidth error={!!errorMsg}>
            <InputLabel htmlFor={id} required={required}>
                {label}
            </InputLabel>
            <Select id={id} value={value || ""} {...field} {...custom}>
                {children}
            </Select>
            <FormHelperText>{errorMsg}</FormHelperText>
        </FormControl>
    );
};

export {
    FormCheckbox,
    FormCheckboxStringValue,
    FormCheckboxTableCell,
    FormIntegerField,
    FormMultilineTextField,
    FormNumberField,
    FormSearchField,
    FormSelectField,
    FormTextField,
    FormTimestampField,
    getFormError,
};
