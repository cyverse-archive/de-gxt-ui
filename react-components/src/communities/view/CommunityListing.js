import DEHyperLink from "../../util/hyperlink/DEHyperLink";
import EnhancedTableHead from "../../util/table/EnhancedTableHead";
import { getSorting, stableSort } from "../../util/table/TableSort";
import ids from "../ids";
import styles from "../styles";

import PropTypes from "prop-types";
import React, { Component } from "react";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableRow from "@material-ui/core/TableRow";
import { withStyles } from "@material-ui/core/styles";
import CircularProgress from "@material-ui/core/CircularProgress/CircularProgress";

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
            loading,
            collaboratorsUtil,
            parentId,
            data,
            onCommunityClicked,
            classes,
        } = this.props;

        return (
            <div className={classes.wrapper}>
                {loading &&
                <CircularProgress size={30} classes={{root: classes.loading}} thickness={7}/>
                }
                <Table>
                    <EnhancedTableHead selectable={false}
                                       rowCount={data ? data.length : 0}
                                       order={order}
                                       orderBy={orderBy}
                                       baseId={parentId}
                                       ids={ids.TABLE_HEADER}
                                       columnData={tableColumns}
                                       onRequestSort={this.onRequestSort}
                    />
                    <TableBody>
                        {data && data.length > 0 && stableSort(data, getSorting(order, orderBy)).map(community => {
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
            </div>
        )
    }
}

const tableColumns = [
    {name: "Name", numeric: false, enableSorting: true,},
    {name: "Description", numeric: false, enableSorting: false,},
];

CommunityListing.propTypes = {
    loading: PropTypes.bool.isRequired,
    collaboratorsUtil: PropTypes.object.isRequired,
    parentId: PropTypes.string.isRequired,
    data: PropTypes.array.isRequired,
    onCommunityClicked: PropTypes.func.isRequired,
};

export default withStyles(styles)(CommunityListing);