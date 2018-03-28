/**
 *  @author sriram
 * */
import ReactTable from "react-table";
import React, {Component} from "react";
import "react-table/react-table.css";
import {FormattedDate, IntlProvider} from "react-intl";
import {Toolbar, ToolbarGroup, ToolbarSeparator, ToolbarTitle} from "material-ui/Toolbar";
import TextField from "material-ui/TextField";
import DatePicker from "material-ui/DatePicker";

class AppStats extends Component {
    constructor(props) {
        super(props);
        this.state = {
            data: [],
            loading: true
        };
        this.handleSearch = this.handleSearch.bind(this);
        this.fetchAppStats = this.fetchAppStats.bind(this);
    }

    handleSearch(event, searchText) {
        console.log(searchText);
        if (searchText && searchText.length >= 3 || !searchText) {
            this.fetchAppStats(searchText);
        }
    }

    componentDidMount() {
        this.fetchAppStats();
    }

    fetchAppStats(searchText) {
        this.setState({
            loading: true,
        });
        this.props.presenter.searchApps((searchText) ? searchText : "", (apps) => {
            this.setState({
                loading: false,
                data: apps,
            })
        })
    }

    render() {
        const appearance = this.props.appearance;
        const {data, loading} = this.state;
        const toolbarStyle = {
            height: '30px',
        };
        const divStyle = {
            height: '800px',  //fixed header with vertical scroll
        };
        return (
            <div>
                <div>
                    <Toolbar style={toolbarStyle}>
                        <ToolbarGroup>
                            <TextField
                                hintText="Search Apps..." onChange={this.handleSearch}/>
                            <ToolbarSeparator />
                            <DatePicker hintText="From date..." container="inline"/>
                            <ToolbarSeparator />
                            <DatePicker hintText="To date..." container="inline"/>
                        </ToolbarGroup>
                    </Toolbar>
                </div>
                <div style={divStyle}>
                    <ReactTable
                        data={data}
                        loading={loading}
                        style={divStyle}
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
                                    </IntlProvider> : "-"
                            }, {
                                id: 'lastUsed',
                                Header: appearance.lastUsed(),
                                accessor: s => s.job_stats.last_used ? <IntlProvider locale="en">
                                        <FormattedDate value={Number(s.job_stats.last_used)}
                                                       day="numeric"
                                                       month="short"
                                                       year="numeric"/>
                                    </IntlProvider> : "-"

                            }
                        ]}
                    />
                </div>
            </div>
        )

    }

}

export default AppStats;