import DEHyperLink from "../../util/hyperlink/DEHyperLink";
import EnhancedTableHead from "../../util/table/EnhancedTableHead";
import EmptyTable from "../../util/table/EmptyTable";
import { getSorting, stableSort } from "../../util/table/TableSort";
import ids from "../ids";
import messages from "../messages";
import styles from "../styles";
import withI18N, { getMessage } from "../../util/I18NWrapper";

import PropTypes from "prop-types";
import React, { Component, Fragment } from "react";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableRow from "@material-ui/core/TableRow";
import Tooltip from "@material-ui/core/Tooltip";
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
            order: "asc",
            orderBy: "Name",
        };

        this.onRequestSort = this.onRequestSort.bind(this);
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
        const { order, orderBy } = this.state;

        const {
            loading,
            collaboratorsUtil,
            parentId,
            data,
            onCommunityClicked,
            classes,
        } = this.props;

        return (
            <Fragment>
                {loading && (
                    <CircularProgress
                        size={30}
                        classes={{ root: classes.loading }}
                        thickness={7}
                    />
                )}
                <div className={classes.table}>
                    <Table>
                        <TableBody>
                            {(!data || data.length === 0) && (
                                <EmptyTable
                                    message={getMessage("noCommunities")}
                                    numColumns={tableColumns.length}
                                />
                            )}
                            {data &&
                                data.length > 0 &&
                                stableSort(
                                    data,
                                    getSorting(order, orderBy)
                                ).map((community) => {
                                    return (
                                        <TableRow
                                            tabIndex={-1}
                                            hover
                                            key={community.id}
                                        >
                                            <TableCell>
                                                <Tooltip
                                                    title={getMessage(
                                                        "communityNameToolTip"
                                                    )}
                                                >
                                                    <DEHyperLink
                                                        onClick={() =>
                                                            onCommunityClicked(
                                                                community
                                                            )
                                                        }
                                                        text={collaboratorsUtil.getSubjectDisplayName(
                                                            community
                                                        )}
                                                    />
                                                </Tooltip>
                                            </TableCell>
                                            <TableCell>
                                                {community.description}
                                            </TableCell>
                                        </TableRow>
                                    );
                                })}
                        </TableBody>
                        <EnhancedTableHead
                            selectable={false}
                            rowCount={data ? data.length : 0}
                            order={order}
                            orderBy={orderBy}
                            baseId={parentId}
                            ids={ids.TABLE_HEADER}
                            columnData={tableColumns}
                            onRequestSort={this.onRequestSort}
                        />
                    </Table>
                </div>
            </Fragment>
        );
    }
}

const tableColumns = [
    { name: "Name", numeric: false, enableSorting: true },
    { name: "Description", numeric: false, enableSorting: false },
];

CommunityListing.propTypes = {
    loading: PropTypes.bool.isRequired,
    collaboratorsUtil: PropTypes.object.isRequired,
    parentId: PropTypes.string.isRequired,
    data: PropTypes.array.isRequired,
    onCommunityClicked: PropTypes.func.isRequired,
};

export default withStyles(styles)(withI18N(CommunityListing, messages));
