import React from "react";
import styles from "../styles";

import DeleteIcon from "@material-ui/icons/Delete";
import { Fab, withStyles } from "@material-ui/core";

/**
 * A button used by QueryBuilder.  Clicking it removes the Condition in the same row
 */
function DeleteBtn(props) {
    const { classes, ...custom } = props;
    return (
        <Fab
            color="secondary"
            size="small"
            classes={{ root: classes.conditionButton }}
            {...custom}
        >
            <DeleteIcon />
        </Fab>
    );
}

export default withStyles(styles)(DeleteBtn);
