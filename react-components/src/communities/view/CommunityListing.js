import DEHyperLink from "../../util/hyperlink/DEHyperLink";
import EnhancedTableHead from "../../util/table/EnhancedTableHead";
import { getSorting, stableSort } from "../../util/table/TableSort";
import ids from "../ids";

import PropTypes from "prop-types";
import React, { Component } from "react";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableRow from "@material-ui/core/TableRow";

/**
 * @author aramsey
 *
 * A component that will show a listing of communities (Group type)
 */
class CommunityListing extends Component {

    constructor(props) {
        super(props);

        this.state = {
            order: "desc",
            orderBy: "Name",
        };

        this.onRequestSort = this.onRequestSort.bind(this);
    }

    onRequestSort(event, property) {
        const orderBy = property;
        let order = 'desc';

        if (this.state.orderBy === property && this.state.order === 'desc') {
            order = 'asc';
        }
        this.setState({order, orderBy});
    }

    render() {
        const {
            order,
            orderBy,
        } = this.state;

        const {
            collaboratorsUtil,
            parentId,
            data,
            onCommunityClicked,
        } = this.props;

        return (
            <Table>
                <EnhancedTableHead selectable={false}
                                   rowCount={data.length}
                                   order={order}
                                   orderBy={orderBy}
                                   baseId={parentId}
                                   ids={ids.TABLE_HEADER}
                                   columnData={tableColumns}
                                   onRequestSort={this.onRequestSort}
                />
                <TableBody>
                    {data && stableSort(data, getSorting(order, orderBy)).map(community => {
                        return (
                            <TableRow tabIndex={-1}
                                      hover
                                      key={community.id}>
                                <TableCell>
                                    <DEHyperLink onClick={() => onCommunityClicked(community)}
                                                 text={collaboratorsUtil.getSubjectDisplayName(community)}/>
                                </TableCell>
                                <TableCell>
                                    {community.description}
                                </TableCell>
                            </TableRow>
                        )
                    })}
                </TableBody>
            </Table>
        )
    }
}

const tableColumns = [
    {name: "Name",          numeric: false, enableSorting: true,},
    {name: "Description",   numeric: false, enableSorting: false,},
];

CommunityListing.propTypes = {
    collaboratorsUtil: PropTypes.object.isRequired,
    parentId: PropTypes.string.isRequired,
    data: PropTypes.array.isRequired,
    onCommunityClicked: PropTypes.func.isRequired,
};

export default CommunityListing;