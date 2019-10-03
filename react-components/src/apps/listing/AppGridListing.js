import React, { Component } from "react";
import PropTypes from "prop-types";

import DeleteBtn from "../../data/search/queryBuilder/DeleteBtn";

import {
    AppMenu,
    AppName,
    AppStatusIcon,
    EmptyTable,
    EnhancedTableHead,
    getMessage,
    Rate,
    stableSort,
    withI18N,
    build,
    Highlighter,
} from "@cyverse-de/ui-lib";

import ids from "./ids";
import messages from "../messages";

import Checkbox from "@material-ui/core/Checkbox";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableRow from "@material-ui/core/TableRow";
import AppFields from "./AppFields";
import appType from "../../appType";

/**
 * @author aramsey
 *
 * A component that will show a listing of type App
 */
class AppGridListing extends Component {
    constructor(props) {
        super(props);

        this.state = {
            order: "asc",
            orderBy: "Name",
        };

        ["handleSelectAllClick", "onRequestSort"].forEach(
            (fn) => (this[fn] = this[fn].bind(this))
        );
    }

    onRequestSort(event, property) {
        const orderBy = property;
        let order = "desc";

        if (this.state.orderBy === property && this.state.order === "desc") {
            order = "asc";
        }
        this.setState({ order, orderBy });
    }

    handleSelectAllClick(event, checked) {
        this.props.handleSelectAll(checked);
    }

