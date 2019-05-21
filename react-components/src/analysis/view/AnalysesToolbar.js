/**
 *  @author sriram
 *
 **/

import React, { Component } from "react";
import PropTypes from "prop-types";
import { injectIntl } from "react-intl";

import ids from "../ids";
import intlData from "../messages";
import appType from "../model/appType";
import viewFilter from "../model/viewFilterOptions";
import exStyles from "../style";

import {
    build,
    formatMessage,
    getMessage,
    SearchField,
    withI18N,
} from "@cyverse-de/ui-lib";

import AnalysesMenuItems from "./AnalysesMenuItems";

import Button from "@material-ui/core/Button";
import FormControl from "@material-ui/core/FormControl";
import InputLabel from "@material-ui/core/InputLabel/InputLabel";
import OutlinedInput from "@material-ui/core/OutlinedInput/OutlinedInput";
import Menu from "@material-ui/core/Menu";
import MenuItem from "@material-ui/core/MenuItem/MenuItem";
import Select from "@material-ui/core/Select";
import ToolbarGroup from "@material-ui/core/Toolbar";
import { withStyles } from "@material-ui/core/styles";

import MenuIcon from "@material-ui/icons/Menu";
import RefreshIcon from "@material-ui/icons/Refresh";

class AnalysesToolbar extends Component {
    constructor(props) {
        super(props);
        this.state = {
            anchorEl: null,
        };
    }

    handleClick = (event) => {
        this.setState({ anchorEl: event.currentTarget });
    };

    handleClose = () => {
        this.setState({ anchorEl: null });
    };

    render() {
        const {
            classes,
            baseDebugId,
            baseToolbarId,
            intl,
            searchInputValue,
        } = this.props;
        const { anchorEl } = this.state;
        return (
            <div className={classes.toolbar}>
                <ToolbarGroup style={{ paddingLeft: 0 }}>
                    <Button
                        id={baseDebugId}
                        aria-owns={anchorEl ? "simple-menu" : null}
                        aria-haspopup="true"
                        onClick={this.handleClick}
                        className={classes.toolbarButton}
                        variant="outlined"
                    >
                        <MenuIcon className={classes.toolbarItemColor} />
                        {getMessage("analyses")}
                    </Button>
                    <Menu
                        id={baseDebugId}
                        anchorEl={anchorEl}
                        open={Boolean(anchorEl)}
                        onClose={this.handleClose}
                    >
                        <AnalysesMenuItems
                            handleClose={this.handleClose}
                            {...this.props}
                        />
                    </Menu>
                    <Button
                        id={build(baseToolbarId, ids.BUTTON_REFRESH)}
                        variant="contained"
                        className={classes.toolbarButton}
                        onClick={this.props.handleRefresh}
                    >
                        <RefreshIcon className={classes.toolbarItemColor} />
                        {getMessage("refresh")}
                    </Button>
                    <form autoComplete="off">
                        <FormControl
                            id={build(baseToolbarId, ids.VIEW_FILTER)}
                            className={classes.dropDown}
                        >
                            <InputLabel className={classes.dropDownLabel}>
                                {getMessage("viewFilter")}
                            </InputLabel>
                            <Select
                                value={this.props.viewFilter}
                                onChange={(e) =>
                                    this.props.onViewFilterChange(
                                        e.target.value
                                    )
                                }
                                input={<OutlinedInput name="permission" />}
                                style={{ minWidth: 200 }}
                            >
                                <MenuItem
                                    id={build(
                                        baseToolbarId,
                                        ids.VIEW_FILTER + ids.ALL
                                    )}
                                    value={viewFilter.all}
                                >
                                    {viewFilter.all}
                                </MenuItem>
                                <MenuItem
                                    id={build(
                                        baseToolbarId,
                                        ids.VIEW_FILTER + ids.MINE
                                    )}
                                    value={viewFilter.mine}
                                >
                                    {viewFilter.mine}
                                </MenuItem>
                                <MenuItem
                                    id={build(
                                        baseToolbarId,
                                        ids.VIEW_FILTER + ids.THEIRS
                                    )}
                                    value={viewFilter.theirs}
                                >
                                    {viewFilter.theirs}
                                </MenuItem>
                            </Select>
                        </FormControl>
                        <FormControl
                            id={build(baseToolbarId, ids.APP_TYPE)}
                            className={classes.dropDown}
                        >
                            <InputLabel className={classes.dropDownLabel}>
                                {getMessage("type")}
                            </InputLabel>
                            <Select
                                value={this.props.typeFilter}
                                onChange={(e) =>
                                    this.props.onTypeFilterChange(
                                        e.target.value
                                    )
                                }
                                input={<OutlinedInput name="type" />}
                                style={{ minWidth: 120 }}
                            >
                                <MenuItem
                                    id={build(
                                        baseToolbarId,
                                        ids.TYPE + ids.ALL
                                    )}
                                    value={"All"}
                                >
                                    {appType.all}
                                </MenuItem>
                                <MenuItem
                                    id={build(
                                        baseToolbarId,
                                        ids.TYPE + ids.AGAVE
                                    )}
                                    value={"Agave"}
                                >
                                    {appType.agave}
                                </MenuItem>
                                <MenuItem
                                    id={build(baseToolbarId, ids.TYPE + ids.DE)}
                                    value={"DE"}
                                >
                                    {appType.de}
                                </MenuItem>
                                <MenuItem
                                    id={build(
                                        baseToolbarId,
                                        ids.TYPE + ids.INTERACTIVE
                                    )}
                                    value={"Interactive"}
                                >
                                    {appType.interactive}
                                </MenuItem>
                                <MenuItem
                                    id={build(
                                        baseToolbarId,
                                        ids.TYPE + ids.OSG
                                    )}
                                    value={"OSG"}
                                >
                                    {appType.osg}
                                </MenuItem>
                            </Select>
                        </FormControl>
                        <FormControl style={{ margin: 5 }}>
                            <SearchField
                                id={build(baseToolbarId, ids.FIELD_SEARCH)}
                                handleSearch={this.props.onSearch}
                                value={searchInputValue}
                                placeholder={formatMessage(intl, "search")}
                            />
                        </FormControl>
                    </form>
                </ToolbarGroup>
            </div>
        );
    }
}

AnalysesToolbar.propTypes = {
    baseDebugId: PropTypes.string.isRequired,
    handleGoToOutputFolder: PropTypes.func.isRequired,
    handleViewParams: PropTypes.func.isRequired,
    handleRelaunch: PropTypes.func.isRequired,
    handleViewInfo: PropTypes.func.isRequired,
    handleShare: PropTypes.func.isRequired,
    handleCancel: PropTypes.func.isRequired,
    handleDeleteClick: PropTypes.func.isRequired,
    handleRename: PropTypes.func.isRequired,
    handleUpdateComments: PropTypes.func.isRequired,
    handleSaveAndComplete: PropTypes.func.isRequired,
    handleRefresh: PropTypes.func.isRequired,
    viewFilter: PropTypes.string.isRequired,
    typeFilter: PropTypes.string.isRequired,
    onPermissionsFilterChange: PropTypes.func.isRequired,
    onTypeFilterChange: PropTypes.func.isRequired,
    onSearch: PropTypes.func.isRequired,
    searchInputValue: PropTypes.string.isRequired,
    selectionCount: PropTypes.number.isRequired,
    owner: PropTypes.string.isRequired,
    sharable: PropTypes.bool.isRequired,
    disableCancel: PropTypes.bool.isRequired,
};

export default withStyles(exStyles)(
    withI18N(injectIntl(AnalysesToolbar), intlData)
);
