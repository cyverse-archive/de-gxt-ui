import React from "react";
import TextField from "@material-ui/core/TextField";
import { makeStyles } from "@material-ui/core/styles";

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

function NumberTextfield() {
    const classes = useStyles();

    return (
        <div>
            <form className={classes.container} noValidate autoComplete="off">
                <TextField
                    id="filled-number"
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
