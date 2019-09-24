import React, { Component, useState } from "react";
import PropTypes from "prop-types";

import DeleteBtn from "../../data/search/queryBuilder/DeleteBtn";

import {
    AppName,
    AppStatusIcon,
    EmptyTable,
    EnhancedTableHead,
    getMessage,
    getSorting,
    Rate,
    stableSort,
    withI18N,
} from "@cyverse-de/ui-lib";

import ids from "./ids";
import messages from "../messages";

import Checkbox from "@material-ui/core/Checkbox";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableRow from "@material-ui/core/TableRow";
import { Rating } from "@material-ui/lab";
import VerticalMenuItems from "./VerticalMenuItems";
import IconButton from "@material-ui/core/IconButton";
import MoreVertIcon from "@material-ui/icons/MoreVert";
import Menu from "@material-ui/core/Menu";
import AppFields from "./AppFields";

function AppMenu(props) {
    const {
        app,
        onAppInfoClick,
        onCommentsClick,
        onFavoriteClick,
        baseDebugId,
    } = props;
    const external = app.app_type !== "DE";
    const [anchorEl, setAnchorEl] = useState(null);
    const open = Boolean(anchorEl);
    const handleClose = () => setAnchorEl(null);
    return (
        <div>
            <IconButton
                id={baseDebugId}
                aria-label="More"
                aria-owns={open ? "long-menu" : null}
                aria-haspopup="true"
                onClick={(event) => setAnchorEl(event.currentTarget)}
            >
                <MoreVertIcon />
            </IconButton>
            <Menu
                id={baseDebugId + ".menu"}
                anchorEl={anchorEl}
                open={open}
                onClose={handleClose}
            >
                <VerticalMenuItems
                    isExternal={external}
                    isFavorite={app.is_favorite}
                    handleAppInfoClick={() => onAppInfoClick(app)}
                    handleCommentsClick={() => onCommentsClick(app)}
                    handleFavoriteClick={() => onFavoriteClick(app)}
                    handleMenuClose={handleClose}
                />
                />
            </Menu>
        </div>
    );
}

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
        const { apps, handleAppSelection, resetAppSelection } = this.props;
        if (checked) {
            apps.forEach((app) => handleAppSelection(app.id));
            return;
        }
        resetAppSelection();
    }

    render() {
        const { selected, order, orderBy } = this.state;

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
        } = this.props;

        let columnData = getTableColumns(deletable, enableMenu);

        return (
            <Table stickyHeader={true}>
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
                                return (
                                    <TableRow
                                        role="checkbox"
                                        tabIndex={-1}
                                        hover
                                        selected={selected}
                                        aria-checked={selected}
                                        onClick={() => handleAppSelection(app)}
                                        key={app.id}
                                    >
                                        {selectable && (
                                            <TableCell padding="checkbox">
                                                <Checkbox checked={selected} />
                                            </TableCell>
                                        )}
                                        <TableCell>
                                            <AppStatusIcon
                                                isPublic={app.is_public}
                                                isBeta={app.beta}
                                                isDisabled={app.disabled}
                                            />
                                        </TableCell>
                                        <TableCell>
                                            <AppName
                                                isDisabled={app.disabled}
                                                name={app.name}
                                                onAppNameClicked={() =>
                                                    onAppNameClick(app)
                                                }
                                            />
                                        </TableCell>
                                        <TableCell>
                                            {app.integrator_name}
                                        </TableCell>
                                        <TableCell>
                                            <Rate
                                                value={
                                                    userRating || averageRating
                                                }
                                                readOnly={
                                                    app.app_type !== "DE" ||
                                                    !app.is_public
                                                }
                                                total={totalRating}
                                            />
                                        </TableCell>
                                        <TableCell>{app.system_id}</TableCell>
                                        {deletable && (
                                            <TableCell>
                                                <DeleteBtn
                                                    onClick={() =>
                                                        onRemoveApp(app)
                                                    }
                                                />
                                            </TableCell>
                                        )}
                                        {enableMenu && (
                                            <TableCell>
                                                <AppMenu
                                                    app={app}
                                                    onAppInfoClick={
                                                        onAppInfoClick
                                                    }
                                                    onCommentsClick={
                                                        onCommentsClick
                                                    }
                                                    onFavoriteClick={
                                                        onFavoriteClick
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
                    numSelected={selectedApps ? selectedApps.length : 0}
                    rowCount={apps ? apps.length : 0}
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
            numeric: false,
            enableSorting: true,
            key: AppFields.NAME.key,
        },
        {
            name: "Integrated By",
            numeric: false,
            enableSorting: true,
            key: AppFields.INTEGRATOR.key,
        },
        {
            name: AppFields.RATING.fieldName,
            numeric: false,
            enableSorting: true,
            key: AppFields.RATING.key,
        },
        {
            name: AppFields.SYSTEM.fieldName,
            numeric: false,
            enableSorting: true,
            key: AppFields.SYSTEM.key,
        },
    ];

    if (deletable) {
        tableColumns.push({
            name: "",
            numeric: false,
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
};

export default withI18N(AppGridListing, messages);
