import styles from "../styles";

import Chip from "@material-ui/core/Chip";
import Paper from "@material-ui/core/Paper";
import PropTypes from "prop-types";
import React from "react";
import Tooltip from "@material-ui/core/Tooltip";
import { withStyles } from "@material-ui/core/styles";

/**
 * A simple Paper panel that will hold Collaborator objects as chips
 * The chips contain the collaborator name and hovering over the chip shows a tooltip
 * with either the collaborator's institution or group description
 */
function UserPanel(props) {
    let {
        users,
        onDelete,
        classes,
        id
    } = props;
    let chips = users && users.map((user, index) =>
        <Tooltip key={user.id}
                 title={user.institution ? user.institution : user.description}>
            <Chip key={user.id}
                  id={user.id}
                  className={classes.userChip}
                  onDelete={() => onDelete(index)}
                  label={user.name}/>
        </Tooltip>
    );

    return (
        <Paper className={classes.permissionUsers}
               id={id}>
            {chips}
        </Paper>
    )
}

UserPanel.propTypes = {
    users: PropTypes.array.isRequired,
    onDelete: PropTypes.func.isRequired,
    id: PropTypes.string.isRequired,
};

export default withStyles(styles)(UserPanel);