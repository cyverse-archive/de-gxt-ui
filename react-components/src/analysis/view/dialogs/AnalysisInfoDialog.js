/**
 *
 * @author sriram
 *
 */

import React, { Component } from "react";

import exStyles from "../../style";
import ids from "../../ids";
import intlData from "../../messages";
import { injectIntl } from "react-intl";

import {
    build,
    formatDate,
    CopyTextArea,
    DEDialogHeader,
    EnhancedTableHead,
    getMessage,
    withI18N,
} from "@cyverse-de/ui-lib";

import ExpandMoreIcon from "@material-ui/icons/ExpandMore";

import {
    DialogContent,
    Dialog,
    withStyles,
    ExpansionPanel,
    ExpansionPanelSummary,
    Typography,
    ExpansionPanelDetails,
    TableBody,
    Table,
    TableRow,
    TableCell,
} from "@material-ui/core";

const columnData = [
    {
        id: ids.INFO.TIMESTAMP,
        name: "Date",
        numeric: false,
        enableSorting: false,
    },
    {
        id: ids.INFO.MESSAGE,
        name: "Message",
        numeric: false,
        enableSorting: false,
    },
    {
        id: ids.INFO.STATUS,
        name: "Status",
        numeric: false,
        enableSorting: false,
    },
];

function Updates(props) {
    const { updates, classes, debugId } = props;
    return (
        <Table>
            <TableBody>
                {updates.map((update, index) => {
                    const status = update.status;
                    const timestamp = update.timestamp;

                    return (
                        <TableRow key={index}>
                            <TableCell className={classes.analysisInfoFont}>
                                {formatDate(timestamp)}
                            </TableCell>
                            <TableCell className={classes.analysisInfoFont}>
                                {update.message}
                            </TableCell>
                            <TableCell className={classes.analysisInfoFont}>
                                {status[0].toUpperCase() +
                                    status
                                        .slice(1)
                                        .toLowerCase()
                                        .replace(/[_]/gi, " ")}
                            </TableCell>
                        </TableRow>
                    );
                })}
            </TableBody>
            <EnhancedTableHead
                selectable={false}
                columnData={columnData}
                rowsInPage={updates.length}
                baseId={debugId}
            />
        </Table>
    );
}

function Step(props) {
    const { step_number, external_id, step_type, status, updates } = props.step;
    const { classes, debugId } = props;
    return (
        <ExpansionPanel>
            <ExpansionPanelSummary expandIcon={<ExpandMoreIcon />}>
                <Typography className={classes.heading}>
                    {step_number}: {step_type} - {status}
                </Typography>
                <Typography
                    className={classes.secondaryHeading}
                    variant="subtitle2"
                >
                    {getMessage("analysisId")}: {external_id}
                </Typography>
            </ExpansionPanelSummary>
            <ExpansionPanelDetails
                classes={{ root: classes.expansionPanelDetails }}
            >
                <div className={classes.copyAnalysisId}>
                    <CopyTextArea
                        text={external_id}
                        btnText={getMessage("copyAnalysisId")}
                    />
                </div>
                <Updates
                    updates={updates}
                    classes={classes}
                    debugId={debugId}
                />
            </ExpansionPanelDetails>
        </ExpansionPanel>
    );
}

class AnalysisInfoDialog extends Component {
    render() {
        const {
            info,
            dialogOpen,
            onInfoDialogClose,
            intl,
            classes,
            baseDebugId,
        } = this.props;
        const debugId = build(baseDebugId, ids.INFO.INFO);
        return (
            <Dialog open={dialogOpen} maxWidth="md" id={debugId}>
                <DEDialogHeader
                    heading={intl.formatMessage({ id: "analysisInfoDlgTitle" })}
                    onClose={onInfoDialogClose}
                />
                <DialogContent>
                    {info.steps.map((s, index) => {
                        return (
                            <Step
                                key={index}
                                step={s}
                                classes={classes}
                                debugId={debugId}
                            />
                        );
                    })}
                </DialogContent>
            </Dialog>
        );
    }
}

export default withStyles(exStyles)(
    withI18N(injectIntl(AnalysisInfoDialog), intlData)
);
