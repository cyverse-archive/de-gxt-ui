/**
 *  @author sriram
 * */
import ReactTable from "react-table";
import React, {Component} from "react";
import 'react-table/react-table.css';
class AppStats extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        const appearance = this.props.appearance;
        return (
            <div>
                <ReactTable
                    data={this.props.appStats}
                    columns={[
                        {
                            Header: appearance.name,
                            accessor: "name"
                        }, {
                            id: 'rating',
                            Header: appearance.rating,
                            accessor: r => r.rating.average
                        }, {
                            id: 'total',
                            Header: appearance.total,
                            accessor: s => s.job_stats["job_count"]
                        }, {
                            id: 'completed',
                            Header: appearance.completed,
                            accessor: s => s.job_stats["job_count_completed"]
                        }, {
                            id: 'failed',
                            Header: appearance.failed,
                            accessor: s => s.job_stats["job_count_failed"]
                        }, {
                           id: 'lastCompleted',
                           Header: appearance.lastCompleted,
                           accessor: s => s.job_stats["job_last_completed"]
                        }, {
                            id: 'lastUsed',
                            Header: appearance.lastUsed,
                            accessor: s => s.job_stats["last_used"]
                        }
                    ]}
                />
            </div>
        )

    }

}

export default AppStats;