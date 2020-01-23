/**
 * This function consists of everything under the Users Tab
 */

import React from "react";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableRow,
    Paper,
} from "@material-ui/core";
import usersTableData from "./dataFiles/usersData";
import ids from "./AllStatsIDs";
import { getMessage, withI18N, build } from "@cyverse-de/ui-lib";
import myMessagesFile from "./messages";

const rows = usersTableData.users;

function UsersTab(props) {
    const { baseId } = props;
    return (
        <Paper id={build(baseId, ids.PAPER)}>
            <Table>
                <TableHead id={build(baseId, ids.PAPER, ids.HEADER)}>
                    <TableRow>
                        <TableCell> {getMessage("userName")} </TableCell>
                        <TableCell align="center">
                            {getMessage("count")}
                        </TableCell>
                    </TableRow>
                </TableHead>
                <TableBody id={build(baseId, ids.PAPER, ids.TABLE)}>
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
