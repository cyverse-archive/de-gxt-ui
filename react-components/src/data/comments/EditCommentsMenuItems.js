import React, { Component } from "react";
import exStyles from "./style.js";
import messages from "./messages";
import { withStyles, MenuItem } from "@material-ui/core";
import { getMessage, withI18N } from "@cyverse-de/ui-lib";
import {
    Update,
    Restore,
    SupervisedUserCircleRounded,
} from "@material-ui/icons";

class EditCommentsMenuItems extends Component {
    render() {
        const {
            classes,
            handleSortMostRecent,
            handleSortLeastRecent,
            handleSortOwner,
            handleClose,
        } = this.props;

        return (
            <React.Fragment>
                <MenuItem
                    onClick={() => {
                        handleClose();
                        handleSortMostRecent();
                    }}
                    className={classes.MenuItem}
                >
                    <Update />
                    {getMessage("ddMostRecent")}
                </MenuItem>

                <MenuItem
                    onClick={() => {
                        handleClose();
                        handleSortLeastRecent();
                    }}
                    className={classes.MenuItem}
                >
                    <Restore />
                    {getMessage("ddLeastRecent")}
                </MenuItem>

                <MenuItem
                    onClick={() => {
                        handleClose();
                        handleSortOwner();
                    }}
                    className={classes.MenuItem}
                >
                    <SupervisedUserCircleRounded />
                    {getMessage("ddByUser")}
                </MenuItem>
            </React.Fragment>
        );
    }
}

export default withI18N(withStyles(exStyles)(EditCommentsMenuItems), messages);
