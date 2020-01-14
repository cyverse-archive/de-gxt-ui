import React from "react";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import Paper from "@material-ui/core/Paper";
import jobsTableData from "./dataFiles/jobsData";
import distinctLoginData from "./dataFiles/distinctLoginData";
import ids from "./AllStatsIDs";
import { withI18N, getMessage, formatMessage } from "@cyverse-de/ui-lib";
import messages from "./messages";
import { injectIntl } from "react-intl";
import build from "@cyverse-de/ui-lib/src/util/DebugIDUtil";

function JobsTab(props) {
    const rows = jobsTableData.jobs;
    return (
        <Paper
            className="jobsTablePaper"
            id={build(
                ids.MAIN_PAGE,
                ids.NAV_TAB,
                ids.JOBS_TAB,
                ids.TABLE,
                ids.PAPER
            )}
        >
            <Table className="jobsTable" aria-label="simple table">
                <TableHead>
                    <TableRow
                        id={build(
                            ids.MAIN_PAGE,
                            ids.NAV_TAB,
                            ids.JOBS_TAB,
                            ids.PAPER,
                            ids.HEADER
                        )}
                    >
                        <TableCell> {getMessage("jobType")} </TableCell>
                        <TableCell align="center">
                            {getMessage("jobCount")}
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
                            <TableCell>
                                {getMessage("jobsData", {
                                    values: {
                                        category: row.Category,
                                        jobStatus: row.Status,
                                    },
                                })}
                            </TableCell>
                            <TableCell align="center">{row.Count}</TableCell>
                        </TableRow>
                    ))}
                    <TableRow>
                        <TableCell>
                            {formatMessage(props.intl, "distinctLogins")}
                        </TableCell>
                        <TableCell align="center">
                            {distinctLoginData.count}
                        </TableCell>
                    </TableRow>
                    <TableRow>
                        <TableCell>
                            {formatMessage(props.intl, "totalLogins")}
                        </TableCell>
                        <TableCell align="center">
                            {distinctLoginData.distinct}
                        </TableCell>
                    </TableRow>
                </TableBody>
            </Table>
        </Paper>
    );
}

export default withI18N(injectIntl(JobsTab), messages);
