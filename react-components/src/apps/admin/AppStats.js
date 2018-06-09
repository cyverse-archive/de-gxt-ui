/**
 *  @author sriram, psarando
 * */
import React, { Component } from "react";
import Toolbar from "@material-ui/core/Toolbar";
import ToolbarGroup from "@material-ui/core/Toolbar";
import ToolbarSeparator from "@material-ui/core/Toolbar";
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableFooter from "@material-ui/core/TableFooter";
import TablePagination from "@material-ui/core/TablePagination";
import TableRow from "@material-ui/core/TableRow";
import TableHead from "@material-ui/core/TableHead";
import TableSortLabel from '@material-ui/core/TableSortLabel';
import Tooltip from '@material-ui/core/Tooltip';
import IconButton from "@material-ui/core/IconButton";
import FirstPageIcon from "@material-ui/icons/FirstPage";
import KeyboardArrowLeft from "@material-ui/icons/KeyboardArrowLeft";
import KeyboardArrowRight from "@material-ui/icons/KeyboardArrowRight";
import LastPageIcon from "@material-ui/icons/LastPage";
import CircularProgress from "@material-ui/core/CircularProgress";
import PropTypes from "prop-types";
import moment from "moment";
import exStyles from "../style";
import constants from "../../constants";
import intlData from "../messages";
import withI18N, { getMessage } from "../../util/I18NWrapper";
import injectSheet from "react-jss";

class TablePaginationActions extends React.Component {
    handleFirstPageButtonClick = event => {
        this.props.onChangePage(event, 0);
    };

    handleBackButtonClick = event => {
        this.props.onChangePage(event, this.props.page - 1);
    };

    handleNextButtonClick = event => {
        this.props.onChangePage(event, this.props.page + 1);
    };

    handleLastPageButtonClick = event => {
        this.props.onChangePage(
            event,
            Math.max(0, Math.ceil(this.props.count / this.props.rowsPerPage) - 1),
        );
    };

    render() {
        const {count, page, rowsPerPage, theme} = this.props;

        return (
                <div>
                    <IconButton
                        onClick={this.handleFirstPageButtonClick}
                        disabled={page === 0}
                        aria-label={getMessage("firstPage")}>
                        {theme.direction === 'rtl' ? <LastPageIcon /> : <FirstPageIcon />}
                    </IconButton>
                    <IconButton
                        onClick={this.handleBackButtonClick}
                        disabled={page === 0}
                        aria-label={getMessage("prevPage")}>
                        {theme.direction === 'rtl' ? <KeyboardArrowRight /> : <KeyboardArrowLeft />}
                    </IconButton>
                    <IconButton
                        onClick={this.handleNextButtonClick}
                        disabled={page >= Math.ceil(count / rowsPerPage) - 1}
                        aria-label={getMessage("nextPage")}>
                        {theme.direction === 'rtl' ? <KeyboardArrowLeft /> : <KeyboardArrowRight />}
                    </IconButton>
                    <IconButton
                        onClick={this.handleLastPageButtonClick}
                        disabled={page >= Math.ceil(count / rowsPerPage) - 1}
                        aria-label={getMessage("lastPage")}>
                        {theme.direction === 'rtl' ? <FirstPageIcon /> : <LastPageIcon />}
                    </IconButton>
                </div>
        );
    }
}

TablePaginationActions.propTypes = {
    classes: PropTypes.object.isRequired,
    count: PropTypes.number.isRequired,
    onChangePage: PropTypes.func.isRequired,
    page: PropTypes.number.isRequired,
    rowsPerPage: PropTypes.number.isRequired,
    theme: PropTypes.object.isRequired,
};

const TablePaginationActionsWrapped = withI18N(injectSheet(exStyles, {withTheme: true})(
    TablePaginationActions,
), intlData);


const columnData = [
    { name: "appName",       numeric: false },
    { name: "rating",        numeric: true },
    { name: "total",         numeric: true },
    { name: "completed",     numeric: true },
    { name: "failed",        numeric: true },
    { name: "lastCompleted", numeric: true },
    { name: "lastUsed",      numeric: true },
];

class AppStats extends Component {
    constructor(props) {
        super(props);
        let today = new Date();
        this.state = {
            data: [],
            loading: true,
            searchText: null,
            startDate: new Date(today.setMonth(today.getMonth() - 3)),  // set default date range for 90 days!
            endDate: new Date(),
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
        this.setState({page});
    };

    handleChangeRowsPerPage = event => {
        this.setState({rowsPerPage: event.target.value});
    };


    handleSearch(event) {
        if (event.target.value && event.target.value.length >= 3) {
            this.setState({searchText: event.target.value});
        } else {
            this.setState({searchText: ""});
        }
    }

    onStartDateChange(event) {
        if (Date.parse(event.target.value)) {
            this.setState({startDate: event.target.value});
        } else {
            this.setState({startDate: null});
        }
    }

    onEndDateChange(event) {
        if (Date.parse(event.target.value)) {
            this.setState({endDate: event.target.value});
        } else {
            this.setState({endDate: null});
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

        startDate = startDate ? moment(startDate).format(constants.DATE_FORMAT) : "";
        endDate = endDate ? moment(endDate).format(constants.DATE_FORMAT) : "";

        this.props.presenter.searchApps(searchText, startDate, endDate, (appList) => {
            this.setState({
                loading: false,
                data: this.sortRows(order, orderBy, appList.apps),
            })
            }, (errorCode, errorMessage) => {
                this.setState({
                    loading: false,
                });
            },
        )
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

            return (
                order === "desc"
                    ? comparator(bVal, aVal)
                    : comparator(aVal, bVal)
            );
        });
    };

