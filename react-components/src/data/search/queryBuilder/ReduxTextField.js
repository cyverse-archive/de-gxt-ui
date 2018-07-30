import Grid from "@material-ui/core/Grid";
import React from "react";
import TextField from "@material-ui/core/TextField";

/**
 * A general material-ui TextField that can be used with redux-form
 */
function ReduxTextField(props) {
    let {
        input,
        label,
        meta: {error},
        ...custom
    } = props;

    return (
        <Grid item>
            <TextField
                label={error ? error : label}
                fullWidth={true}
                {...input}
                {...custom}
            />
        </Grid>
    )
}

export default ReduxTextField;