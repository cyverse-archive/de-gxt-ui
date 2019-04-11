/**
 *  @author sriram
 *
 **/

import React, { Component } from "react";

import exStyles from "../style";
import ids from "../ids";
import build from "../../util/DebugIDUtil";
import Color from "../../util/CyVersePalette";
import { getMessage } from "../../util/I18NWrapper";

import MenuItem from "@material-ui/core/MenuItem";
import { withStyles } from "@material-ui/core/styles";

import CancelIcon from "@material-ui/icons/Cancel";
import CommentIcon from "@material-ui/icons/Comment";
import DeleteIcon from "@material-ui/icons/Delete";
import EditIcon from "@material-ui/icons/Edit";
import FolderIcon from "@material-ui/icons/FolderOpen";
import InfoIcon from "@material-ui/icons/Info";
import RepeatIcon from "@material-ui/icons/Repeat";
import SaveIcon from "@material-ui/icons/Save";
import ShareIcon from "@material-ui/icons/Share";

class AnalysesMenuItems extends Component {
    render() {
        const {
            classes,
            handleRename,
            handleClose,
            handleUpdateComments,
            handleGoToOutputFolder,
            handleRelaunch,
            handleCancel,
            handleShare,
            handleDeleteClick,
            handleViewParams,
            handleViewInfo,
            handleSaveAndComplete,
            baseDebugId,
            selectionCount,
            owner,
            sharable,
            disableCancel,
        } = this.props;

        const disableSingleSelectionMenuItem = selectionCount !== 1;
        const disableShare = !selectionCount || !owner || !sharable;
        const noSelection = !selectionCount;

        return (
            <React.Fragment>
                <MenuItem
                    id={build(baseDebugId, ids.MENUITEM_GO_TO_FOLDER)}
                    disabled={disableSingleSelectionMenuItem}
                    onClick={() => {
                        handleClose();
                        handleGoToOutputFolder();
                    }}
                    className={classes.menuItem}
                    data-disabled={disableSingleSelectionMenuItem}
                >
                    <FolderIcon style={{ color: Color.darkBlue }} />
                    {getMessage("goOutputFolder")}
                </MenuItem>
                <MenuItem
                    id={build(baseDebugId, ids.MENUITEM_VIEW_PARAMS)}
                    disabled={disableSingleSelectionMenuItem}
                    onClick={() => {
                        handleClose();
                        handleViewParams();
                    }}
                    className={classes.menuItem}
                    data-disabled={disableSingleSelectionMenuItem}
                >
                    <FolderIcon className={classes.toolbarItemColor} />
                    {getMessage("viewParam")}
                </MenuItem>
                <MenuItem
                    id={build(baseDebugId, ids.MENUITEM_RELAUNCH)}
                    disabled={disableSingleSelectionMenuItem}
                    onClick={() => {
                        handleClose();
                        handleRelaunch();
                    }}
                    className={classes.menuItem}
                    data-disabled={disableSingleSelectionMenuItem}
                >
                    <RepeatIcon className={classes.toolbarItemColor} />
                    {getMessage("relaunch")}
                </MenuItem>
                <MenuItem
                    id={build(baseDebugId, ids.MENUITEM_VIEW_ANALYSES_INFO)}
                    disabled={disableSingleSelectionMenuItem}
                    onClick={() => {
                        handleClose();
                        handleViewInfo();
                    }}
                    className={classes.menuItem}
                    data-disabled={disableSingleSelectionMenuItem}
                >
                    <InfoIcon className={classes.toolbarItemColor} />
                    {getMessage("analysisInfo")}
                </MenuItem>
                <MenuItem
                    id={build(baseDebugId, ids.MENUITEM_SHARE_COLLAB)}
                    disabled={disableShare}
                    onClick={() => {
                        handleClose();
                        handleShare();
                    }}
                    data-disabled={disableShare}
                    className={classes.menuItem}
                >
                    <ShareIcon className={classes.toolbarItemColor} />
                    {getMessage("share")}
                </MenuItem>
                <MenuItem
                    id={build(baseDebugId, ids.MENUITEM_COMPLETE)}
                    disabled={disableCancel || !owner}
                    onClick={() => {
                        handleClose();
                        handleSaveAndComplete();
                    }}
                    data-disabled={disableCancel || !owner}
                    className={classes.menuItem}
                >
                    <SaveIcon className={classes.toolbarItemColor} />
                    {getMessage("completeAndSave")}
                </MenuItem>
                <MenuItem
                    id={build(baseDebugId, ids.MENUITEM_CANCEL)}
                    disabled={disableCancel || !owner}
                    onClick={() => {
                        handleClose();
                        handleCancel();
                    }}
                    data-disabled={disableCancel || !owner}
                    className={classes.menuItem}
                >
                    <CancelIcon className={classes.toolbarItemColor} />
                    {getMessage("cancel")}
                </MenuItem>
                <MenuItem
                    id={build(baseDebugId, ids.MENUITEM_DELETE)}
                    disabled={noSelection || !owner}
                    onClick={() => {
                        handleClose();
                        handleDeleteClick();
                    }}
                    data-disabled={noSelection || !owner}
                    className={classes.menuItem}
                >
                    <DeleteIcon className={classes.toolbarItemColor} />
                    {getMessage("delete")}
                </MenuItem>
                <MenuItem
                    id={build(baseDebugId, ids.MENUITEM_RENAME)}
                    disabled={disableSingleSelectionMenuItem || !owner}
                    onClick={() => {
                        handleClose();
                        handleRename();
                    }}
                    data-disabled={disableSingleSelectionMenuItem || !owner}
                    className={classes.menuItem}
                >
                    <EditIcon className={classes.toolbarItemColor} />
                    {getMessage("rename")}
                </MenuItem>
                <MenuItem
                    id={build(baseDebugId, ids.MENUITEM_UPDATE_COMMENTS)}
                    disabled={disableSingleSelectionMenuItem || !owner}
                    onClick={() => {
                        handleClose();
                        handleUpdateComments();
                    }}
                    data-disabled={disableSingleSelectionMenuItem || !owner}
                    className={classes.menuItem}
                >
                    <CommentIcon className={classes.toolbarItemColor} />
                    {getMessage("updateComments")}
                </MenuItem>
            </React.Fragment>
        );
    }
}

export default withStyles(exStyles)(AnalysesMenuItems);
