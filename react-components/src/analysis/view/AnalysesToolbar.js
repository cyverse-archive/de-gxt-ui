/**
 *  @author sriram
 *
 **/

import React, { Component } from 'react';
import Toolbar from "@material-ui/core/Toolbar";
import ToolbarGroup from "@material-ui/core/Toolbar";
import Button from '@material-ui/core/Button';
import Menu from '@material-ui/core/Menu';
import withI18N, { getMessage } from "../../util/I18NWrapper";
import exStyles from "../style";
import ids from "../ids";
import { withStyles } from "@material-ui/core/styles";
import intlData from "../messages";
import RefreshIcon from "@material-ui/icons/Refresh";
import MenuIcon from "@material-ui/icons/Menu";
import FormControl from "@material-ui/core/FormControl";
import Select from "@material-ui/core/Select";
import permission from "../model/permission";
import appType from "../model/appType";
import AnalysesMenu from "./AnalysesMenu";
import { injectIntl } from "react-intl";
import MenuItem from "@material-ui/core/MenuItem/MenuItem";
import SearchField from "../../util/SearchField";

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
        const {classes, baseDebugId, intl} = this.props;
        const baseId = baseDebugId + ids.TOOLBAR;

        const {anchorEl} = this.state;

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
                        id="simple-menu"
                        style={{zIndex: 888887,}}
                        anchorEl={anchorEl}
                        open={Boolean(anchorEl)}
                        onClose={this.handleClose}>
                        <AnalysesMenu handleClose={this.handleClose}
                                      {...this.props}/>
                    </Menu>
                    <Button variant="raised"
                            size="small"
                            className={classes.toolbarButton}
                            onClick={this.props.handleRefersh}>
                        <RefreshIcon className={classes.toolbarItemColor}/>
                        {getMessage("refresh")}
                    </Button>
                    <form autoComplete="off">
                        <FormControl className={classes.toolbarMargins} style={{paddingTop: 10}}>
                            <Select
                                value={this.props.permFilter}
                                onChange={(e) => this.props.onPermissionsFilterChange(e.target.value)}
                                inputProps={{
                                    name: 'perm-filter',
                                    id: "sharingFilter",
                                }}
                                style={{minWidth: 200}}>
                                <MenuItem
                                    value={permission.all}>{permission.all}</MenuItem>
                                <MenuItem
                                    value={permission.mine}>{permission.mine}</MenuItem>
                                <MenuItem
                                    value={permission.theirs}>{permission.theirs}</MenuItem>
                            </Select>
                        </FormControl>
                        <FormControl className={classes.toolbarMargins} style={{paddingTop: 10}}>
                            <Select
                                value={this.props.typeFilter}
                                onChange={(e) => this.props.onTypeFilterChange(e.target.value)}
                                inputProps={{
                                    name: 'apps-filter',
                                    id: "appsFilter",
                                }} style={{minWidth: 120}}>
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
                        <FormControl className={classes.toolbarMargins} style={{paddingTop: 10}}>
                            <SearchField handleSearch={this.props.onSearch} helperText="Search..."/>
                        </FormControl>

                    </form>

                </ToolbarGroup>
            </Toolbar>
        );
    }
}

AnalysesToolbar.propTypes = {};

export default withStyles(exStyles)(withI18N(injectIntl(AnalysesToolbar), intlData));
