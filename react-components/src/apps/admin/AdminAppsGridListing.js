import React from "react";
import getAppsSearchRegex from "../appSearchRegex";
import {
    AppName,
    AppStatusIcon,
    build,
    DETableRow,
    EmptyTable,
    EnhancedTableHead,
    getMessage,
    Highlighter,
    LoadingMask,
    palette,
} from "@cyverse-de/ui-lib";

import { makeStyles, Table, TableBody, TableCell } from "@material-ui/core";
import InfoIcon from "@material-ui/icons/InfoOutlined";
import AppFields from "../AppFields";
import ids from "../listing/ids";

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

export default function AdminAppGridListing(props) {
    const { apps, searchText, parentId, loading } = props;
    const searchRegex = getAppsSearchRegex(searchText);
    const classes = useStyles();
    const onRequestSort = () => {
        console.log("Sorting...");
    };
    return (
        <div className={classes.container}>
            <LoadingMask loading={loading}>
                <Table stickyHeader={true} size="small">
                    <TableBody>
                        {(!apps || apps.length === 0) && (
                            <EmptyTable
                                message={getMessage("noApps")}
                                numColumns={tableColumns.length}
                            />
                        )}
                        {apps &&
                            apps.length > 0 &&
                            apps.map((app) => {
                                const rowId = build(parentId, app.id);
                                return (
                                    <DETableRow
                                        tabIndex={-1}
                                        hover
                                        key={app.id}
                                        id={rowId}
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
                                            <InfoIcon
                                                className={
                                                    classes.toolbarItemColor
                                                }
                                            />
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
                        order="asc"
                        orderBy="name"
                        baseId={parentId}
                        ids={ids.FIELD}
                        columnData={tableColumns}
                        onRequestSort={onRequestSort}
                    />
                </Table>
            </LoadingMask>
        </div>
    );
}
