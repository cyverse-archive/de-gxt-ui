/**
 *  @author sriram
 * */
import React, {Component} from "react";
import Toolbar from "material-ui-next/Toolbar";
import ToolbarGroup from "material-ui-next/Toolbar";
import ToolbarSeparator from "material-ui-next/Toolbar";
import TextField from "material-ui-next/TextField";
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
import PropTypes from "prop-types";
import moment  from "moment";
import RefreshIndicator from "material-ui/RefreshIndicator";


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
        const {classes, count, page, rowsPerPage, theme, appearance} = this.props;

        return (
            <div className={classes.root}>
                <IconButton
                    onClick={this.handleFirstPageButtonClick}
                    disabled={page === 0}
                    aria-label='First Page'
                >
                    {theme.direction === 'rtl' ? <LastPageIcon /> : <FirstPageIcon />}
                </IconButton>
                <IconButton
                    onClick={this.handleBackButtonClick}
                    disabled={page === 0}
                    aria-label='Previous Page'
                >
                    {theme.direction === 'rtl' ? <KeyboardArrowRight /> : <KeyboardArrowLeft />}
                </IconButton>
                <IconButton
                    onClick={this.handleNextButtonClick}
                    disabled={page >= Math.ceil(count / rowsPerPage) - 1}
                    aria-label='Next Page'
                >
                    {theme.direction === 'rtl' ? <KeyboardArrowLeft /> : <KeyboardArrowRight />}
                </IconButton>
                <IconButton
                    onClick={this.handleLastPageButtonClick}
                    disabled={page >= Math.ceil(count / rowsPerPage) - 1}
                    aria-label='Last Page'
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
    root: {
        width: "100%",
        marginTop: theme.spacing.unit * 3,
        overflow: "auto",
        height: 800,
    },
    textField: {
        marginLeft: theme.spacing.unit,
        marginRight: theme.spacing.unit,
        width: 200,
    },
    button: {
        margin: 1,
    },
    table: {
        overflow: "auto",
    },
    head: {
        backgroundColor: "#e2e2e2",
        position: "sticky",
        top: 0
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
        var searchText = this.state.searchText;
        var startDate = (this.state.startDate) ? (moment(this.state.startDate).format(this.props.appearance.dateFormat())) : "";
        var endDate = (this.state.endDate) ? (moment(this.state.endDate).format(this.props.appearance.dateFormat())) : "";
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
        const classes = this.props.classes;
        const {data, rowsPerPage, page} = this.state;
        return (
            <div className={classes.root}>
                <RefreshIndicator
                    size={50}
                    left={600}
                    top={400}
                    loadingColor="#FF9800"
                    status={(this.state.loading) ? "loading" : "hide"}/>
                <div>
                    <Toolbar>
                        <ToolbarGroup>
                            <TextField className={classes.textField}
                                       label={appearance.searchApps()} onChange={this.handleSearch}/>
                            <ToolbarSeparator />
                            <TextField label={appearance.startDate()} type="date"
                                       defaultValue={moment(this.state.startDate).format(appearance.dateFormat())}
                                       InputLabelProps={{
                                           shrink: true,
                                       }} onChange={this.onStartDateChange}/>
                            <TextField label={appearance.endDate()} type="date"
                                       defaultValue={moment(this.state.endDate).format(appearance.dateFormat())}
                                       InputLabelProps={{
                                           shrink: true,
                                       }} onChange={this.onEndDateChange}/>
                            <ToolbarSeparator />
                            <Button variant="raised" onClick={this.applyFilter}
                                    className={classes.button}>{appearance.applyFilter()}</Button>

                        </ToolbarGroup>
                    </Toolbar>
                </div>
                    <Table className={classes.table}>
                        <TableHead>
                            <TableRow hover>
                                <TableCell className={classes.head}>{appearance.name()}</TableCell>
                                <TableCell className={classes.head}
                                           numeric>{appearance.rating()}</TableCell>
                                <TableCell className={classes.head}
                                           numeric>{appearance.total()}</TableCell>
                                <TableCell className={classes.head}
                                           numeric>{appearance.completed()}</TableCell>
                                <TableCell className={classes.head}
                                           numeric>{appearance.failed()}</TableCell>
                                <TableCell className={classes.head}
                                           numeric>{appearance.lastCompleted()}</TableCell>
                                <TableCell className={classes.head}
                                           numeric>{appearance.lastUsed()}</TableCell>
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
                                        <TableCell>{(n.job_stats.job_last_completed) ? moment(Number(n.job_stats.job_last_completed), "x").format(appearance.dateFormat()) : appearance.emptyValue()} </TableCell>
                                        <TableCell>{(n.job_stats.last_used) ? moment(Number(n.job_stats.last_used), "x").format(appearance.dateFormat()) : appearance.emptyValue()}</TableCell>
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

export default withStyles(styles)(AppStats, TablePaginationActionsWrapped);