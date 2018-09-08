/**
 * @author psarando
 */
import React from "react";

import { getIn } from 'formik';
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
}) => {
    const errorMsg = getFormikErrorMessage(field.name, touched, errors);
    return (
        <TextField
            label={label}
            error={!!errorMsg}
            helperText={errorMsg}
            required={required}
            fullWidth
            {...field}
            {...custom}
        />
    )
};

const FormikSearchField = ({
    field: {value, onBlur, onChange, ...field},
    label,
    required,
    form: {touched, errors, setFieldTouched, setFieldValue},
    ...props
}) => {
    const errorMsg = getFormikErrorMessage(field.name, touched, errors);
    const onOptionSelected = option => {
        setFieldValue(field.name, option ? option[props.valueKey] : "");
    };
    const onSearchBlur = event => {
        setFieldTouched(field.name, true);
    };

    return (
        <FormControl fullWidth error={!!errorMsg}>
            <Autocomplete label={label}
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

const FormMultilineTextField = (props) => (
    <FormikTextField multiline
                     rows={3}
                     {...props}
    />
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
    field: {value, onChange, ...field},
    form: {touched, errors, setFieldValue},
    ...custom
}) => {
    const errorMsg = getFormikErrorMessage(field.name, touched, errors);
    const date = value && Date.parse(value);

    return (
        <FormControl error={!!errorMsg}>
            <TextField id={build(id, "date")}
                       type="date"
                       label={label}
                       error={!!errorMsg}
                       required={required}
                       value={date ? moment(date).format("YYYY-MM-DD") : ""}
                       onChange={onDateChange(date, field.name, setFieldValue)}
                       {...field}
                       {...custom}
            />
            <TextField id={build(id, "time")}
                       type="time"
                       error={!!errorMsg}
                       required={required}
                       value={date ? moment(date).format("HH:mm:ss") : ""}
                       onChange={onTimeChange(date, field.name, setFieldValue)}
                       {...field}
                       {...custom}
            />
            <FormHelperText>{errorMsg}</FormHelperText>
        </FormControl>
    )
};

const FormikSelectField = ({
    id,
    field: {value, ...field},
    label,
    required,
    form: {touched, errors},
    children,
    ...custom,
}) => {
    const errorMsg = getFormikErrorMessage(field.name, touched, errors);
    return (
        <FormControl fullWidth error={!!errorMsg}>
            <InputLabel htmlFor={id}
                        required={required}
            >
                {label}
            </InputLabel>
            <Select id={id}
                    value={value || ""}
                    {...field}
                    {...custom}
            >
                {children}
            </Select>
            <FormHelperText>{errorMsg}</FormHelperText>
        </FormControl>
    )
};

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
    FormikIntegerField,
    FormMultilineTextField,
    FormikNumberField,
    FormikSearchField,
    FormikSelectField,
    FormikTextField,
    FormTimestampField,
};
