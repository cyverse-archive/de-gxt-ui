/**
 *  @author sriram
 * */
import React, {Component} from "react";
import Toolbar from "material-ui-next/Toolbar";
import ToolbarGroup from "material-ui-next/Toolbar";
import ToolbarSeparator from "material-ui-next/Toolbar";
import TextField from "material-ui-next/TextField";
import DatePicker from "react-datepicker";
import Button from "material-ui-next/Button";
import {withStyles} from "material-ui-next/styles";
import Table, {
    TableHead,
    TableBody,
    TableCell,
    TableFooter,
    TablePagination,
    TableRow
} from "material-ui-next/Table";
import IconButton from "material-ui-next/IconButton";
import FirstPageIcon from "@material-ui/icons/FirstPage";
import KeyboardArrowLeft from "@material-ui/icons/KeyboardArrowLeft";
import KeyboardArrowRight from "@material-ui/icons/KeyboardArrowRight";
import LastPageIcon from "@material-ui/icons/LastPage";
import Paper from "material-ui-next/Paper";
import PropTypes from "prop-types";
import * as moment from "moment";
import "react-datepicker/dist/react-datepicker.css";

const pagingStyles = theme => ({
    root: {
        flexShrink: 0,
        color: theme.palette.text.secondary,
        marginLeft: theme.spacing.unit * 2.5,

    },
});

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
        const {classes, count, page, rowsPerPage, theme} = this.props;

        return (
            <div className={classes.root}>
                <IconButton
                    onClick={this.handleFirstPageButtonClick}
                    disabled={page === 0}
                    aria-label="First Page"
                >
                    {theme.direction === 'rtl' ? <LastPageIcon /> : <FirstPageIcon />}
                </IconButton>
                <IconButton
                    onClick={this.handleBackButtonClick}
                    disabled={page === 0}
                    aria-label="Previous Page"
                >
                    {theme.direction === 'rtl' ? <KeyboardArrowRight /> : <KeyboardArrowLeft />}
                </IconButton>
                <IconButton
                    onClick={this.handleNextButtonClick}
                    disabled={page >= Math.ceil(count / rowsPerPage) - 1}
                    aria-label="Next Page"
                >
                    {theme.direction === 'rtl' ? <KeyboardArrowLeft /> : <KeyboardArrowRight />}
                </IconButton>
                <IconButton
                    onClick={this.handleLastPageButtonClick}
                    disabled={page >= Math.ceil(count / rowsPerPage) - 1}
                    aria-label="Last Page"
                >
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

const TablePaginationActionsWrapped = withStyles(pagingStyles, {withTheme: true})(
    TablePaginationActions,
);

const styles = theme => ({
    paper: {
        width: '95%',
        marginTop: theme.spacing.unit * 3,
    },
    table: {
        minWidth: 500,
    },
    tableWrapper: {
        overflowX: 'auto',
    },
    textField: {
        marginLeft: 1,
        marginRight: 1,
        width: 200,
    },
    button: {
        margin: 1,
    },
});

class AppStats extends Component {
    constructor(props) {
        super(props);
        var today = new Date();
        this.state = {
            data: [],
            loading: true,
            searchText: null,
            startDate: new Date(today.setMonth(today.getMonth() - 3)),  // set default date range for 90 days!
            endDate: new Date(),
            filterDisabled: true,
            page: 0,
            rowsPerPage: 100,
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
        console.log(event.target.value);
        if (!event.target.value || event.target.value.length >= 3 || event.target.value.length === 0) {
            this.setState({searchText: event.target.value});
        }
    }

    onStartDateChange(date) {
        this.setState({startDate: date});
    }

    onEndDateChange(date) {
        this.setState({endDate: date});
    }

    componentDidMount() {
       this.fetchAppStats();
    }

    fetchAppStats() {
        this.setState({
            loading: true,
        });
        var searchText = this.state.searchText;
        var startDate = moment(this.state.startDate).format("YYYY-MM-DD");
        var endDate = moment(this.state.endDate).format("YYYY-MM-DD");
        this.props.presenter.searchApps(searchText, startDate, endDate, (apps) => {
            this.setState({
                loading: false,
                data: apps,
            })
        });
    }

    applyFilter() {
        this.fetchAppStats();
    }

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
        const appearance = this.props.appearance;
        const btnDisabled = (this.state.startDate && this.state.endDate) ? false : true;
        const classes = this.props.classes;
        const {data, rowsPerPage, page} = this.state;
        const containerStyle = {
            height: 500,
            overflow: 'auto',
        }

        let filterBtn = null;
        if (btnDisabled) {
            filterBtn = <Button variant="raised" disabled onClick={this.applyFilter}
                                className={classes.button}>{appearance.applyFilter()}</Button>
        } else {
            filterBtn = <Button variant="raised" onClick={this.applyFilter}
                                className={classes.button}>{appearance.applyFilter()}</Button>
        }
        return (
            <div>
                <div>
                    <Toolbar style={appearance.toolbarStyle()}>
                        <ToolbarGroup>
                            <TextField className={classes.textField}
                                       label={appearance.searchApps()} onChange={this.handleSearch}/>
                            <ToolbarSeparator />
                            <DatePicker placeholderText={appearance.startDate()}
                                        onChange={this.onStartDateChange}
                                        selected={moment(this.state.startDate)}/>
                            <ToolbarSeparator />
                            <DatePicker placeholderText={appearance.endDate()}
                                        onChange={this.onEndDateChange}
                                        selected={moment(this.state.endDate)}/>

                            <ToolbarSeparator />
                            {filterBtn}
                        </ToolbarGroup>
                    </Toolbar>
                </div>
                <div style={containerStyle}>
                    <Paper className={classes.paper}>
                    <Table className={classes.table}>
                        <TableHead>
                            <TableRow hover>
                                <TableCell>{appearance.name()}</TableCell>
                                <TableCell numeric>{appearance.rating()}</TableCell>
                                <TableCell numeric>{appearance.total()}</TableCell>
                                <TableCell numeric>{appearance.completed()}</TableCell>
                                <TableCell numeric>{appearance.failed()}</TableCell>
                                <TableCell numeric>{appearance.lastCompleted()}</TableCell>
                                <TableCell numeric>{appearance.lastUsed()}</TableCell>
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
                                        <TableCell>{(n.job_stats.job_count_completed) ? moment(Number(n.job_stats.job_last_completed, "x")).format("YYYY-MM-DD") : ""} </TableCell>
                                        <TableCell>{(n.job_stats.last_used) ? moment(Number(n.job_stats.last_used, "x")).format("YYYY-MM-DD") : ""}</TableCell>
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
                                />
                            </TableRow>
                        </TableFooter>
                    </Table>
                </Paper>
                </div>
            </div>
        )

    }

}
AppStats.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(AppStats, TablePaginationActionsWrapped);