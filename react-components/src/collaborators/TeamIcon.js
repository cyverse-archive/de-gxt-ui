import React from "react";
import messages from "./messages";
import styles from "./styles";
import { getMessage, withI18N } from "@cyverse-de/ui-lib";

import Group from "@material-ui/icons/Group";

import { Tooltip as ToolTip, withStyles } from "@material-ui/core";

/**
 * @author aramsey
 *
 * An icon used to visually indicate a Team, also uses a tooltip
 */
function TeamIcon(props) {
    const { classes } = props;
    return (
        <ToolTip title={getMessage("teamToolTip")}>
            <Group className={classes.searchIcon} />
        </ToolTip>
    );
}

export default withStyles(styles)(withI18N(TeamIcon, messages));
