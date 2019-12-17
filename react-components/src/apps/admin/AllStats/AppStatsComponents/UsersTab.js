import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import Paper from "@material-ui/core/Paper";
import usersTableData from "./dataFiles/usersData";

const useStyles = makeStyles({
    root: {
        width: "100%",
        overflowX: "auto",
    },
    table: {
        minWidth: 50,
    },
});

function createData(userName, userCount) {
    return { userName, userCount };
}

const duration = "24hrs";

const rows = usersTableData[0].users.map((data) =>
    createData(data.username, data.count)
);

export default function UsersTab() {
    return (
        <Paper className="usersTablePaper">
            <Table className="usersTable" aria-label="simple table">
                <TableHead>
                    <TableRow>
                        <TableCell> User Name </TableCell>
                        <TableCell align="center">Count</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {rows.map((row) => (
                        <TableRow key={row.userName}>
                            <TableCell>{row.userName}</TableCell>
                            <TableCell align="center">
                                {row.userCount}
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </Paper>
    );
}
