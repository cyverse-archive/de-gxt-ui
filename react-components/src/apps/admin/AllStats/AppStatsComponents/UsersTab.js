import React from "react";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import Paper from "@material-ui/core/Paper";
import usersTableData from "./dataFiles/usersData";
import ids from "./AllStatsIDs";
import { getMessage, withI18N } from "@cyverse-de/ui-lib";
import myMessagesFile from "./messages";
import build from "@cyverse-de/ui-lib/src/util/DebugIDUtil";

const rows = usersTableData.users;

function UsersTab() {
    return (
        <Paper
            className="usersTablePaper"
            id={build(
                ids.MAIN_PAGE,
                ids.NAV_TAB,
                ids.USERS_TAB,
                ids.TABLE,
                ids.PAPER
            )}
        >
            <Table className="usersTable" aria-label="simple table">
                <TableHead
                    id={build(
                        ids.MAIN_PAGE,
                        ids.NAV_TAB,
                        ids.USERS_TAB,
                        ids.PAPER,
                        ids.HEADER
                    )}
                >
                    <TableRow>
                        <TableCell> {getMessage("userName")} </TableCell>
                        <TableCell align="center">
                            {getMessage("count")}
                        </TableCell>
                    </TableRow>
                </TableHead>
                <TableBody
                    id={build(
                        ids.MAIN_PAGE,
                        ids.NAV_TAB,
                        ids.APPS_TAB,
                        ids.PAPER,
                        ids.TABLE
                    )}
                >
                    {rows.map((row) => (
                        <TableRow>
                            <TableCell>{row.username}</TableCell>
                            <TableCell align="center">{row.count}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </Paper>
    );
}

export default withI18N(UsersTab, myMessagesFile);
