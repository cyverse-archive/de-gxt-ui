import React from "react";
import { makeStyles, withStyles } from "@material-ui/core/styles";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import Paper from "@material-ui/core/Paper";
import appsTableData from "./dataFiles/appsData";
import ids from "./AllStatsIDs";
import { getMessage, withI18N } from "@cyverse-de/ui-lib";
import styles from "../AllStatsStyle";
import myMessagesFile from "./messages";
import build from "@cyverse-de/ui-lib/src/util/DebugIDUtil";

function createData(appName, appID, appCount) {
    return { appName, appID, appCount };
}

const duration = "24hrs";
const rows = appsTableData.apps;

function AppsTab() {
    return (
        <Paper
            className="appsTablePaper"
            id={build(
                ids.MAIN_PAGE,
                ids.NAV_TAB,
                ids.APPS_TAB,
                ids.TABLE,
                ids.PAPER
            )}
        >
            <Table className="appsTable" aria-label="simple table">
                <TableHead
                    id={build(
                        ids.MAIN_PAGE,
                        ids.NAV_TAB,
                        ids.APPS_TAB,
                        ids.PAPER,
                        ids.HEADER
                    )}
                >
                    <TableRow>
                        <TableCell> {getMessage("appName")} </TableCell>
                        <TableCell> {getMessage("appID")} </TableCell>
                        <TableCell align="center">
                            {" "}
                            {getMessage("appCount")}{" "}
                        </TableCell>
                    </TableRow>
                </TableHead>
                <TableBody
                    id={build(
                        ids.MAIN_PAGE,
                        ids.NAV_TAB,
                        ids.JOBS_TAB,
                        ids.PAPER,
                        ids.TABLE
                    )}
                >
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

export default withI18N(AppsTab, myMessagesFile);
