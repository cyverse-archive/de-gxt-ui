import EnhancedTableHead from "../util/table/EnhancedTableHead";
import ids from "./ids";

import PropTypes from "prop-types";
import Checkbox from "@material-ui/core/Checkbox";
import React, { Component } from "react";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableRow from "@material-ui/core/TableRow";
import TeamIcon from "./TeamIcon";
import CollaboratorListIcon from "./CollaboratorListIcon";
import DeleteBtn from "../data/search/queryBuilder/DeleteBtn";

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
            order: "desc",
            orderBy: "Name",
        };

        [
            'isSelected',
            'handleRowClick',
            'handleSelectAllClick',
        ].forEach((fn) => this[fn] = this[fn].bind(this));

    }

    isSelected(subject) {
        return this.state.selected.indexOf(subject.id) !== -1
    }

    handleSelectAllClick(event, checked) {
        if (checked) {
            this.setState({selected: this.props.data.map(subject => subject.id)});
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
            selectable,
            collaboratorsUtil,
            parentId,
            data,
            deletable,
            onDeleteCollaborator
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
                                   onSelectAllClick={this.handleSelectAllClick}
                />
                <TableBody>
                    {data.map(subject => {
                        const isSelected = this.isSelected(subject);
                        return (
                            <TableRow role="checkbox"
                                      tabIndex={-1}
                                      hover
                                      selected={isSelected}
                                      aria-checked={isSelected}
                                      onClick={() => this.handleRowClick(subject.id)}
                                      key={subject.id}>
                                {selectable &&
                                <TableCell padding="checkbox">
                                    <Checkbox checked={isSelected}/>
                                </TableCell>}
                                <TableCell>
                                    {collaboratorsUtil.isTeam(subject) && <TeamIcon/>}
                                    {collaboratorsUtil.isCollaboratorList(subject) && <CollaboratorListIcon/>}
                                    {collaboratorsUtil.getSubjectDisplayName(subject)}
                                </TableCell>
                                <TableCell>
                                    {subject.institution ? subject.institution : subject.description}
                                </TableCell>
                                {deletable &&
                                <TableCell>
                                    <DeleteBtn onClick={() => onDeleteCollaborator(subject)}/>
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
        {name: "Name",                      numeric: false, enableSorting: true,},
        {name: "Institution/Description",   numeric: false, enableSorting: false,},
    ];
    if (deletable) {
        tableColumns.push(
            {name: "", numeric: false, enableSorting: false}
        )
    }

    return tableColumns;
}

CollaboratorListing.propTypes = {
    collaboratorsUtil: PropTypes.object.isRequired,
    parentId: PropTypes.string.isRequired,
    data: PropTypes.array.isRequired,
    onDeleteCollaborator: PropTypes.func,
    deletable: PropTypes.bool,
};

export default CollaboratorListing;