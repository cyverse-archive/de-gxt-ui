/**
 *  @author sriram
 * */
import React, { Component } from "react";
import Toolbar from "@material-ui/core/Toolbar";
import ToolbarGroup from "@material-ui/core/Toolbar";
import ToolbarSeparator from "@material-ui/core/Toolbar";
import Select from "@material-ui/core/Select";
import InputLabel from "@material-ui/core/InputLabel";
import MenuItem from "@material-ui/core/MenuItem";
import FormControl from "@material-ui/core/FormControl";
import RefreshIcon from "@material-ui/icons/Refresh";
import DeleteIcon from "@material-ui/icons/Delete";
import CheckIcon from "@material-ui/icons/Check";
import intlData from "../messages";
import withI18N, { getMessage } from "../../util/I18NWrapper";
import Button from "@material-ui/core/Button";
import { withStyles } from "@material-ui/core/styles";
import exStyles from "../style";
import notificationCategory from "../model/notificationCategory";

class NotificationToolbar extends Component {
    render() {
        const {classes} = this.props;
        return (
            <Toolbar className={classes.toolbar}>
                <ToolbarGroup>
                    <form autoComplete="off">
                        <FormControl>
                            <InputLabel htmlFor="filter-simple">Filter</InputLabel>
                            <Select
                                value={this.props.filter}
                                onChange={this.props.onFilterChange}
                                inputProps={{
                                    name: 'filter',
                                    id: 'filter-simple',
                                }}>
                                <MenuItem
                                    value={notificationCategory.new}>{notificationCategory.new}</MenuItem>
                                <MenuItem
                                    value={notificationCategory.all}>{notificationCategory.all}</MenuItem>
                                <MenuItem
                                    value={notificationCategory.analysis}>{notificationCategory.analysis}</MenuItem>
                                <MenuItem
                                    value={notificationCategory.data}>{notificationCategory.data}</MenuItem>
                                <MenuItem
                                    value={notificationCategory.tool_request}>{notificationCategory.tool_request}</MenuItem>
                                <MenuItem
                                    value={notificationCategory.apps}>{notificationCategory.apps}</MenuItem>
                                <MenuItem
                                    value={notificationCategory.permanent_id_request}>{notificationCategory.permanent_id_request}</MenuItem>
                                <MenuItem
                                    value={notificationCategory.team}>{notificationCategory.team}</MenuItem>
                            </Select>
                        </FormControl>
                    </form>
                    <ToolbarSeparator/>
                    <Button variant="raised"
                            size="small"
                            className={classes.toolbarButton}
                            onClick={this.props.onRefreshClicked}><RefreshIcon />{getMessage("refresh")}
                    </Button>
                    <Button variant="raised"
                            size="small"
                            disabled={this.props.markSeenDisabled}
                            className={classes.toolbarButton}
                            onClick={this.props.onMarkSeenClicked}><CheckIcon />{getMessage("markSeen")}
                    </Button>
                    <Button variant="raised"
                            size="small"
                            disabled={this.props.deleteDisabled}
                            onClick={this.props.onDeleteClicked}
                            className={classes.toolbarButton}><DeleteIcon />{getMessage("delete")}
                    </Button>

                </ToolbarGroup>
            </Toolbar>

        )
    }
}
export default withStyles(exStyles)(withI18N(NotificationToolbar, intlData));