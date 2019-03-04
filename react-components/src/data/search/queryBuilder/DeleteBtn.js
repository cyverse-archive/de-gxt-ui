import styles from "../styles";

import DeleteIcon from "@material-ui/icons/Delete";
import Fab from "@material-ui/core/Fab/Fab";
import React from "react";
import { withStyles } from "@material-ui/core/styles";

/**
 * A button used by QueryBuilder.  Clicking it removes the Condition in the same row
 */
function DeleteBtn(props) {
    const {classes, ...custom} = props;
    return (
        <Fab color="secondary"
             size="small"
             classes={{root: classes.conditionButton}}
             {...custom}>
            <DeleteIcon/>
        </Fab>
    )
}

export default withStyles(styles)(DeleteBtn);