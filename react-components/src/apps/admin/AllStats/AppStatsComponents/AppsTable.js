/**
 * This function is imported in AppsTab.js
 * It contains the components to create the AppsTable
 */

import React from "react";
import {
    Paper,
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableRow,
} from "@material-ui/core";
import appsTableData from "./dataFiles/appsData";
import ids from "./AllStatsIDs";
import { getMessage, withI18N, build } from "@cyverse-de/ui-lib";
import myMessagesFile from "./messages";

const rows = appsTableData.apps;

function AppsTable(props) {
    const { baseId } = props;
    return (
        <Paper id={build(baseId, ids.PAPER)}>
            <Table>
                <TableHead id={build(baseId, ids.PAPER, ids.HEADER)}>
                    <TableRow>
                        <TableCell> {getMessage("appName")} </TableCell>
                        <TableCell> {getMessage("appID")} </TableCell>
                        <TableCell align="center">
                            {getMessage("appCount")}
                        </TableCell>
                    </TableRow>
                </TableHead>
                <TableBody id={build(baseId, ids.PAPER, ids.TABLE_BODY)}>
                    {rows.map((row) => (
                        <TableRow>
                            <TableCell>{row.appName}</TableCell>
                            <TableCell>{row.appID}</TableCell>
                            <TableCell align="center">{row.appCount}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </Paper>
    );
}

export default withI18N(AppsTable, myMessagesFile);
