/**
 *
 * @author Sriram
 *
 **/
import React, { Component } from "react";
import { withStyles } from "@material-ui/core/styles";
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


const columnData = [
    {name: "Status", numeric: false, enableSorting: false},
    {name: "Date", numeric: false, enableSorting: true},
    {name: "Comment", numeric: false, enableSorting: false},
];


class RequestHistoryDialog extends Component {
    constructor(props) {
        super(props);
        this.state = {
            data: [],
            loading: true,
            order: 'DESC',
            orderBy: 'Date',

        };
    }

    render() {
        const {data} = this.props;
        return (
            <Dialog
                open={this.props.open}
                onClose={this.props.handleRequestHistoryClose}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description"
            >
                <DialogTitle style={{backgroundColor: Color.blue}}
                             id="alert-dialog-title">
                    <Typography
                        style={{color: Color.white}}> {getMessage("denyRequestHeader")}</Typography>
                </DialogTitle>
                <DialogContent>
                    <Table>
                        <EnhancedTableHead
                            columnData={columnData}
                            selectable={false}
                            order={this.state.order}
                            orderBy={this.state.orderBy}
                        />
                        <TableBody>
                            {data.map(n => {
                                return (
                                    <TableRow>
                                        <TableCell>{n.status}</TableCell>
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
                    <Button onClick={this.handleClose} color="primary">
                        {getMessage("okBtnText")}
                    </Button>
                </DialogActions>
            </Dialog>
        );
    }
}
export default (withI18N(RequestHistoryDialog, intlData));
