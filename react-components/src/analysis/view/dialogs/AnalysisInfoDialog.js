import React, { Component } from 'react';
import Button from '@material-ui/core/Button';
import DialogContent from '@material-ui/core/DialogContent';
import DialogActions from '@material-ui/core/DialogActions';
import Dialog from '@material-ui/core/Dialog';
import withI18N, { getMessage } from "../../../util/I18NWrapper";
import Table from "@material-ui/core/Table";
import EnhancedTableHead from "../../../util/table/EnhancedTableHead";
import ids from "../../ids";
import TableRow from "@material-ui/core/TableRow";
import intlData from "../../messages"
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import { withStyles } from "@material-ui/core/styles";
import exStyles from "../../style";
import DEDialogHeader from "../../../util/dialog/DEDialogHeader";


const columnData = [
    {name: "Job Id", numeric: false, enableSorting: false},
    {name: "Type", numeric: false, enableSorting: false},
];

class AnalysisInfoDialog extends Component {
    constructor(props) {
        super(props);
        this.state = {
            dialogOpen: props.dialogOpen,
        }
    }

    render() {
        const {info, classes} = this.props;
        return (
            <Dialog open={this.state.dialogOpen}>
                <DEDialogHeader
                    heading={getMessage("analysisInfo")}
                    onClose={() => {
                        this.setState({dialogOpen: false})
                    }}/>
                <DialogContent>
                    <Table>
                        <EnhancedTableHead
                            columnData={columnData}
                            ids={ids}
                            baseId="analysis"
                        />
                        <TableBody>
                            {info.steps.map(n => {
                                return (
                                    <TableRow key={n.step_number}>
                                        <TableCell>{n.external_id}</TableCell>
                                        <TableCell>{n.step_type}</TableCell>
                                    </TableRow>
                                );
                            })}
                        </TableBody>
                    </Table>
                </DialogContent>
                <DialogActions>
                    <Button
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

export default withStyles(exStyles)(withI18N(AnalysisInfoDialog, intlData));