/**
 *  @author sriram, psarando
 * */
import React, { Component } from "react";
import PropTypes from "prop-types";

import exStyles from "../style";
import intlData from "../messages";

import {
    dateConstants,
    formatDate,
    getMessage,
    TablePaginationActions,
    LoadingMask,
    withI18N,
} from "@cyverse-de/ui-lib";

import Button from "@material-ui/core/Button";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TablePagination from "@material-ui/core/TablePagination";
import TableRow from "@material-ui/core/TableRow";
import TableHead from "@material-ui/core/TableHead";
import TableSortLabel from "@material-ui/core/TableSortLabel";
import Toolbar from "@material-ui/core/Toolbar";
import ToolbarGroup from "@material-ui/core/Toolbar";
import ToolbarSeparator from "@material-ui/core/Toolbar";
import TextField from "@material-ui/core/TextField";
import Tooltip from "@material-ui/core/Tooltip";
import { withStyles } from "@material-ui/core";

const columnData = [
    { name: "appName", numeric: false },
    { name: "rating", numeric: true },
    { name: "total", numeric: true },
    { name: "completed", numeric: true },
    { name: "failed", numeric: true },
    { name: "lastCompleted", numeric: true },
    { name: "lastUsed", numeric: true },
];

class AppStats extends Component {
    constructor(props) {
        super(props);
        let today = new Date();
        this.state = {
            data: [],
            loading: true,
            searchText: null,
            startDate: new Date(today.setMonth(today.getMonth() - 3)).valueOf(), // set default date range for
            // 90 days!
            endDate: new Date().valueOf(),
            filterDisabled: true,
            page: 0,
            rowsPerPage: 100,
            orderBy: props.orderBy || "total",
            order: props.order || "desc",
        };
        this.handleSearch = this.handleSearch.bind(this);
        this.fetchAppStats = this.fetchAppStats.bind(this);
        this.applyFilter = this.applyFilter.bind(this);
        this.onStartDateChange = this.onStartDateChange.bind(this);
        this.onEndDateChange = this.onEndDateChange.bind(this);
    }

    handleChangePage = (event, page) => {
        this.setState({ page });
    };

    handleChangeRowsPerPage = (event) => {
        this.setState({ rowsPerPage: event.target.value });
    };

    handleSearch(event) {
        if (event.target.value && event.target.value.length >= 3) {
            this.setState({ searchText: event.target.value });
        } else {
            this.setState({ searchText: "" });
        }
    }

    onStartDateChange(event) {
        const parsedDate = Date.parse(event.target.value).valueOf();
        if (parsedDate) {
            this.setState({ startDate: parsedDate });
        } else {
            this.setState({ startDate: null });
        }
    }

    onEndDateChange(event) {
        const parsedDate = Date.parse(event.target.value).valueOf();
        if (parsedDate) {
            this.setState({ endDate: parsedDate });
        } else {
            this.setState({ endDate: null });
        }
    }

    componentDidMount() {
        this.fetchAppStats();
    }

    fetchAppStats() {
        this.setState({
            loading: true,
        });

        const { searchText, order, orderBy } = this.state;
        let { startDate, endDate } = this.state;

        startDate = startDate
            ? formatDate(startDate, dateConstants.DATE_FORMAT)
            : "";
        endDate = endDate ? formatDate(endDate, dateConstants.DATE_FORMAT) : "";

        this.props.presenter.searchApps(
            searchText,
            startDate,
            endDate,
            (appList) => {
                this.setState({
                    loading: false,
                    data: this.sortRows(order, orderBy, appList.apps),
                });
            },
            (errorCode, errorMessage) => {
                this.setState({
                    loading: false,
                });
            }
        );
    }

    applyFilter() {
        this.fetchAppStats();
    }

    handleRequestSort = (event, property) => {
        const orderBy = property;
        let order = "desc";

        if (this.state.orderBy === property && this.state.order === order) {
            order = "asc";
        }

        this.setState({
            order,
            orderBy,
            data: this.sortRows(order, orderBy, this.state.data),
        });
    };

    sortRows = (order, orderBy, data) => {
        const comparator = (a, b) => (a < b ? -1 : 1);

        return data.sort((a, b) => {
            let aVal = this.extractRowValue(orderBy, a);
            let bVal = this.extractRowValue(orderBy, b);

            return order === "desc"
                ? comparator(bVal, aVal)
                : comparator(aVal, bVal);
        });
    };

