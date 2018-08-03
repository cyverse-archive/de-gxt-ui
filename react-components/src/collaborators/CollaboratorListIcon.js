import messages from "./messages";
import styles from "./styles";
import withI18N, { getMessage } from "../util/I18NWrapper";

import ListAlt from "@material-ui/icons/ListAlt";
import React from "react";
import ToolTip from "@material-ui/core/Tooltip";
import { withStyles } from "@material-ui/core/styles";

/**
 * @author aramsey
 *
 * An icon used to visually indicate a Collaborator List, also uses a tooltip
 */
function CollaboratorListIcon(props) {
    let {classes} = props;
    return (
        <ToolTip title={getMessage('collaboratorListToolTip')}>
            <ListAlt className={classes.searchIcon}/>
        </ToolTip>
    )
}

export default withStyles(styles)(withI18N(CollaboratorListIcon, messages));