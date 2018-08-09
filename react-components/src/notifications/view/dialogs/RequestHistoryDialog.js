/**
 *
 * @author Sriram
 *
 **/
import React, { Component } from "react";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableRow from "@material-ui/core/TableRow";
import moment from "moment";
import constants from "../../../constants";
import withI18N, { getMessage } from "../../../util/I18NWrapper";
import EnhancedTableHead from "../../../util/table/EnhancedTableHead";
import Color from "../../../util/CyVersePalette";
import intlData from "../../messages";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogTitle from "@material-ui/core/DialogTitle";
import Typography from "@material-ui/core/Typography";
import Button from "@material-ui/core/Button";
import toolStatusHelpMapping from "../../model/toolStatusHelpMapping";
import permIdStatusHelpMapping from "../../model/permIdStatusHelpMapping";
import notificationCategory from "../../model/notificationCategory";
import build from "../../../util/DebugIDUtil";
import ids from "../../ids";

const columnData = [
    {name: "Status", numeric: false, enableSorting: false},
    {name: "Date", numeric: false, enableSorting: true},
    {name: "Comment", numeric: false, enableSorting: false},
];


class RequestHistoryDialog extends Component {
    constructor(props) {
        super(props);
        this.state = {
            order: 'desc',
            orderBy: 'Date',
            dialogOpen: props.dialogOpen,
        };
    }

    render() {
        const {history, category, name} = this.props;
        const helpMap = category === notificationCategory.tool_request ? toolStatusHelpMapping : permIdStatusHelpMapping;
        const baseId = ids.REQUEST_HISTORY_DLG;
        return (
            <Dialog id={baseId}
                    open={this.state.dialogOpen}>
                <DialogTitle style={{backgroundColor: Color.blue}}>
                    <Typography
                        style={{color: Color.white}}> {name}</Typography>
                </DialogTitle>
                <DialogContent>
                    <Table>
                        <EnhancedTableHead
                            columnData={columnData}
                            selectable={false}
                            order={this.state.order}
                            orderBy={this.state.orderBy}
                            ids={ids}
                            baseId={baseId}
                        />
                        <TableBody>
                            {history.map(n => {
                                return (
                                    <TableRow key={n.status}>
                                        <TableCell><span
                                            title={intlData.messages[helpMap[n.status]]}>{n.status}</span></TableCell>
                                        <TableCell>{(n.status_date) ? moment(n.status_date, "x").format(
                                                constants.DATE_FORMAT) :
                                            getMessage("emptyValue")} </TableCell>
                                        <TableCell>{n.comments}</TableCell>
                                    </TableRow>
                                );
                            })}
                        </TableBody>
                    </Table>
                </DialogContent>
                <DialogActions>
                    <Button
                        id={build(baseId, ids.OK_BTN)}
                        onClick={() => {
                            this.setState({dialogOpen: false})
                        }}
                        color="primary">
                        {getMessage("okBtnText")}
                    </Button>
                </DialogActions>
            </Dialog>
        );
    }
}
export default (withI18N(RequestHistoryDialog, intlData));
