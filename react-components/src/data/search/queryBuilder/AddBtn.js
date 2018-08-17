import styles from "../styles";

import AddIcon from "@material-ui/icons/Add";
import Button from "@material-ui/core/Button";
import React from "react";
import { withStyles } from "@material-ui/core/styles";

/**
 * A button used by QueryBuilder.  Clicking it adds a Condition
 */
function AddBtn(props) {
    const {classes, ...custom} = props;
    return (
        <Button variant='fab'
                color='primary'
                classes={{root: classes.conditionButton}}
                mini
                {...custom}>
            <AddIcon />
        </Button>
    )
}

export default withStyles(styles)(AddBtn);