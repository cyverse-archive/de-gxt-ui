import React, { Component } from "react";

import MenuItem from "@material-ui/core/MenuItem";
import MostRecentIcon from "@material-ui/icons/Update";
import LeastRecentIcon from "@material-ui/icons/Restore";
import OwnerIcon from "@material-ui/icons/SupervisedUserCircleRounded";
import exStyles from "./style.js";
import { withStyles } from "@material-ui/core/styles";

const MOST_RECENT = "Most Recent";
const LEAST_RECENT = "Least Recent";
const BY_OWNER = "By Owner";

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
                    <MostRecentIcon />
                    {MOST_RECENT}
                </MenuItem>

                <MenuItem
                    onClick={() => {
                        handleClose();
                        handleSortLeastRecent();
                    }}
                    className={classes.MenuItem}
                >
                    <LeastRecentIcon />
                    {LEAST_RECENT}
                </MenuItem>

                <MenuItem
                    onClick={() => {
                        handleClose();
                        handleSortOwner();
                    }}
                    className={classes.MenuItem}
                >
                    <OwnerIcon />
                    {BY_OWNER}
                </MenuItem>
            </React.Fragment>
        );
    }
}

export default withStyles(exStyles)(EditCommentsMenuItems);
