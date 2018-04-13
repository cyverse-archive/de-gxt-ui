/**
 *  @author sriram
 * */
import React, {Component} from "react";
import {FormattedDate, IntlProvider} from "react-intl";
import Toolbar from "material-ui-next/Toolbar";
import ToolbarGroup from "material-ui-next/Toolbar";
import ToolbarSeparator from "material-ui-next/Toolbar";
import TextField from "material-ui-next/TextField";
import DatePicker from "react-datepicker";
import Button from "material-ui-next/Button";
import {withStyles} from "material-ui-next/styles";
import Table, {TableBody, TableCell, TableHead, TableRow} from "material-ui-next/Table";
import Paper from "material-ui-next/Paper";
import PropTypes from "prop-types";
import * as moment from "moment";
import "react-datepicker/dist/react-datepicker.css";
import Modal from 'material-ui-next/Modal';
import Typography from 'material-ui-next/Typography';

const styles = theme => ({
    root: {
        width: '95%',
        marginTop: 3,
        overflow: 'auto',
        height: 800,
    },
    table: {
        minWidth: 700,
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
        };
        this.handleSearch = this.handleSearch.bind(this);
        this.fetchAppStats = this.fetchAppStats.bind(this);
        this.applyFilter = this.applyFilter.bind(this);
        this.onStartDateChange = this.onStartDateChange.bind(this);
        this.onEndDateChange = this.onEndDateChange.bind(this);
    }

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

    render() {
        const appearance = this.props.appearance;
        const {data, loading} = this.state;
        const btnDisabled = (this.state.startDate && this.state.endDate) ? false : true;
        const classes = this.props.classes;
        let filterBtn = null;
        if (btnDisabled) {
            filterBtn = <Button variant="raised" disabled onClick={this.applyFilter}
                                className={classes.button}>{appearance.applyFilter()}</Button>
        } else {
            filterBtn = <Button variant="raised" onClick={this.applyFilter}
                                className={classes.button}>{appearance.applyFilter()}</Button>
        }
        console.log("start date->" + this.state.startDate + " end date->" + this.state.endDate);
         const divStyle = {
             width: '100px',
             height: '50px',
             border: '1px solid',
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
                <Paper className={classes.root}>
                    <Modal
                        aria-labelledby="simple-modal-title"
                        aria-describedby="simple-modal-description"
                        open={this.state.loading}>
                        <div style={divStyle}>
                            <Typography variant="title" id="modal-title">
                                Loading...
                            </Typography>
                        </div>
                    </Modal>
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
                        <TableBody className={classes.tableBody}>
                            {data.map(n => {
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
                    </Table>
                </Paper>
            </div>
        )

    }

}
AppStats.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(AppStats);