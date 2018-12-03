/**
 *  @author sriram
 *
 **/

import React, { Component } from 'react';

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


class AnalysesMenu extends Component {
    render() {
        const {
            onClick,
            isDisabled,
            isMultiSelect,
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
                          disabled={isDisabled() || isMultiSelect()}
                          onClick={() => {
                              handleClose();
                              handleGoToOutputFolder();
                          }}
                          className={classes.menuItem}>
                    <FolderIcon style={{color: Color.darkBlue}}/>
                    {getMessage("goOutputFolder")}
                </MenuItem>
                <MenuItem id={build(baseDebugId, ids.MENUITEM_VIEW_PARAMS)}
                          disabled={isDisabled() || isMultiSelect()}
                          onClick={() => {
                              handleClose();
                              handleViewParams();
                          }}
                          className={classes.menuItem}>
                    <FolderIcon className={classes.toolbarItemColor}/>
                    {getMessage("viewParam")}
                </MenuItem>
                <MenuItem id={build(baseDebugId, ids.MENUITEM_RELAUNCH)}
                          disabled={isDisabled() || isMultiSelect()}
                          onClick={() => {
                              handleClose();
                              handleRelaunch();
                          }}
                          className={classes.menuItem}>
                    <RepeatIcon className={classes.toolbarItemColor}/>
                    {getMessage("relaunch")}
                </MenuItem>
                <MenuItem id={build(baseDebugId, ids.MENUITEM_VIEW_ANALYSES_INFO)}
                          disabled={isDisabled() || isMultiSelect()}
                          onClick={() => {
                              handleClose();
                              handleViewInfo();
                          }}
                          className={classes.menuItem}>
                    <InfoIcon className={classes.toolbarItemColor}/>
                    {getMessage("analysisInfo")}
                </MenuItem>
                <MenuItem id={build(baseDebugId, ids.MENUITEM_SHARE_COLLAB)}
                          disabled={isDisabled() || !isOwner()}
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
                          disabled={isDisabled() || !isOwner()}
                          onClick={() => {
                              handleClose();
                              handleDeleteClick();
                          }}
                          className={classes.menuItem}>
                    <DeleteIcon className={classes.toolbarItemColor}/>
                    {getMessage("delete")}
                </MenuItem>
                <MenuItem id={build(baseDebugId, ids.MENUITEM_RENAME)}
                          disabled={isDisabled() || isMultiSelect() || !isOwner()}
                          onClick={() => {
                              handleClose();
                              handleRename();
                          }}
                          className={classes.menuItem}>
                    <EditIcon className={classes.toolbarItemColor}/>
                    {getMessage("rename")}
                </MenuItem>
                <MenuItem id={build(baseDebugId, ids.MENUITEM_UPDATE_COMMENTS)}
                          disabled={isDisabled() || isMultiSelect() || !isOwner()}
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