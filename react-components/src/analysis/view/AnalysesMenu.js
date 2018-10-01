import React, { Component } from 'react';
import MenuItem from "@material-ui/core/MenuItem";
import FolderIcon from "@material-ui/icons/FolderOpen";
import Color from "../../util/CyVersePalette";
import { getMessage } from "../../util/I18NWrapper";
import InfoIcon from "@material-ui/icons/Info";
import ShareIcon from "@material-ui/icons/Share";
import CancelIcon from "@material-ui/icons/Cancel";
import DeleteIcon from "@material-ui/icons/Delete";
import RepeatIcon from "@material-ui/icons/Repeat";
import EditIcon from "@material-ui/icons/Edit";
import CommentIcon from "@material-ui/icons/Comment";
import { withStyles } from "@material-ui/core/styles";
import exStyles from "../style";


class AnalysesMenu extends Component {
    render() {
        const {
            onClick,
            disabled,
            multiSelect,
            shouldDisableCancel,
            isOwner,
            classes,
            handleRename,
            handleClose,
            handleUpdateComments,
            handleGoToOutputFolder,
            handleRelaunch,
            handleCancel,
            handleShare,
            handleDelete,
        } = this.props;
        return (
            <React.Fragment>
                <MenuItem disabled={disabled() || multiSelect()}
                          onClick={() => {
                              handleClose();
                              handleGoToOutputFolder();
                          }}
                          className={classes.menuItem}>
                    <FolderIcon style={{color: Color.darkBlue}}/>
                    {getMessage("outputFolder")}
                </MenuItem>
                <MenuItem disabled={disabled() || multiSelect()}
                          onClick={onClick}
                          className={classes.menuItem}>
                    <FolderIcon className={classes.toolbarItemColor}/>
                    {getMessage("viewParam")}
                </MenuItem>
                <MenuItem disabled={disabled() || multiSelect()}
                          onClick={() => {
                              handleClose();
                              handleRelaunch();
                          }}
                          className={classes.menuItem}>
                    <RepeatIcon className={classes.toolbarItemColor}/>
                    {getMessage("relaunch")}
                </MenuItem>
                <MenuItem disabled={disabled() || multiSelect()}
                          onClick={onClick}
                          className={classes.menuItem}>
                    <InfoIcon className={classes.toolbarItemColor}/>
                    {getMessage("analysisInfo")}
                </MenuItem>
                <MenuItem disabled={disabled() || !isOwner()}
                          onClick={()=> {
                              handleClose();
                              handleShare();
                          }}
                          className={classes.menuItem}>
                    <ShareIcon className={classes.toolbarItemColor}/>
                    {getMessage("share")}
                </MenuItem>
                <MenuItem disabled={shouldDisableCancel() || !isOwner()}
                          onClick={()=> {
                              handleClose();
                              handleCancel();
                          }}
                          className={classes.menuItem}>
                    <CancelIcon className={classes.toolbarItemColor}/>
                    {getMessage("cancel")}
                </MenuItem>
                <MenuItem disabled={disabled() || !isOwner()}
                          onClick={() => {
                              handleClose();
                              handleDelete();
                          }}
                          className={classes.menuItem}>
                    <DeleteIcon className={classes.toolbarItemColor}/>
                    {getMessage("delete")}
                </MenuItem>
                <MenuItem disabled={disabled() || multiSelect() || !isOwner()}
                          onClick={() => {
                              handleClose();
                              handleRename();
                          }}
                          className={classes.menuItem}>
                    <EditIcon className={classes.toolbarItemColor}/>
                    {getMessage("rename")}
                </MenuItem>
                <MenuItem disabled={disabled() || multiSelect() || !isOwner()}
                          onClick={() => {
                              handleClose();
                              handleUpdateComments();
                          }}
                          className={classes.menuItem}>
                    <CommentIcon className={classes.toolbarItemColor}/>
                    {getMessage("updateComments")}
                </MenuItem>
            </React.Fragment>
        );
    }
}

export default withStyles(exStyles)(AnalysesMenu);