    render() {
        const { order, orderBy } = this.state;

        const {
            parentId,
            apps,
            selectable,
            deletable,
            onRemoveApp,
            selectedApps,
            handleAppSelection,
            isSelected,
            onAppNameClick,
            onAppInfoClick,
            onCommentsClick,
            onFavoriteClick,
            enableMenu,
            getAppsSorting,
            onRatingDeleteClick,
            onRatingClick,
            searchRegexPattern,
        } = this.props;

        let columnData = getTableColumns(deletable, enableMenu);

        return (
            <Table stickyHeader={true} size="small">
                <TableBody>
                    {(!apps || apps.length === 0) && (
                        <EmptyTable
                            message={getMessage("noApps")}
                            numColumns={columnData.length}
                        />
                    )}
                    {apps &&
                        apps.length > 0 &&
                        stableSort(apps, getAppsSorting(order, orderBy)).map(
                            (app) => {
                                const {
                                    average: averageRating,
                                    user: userRating,
                                    total: totalRating,
                                } = app.rating;
                                const selected = isSelected(app.id);
                                const external = app.app_type !== appType.de;
                                const rowId = build(parentId, app.id);
                                return (
                                    <TableRow
                                        role="checkbox"
                                        tabIndex={-1}
                                        hover
                                        selected={selected}
                                        aria-checked={selected}
                                        onClick={() => handleAppSelection(app)}
                                        key={app.id}
                                        id={rowId}
                                    >
                                        {selectable && (
                                            <TableCell padding="checkbox">
                                                <Checkbox checked={selected} />
                                            </TableCell>
                                        )}
                                        <TableCell padding="none">
                                            <AppStatusIcon
                                                isPublic={app.is_public}
                                                isBeta={app.beta}
                                                isDisabled={app.disabled}
                                            />
                                        </TableCell>
                                        <TableCell>
                                            <AppName
                                                baseDebugId={build(
                                                    rowId,
                                                    ids.LISTING.APP_NAME
                                                )}
                                                isDisabled={app.disabled}
                                                name={app.name}
                                                onAppNameClicked={
                                                    onAppNameClick
                                                        ? () =>
                                                              onAppNameClick(
                                                                  app
                                                              )
                                                        : undefined
                                                }
                                                searchRegexPattern={
                                                    searchRegexPattern
                                                }
                                            />
                                        </TableCell>
                                        <TableCell>
                                            <Highlighter
                                                search={searchRegexPattern}
                                            >
                                                {app.integrator_name}
                                            </Highlighter>
                                        </TableCell>
                                        <TableCell>
                                            <Rate
                                                name={app.id}
                                                value={
                                                    userRating || averageRating
                                                }
                                                readOnly={
                                                    external ||
                                                    !app.is_public ||
                                                    !onRatingClick
                                                }
                                                total={totalRating}
                                                onChange={(event, score) => {
                                                    onRatingClick(
                                                        event,
                                                        app,
                                                        score
                                                    );
                                                }}
                                                onDelete={
                                                    userRating
                                                        ? () =>
                                                              onRatingDeleteClick(
                                                                  app
                                                              )
                                                        : undefined
                                                }
                                            />
                                        </TableCell>
                                        <TableCell>{app.system_id}</TableCell>
                                        {deletable && (
                                            <TableCell align="right">
                                                <DeleteBtn
                                                    onClick={() =>
                                                        onRemoveApp(app)
                                                    }
                                                />
                                            </TableCell>
                                        )}
                                        {enableMenu && (
                                            <TableCell
                                                padding="none"
                                                align="right"
                                            >
                                                <AppMenu
                                                    baseDebugId={rowId}
                                                    onAppInfoClick={() =>
                                                        onAppInfoClick(app)
                                                    }
                                                    onCommentsClick={() =>
                                                        onCommentsClick(app)
                                                    }
                                                    onFavoriteClick={() =>
                                                        onFavoriteClick(app)
                                                    }
                                                    isExternal={external}
                                                    isFavorite={app.is_favorite}
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
                    numSelected={selectedApps ? selectedApps.length : 0}
                    rowsInPage={apps ? apps.length : 0}
                    order={order}
                    orderBy={orderBy}
                    baseId={parentId}
                    ids={ids.FIELD}
                    columnData={columnData}
                    onRequestSort={this.onRequestSort}
                    onSelectAllClick={this.handleSelectAllClick}
                />
            </Table>
        );
    }
}

function getTableColumns(deletable, enableMenu) {
    let tableColumns = [
        { name: "", numeric: false, enableSorting: false, key: "status" },
        {
            name: AppFields.NAME.fieldName,
            enableSorting: true,
            key: AppFields.NAME.key,
        },
        {
            name: AppFields.INTEGRATOR.fieldName,
            enableSorting: true,
            key: AppFields.INTEGRATOR.key,
        },
        {
            name: AppFields.RATING.fieldName,
            enableSorting: true,
            key: AppFields.RATING.key,
        },
        {
            name: AppFields.SYSTEM.fieldName,
            enableSorting: true,
            key: AppFields.SYSTEM.key,
        },
    ];

    if (deletable) {
        tableColumns.push({
            name: "",
            enableSorting: false,
            key: "remove",
        });
    }

    if (enableMenu) {
        tableColumns.push({
            name: "",
            enableSorting: false,
            key: "menu",
        });
    }

    return tableColumns;
}

AppGridListing.defaultProps = {
    deletable: false,
    selectable: true,
    enableMenu: false,
};

AppGridListing.propTypes = {
    parentId: PropTypes.string.isRequired,
    apps: PropTypes.array.isRequired,
    selectable: PropTypes.bool,
    deletable: PropTypes.bool,
    onRemoveApp: PropTypes.func,
    selectedApps: PropTypes.array.isRequired,
    handleAppSelection: PropTypes.func.isRequired,
    resetAppSelection: PropTypes.func.isRequired,
    enableMenu: PropTypes.bool,
    onAppNameClick: PropTypes.func,
    onAppInfoClick: PropTypes.func,
    onCommentsClick: PropTypes.func,
    onFavoriteClick: PropTypes.func,
    getAppsSorting: PropTypes.func,
    onRatingDeleteClick: PropTypes.func,
    onRatingClick: PropTypes.func,
    searchRegexPattern: PropTypes.func,
};

export default withI18N(AppGridListing, messages);
