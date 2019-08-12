import React, { Component } from "react";
import PropTypes from "prop-types";

import ids from "./ids";
import messages from "./messages";
import TeamIcon from "./TeamIcon";
import CollaboratorListIcon from "./CollaboratorListIcon";
import DeleteBtn from "../data/search/queryBuilder/DeleteBtn";

import {
    EnhancedTableHead,
    EmptyTable,
    getSorting,
    stableSort,
    withI18N,
} from "@cyverse-de/ui-lib";

import {
    Checkbox,
    Table,
    TableBody,
    TableCell,
    TableRow,
} from "@material-ui/core";

/**
 * @author aramsey
 *
 * A component that will show a listing of type Subject
 */
class CollaboratorListing extends Component {
    constructor(props) {
        super(props);

        this.state = {
            selected: [],
            order: "asc",
            orderBy: "Name",
        };

        [
            "isSelected",
            "handleRowClick",
            "handleSelectAllClick",
            "onRequestSort",
        ].forEach((fn) => (this[fn] = this[fn].bind(this)));
    }

    isSelected(subject) {
        return this.state.selected.indexOf(subject.id) !== -1;
    }

    handleSelectAllClick(event, checked) {
        if (checked) {
            this.setState({
                selected: this.props.data.map((subject) => subject.id),
            });
            return;
        }
        this.setState({ selected: [] });
    }

    handleRowClick(id) {
        const { selected } = this.state;
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
                selected.slice(selectedIndex + 1)
            );
        }

        this.setState({ selected: newSelected });
    }

    onRequestSort(event, property) {
        const orderBy = property;
        let order = "desc";

        if (this.state.orderBy === property && this.state.order === "desc") {
            order = "asc";
        }
        this.setState({ order, orderBy });
    }

    render() {
        const { selected, order, orderBy } = this.state;

        const {
            selectable,
            collaboratorsUtil,
            parentId,
            data,
            deletable,
            onDeleteCollaborator,
            emptyTableMsg,
        } = this.props;

        let columnData = getTableColumns(deletable);

        return (
            <Table>
                <TableBody>
                    {(!data || data.length === 0) && (
                        <EmptyTable
                            message={emptyTableMsg}
                            numColumns={columnData.length}
                        />
                    )}
                    {data &&
                        data.length > 0 &&
                        stableSort(data, getSorting(order, orderBy)).map(
                            (subject) => {
                                const isSelected = this.isSelected(subject);
                                return (
                                    <TableRow
                                        role="checkbox"
                                        tabIndex={-1}
                                        hover
                                        selected={isSelected}
                                        aria-checked={isSelected}
                                        onClick={() =>
                                            this.handleRowClick(subject.id)
                                        }
                                        key={subject.id}
                                    >
                                        {selectable && (
                                            <TableCell padding="checkbox">
                                                <Checkbox
                                                    checked={isSelected}
                                                />
                                            </TableCell>
                                        )}
                                        <TableCell>
                                            {collaboratorsUtil.isTeam(
                                                subject
                                            ) && <TeamIcon />}
                                            {collaboratorsUtil.isCollaboratorList(
                                                subject
                                            ) && <CollaboratorListIcon />}
                                            {collaboratorsUtil.getSubjectDisplayName(
                                                subject
                                            )}
                                        </TableCell>
                                        <TableCell>
                                            {subject.institution
                                                ? subject.institution
                                                : subject.description}
                                        </TableCell>
                                        {deletable && (
                                            <TableCell>
                                                <DeleteBtn
                                                    onClick={() =>
                                                        onDeleteCollaborator(
                                                            subject
                                                        )
                                                    }
                                                />
                                            </TableCell>
                                        )}
                                    </TableRow>
                                );
                            }
                        )}
                </TableBody>
                <EnhancedTableHead
                    selectable={selectable}
                    numSelected={selected.length}
                    rowCount={data ? data.length : 0}
                    order={order}
                    orderBy={orderBy}
                    baseId={parentId}
                    ids={ids.TABLE_HEADER}
                    columnData={columnData}
                    onRequestSort={this.onRequestSort}
                    onSelectAllClick={this.handleSelectAllClick}
                />
            </Table>
        );
    }
}

function getTableColumns(deletable) {
    let tableColumns = [
        { name: "Name", numeric: false, enableSorting: true },
        {
            name: "Institution/Description",
            numeric: false,
            enableSorting: false,
        },
    ];
    if (deletable) {
        tableColumns.push({ name: "", numeric: false, enableSorting: false });
    }

    return tableColumns;
}

CollaboratorListing.propTypes = {
    collaboratorsUtil: PropTypes.object.isRequired,
    parentId: PropTypes.string.isRequired,
    data: PropTypes.array.isRequired,
    onDeleteCollaborator: PropTypes.func,
    deletable: PropTypes.bool,
    emptyTableMsg: PropTypes.oneOfType([PropTypes.string, PropTypes.object])
        .isRequired,
};

export default withI18N(CollaboratorListing, messages);
