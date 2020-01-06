import React, { useState } from "react";
import getAppsSearchRegex from "../appSearchRegex";
import {
    AppName,
    AppStatusIcon,
    build,
    DETableRow,
    EmptyTable,
    EnhancedTableHead,
    getMessage,
    getSorting,
    Highlighter,
    LoadingMask,
    palette,
    stableSort,
    withI18N,
} from "@cyverse-de/ui-lib";

import {
    IconButton,
    makeStyles,
    Table,
    TableBody,
    TableCell,
    Typography,
} from "@material-ui/core";
import InfoIcon from "@material-ui/icons/InfoOutlined";
import AppFields from "../AppFields";
import ids from "../listing/ids";
import { injectIntl } from "react-intl";
import messages from "../messages";

const useStyles = makeStyles((theme) => ({
    toolbarItemColor: {
        color: palette.darkBlue,
    },
    listingFont: {
        fontSize: 12,
    },
    container: {
        width: "100%",
        height: "100%",
        marginTop: 0,
        overflow: "auto",
        backgroundColor: palette.white,
    },
    heading: {
        backgroundColor: palette.lightGray,
        width: "100%",
        height: 30,
    },
}));
const tableColumns = [
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
        name: "",
        enableSorting: false,
        key: "AppInfo",
    },
    {
        name: "",
        enableSorting: false,
        key: "Status",
    },
];

function AdminAppGridListing(props) {
    const {
        baseId,
        apps,
        heading,
        presenter,
        searchText,
        loading,
        intl,
    } = props;
    const [selectedApps, setSelectedApps] = useState([]);
    const [sortConfig, setSortConfig] = useState({
        order: "asc",
        orderBy: "name",
    });
    const searchRegex = getAppsSearchRegex(searchText);
    const classes = useStyles();
    const onRequestSort = (event, property) => {
        const orderBy = property;
        let order = "desc";

        if (sortConfig.orderBy === property && sortConfig.order === "desc") {
            order = "asc";
        }

        setSortConfig({
            order: order,
            orderBy: orderBy,
        });
    };
    const onInfoClick = (app) => {
        presenter.onAppInfoSelected(app, app.id, app.system_id, app.is_public);
    };

    const handleAppSelection = (app) => {
        const selectedIndex = selectedApps.indexOf(app);
        let newSelected = [];

        if (selectedIndex === -1) {
            newSelected = newSelected.concat(selectedApps, app);
        } else if (selectedIndex === 0) {
            newSelected = newSelected.concat(selectedApps.slice(1));
        } else if (selectedIndex === selectedApps.length - 1) {
            newSelected = newSelected.concat(selectedApps.slice(0, -1));
        } else if (selectedIndex > 0) {
            newSelected = newSelected.concat(
                selectedApps.slice(0, selectedIndex),
                selectedApps.slice(selectedIndex + 1)
            );
        }
        setSelectedApps(newSelected);
        presenter.onAppSelectionChanged(newSelected);
    };

    const isSelected = (id) => {
        if (selectedApps && selectedApps.length > 0) {
            return selectedApps.filter((app) => app.id === id).length > 0;
        }
        return false;
    };

    return (
        <div className={classes.container}>
            <div className={classes.heading}>
                <Typography variant="subtitle1">{heading}</Typography>
            </div>
            <LoadingMask loading={loading}>
                <Table stickyHeader={true} size="small" id={baseId}>
                    <TableBody>
                        {(!apps || apps.length === 0) && (
                            <EmptyTable
                                message={getMessage("noApps")}
                                numColumns={tableColumns.length}
                            />
                        )}
                        {apps &&
                            apps.length > 0 &&
                            stableSort(
                                apps,
                                getSorting(sortConfig.order, sortConfig.orderBy)
                            ).map((app) => {
                                const rowId = build(baseId, app.id);
                                const selected = isSelected(app.id);
                                return (
                                    <DETableRow
                                        tabIndex={-1}
                                        hover
                                        selected={selected}
                                        key={app.id}
                                        id={rowId}
                                        onClick={() => handleAppSelection(app)}
                                    >
                                        <TableCell>
                                            <AppName
                                                baseDebugId={build(
                                                    rowId,
                                                    ids.LISTING.APP_NAME
                                                )}
                                                isDisabled={app.disabled}
                                                name={app.name}
                                                searchText={searchRegex}
                                            />
                                        </TableCell>
                                        <TableCell>
                                            <span
                                                className={classes.listingFont}
                                            >
                                                <Highlighter
                                                    search={searchRegex}
                                                >
                                                    {app.integrator_name}
                                                </Highlighter>
                                            </span>
                                        </TableCell>
                                        <TableCell>
                                            <IconButton
                                                onClick={() => onInfoClick(app)}
                                                className={
                                                    classes.toolbarItemColor
                                                }
                                            >
                                                <InfoIcon />
                                            </IconButton>
                                        </TableCell>
                                        <TableCell padding="none">
                                            <AppStatusIcon
                                                isPublic={app.is_public}
                                                isBeta={app.beta}
                                                isDisabled={app.disabled}
                                            />
                                        </TableCell>
                                    </DETableRow>
                                );
                            })}
                    </TableBody>
                    <EnhancedTableHead
                        selectable={false}
                        rowsInPage={apps ? apps.length : 0}
                        order={sortConfig.order}
                        orderBy={sortConfig.orderBy}
                        baseId={baseId}
                        ids={ids.FIELD}
                        columnData={tableColumns}
                        onRequestSort={onRequestSort}
                    />
                </Table>
            </LoadingMask>
        </div>
    );
}

export default withI18N(injectIntl(AdminAppGridListing), messages);
