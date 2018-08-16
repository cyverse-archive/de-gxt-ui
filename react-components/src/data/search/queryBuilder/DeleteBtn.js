import styles from "../styles";

import Button from "@material-ui/core/Button";
import DeleteIcon from "@material-ui/icons/Delete";
import React from "react";
import { withStyles } from "@material-ui/core/styles";

/**
 * A button used by QueryBuilder.  Clicking it removes the Condition in the same row
 */
function DeleteBtn(props) {
    const {classes, ...custom} = props;
    return (
        <Button variant='fab'
                color='secondary'
                classes={{root: classes.conditionButton}}
                mini
                {...custom}>
            <DeleteIcon/>
        </Button>
    )
}

export default withStyles(styles)(DeleteBtn);