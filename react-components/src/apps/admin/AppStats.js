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

    handleSearch(event, searchText) {
        console.log(searchText);
        if (!searchText || searchText.length >= 3 || searchText.length === 0) {
            this.setState({searchText: searchText});
            this.fetchAppStats();
        }
    }

    onStartDateChange(undef, date) {
        this.setState({startDate: date});
    }

    onEndDateChange(undef, date) {
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
        var startDate = dateformat(this.state.startDate, "yyyy-mm-dd");
        var endDate = dateformat(this.state.endDate, "yyyy-mm-dd");
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
        console.log("start date->" + this.state.startDate + " end date->" + this.state.endDate);
        return (
            <div>
                <div>
                    <Toolbar style={appearance.toolbarStyle()}>
                        <ToolbarGroup>
                            <TextField
                                hintText={appearance.searchApps()} onChange={this.handleSearch}/>
                            <ToolbarSeparator />
                            <DatePicker hintText={appearance.startDate()} container="inline"
                                        onChange={this.onStartDateChange} value={this.state.startDate}/>
                            <ToolbarSeparator />
                            <DatePicker hintText={appearance.endDate()} container="inline"
                                        onChange={this.onEndDateChange} value={this.state.endDate}/>
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
                        filterable
                        defaultFilterMethod={(filter, row) =>
                        String(row[filter.id]) === filter.value}
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
                                accessor: s => s.job_stats.job_last_completed ?    //example of how to provide custom JSX
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

                            }, {
                                id: 'integrator',
                                Header: appearance.integrator(),
                                accessor: s => s.integrator_name,
                            }, {
                                id: 'beta',
                                Header: appearance.beta(),
                                accessor: s => s.beta ? new Boolean(s.beta).toString() : "false",
                                filterMethod: (filter, row) => {
                                    if (filter.value === "all") {
                                        return true;
                                    }
                                    if (filter.value === "true") {
                                        return row[filter.id] === "true";
                                    }
                                    return row[filter.id] === "false";
                                },
                                Filter: ({filter, onChange}) =>
                                    <select
                                        onChange={event => onChange(event.target.value)}
                                        style={{width: "100%"}}
                                        value={filter ? filter.value : "all"}
                                    >
                                        <option value="all">Show All</option>
                                        <option value="true">Apps In Beta</option>
                                        <option value="false">Apps Not In Beta</option>
                                    </select>
                            }, {
                                id: 'system',
                                Header: appearance.system(),
                                accessor: s => s.system_id,
                            },
                        ]}
                    />
                </div>
            </div>
        )

    }

}

export default AppStats;