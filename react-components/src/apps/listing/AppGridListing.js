import AppStatusIcon from "./AppStatusIcon";
import DeleteBtn from "../../data/search/queryBuilder/DeleteBtn";
import EnhancedTableHead from "../../util/table/EnhancedTableHead";
import { getSorting, stableSort } from "../../util/table/TableSort";
import ids from "./ids";

import PropTypes from "prop-types";
import Checkbox from "@material-ui/core/Checkbox";
import React, { Component } from "react";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableRow from "@material-ui/core/TableRow";

/**
 * @author aramsey
 *
 * A component that will show a listing of type App
 */
class AppGridListing extends Component {

    constructor(props) {
        super(props);

        this.state = {
            selected: [],
            order: "desc",
            orderBy: "Name",
        };

        [
            'isSelected',
            'handleRowClick',
            'handleSelectAllClick',
            'onRequestSort',
        ].forEach((fn) => this[fn] = this[fn].bind(this));

    }

    onRequestSort(event, property) {
        const orderBy = property;
        let order = 'desc';

        if (this.state.orderBy === property && this.state.order === 'desc') {
            order = 'asc';
        }
        this.setState({order, orderBy});
    }

    isSelected(app) {
        return this.state.selected.indexOf(app.id) !== -1
    }

    handleSelectAllClick(event, checked) {
        if (checked) {
            this.setState({selected: this.props.data.map(app => app.id)});
            return;
        }
        this.setState({selected: []});
    };

    handleRowClick(id) {
        const {selected} = this.state;
        const selectedIndex = selected.indexOf(id);
        let newSelected = [];

        if (selectedIndex === -1) {
            newSelected = newSelected.concat(selected, id);
        } else if (selectedIndex === 0) {
            newSelected = newSelected.concat(selected.slice(1));
        } else if (selectedIndex === selected.length - 1) {
            newSelected = newSelected.concat(selected.slice(0, -1));
        } else if (selectedIndex > 0) {
            newSelected = newSelected.concat(
                selected.slice(0, selectedIndex),
                selected.slice(selectedIndex + 1),
            );
        }

        this.setState({selected: newSelected});
    };

    render() {
        const {
            selected,
            order,
            orderBy,
        } = this.state;

        const {
            parentId,
            data,
            selectable,
            deletable,
            onRemoveApp,
        } = this.props;

        return (
            <Table>
                <EnhancedTableHead selectable={selectable}
                                   numSelected={selected.length}
                                   rowCount={data.length}
                                   order={order}
                                   orderBy={orderBy}
                                   baseId={parentId}
                                   ids={ids.TABLE_HEADER}
                                   columnData={getTableColumns(deletable)}
                                   onRequestSort={this.onRequestSort}
                                   onSelectAllClick={this.handleSelectAllClick}
                />
                <TableBody>
                    {data && stableSort(data, getSorting(order, orderBy)).map(app => {
                        const isSelected = this.isSelected(app);
                        return (
                            <TableRow role="checkbox"
                                      tabIndex={-1}
                                      hover
                                      selected={isSelected}
                                      aria-checked={isSelected}
                                      onClick={() => this.handleRowClick(app.id)}
                                      key={app.id}>
                                {selectable &&
                                <TableCell padding="checkbox">
                                    <Checkbox checked={isSelected}/>
                                </TableCell>
                                }
                                <TableCell>
                                    <AppStatusIcon app={app}/>
                                </TableCell>
                                <TableCell>
                                    {app.name}
                                </TableCell>
                                <TableCell>
                                    {app.integrator_name}
                                </TableCell>
                                <TableCell>
                                    {app.system_id}
                                </TableCell>
                                {deletable &&
                                <TableCell>
                                    <DeleteBtn onClick={() => onRemoveApp(app)}/>
                                </TableCell>
                                }
                            </TableRow>
                        )
                    })}
                </TableBody>
            </Table>
        )
    }
}

function getTableColumns(deletable) {
    let tableColumns = [
        {name: "",              numeric: false, enableSorting: false, key: "status"},
        {name: "Name",          numeric: false, enableSorting: true,},
        {name: "Integrated By", numeric: false, enableSorting: true, key: "integrator_name"},
        {name: "System",        numeric: false, enableSorting: true, key: "system_id"},
    ];

    if (deletable) {
        tableColumns.push(
            {name: "", numeric: false, enableSorting: false, key: "remove"}
        )
    }

    return tableColumns;
}

AppGridListing.propTypes = {
    parentId: PropTypes.string.isRequired,
    data: PropTypes.array.isRequired,
    selectable: PropTypes.bool,
    deletable: PropTypes.bool,
    onRemoveApp: PropTypes.func,
};

export default AppGridListing;