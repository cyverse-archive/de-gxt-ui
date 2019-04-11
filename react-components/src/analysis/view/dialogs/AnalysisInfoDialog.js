import React, { Component } from "react";

import ids from "../../ids";
import intlData from "../../messages";
import exStyles from "../../style";
import withI18N, { getMessage } from "../../../util/I18NWrapper";

import DEDialogHeader from "../../../util/dialog/DEDialogHeader";
import EnhancedTableHead from "../../../util/table/EnhancedTableHead";

import Button from "@material-ui/core/Button";
import DialogContent from "@material-ui/core/DialogContent";
import DialogActions from "@material-ui/core/DialogActions";
import Dialog from "@material-ui/core/Dialog";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableRow from "@material-ui/core/TableRow";
import { withStyles } from "@material-ui/core/styles";

import { injectIntl } from "react-intl";

const columnData = [
    {
        id: ids.JOB_ID,
        name: "Job Id",
        numeric: false,
        enableSorting: false,
    },
    {
        id: ids.TYPE,
        name: "Type",
        numeric: false,
        enableSorting: false,
    },
];

class AnalysisInfoDialog extends Component {
    render() {
        const { info, dialogOpen, onInfoDialogClose, intl } = this.props;
        return (
            <Dialog open={dialogOpen}>
                <DEDialogHeader
                    heading={intl.formatMessage({ id: "analysisInfo" })}
                    onClose={onInfoDialogClose}
                />
                <DialogContent>
                    <Table>
                        <TableBody>
                            {info.steps.map((n) => {
                                return (
                                    <TableRow key={n.step_number}>
                                        <TableCell>{n.external_id}</TableCell>
                                        <TableCell>{n.step_type}</TableCell>
                                    </TableRow>
                                );
                            })}
                        </TableBody>
                        <EnhancedTableHead
                            columnData={columnData}
                            baseId="analysis"
                        />
                    </Table>
                </DialogContent>
                <DialogActions>
                    <Button onClick={onInfoDialogClose} color="primary">
                        {getMessage("okBtnText")}
                    </Button>
                </DialogActions>
            </Dialog>
        );
    }
}

export default withStyles(exStyles)(
    withI18N(injectIntl(AnalysisInfoDialog), intlData)
);
