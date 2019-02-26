import React, { Component } from 'react';

import exStyles from "../../style";
import ids from "../../ids";
import intlData from "../../messages";
import withI18N, { getMessage } from "../../../util/I18NWrapper";
import ArgumentType from "../../../../src/apps/ArgumentType";


import DEDialogHeader from "../../../util/dialog/DEDialogHeader";
import EnhancedTableHead from "../../../util/table/EnhancedTableHead";

import Button from '@material-ui/core/Button';
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import TableCell from "@material-ui/core/TableCell";
import TableRow from "@material-ui/core/TableRow";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import { withStyles } from "@material-ui/core/styles";

import { injectIntl } from "react-intl";


function ParameterValue(props) {
    const {
        parameter,
        parameter: {
            info_type,
            param_type,
            param_value: {value},
        },
        classes,
        diskResourceUtil,
        onValueClick,
    } = props;
    let valid_info_type = (!(info_type === "ReferenceGenome")
        && !(info_type === "ReferenceSequence")
        && !(info_type === "ReferenceAnnotation"));
    let displayValue = value ? (value.display ? value.display : value) : "";

    if ((ArgumentType.Input === (param_type)
        || ArgumentType.FileInput === (param_type)
        || ArgumentType.FolderInput === (param_type)
        || ArgumentType.MultiFileSelector === (param_type) ||
        ArgumentType.FileFolderInput === (param_type))
        && valid_info_type) {
        return (
            <span
                className={classes.inputType}
                title={displayValue}
                onClick={() => onValueClick(parameter)}
            >
                {diskResourceUtil.parseNameFromPath(displayValue)}
            </span>);
    } else {
        return (<span className={classes.otherType}>{displayValue}</span>);
    }

}

const columnData = [
    {
        id: ids.NAME,
        name: "Name",
        numeric: false,
        enableSorting: true,
    },
    {
        id: ids.TYPE,
        name: "Type",
        numeric: false,
        enableSorting: true,
    },
    {
        id: ids.VALUE,
        name: "Value",
        numeric: false,
        enableSorting: false,
    },
];

class AnalysisParametersDialog extends Component {
    constructor(props) {
        super(props);
        this.state = {
            order: 'desc',
            orderBy: 'Name',
        }
    }

    render() {
        const {classes, analysisName, parameters, onViewParamDialogClose, onSaveClick, dialogOpen, intl} = this.props;
        const {order, orderBy} = this.state;
        return (
            <Dialog open={dialogOpen}>
                <DEDialogHeader
                    heading={intl.formatMessage({id:"analysisParamTitle",  name: analysisName})}
                    onClose={onViewParamDialogClose}/>
                <DialogContent>
                    <Table>
                        <TableBody>
                            {parameters.map(n => {
                                return (
                                    <TableRow key={n.param_id}>
                                        <TableCell>{n.param_name}</TableCell>
                                        <TableCell>{n.param_type}</TableCell>
                                        <TableCell>
                                            <ParameterValue
                                                parameter={n}
                                                classes={classes}
                                                diskResourceUtil={this.props.diskResourceUtil}
                                                onValueClick={this.props.onValueClick}/>
                                        </TableCell>
                                    </TableRow>
                                );
                            })}
                        </TableBody>
                        <EnhancedTableHead columnData={columnData}
                                           baseId="analysis"
                                           order={order}
                                           orderBy={orderBy}
                        />
                    </Table>
                </DialogContent>
                <DialogActions>
                    <Button
                        onClick={() => onSaveClick(parameters)}
                        variant="contained"
                        color="secondary"
                        style={{textTransform: "none"}}>
                        {getMessage("saveToFile")}
                    </Button>

                    <Button
                        onClick={onViewParamDialogClose}
                        variant="contained"
                        color="primary"
                        style={{textTransform: "none"}}>
                        {getMessage("okBtnText")}
                    </Button>
                </DialogActions>
            </Dialog>
        );
    }
}

export default withStyles(exStyles)(withI18N(injectIntl(AnalysisParametersDialog), intlData));
