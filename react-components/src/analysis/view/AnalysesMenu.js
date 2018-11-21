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
import SaveIcon from "@material-ui/icons/Save";
import { withStyles } from "@material-ui/core/styles";
import exStyles from "../style";
import build from "../../util/DebugIDUtil";
import ids from "../ids";


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
            handleDeleteClick,
            handleViewParams,
            handleViewInfo,
            handleSaveAndComplete,
            baseDebugId
        } = this.props;
        return (
            <React.Fragment>
                <MenuItem id={build(baseDebugId, ids.MENUITEM_GO_TO_FOLDER)}
                          disabled={disabled() || multiSelect()}
                          onClick={() => {
                              handleClose();
                              handleGoToOutputFolder();
                          }}
                          className={classes.menuItem}>
                    <FolderIcon style={{color: Color.darkBlue}}/>
                    {getMessage("goOutputFolder")}
                </MenuItem>
                <MenuItem id={build(baseDebugId, ids.MENUITEM_VIEW_PARAMS)}
                          disabled={disabled() || multiSelect()}
                          onClick={() => {
                              handleClose();
                              handleViewParams();
                          }}
                          className={classes.menuItem}>
                    <FolderIcon className={classes.toolbarItemColor}/>
                    {getMessage("viewParam")}
                </MenuItem>
                <MenuItem id={build(baseDebugId, ids.MENUITEM_RELAUNCH)}
                          disabled={disabled() || multiSelect()}
                          onClick={() => {
                              handleClose();
                              handleRelaunch();
                          }}
                          className={classes.menuItem}>
                    <RepeatIcon className={classes.toolbarItemColor}/>
                    {getMessage("relaunch")}
                </MenuItem>
                <MenuItem id={build(baseDebugId, ids.MENUITEM_VIEW_ANALYSES_INFO)}
                          disabled={disabled() || multiSelect()}
                          onClick={() => {
                              handleClose();
                              handleViewInfo();
                          }}
                          className={classes.menuItem}>
                    <InfoIcon className={classes.toolbarItemColor}/>
                    {getMessage("analysisInfo")}
                </MenuItem>
                <MenuItem id={build(baseDebugId, ids.MENUITEM_SHARE_COLLAB)}
                          disabled={disabled() || !isOwner()}
                          onClick={()=> {
                              handleClose();
                              handleShare();
                          }}
                          className={classes.menuItem}>
                    <ShareIcon className={classes.toolbarItemColor}/>
                    {getMessage("share")}
                </MenuItem>
                <MenuItem id={build(baseDebugId, ids.MENUITEM_COMPLETE)}
                          disabled={shouldDisableCancel() || !isOwner()}
                          onClick={() => {
                              handleClose();
                              handleSaveAndComplete();
                          }}
                          className={classes.menuItem}>
                    <SaveIcon className={classes.toolbarItemColor}/>
                    {getMessage("completeAndSave")}
                </MenuItem>
                <MenuItem id={build(baseDebugId, ids.MENUITEM_CANCEL)}
                          disabled={shouldDisableCancel() || !isOwner()}
                          onClick={()=> {
                              handleClose();
                              handleCancel();
                          }}
                          className={classes.menuItem}>
                    <CancelIcon className={classes.toolbarItemColor}/>
                    {getMessage("cancel")}
                </MenuItem>
                <MenuItem id={build(baseDebugId, ids.MENUITEM_DELETE)}
                          disabled={disabled() || !isOwner()}
                          onClick={() => {
                              handleClose();
                              handleDeleteClick();
                          }}
                          className={classes.menuItem}>
                    <DeleteIcon className={classes.toolbarItemColor}/>
                    {getMessage("delete")}
                </MenuItem>
                <MenuItem id={build(baseDebugId, ids.MENUITEM_RENAME)}
                          disabled={disabled() || multiSelect() || !isOwner()}
                          onClick={() => {
                              handleClose();
                              handleRename();
                          }}
                          className={classes.menuItem}>
                    <EditIcon className={classes.toolbarItemColor}/>
                    {getMessage("rename")}
                </MenuItem>
                <MenuItem id={build(baseDebugId, ids.MENUITEM_UPDATE_COMMENTS)}
                          disabled={disabled() || multiSelect() || !isOwner()}
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