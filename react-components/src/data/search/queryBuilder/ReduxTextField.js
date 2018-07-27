import React from "react";
import TextField from "@material-ui/core/TextField";

function ReduxTextField(props) {
    let {
        input,
        label,
        meta: {error},
        ...custom
    } = props;
    return (
        <TextField
            label={error ? error : label}
            fullWidth={true}
            {...input}
            {...custom}
        />
    )
}

export default ReduxTextField;