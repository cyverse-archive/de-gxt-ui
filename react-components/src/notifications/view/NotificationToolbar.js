/**
 * Toolbar for notification window.
 *
 *  @author sriram
 * */
import React, { Component } from "react";
import Toolbar from "@material-ui/core/Toolbar";
import ToolbarGroup from "@material-ui/core/Toolbar";
import ToolbarSeparator from "@material-ui/core/Toolbar";
import Select from "@material-ui/core/Select";
import InputLabel from "@material-ui/core/InputLabel";
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
import build from "../../util/DebugIDUtil";
import ids from "../ids";
import Color from "../../util/CyVersePalette";

class NotificationToolbar extends Component {
    render() {
        const {classes, baseDebugId} = this.props;
        const baseId = baseDebugId + ids.TOOLBAR;
        return (
            <Toolbar className={classes.toolbar}>
                <ToolbarGroup>
                    <form autoComplete="off">
                        <FormControl>
                            <InputLabel htmlFor="filter-simple">Filter</InputLabel>
                            <Select
                                native
                                value={this.props.filter}
                                onChange={this.props.onFilterChange}
                                inputProps={{
                                    name: 'filter',
                                    id: build(baseId, ids.FILTER),
                                }}>
                                <option
                                    value={notificationCategory.new}>{notificationCategory.new}</option>
                                <option
                                    value={notificationCategory.all}>{notificationCategory.all}</option>
                                <option
                                    value={notificationCategory.analysis}>{notificationCategory.analysis}</option>
                                <option
                                    value={notificationCategory.data}>{notificationCategory.data}</option>
                                <option
                                    value={notificationCategory.tool_request}>{notificationCategory.tool_request}</option>
                                <option
                                    value={notificationCategory.apps}>{notificationCategory.apps}</option>
                                <option
                                    value={notificationCategory.permanent_id_request}>{notificationCategory.permanent_id_request}</option>
                                <option
                                    value={notificationCategory.team}>{notificationCategory.team}</option>
                            </Select>
                        </FormControl>
                    </form>
                    <ToolbarSeparator/>
                    <Button id={build(baseId, ids.REFRESH_BTN)}
                            variant="raised"
                            size="small"
                            className={classes.toolbarButton}
                            onClick={this.props.onRefreshClicked}><RefreshIcon style={{color: Color.darkBlue}}/>{getMessage("refresh")}
                    </Button>
                    <Button id={build(baseId, ids.MARK_ALL_SEEN_BTN)}
                            variant="raised"
                            size="small"
                            disabled={this.props.markSeenDisabled}
                            className={classes.toolbarButton}
                            onClick={this.props.onMarkSeenClicked}><CheckIcon style={{color: Color.darkBlue}}/>{getMessage("markSeen")}
                    </Button>
                    <Button id={build(baseId, ids.DELETE_BTN)}
                            variant="raised"
                            size="small"
                            disabled={this.props.deleteDisabled}
                            onClick={this.props.onDeleteClicked}
                            className={classes.toolbarButton}><DeleteIcon style={{color: Color.darkBlue}}/>{getMessage("delete")}
                    </Button>

                </ToolbarGroup>
            </Toolbar>

        )
    }
}
export default withStyles(exStyles)(withI18N(NotificationToolbar, intlData));