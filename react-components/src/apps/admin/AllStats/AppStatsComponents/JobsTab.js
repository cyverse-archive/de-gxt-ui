/**
 * This function consists of everything under the Jobs & Logins tab
 */

import React from "react";
import {
    Table,
    TableBody,
    TableCell,
    TableRow,
    Paper,
    TableHead,
} from "@material-ui/core";
import ids from "./AllStatsIDs";
import { withI18N, getMessage, build } from "@cyverse-de/ui-lib";
import messages from "./messages";

function JobsTab(props) {
    const rows = props.jobs,
        distinctLoginData = props.distinctLoginData,
        baseId = props.baseId;

    return (
        <Paper id={build(baseId, ids.PAPER)}>
            <Table aria-label="simple table">
                <TableHead id={build(baseId, ids.PAPER, ids.HEADER)}>
                    <TableRow>
                        <TableCell> {getMessage("jobType")} </TableCell>
                        <TableCell align="center">
                            {getMessage("jobCount")}
                        </TableCell>
                    </TableRow>
                </TableHead>
                <TableBody id={build(baseId, ids.PAPER, ids.TABLE)}>
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
                        <TableCell>{getMessage("distinctLogins")}</TableCell>
                        <TableCell align="center">
                            {distinctLoginData.count}
                        </TableCell>
                    </TableRow>
                    <TableRow>
                        <TableCell>{getMessage("totalLogins")}</TableCell>
                        <TableCell align="center">
                            {distinctLoginData.distinct}
                        </TableCell>
                    </TableRow>
                </TableBody>
            </Table>
        </Paper>
    );
}

export default withI18N(JobsTab, messages);
