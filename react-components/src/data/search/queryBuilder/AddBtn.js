import React from "react";
import styles from "../styles";

import AddIcon from "@material-ui/icons/Add";
import { Fab, withStyles } from "@material-ui/core";

/**
 * A button used by QueryBuilder.  Clicking it adds a Condition
 */
function AddBtn(props) {
    const { classes, ...custom } = props;
    return (
        <Fab
            color="primary"
            size="small"
            classes={{ root: classes.conditionButton }}
            {...custom}
        >
            <AddIcon />
        </Fab>
    );
}

export default withStyles(styles)(AddBtn);
