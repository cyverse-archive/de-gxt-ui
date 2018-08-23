import React, { Component } from 'react';
import Dialog from "@material-ui/core/Dialog";
import withI18N, { getMessage } from "../../../util/I18NWrapper";
import DialogContent from "@material-ui/core/DialogContent";
import EnhancedTableHead from "../../../util/table/EnhancedTableHead";
import ids from "../../ids";
import TableBody from "@material-ui/core/TableBody";
import DialogActions from "@material-ui/core/DialogActions";
import TableCell from "@material-ui/core/TableCell";
import TableRow from "@material-ui/core/TableRow";
import Table from "@material-ui/core/Table";
import Button from '@material-ui/core/Button';
import { withStyles } from "@material-ui/core/styles";
import exStyles from "../../style";
import intlData from "../../messages";
import DEDialogHeader from "../../../util/dialog/DEDialogHeader";

const ArgumentType = {
    Input: "Input",
    FileInput: "FileInput",
    FolderInput: "FolderInput",
    MultiFileSelector: "MultiFileSelector",
    FileFolderInput: "FileFolderInput",
    EnvironmentVariable: "EnvironmentVariable",
    Flag: "Flag",
    Info: "Info",
    MultiLineText: "MultiLineText",
    Integer: "Integer",
    Double: "Double",
    Text: "Text",
    TextSelection: "TextSelection", // For selecting from a list of string values.
    IntegerSelection: "IntegerSelection", // For selecting from a list of integers
    DoubleSelection: "DoubleSelection", // For selecting from a list of doubles
    TreeSelection: "TreeSelection",
    Output: "Output",
    FileOutput: "FileOutput",
    FolderOutput: "FolderOutput",
    MultiFileOutput: "MultiFileOutput",
    ReferenceGenome: "ReferenceGenome",
    ReferenceSequence: "ReferenceSequence",
    ReferenceAnnotation: "ReferenceAnnotation",
    Selection: "Selection",
    ValueSelection: "ValueSelection",
    Number: "Number",
    Group: "Group",
};

function ParameterValue(props) {
    const {parameter} = props;
    let info_type = parameter.info_type;
    let valid_info_type = (!(info_type === "ReferenceGenome")
        && !(info_type === "ReferenceSequence")
        && !(info_type === "ReferenceAnnotation"));
    let displayValue = parameter.param_value.value.display ? parameter.param_value.value.display : parameter.param_value.value;

    if (!displayValue) {
        displayValue = "";
    }
    let type = parameter.param_type;

    if ((ArgumentType.Input === (type)
        || ArgumentType.FileInput === (type)
        || ArgumentType.FolderInput === (type)
        || ArgumentType.MultiFileSelector === (type) ||
        ArgumentType.FileFolderInput === (type))
        && valid_info_type) {
        return (<span> {displayValue} </span>);
    } else {
        return (<span>{displayValue}</span>);
    }

}

const columnData = [
    {name: "Name", numeric: false, enableSorting: true},
    {name: "Type", numeric: false, enableSorting: true},
    {name: "Value", numeric: false, enableSorting: false},
];

class AnalysisParametersDialog extends Component {
    constructor(props) {
        super(props);
        this.state = {
            dialogOpen: props.dialogOpen,
            order: 'desc',
            orderBy: 'Name',
        }
    }

    render() {
        const {classes, analysisName, parameters} = this.props;
        const {order, orderBy} = this.state;
        return (
            <Dialog open={this.state.dialogOpen}>
                <DEDialogHeader
                    heading={getMessage("analysisParamTitle", {values: {name: analysisName}})}
                    onClose={() => {
                        this.setState({dialogOpen: false})
                    }}/>
                <DialogContent>
                    <Table className={classes.table}>
                        <EnhancedTableHead
                            columnData={columnData}
                            ids={ids}
                            baseId="analysis"
                            order={order}
                            orderBy={orderBy}
                        />
                        <TableBody>
                            {parameters.map(n => {
                                return (
                                    <TableRow key={n.param_id}>
                                        <TableCell>{n.param_name}</TableCell>
                                        <TableCell>{n.param_type}</TableCell>
                                        <TableCell><ParameterValue parameter={n}/></TableCell>
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

export default withStyles(exStyles)(withI18N(AnalysisParametersDialog, intlData));