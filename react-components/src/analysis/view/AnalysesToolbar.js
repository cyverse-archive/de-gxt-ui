/**
 *  @author sriram
 *
 **/

import React, { Component } from 'react';
import { injectIntl } from "react-intl";

import ids from "../ids";
import intlData from "../messages";
import appType from "../model/appType";
import permission from "../model/permission";
import exStyles from "../style";
import build from "../../util/DebugIDUtil";
import withI18N, { formatMessage, getMessage } from "../../util/I18NWrapper";

import AnalysesMenuItems from "./AnalysesMenuItems";
import SearchField from "../../util/SearchField";

import Button from '@material-ui/core/Button';
import FormControl from "@material-ui/core/FormControl";
import InputLabel from "@material-ui/core/InputLabel/InputLabel";
import OutlinedInput from "@material-ui/core/OutlinedInput/OutlinedInput";
import Menu from '@material-ui/core/Menu';
import MenuItem from "@material-ui/core/MenuItem/MenuItem";
import Select from "@material-ui/core/Select";
import Toolbar from "@material-ui/core/Toolbar";
import ToolbarGroup from "@material-ui/core/Toolbar";
import { withStyles } from "@material-ui/core/styles";

import MenuIcon from "@material-ui/icons/Menu";
import RefreshIcon from "@material-ui/icons/Refresh";

class AnalysesToolbar extends Component {
    constructor(props) {
        super(props);
        this.state = {
            anchorEl: null,
        }

    }

    handleClick = event => {
        this.setState({anchorEl: event.currentTarget});
    };

    handleClose = () => {
        this.setState({anchorEl: null});
    };


    render() {
        const {classes, baseDebugId, intl, searchInputValue} = this.props;
        const {anchorEl} = this.state;
         const analysesMenuBaseId = build(baseDebugId, ids.MENUITEM_ANALYSES);
        return (
            <Toolbar className={classes.toolbar}>
                <ToolbarGroup style={{paddingLeft: 0}}>
                    <Button
                        aria-owns={anchorEl ? 'simple-menu' : null}
                        aria-haspopup="true"
                        onClick={this.handleClick}
                        className={classes.toolbarButton}
                        variant="outlined">
                        <MenuIcon className={classes.toolbarItemColor}/>
                        {getMessage("analyses")}
                    </Button>
                    <Menu
                        id={analysesMenuBaseId}
                        anchorEl={anchorEl}
                        open={Boolean(anchorEl)}
                        onClose={this.handleClose}>
                        <AnalysesMenuItems
                            handleClose={this.handleClose}
                            {...this.props}/>
                    </Menu>
                    <Button
                        id={build(baseDebugId, ids.BUTTON_REFRESH)}
                        variant="raised"
                        size="small"
                        className={classes.toolbarButton}
                        onClick={this.props.handleRefersh}>
                        <RefreshIcon className={classes.toolbarItemColor}/>
                        {getMessage("refresh")}
                    </Button>
                    <form autoComplete="off">
                        <FormControl
                            className={classes.toolbarMargins}
                            style={{margin: 5}}>
                            <InputLabel style={{paddingLeft: 5}}>{getMessage("permission")}</InputLabel>
                            <Select
                                value={this.props.permFilter}
                                onChange={(e) => this.props.onPermissionsFilterChange(e.target.value)}
                                input={
                                    <OutlinedInput name="permission" id="permission"/>
                                }
                                style={{minWidth: 200}}>
                                <MenuItem
                                    value={permission.all}>{permission.all}</MenuItem>
                                <MenuItem
                                    value={permission.mine}>{permission.mine}</MenuItem>
                                <MenuItem
                                    value={permission.theirs}>{permission.theirs}</MenuItem>
                            </Select>
                        </FormControl>
                        <FormControl
                            className={classes.toolbarMargins}
                            style={{margin: 5}}>
                            <InputLabel style={{paddingLeft: 5}}>{getMessage("type")}</InputLabel>
                            <Select
                                value={this.props.typeFilter}
                                onChange={(e) => this.props.onTypeFilterChange(e.target.value)}
                                input={
                                    <OutlinedInput name="type" id="appType"/>
                                } style={{minWidth: 120}}>
                                <MenuItem
                                    value={"All"}>{appType.all}</MenuItem>
                                <MenuItem
                                    value={"Agave"}>{appType.agave}</MenuItem>
                                <MenuItem
                                    value={"DE"}>{appType.de}</MenuItem>
                                <MenuItem
                                    value={"Interactive"}>{appType.interactive}</MenuItem>
                                <MenuItem
                                    value={"OSG"}>{appType.osg}</MenuItem>
                            </Select>
                        </FormControl>
                        <FormControl className={classes.toolbarMargins} style={{margin: 5}}>
                            <SearchField id={build(baseDebugId, ids.FIELD_SEARCH)}
                                         handleSearch={this.props.onSearch}
                                         value={searchInputValue}
                                         placeholder={formatMessage(intl, "search")}/>
                        </FormControl>

                    </form>

                </ToolbarGroup>
            </Toolbar>
        );
    }
}

AnalysesToolbar.propTypes = {};

export default withStyles(exStyles)(withI18N(injectIntl(AnalysesToolbar), intlData));
