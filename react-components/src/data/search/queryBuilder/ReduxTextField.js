import Validations from "./Validations";

import Grid from "@material-ui/core/Grid";
import React from "react";
import TextField from "@material-ui/core/TextField";

/**
 * A general material-ui TextField that can be used with redux-form
 */
function ReduxTextField(props) {
    const { input, label, meta, ...custom } = props;

    return (
        <Grid item>
            <TextField label={label} fullWidth={true} {...input} {...custom} />
            <Validations.errorField {...meta} />
        </Grid>
    );
}

export default ReduxTextField;
