/**
 * A dialog to show the history of tool request or perm id request.
 *
 * @author Sriram
 *
 **/
import React, { Component } from "react";

import ids from "../../ids";
import {
    build,
    dateConstants,
    DETableRow,
    EnhancedTableHead,
    formatDate,
    getMessage,
    palette,
    withI18N,
} from "@cyverse-de/ui-lib";

import intlData from "../../messages";
import toolStatusHelpMapping from "../../model/toolStatusHelpMapping";
import permIdStatusHelpMapping from "../../model/permIdStatusHelpMapping";
import notificationCategory from "../../model/notificationCategory";

import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    Typography,
    Table,
    TableBody,
    TableCell,
} from "@material-ui/core";

const columnData = [
    {
        id: ids.STATUS,
        name: "Status",
        numeric: false,
        enableSorting: false,
    },
    {
        id: ids.CREATED_DATE,
        name: "Date",
        numeric: false,
        enableSorting: true,
    },
    {
        id: ids.COMMENT,
        name: "Comment",
        numeric: false,
        enableSorting: false,
    },
];

class RequestHistoryDialog extends Component {
    constructor(props) {
        super(props);
        this.state = {
            order: "desc",
            orderBy: "Date",
            dialogOpen: props.dialogOpen,
        };
    }

    render() {
        const { history, category, name } = this.props;
        const helpMap =
            category === notificationCategory.tool_request
                ? toolStatusHelpMapping
                : permIdStatusHelpMapping;
        const baseId = ids.REQUEST_HISTORY_DLG;
        return (
            <Dialog id={baseId} open={this.state.dialogOpen}>
                <DialogTitle style={{ backgroundColor: palette.blue }}>
                    <Typography style={{ color: palette.white }}>
                        {" "}
                        {name}
                    </Typography>
                </DialogTitle>
                <DialogContent>
                    <Table>
                        <TableBody>
                            {history.map((n) => {
                                return (
                                    <DETableRow key={n.status}>
                                        <TableCell>
                                            <span
                                                title={
                                                    intlData.messages[
                                                        helpMap[n.status]
                                                    ]
                                                }
                                            >
                                                {n.status}
                                            </span>
                                        </TableCell>
                                        <TableCell>
                                            {formatDate(
                                                n.status_date,
                                                dateConstants.DATE_FORMAT
                                            )}
                                        </TableCell>
                                        <TableCell>{n.comments}</TableCell>
                                    </DETableRow>
                                );
                            })}
                        </TableBody>
                        <EnhancedTableHead
                            columnData={columnData}
                            selectable={false}
                            order={this.state.order}
                            orderBy={this.state.orderBy}
                            baseId={baseId}
                        />
                    </Table>
                </DialogContent>
                <DialogActions>
                    <Button
                        id={build(baseId, ids.OK_BTN)}
                        onClick={() => {
                            this.setState({ dialogOpen: false });
                        }}
                        color="primary"
                    >
                        {getMessage("okBtnText")}
                    </Button>
                </DialogActions>
            </Dialog>
        );
    }
}
export default withI18N(RequestHistoryDialog, intlData);
