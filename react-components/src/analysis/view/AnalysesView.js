/**
 *  @author sriram
 *
 **/

import React, { Component } from 'react';
import EnhancedTableHead from "../../util/table/EnhancedTableHead";
import TablePaginationActions from "../../util/table/TablePaginationActions";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TablePagination from "@material-ui/core/TablePagination";
import TableRow from "@material-ui/core/TableRow";
import withI18N, { getMessage } from "../../util/I18NWrapper";
import Checkbox from "@material-ui/core/Checkbox";
import moment from "moment";
import constants from "../../constants";
import ids from "../ids";

const columnData = [
    {name: "Name", numeric: false, enableSorting: true},
    {name: "Owner", numeric: false, enableSorting: true},
    {name: "App", numeric: false, enableSorting: true},
    {name: "Start Date", numeric: false, enableSorting: true},
    {name: "End Date", numeric: false, enableSorting: true},
    {name: "Status", numeric: false, enableSorting: true},
];

class AnalysesView extends Component {
    constructor(props) {
        super(props);
        this.state = {
            data: [],
            loading: true,
            total: 0,
            offset: 0,
            page: 0,
            rowsPerPage: 100,
            selected: [],
            order: 'desc',
            orderBy: 'End Date',
        };
        this.handleChangePage = this.handleChangePage.bind(this);
        this.handleChangeRowsPerPage = this.handleChangeRowsPerPage.bind(this);
        this.handleSelectAllClick = this.handleSelectAllClick.bind(this);
        this.isSelected = this.isSelected.bind(this);
    }

    handleChangePage(event, page) {
        const {rowsPerPage} = this.state;
        this.setState({page: page, offset: rowsPerPage * page});

    }

    handleChangeRowsPerPage(event) {
        this.setState({rowsPerPage: event.target.value});
    }

    handleSelectAllClick(event, checked) {
        if (checked) {
            this.setState(state => ({selected: state.data.map(n => n.message.id)}));
            return;
        }
        this.setState({selected: []});
    }

    isSelected(id) {
        return this.state.selected.indexOf(id) !== -1
    }

    render() {
        const {classes, analyses} = this.props;
        const {
            rowsPerPage,
            page,
            order,
            orderBy,
            selected,
            total
        } = this.state;
            return (
            <div>
                <div className={classes.table}>
                    <Table>
                        <EnhancedTableHead
                            selectable={true}
                            numSelected={selected.length}
                            order={order}
                            orderBy={orderBy}
                            onSelectAllClick={this.handleSelectAllClick}
                            onRequestSort={this.handleRequestSort}
                            rowCount={total}
                            columnData={columnData}
                            baseId="analysis"
                            ids={ids}
                        />
                        <TableBody>
                            {analyses.map(n => {
                                const isSelected = this.isSelected(n.message.id);
                                return (
                                    <TableRow onClick={event => this.handleRowClick(event, n.message.id)}
                                              role="checkbox"
                                              aria-checked={isSelected}
                                              tabIndex={-1}
                                              selected={isSelected}
                                              hover
                                              key={n.message.id}>
                                        <TableCell padding="checkbox">
                                            <Checkbox checked={isSelected}/>
                                        </TableCell>
                                        <TableCell>{n.name} </TableCell>
                                        <TableCell>{n.username} </TableCell>
                                        <TableCell>{n.app_name} </TableCell>
                                        <TableCell>{parseInt(n.startdate, 10) ? moment(parseInt(n.startdate, 10), "x").format(
                                            constants.DATE_FORMAT) :
                                            getMessage("emptyValue")} </TableCell>
                                        <TableCell>{parseInt(n.enddate, 10) ? moment(parseInt(n.enddate, 10), "x").format(
                                            constants.DATE_FORMAT) :
                                            getMessage("emptyValue")}</TableCell>
                                        <TableCell>{n.status} </TableCell>
                                    </TableRow>
                                );
                            })}
                        </TableBody>
                    </Table>
                </div>
                <div>
                    <TablePagination
                        colSpan={3}
                        component="div"
                        count={total}
                        rowsPerPage={rowsPerPage}
                        page={page}
                        onChangePage={this.handleChangePage}
                        onChangeRowsPerPage={this.handleChangeRowsPerPage}
                        ActionsComponent={TablePaginationActions}
                        rowsPerPageOptions={[100, 500, 1000]}
                    />
                </div>
            </div>
        );
    }
}

AnalysesView.propTypes = {};

export default AnalysesView;
