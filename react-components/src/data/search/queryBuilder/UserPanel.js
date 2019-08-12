import React from "react";
import PropTypes from "prop-types";
import styles from "../styles";

import { Chip, Paper, Tooltip, withStyles } from "@material-ui/core";

/**
 * A simple Paper panel that will hold Collaborator objects as chips
 * The chips contain the collaborator name and hovering over the chip shows a tooltip
 * with either the collaborator's institution or group description
 */
function UserPanel(props) {
    const { users, onDelete, classes, collaboratorsUtil, id } = props;
    let chips =
        users &&
        users.map((user, index) => (
            <Tooltip
                key={user.id}
                title={user.institution ? user.institution : user.description}
            >
                <Chip
                    id={user.id}
                    className={classes.userChip}
                    onDelete={() => onDelete(index)}
                    label={collaboratorsUtil.getSubjectDisplayName(user)}
                />
            </Tooltip>
        ));

    return (
        <Paper className={classes.permissionUsers} id={id}>
            {chips}
        </Paper>
    );
}

UserPanel.propTypes = {
    users: PropTypes.array.isRequired,
    onDelete: PropTypes.func.isRequired,
    id: PropTypes.string.isRequired,
    collaboratorsUtil: PropTypes.shape({
        isTeam: PropTypes.func,
        isCollaboratorList: PropTypes.func,
        getSubjectDisplayName: PropTypes.func,
    }),
};

export default withStyles(styles)(UserPanel);
