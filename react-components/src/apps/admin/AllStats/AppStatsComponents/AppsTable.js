import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import Paper from "@material-ui/core/Paper";
import appsTableData from "./dataFiles/appsData";

const useStyles = makeStyles({
    root: {
        width: "100%",
        overflowX: "auto",
    },
    table: {
        minWidth: 50,
    },
});

function createData(appName, appID, appCount) {
    return { appName, appID, appCount };
}

const duration = "24hrs";
const rows = appsTableData[0].apps.map((data) =>
    createData(data.appName, data.appID, data.appCount)
);

export default function AppsTab() {
    return (
        <Paper className="appsTablePaper">
            <Table className="appsTable" aria-label="simple table">
                <TableHead>
                    <TableRow>
                        <TableCell> App Name </TableCell>
                        <TableCell> App ID </TableCell>
                        <TableCell align="center">App Count</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {rows.map((row) => (
                        <TableRow key={row.appName}>
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
