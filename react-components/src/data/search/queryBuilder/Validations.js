import styles from "../styles";

import React from "react";
import { withStyles } from "@material-ui/core/styles";

const nonEmptyField = value => value && value.length > 0 ? undefined : 'Empty Value';
const minValue = value => value && value < 0 ? `Must be at least 0` : undefined;
const error = (props) => {
    let {
        touched,
        error,
        classes
    } = props;
    return (
        <div>
            {touched && ((error && <span className={classes.errorField}>{error}</span>))}
        </div>
    )
};

const errorField = withStyles(styles)(error);

export default {
    nonEmptyField,
    minValue,
    errorField,
}