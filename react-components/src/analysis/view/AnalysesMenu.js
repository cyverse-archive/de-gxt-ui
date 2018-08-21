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

class AnalysesMenu extends Component {
    render() {
        const {onClick, selected, disabled, multiSelect, shouldDisableCancel, isOwner} = this.props;
        return (
            <React.Fragment>
                <MenuItem disabled={disabled(selected) || multiSelect(selected)}
                          onClick={onClick}>
                    <FolderIcon style={{color: Color.darkBlue}}/>
                    {getMessage("outputFolder")}
                </MenuItem>
                <MenuItem disabled={disabled(selected) || multiSelect(selected)}
                          onClick={onClick}>
                    <FolderIcon style={{color: Color.darkBlue}}/>
                    {getMessage("viewParam")}
                </MenuItem>
                <MenuItem disabled={disabled(selected) || multiSelect(selected)}
                          onClick={onClick}>
                    <LaunchIcon style={{color: Color.darkBlue}}/>
                    {getMessage("relaunch")}
                </MenuItem>
                <MenuItem disabled={disabled(selected) || multiSelect(selected)}
                          onClick={onClick}>
                    <InfoIcon style={{color: Color.darkBlue}}/>
                    {getMessage("analysisInfo")}
                </MenuItem>
                <MenuItem disabled={disabled(selected) || !isOwner(selected)} onClick={onClick}>
                    <ShareIcon style={{color: Color.darkBlue}}/>
                    {getMessage("share")}
                </MenuItem>
                <MenuItem disabled={shouldDisableCancel(selected) || !isOwner(selected)}
                          onClick={onClick}>
                    <CancelIcon style={{color: Color.darkBlue}}/>
                    {getMessage("cancel")}
                </MenuItem>
                <MenuItem disabled={disabled(selected) || !isOwner(selected)} onClick={onClick}>
                    <DeleteIcon style={{color: Color.darkBlue}}/>
                    {getMessage("delete")}
                </MenuItem>
                <MenuItem disabled={disabled(selected) || multiSelect(selected) || !isOwner(selected)}
                          onClick={onClick}>
                    <EditIcon style={{color: Color.darkBlue}}/>
                    {getMessage("rename")}
                </MenuItem>
                <MenuItem disabled={disabled(selected) || multiSelect(selected) || !isOwner(selected)}
                          onClick={onClick}>
                    <CommentIcon style={{color: Color.darkBlue}}/>
                    {getMessage("updateComments")}
                </MenuItem>
            </React.Fragment>
        );
    }
}

export default AnalysesMenu;