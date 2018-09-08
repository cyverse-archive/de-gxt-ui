import React, { Component } from 'react';
import MenuItem from "@material-ui/core/MenuItem";
import FolderIcon from "../../../node_modules/@material-ui/icons/FolderOpen";
import Color from "../../util/CyVersePalette";
import { getMessage } from "../../util/I18NWrapper";
import LaunchIcon from "../../../node_modules/@material-ui/icons/Launch";
import InfoIcon from "../../../node_modules/@material-ui/icons/Info";
import ShareIcon from "../../../node_modules/@material-ui/icons/Share";
import CancelIcon from "../../../node_modules/@material-ui/icons/Cancel";
import DeleteIcon from "../../../node_modules/@material-ui/icons/Delete";
import EditIcon from "../../../node_modules/@material-ui/icons/Edit";
import CommentIcon from "../../../node_modules/@material-ui/icons/Comment";
import exStyles from "../style";
import { withStyles } from "@material-ui/core/styles";

class AnalysesMenu extends Component {
    render() {
        const {onClick, selected, disabled, multiSelect, shouldDisableCancel, isOwner, classes} = this.props;
        return (
            <React.Fragment>
                <MenuItem disabled={disabled(selected) || multiSelect(selected)}
                          onClick={onClick}
                          className={classes.menuItem}>
                    <FolderIcon style={{color: Color.darkBlue}}/>
                    {getMessage("outputFolder")}
                </MenuItem>
                <MenuItem disabled={disabled(selected) || multiSelect(selected)}
                          onClick={onClick}
                          className={classes.menuItem}>
                    <FolderIcon style={{color: Color.darkBlue}}/>
                    {getMessage("viewParam")}
                </MenuItem>
                <MenuItem disabled={disabled(selected) || multiSelect(selected)}
                          onClick={onClick}
                          className={classes.menuItem}>
                    <LaunchIcon style={{color: Color.darkBlue}}/>
                    {getMessage("relaunch")}
                </MenuItem>
                <MenuItem disabled={disabled(selected) || multiSelect(selected)}
                          onClick={onClick}
                          className={classes.menuItem}>
                    <InfoIcon style={{color: Color.darkBlue}}/>
                    {getMessage("analysisInfo")}
                </MenuItem>
                <MenuItem disabled={disabled(selected) || !isOwner(selected)}
                          onClick={onClick}
                          className={classes.menuItem}>
                    <ShareIcon style={{color: Color.darkBlue}}/>
                    {getMessage("share")}
                </MenuItem>
                <MenuItem disabled={shouldDisableCancel(selected) || !isOwner(selected)}
                          onClick={onClick}
                          className={classes.menuItem}>
                    <CancelIcon style={{color: Color.darkBlue}}/>
                    {getMessage("cancel")}
                </MenuItem>
                <MenuItem disabled={disabled(selected) || !isOwner(selected)}
                          onClick={onClick}
                          className={classes.menuItem}>
                    <DeleteIcon style={{color: Color.darkBlue}}
                    />
                    {getMessage("delete")}
                </MenuItem>
                <MenuItem disabled={disabled(selected) || multiSelect(selected) || !isOwner(selected)}
                          onClick={onClick}
                          className={classes.menuItem}>
                    <EditIcon style={{color: Color.darkBlue}}/>
                    {getMessage("rename")}
                </MenuItem>
                <MenuItem disabled={disabled(selected) || multiSelect(selected) || !isOwner(selected)}
                          onClick={onClick}
                          className={classes.menuItem}>
                    <CommentIcon style={{color: Color.darkBlue}}/>
                    {getMessage("updateComments")}
                </MenuItem>
            </React.Fragment>
        );
    }
}

export default withStyles(exStyles)(AnalysesMenu);