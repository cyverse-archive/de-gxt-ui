/**
 * Used under the Apps Tab to change the number of apps to display in case the number of apps is too high.
 */

import React from "react";
import TextField from "@material-ui/core/TextField";
import { makeStyles } from "@material-ui/core/styles";
import { build } from "@cyverse-de/ui-lib";
import ids from "./AllStatsIDs";

const useStyles = makeStyles((theme) => ({
    container: {
        display: "flex",
        flexWrap: "wrap",
    },
    textField: {
        marginLeft: theme.spacing(1),
        marginRight: theme.spacing(1),
        width: 200,
    },
}));

function NumberTextfield(props) {
    const classes = useStyles();
    const { baseId } = props;

    return (
        <div>
            <form className={classes.container} noValidate autoComplete="off">
                <TextField
                    id={baseId}
                    label="Number"
                    type="number"
                    className={classes.textField}
                    InputLabelProps={{ shrink: true }}
                    margin="normal"
                    variant="filled"
                />
            </form>
        </div>
    );
}

export default NumberTextfield;
