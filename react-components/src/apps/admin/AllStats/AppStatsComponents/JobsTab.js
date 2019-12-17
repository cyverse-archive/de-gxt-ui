import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import Paper from "@material-ui/core/Paper";
import jobsTableData from "./dataFiles/jobsData";
import loginData from "./dataFiles/loginCount";
import distinctLoginData from "./dataFiles/distinctLoginData";

const useStyles = makeStyles({
    root: {
        width: "100%",
        overflowX: "auto",
    },
    table: {
        minWidth: 50,
    },
});

function createData(jobType, jobCount) {
    return { jobType, jobCount };
}

const duration = "24hrs";
const dataRows = [];

const rows = jobsTableData[0].jobs.map((subData) =>
    createData(
        subData.Category +
            " Jobs " +
            subData.Status +
            " in the past " +
            duration,
        subData.Count
    )
);
rows.push(
    createData(
        "Number of Distinct Logins in the DE in the last " + duration,
        distinctLoginData[0].count
    )
);
rows.push(
    createData(
        "Total Number of Logins in the last " + duration,
        loginData[0].count
    )
);

export default function JobsTable() {
    return (
        <Paper className="jobsTablePaper">
            <Table className="jobsTable" aria-label="simple table">
                <TableHead>
                    <TableRow>
                        <TableCell> Job Type</TableCell>
                        <TableCell align="center">Job Count</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {rows.map((row) => (
                        <TableRow key={row.jobType}>
                            <TableCell>{row.jobType}</TableCell>
                            <TableCell align="center">{row.jobCount}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </Paper>
    );
}
