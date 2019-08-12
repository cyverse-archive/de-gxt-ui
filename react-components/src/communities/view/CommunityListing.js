import React, { Component, Fragment } from "react";
import PropTypes from "prop-types";

import messages from "../messages";
import styles from "../styles";
import ids from "../ids";

import {
    DEHyperlink,
    EmptyTable,
    EnhancedTableHead,
    getMessage,
    getSorting,
    LoadingMask,
    stableSort,
    withI18N,
} from "@cyverse-de/ui-lib";

import {
    Table,
    TableBody,
    TableCell,
    TableRow,
    Tooltip,
    withStyles,
} from "@material-ui/core";

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
                <LoadingMask loading={loading}>
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
                                                        <DEHyperlink
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
                </LoadingMask>
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