    extractRowValue = (colName, row) => {
        const { name, rating, job_stats } = row;
        const {
            job_count,
            job_count_completed,
            job_count_failed,
            job_last_completed,
            last_used,
        } = job_stats;

        const numVal = (n) => (n ? n : 0);

        switch (colName) {
            case "rating":
                return numVal(rating.average);
            case "total":
                return numVal(job_count);
            case "completed":
                return numVal(job_count_completed);
            case "failed":
                return numVal(job_count_failed);
            case "lastCompleted":
                return numVal(job_last_completed);
            case "lastUsed":
                return numVal(last_used);
            default:
                return name;
        }
    };

    createSortHandler = (property) => (event) => {
        this.handleRequestSort(event, property);
    };

    render() {
        const { classes } = this.props;
        const { data, rowsPerPage, page, order, orderBy, loading } = this.state;

        return (
            <div className={classes.statContainer}>
                <LoadingMask loading={loading}>
                    <Toolbar>
                        <ToolbarGroup>
                            <TextField
                                className={classes.statSearchTextField}
                                label={getMessage("searchApps")}
                                onChange={this.handleSearch}
                            />
                            <ToolbarSeparator />
                            <TextField
                                label={getMessage("startDate")}
                                type="date"
                                defaultValue={formatDate(
                                    this.state.startDate,
                                    dateConstants.DATE_FORMAT
                                )}
                                InputLabelProps={{
                                    shrink: true,
                                }}
                                onChange={this.onStartDateChange}
                            />
                            <TextField
                                label={getMessage("endDate")}
                                type="date"
                                defaultValue={formatDate(
                                    this.state.endDate,
                                    dateConstants.DATE_FORMAT
                                )}
                                InputLabelProps={{
                                    shrink: true,
                                }}
                                onChange={this.onEndDateChange}
                            />
                            <ToolbarSeparator />
                            <Button
                                variant="contained"
                                onClick={this.applyFilter}
                                className={classes.statFilterButton}
                            >
                                {getMessage("applyFilter")}
                            </Button>
                        </ToolbarGroup>
                    </Toolbar>

                    <div className={classes.statTable}>
                        <Table>
                            <TableHead>
                                <TableRow hover>
                                    {columnData.map((column) => (
                                        <TableCell
                                            className={classes.statTableHead}
                                            key={column.name}
                                            align={
                                                column.numeric
                                                    ? "right"
                                                    : "inherit"
                                            }
                                            sortDirection={
                                                orderBy === column.name
                                                    ? order
                                                    : false
                                            }
                                        >
                                            <Tooltip
                                                title={getMessage("sort")}
                                                placement="bottom-start"
                                                enterDelay={300}
                                            >
                                                <TableSortLabel
                                                    active={
                                                        orderBy === column.name
                                                    }
                                                    direction={order}
                                                    onClick={this.createSortHandler(
                                                        column.name
                                                    )}
                                                >
                                                    {getMessage(column.name)}
                                                </TableSortLabel>
                                            </Tooltip>
                                        </TableCell>
                                    ))}
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {data
                                    .slice(
                                        page * rowsPerPage,
                                        page * rowsPerPage + rowsPerPage
                                    )
                                    .map((n) => {
                                        return (
                                            <TableRow hover key={n.id}>
                                                <TableCell>{n.name}</TableCell>
                                                <TableCell align="right">
                                                    {n.rating.average
                                                        ? n.rating.average
                                                        : 0}
                                                </TableCell>
                                                <TableCell align="right">
                                                    {n.job_stats.job_count
                                                        ? n.job_stats.job_count
                                                        : 0}
                                                </TableCell>
                                                <TableCell align="right">
                                                    {n.job_stats
                                                        .job_count_completed
                                                        ? n.job_stats
                                                              .job_count_completed
                                                        : 0}
                                                </TableCell>
                                                <TableCell align="right">
                                                    {n.job_stats
                                                        .job_count_failed
                                                        ? n.job_stats
                                                              .job_count_failed
                                                        : 0}
                                                </TableCell>
                                                <TableCell>
                                                    {formatDate(
                                                        n.job_stats
                                                            .job_last_completed,
                                                        dateConstants.DATE_FORMAT
                                                    )}
                                                </TableCell>
                                                <TableCell>
                                                    {formatDate(
                                                        n.job_stats.last_used,
                                                        dateConstants.DATE_FORMAT
                                                    )}
                                                </TableCell>
                                            </TableRow>
                                        );
                                    })}
                            </TableBody>
                        </Table>
                    </div>

                    <TablePagination
                        component="div"
                        colSpan={3}
                        count={data.length}
                        rowsPerPage={rowsPerPage}
                        page={page}
                        onChangePage={this.handleChangePage}
                        onChangeRowsPerPage={this.handleChangeRowsPerPage}
                        ActionsComponent={TablePaginationActions}
                        rowsPerPageOptions={[100, 500, 1000]}
                    />
                </LoadingMask>
            </div>
        );
    }
}
AppStats.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(exStyles)(withI18N(AppStats, intlData));
