/**
 *  @author sriram
 * */
import ReactTable from "react-table";
import React, {Component} from "react";
import "react-table/react-table.css";
import {FormattedDate, IntlProvider} from "react-intl";
class AppStats extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        const appearance = this.props.appearance;
        const divStyle = {
            height: '800px',  //fixed header with vertical scroll
        };
        return (
            <div style={divStyle}>
                <ReactTable
                    data={this.props.appStats}
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
                            accessor: s => s.job_stats.last_used ?  <IntlProvider locale="en">
                                    <FormattedDate value={Number(s.job_stats.last_used)}
                                                   day="numeric"
                                                   month="short"
                                                   year="numeric"/>
                                </IntlProvider> : "-"

                        }
                    ]}
                />
            </div>
        )

    }

}

export default AppStats;