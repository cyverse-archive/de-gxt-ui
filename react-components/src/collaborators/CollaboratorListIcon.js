import React from "react";
import messages from "./messages";
import styles from "./styles";
import { getMessage, withI18N } from "@cyverse-de/ui-lib";

import ListAlt from "@material-ui/icons/ListAlt";
import ToolTip from "@material-ui/core/Tooltip";
import { withStyles } from "@material-ui/core/styles";

/**
 * @author aramsey
 *
 * An icon used to visually indicate a Collaborator List, also uses a tooltip
 */
function CollaboratorListIcon(props) {
    const { classes } = props;
    return (
        <ToolTip title={getMessage("collaboratorListToolTip")}>
            <ListAlt className={classes.searchIcon} />
        </ToolTip>
    );
}

export default withStyles(styles)(withI18N(CollaboratorListIcon, messages));
