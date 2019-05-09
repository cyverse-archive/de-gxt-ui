import React, { Component } from "react";

import MenuItem from "@material-ui/core/MenuItem";
import RecentIcon from "@material-ui/icons/Update";
import OwnerIcon from "@material-ui/icons/SupervisedUserCircleRounded";
import exStyles from "./style.js";
import { withStyles } from "@material-ui/core/styles";

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
                    <RecentIcon />
                    Most Recent
                </MenuItem>

                <MenuItem
                    onClick={() => {
                        handleClose();
                        handleSortLeastRecent();
                    }}
                    className={classes.MenuItem}
                >
                    <RecentIcon />
                    Least Recent
                </MenuItem>

                <MenuItem
                    onClick={() => {
                        handleClose();
                        handleSortOwner();
                    }}
                    className={classes.MenuItem}
                >
                    <OwnerIcon />
                    By Owner
                </MenuItem>
            </React.Fragment>
        );
    }
}

export default withStyles(exStyles)(EditCommentsMenuItems);
