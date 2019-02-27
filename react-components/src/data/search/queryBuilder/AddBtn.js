import styles from "../styles";

import AddIcon from "@material-ui/icons/Add";
import Fab from "@material-ui/core/Fab";
import React from "react";
import { withStyles } from "@material-ui/core/styles";

/**
 * A button used by QueryBuilder.  Clicking it adds a Condition
 */
function AddBtn(props) {
    const {classes, ...custom} = props;
    return (
        <Fab color="primary"
             size="small"
             classes={{root: classes.conditionButton}}
             {...custom}>
            <AddIcon />
        </Fab>
    )
}

export default withStyles(styles)(AddBtn);