    extractRowValue = (colName, row) => {
        const { name, rating, job_stats} = row;
        const {
            job_count,
            job_count_completed,
            job_count_failed,
            job_last_completed,
            last_used,
        } = job_stats;

        const numVal = (n) => n ? n : 0;

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

    createSortHandler = property => event => {
        this.handleRequestSort(event, property);
    };

    render() {
        const { classes } = this.props;
        const { data, rowsPerPage, page, order, orderBy } = this.state;

        return (
                <div className={classes.statContainer}>
                    {this.state.loading &&
                        <CircularProgress size={30} className={classes.loadingStyle} thickness={7}/>
                    }
                    <div>
                        <Toolbar>
                            <ToolbarGroup>
                                <TextField className={classes.statSearchTextField}
                                           label={getMessage("searchApps")}
                                           onChange={this.handleSearch}/>
                                <ToolbarSeparator />
                                <TextField label={getMessage("startDate")} type="date"
                                           defaultValue={moment(this.state.startDate).format(constants.DATE_FORMAT)}
                                           InputLabelProps={{
                                               shrink: true,
                                           }} onChange={this.onStartDateChange}/>
                                <TextField label={getMessage("endDate")} type="date"
                                           defaultValue={moment(this.state.endDate).format(constants.DATE_FORMAT)}
                                           InputLabelProps={{
                                               shrink: true,
                                           }} onChange={this.onEndDateChange}/>
                                <ToolbarSeparator />
                                <Button variant="raised" onClick={this.applyFilter}
                                        className={classes.statFilterButton}>{getMessage("applyFilter")}</Button>

                            </ToolbarGroup>
                        </Toolbar>
                    </div>
                    <Table className={classes.statTable}>
                        <TableHead>
                            <TableRow hover>
                                {columnData.map(column => (
                                    <TableCell className={classes.statTableHead}
                                               key={column.name}
                                               numeric={column.numeric}
                                               sortDirection={orderBy === column.name ? order : false}
                                    >
                                        <Tooltip
                                            title={getMessage("sort")}
                                            placement="bottom-start"
                                            enterDelay={300}
                                        >
                                            <TableSortLabel
                                                active={orderBy === column.name}
                                                direction={order}
                                                onClick={this.createSortHandler(column.name)}
                                            >
                                                {getMessage(column.name)}
                                            </TableSortLabel>
                                        </Tooltip>
                                    </TableCell>
                                ))}
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {data.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map(n => {
                                return (
                                    <TableRow hover key={n.id}>
                                        <TableCell>{n.name}</TableCell>
                                        <TableCell
                                            numeric>{(n.rating.average) ? n.rating.average : 0}</TableCell>
                                        <TableCell
                                            numeric>{(n.job_stats.job_count) ? n.job_stats.job_count : 0 }</TableCell>
                                        <TableCell
                                            numeric>{(n.job_stats.job_count_completed) ? n.job_stats.job_count_completed : 0}</TableCell>
                                        <TableCell
                                            numeric>{(n.job_stats.job_count_failed) ? n.job_stats.job_count_failed : 0 }</TableCell>
                                        <TableCell>{(n.job_stats.job_last_completed) ? moment(Number(n.job_stats.job_last_completed), "x").format(
                                                constants.DATE_FORMAT) :
                                            getMessage("emptyValue")} </TableCell>
                                        <TableCell>{(n.job_stats.last_used) ? moment(Number(n.job_stats.last_used), "x").format(
                                                constants.DATE_FORMAT) :
                                            getMessage("emptyValue")}</TableCell>
                                    </TableRow>
                                );
                            })}
                        </TableBody>
                        <TableFooter>
                            <TableRow>
                                <TablePagination
                                    colSpan={3}
                                    count={data.length}
                                    rowsPerPage={rowsPerPage}
                                    page={page}
                                    onChangePage={this.handleChangePage}
                                    onChangeRowsPerPage={this.handleChangeRowsPerPage}
                                    Actions={TablePaginationActionsWrapped}
                                    rowsPerPageOptions={[100, 500, 1000]}
                                />
                            </TableRow>
                        </TableFooter>
                    </Table>
                </div>
        )

    }
}
AppStats.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default injectSheet(exStyles)(withI18N(AppStats, intlData), TablePaginationActionsWrapped);
