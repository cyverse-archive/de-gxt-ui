/**
 *  @author sriram
 * */
import ReactTable from "react-table";
import React, {Component} from "react";
import "react-table/react-table.css";
import {FormattedDate, IntlProvider} from "react-intl";
import {Toolbar, ToolbarGroup, ToolbarSeparator} from "material-ui/Toolbar";
import TextField from "material-ui/TextField";
import DatePicker from "material-ui/DatePicker";
import RaisedButton from "material-ui/RaisedButton";
import dateformat from "dateformat";

class AppStats extends Component {
    constructor(props) {
        super(props);
        this.state = {
            data: [],
            loading: true,
            searchText: null,
            startDate: null,
            endDate: null,
            filterDisabled: true,
        };
        this.handleSearch = this.handleSearch.bind(this);
        this.fetchAppStats = this.fetchAppStats.bind(this);
        this.applyFilter = this.applyFilter.bind(this);
        this.onStartDateChange = this.onStartDateChange.bind(this);
        this.onEndDateChange = this.onEndDateChange.bind(this);
    }

    handleSearch(event, searchText) {
        console.log(searchText);
        if ((searchText && searchText.length >= 3) || !searchText) {
            this.setState({searchText: searchText});
            this.fetchAppStats();
        }
    }

    onStartDateChange(undef, date) {
        this.setState({startDate: dateformat(date, "yyyy-mm-dd")});
    }

    onEndDateChange(undef, date) {
        this.setState({endDate: dateformat(date, "yyyy-mm-dd")});
    }

    componentDidMount() {
        this.fetchAppStats();
    }

    fetchAppStats() {
        this.setState({
            loading: true,
        });
        var searchText = this.state.searchText;
        var startDate = this.state.startDate;
        var endDate = this.state.endDate;
        this.props.presenter.searchApps(searchText, startDate, endDate, (apps) => {
            this.setState({
                loading: false,
                data: apps,
            })
        })
    }

    applyFilter() {
        this.fetchAppStats();
    }

    render() {
        const appearance = this.props.appearance;
        const {data, loading} = this.state;
        const disabled = (this.state.startDate && this.state.endDate) ? false : true;
        return (
            <div>
                <div>
                    <Toolbar style={appearance.toolbarStyle()}>
                        <ToolbarGroup>
                            <TextField
                                hintText={appearance.searchApps()} onChange={this.handleSearch}/>
                            <ToolbarSeparator />
                            <DatePicker hintText={appearance.startDate()} container="inline"
                                        onChange={this.onStartDateChange}/>
                            <ToolbarSeparator />
                            <DatePicker hintText={appearance.endDate()} container="inline"
                                        onChange={this.onEndDateChange}/>
                            <RaisedButton label={appearance.applyFilter()} onClick={this.applyFilter}
                                          disabled={disabled} buttonStyle={appearance.buttonStyle()}/>
                        </ToolbarGroup>
                    </Toolbar>
                </div>
                <div>
                    <ReactTable
                        data={data}
                        loading={loading}
                        style={appearance.gridStyle()}
                        className="-striped -highlight"
                        defaultPageSize={100}
                        columns={[
                            {
                                Header: appearance.name(),
                                accessor: "name",
                                Cell: row => (
                                    <span title={row.value}>{row.value}</span>  //example of custom cell.
                                )
                            }, {
                                id: 'rating',
                                Header: appearance.rating(),
                                accessor: r => r.rating.average
                            }, {
                                id: 'total',
                                Header: appearance.total(),
                                accessor: s => s.job_stats.job_count ? s.job_stats.job_count : 0
                            }, {
                                id: 'completed',
                                Header: appearance.completed(),
                                accessor: s => s.job_stats.job_count_completed ? s.job_stats.job_count_completed : 0
                            }, {
                                id: 'failed',
                                Header: appearance.failed(),
                                accessor: s => s.job_stats.job_count_failed ? s.job_stats.job_count_failed : 0
                            }, {
                                id: 'lastCompleted',
                                Header: appearance.lastCompleted(),
                                accessor: s => s.job_stats.job_last_completed ?                       //example of how to provide custom JSX
                                    <IntlProvider locale="en">
                                        <FormattedDate value={Number(s.job_stats.job_last_completed)}
                                                       day="numeric"
                                                       month="short"
                                                       year="numeric"/>
                                    </IntlProvider> : appearance.emptyDate()
                            }, {
                                id: 'lastUsed',
                                Header: appearance.lastUsed(),
                                accessor: s => s.job_stats.last_used ? <IntlProvider locale="en">
                                        <FormattedDate value={Number(s.job_stats.last_used)}
                                                       day="numeric"
                                                       month="short"
                                                       year="numeric"/>
                                    </IntlProvider> : appearance.emptyDate()

                            }
                        ]}
                    />
                </div>
            </div>
        )

    }

}

export default AppStats;