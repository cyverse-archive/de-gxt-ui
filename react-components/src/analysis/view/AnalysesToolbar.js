/**
 *  @author sriram
 *
 **/

import React, { Component } from 'react';
import Toolbar from "@material-ui/core/Toolbar";
import ToolbarGroup from "@material-ui/core/Toolbar";
import Button from '@material-ui/core/Button';
import Menu from '@material-ui/core/Menu';
import withI18N, { formatMessage, getMessage } from "../../util/I18NWrapper";
import exStyles from "../style";
import ids from "../ids";
import { withStyles } from "@material-ui/core/styles";
import intlData from "../messages";
import RefreshIcon from "@material-ui/icons/Refresh";
import MenuIcon from "@material-ui/icons/Menu";
import FormControl from "@material-ui/core/FormControl";
import Select from "@material-ui/core/Select";
import permission from "../model/permission";
import InputLabel from "@material-ui/core/InputLabel";
import appType from "../model/appType";
import TextField from '@material-ui/core/TextField';
import AnalysesMenu from "./AnalysesMenu";
import { injectIntl } from "react-intl";

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
                        <FormControl className={classes.toolbarMargins}>
                            <InputLabel htmlFor="filter-simple"
                                        className={classes.toolbarMargins}>Filter</InputLabel>
                            <Select
                                native
                                value={this.props.permFilter}
                                onChange={this.props.onPermissionsFilterChange}
                                variant="outlined"
                                inputProps={{
                                    name: 'perm-filter',
                                    id: "sharingFilter",
                                }}>
                                <option
                                    value={permission.all}>{permission.all}</option>
                                <option
                                    value={permission.mine}>{permission.mine}</option>
                                <option
                                    value={permission.theirs}>{permission.theirs}</option>
                            </Select>
                        </FormControl>
                        <FormControl className={classes.toolbarMargins}>
                            <InputLabel htmlFor="filter-simple">Filter</InputLabel>
                            <Select
                                native
                                value={this.props.appsFilter}
                                onChange={this.props.onAppsFilterChange}
                                variant="outlined"
                                inputProps={{
                                    name: 'apps-filter',
                                    id: "appsFilter",
                                }}>
                                <option
                                    value={appType.all}>{appType.all}</option>
                                <option
                                    value={appType.agave}>{appType.agave}</option>
                                <option
                                    value={appType.de}>{appType.de}</option>
                                <option
                                    value={appType.interactive}>{appType.interactive}</option>
                                <option
                                    value={appType.osg}>{appType.osg}</option>
                            </Select>
                        </FormControl>
                        <FormControl className={classes.toolbarMargins}>
                            <TextField
                                id="search"
                                placeholder={formatMessage(intl,"search")}
                                variant="outlined"
                                style={{fontSize: 10}}
                            />
                        </FormControl>
                    </form>

                </ToolbarGroup>
            </Toolbar>
        );
    }
}

AnalysesToolbar.propTypes = {};

export default withStyles(exStyles)(withI18N(injectIntl(AnalysesToolbar), intlData));
