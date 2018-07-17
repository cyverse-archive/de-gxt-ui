/**
 * @author psarando
 */
import React, { Component, Fragment } from "react";
import { Field, FieldArray } from "redux-form";
import { injectIntl } from "react-intl";

import build from "../util/DebugIDUtil";
import withI18N, { getMessage, formatMessage } from "../util/I18NWrapper";
import ids from "./ids";
import intlData from "./messages";
import styles from "./style";

import { FormTextField } from "../util/FormField";
import MetadataList from "./MetadataList";
import SlideUpTransition from "./SlideUpTransition";

import AppBar from "@material-ui/core/AppBar";
import Dialog from "@material-ui/core/Dialog";
import DialogContent from "@material-ui/core/DialogContent";
import Divider from "@material-ui/core/Divider";
import ExpansionPanel from "@material-ui/core/ExpansionPanel";
import ExpansionPanelSummary from "@material-ui/core/ExpansionPanelSummary";
import ExpansionPanelDetails from "@material-ui/core/ExpansionPanelDetails";
import IconButton from "@material-ui/core/IconButton";
import Toolbar from "@material-ui/core/Toolbar";
import Tooltip from "@material-ui/core/Tooltip";
import Typography from "@material-ui/core/Typography";
import { withStyles } from "@material-ui/core/styles";

import ArrowBack from "@material-ui/icons/ArrowBack";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";

class EditAVU extends Component {
    constructor(props) {
        super(props);

        this.state = {
            editingAttrIndex: -1,
        };
    }

    render() {
        const { classes, intl, field, error, avu, open, parentName } = this.props;
        const { attr, avus } = avu;
        const { editingAttrIndex } = this.state;

        const formID = build(ids.EDIT_METADATA_FORM, field, ids.DIALOG);
        const dialogTitleID = build(formID, ids.TITLE);

        const title = parentName ?
            getMessage("dialogTitleEditAVUFor", {values: { parentName }}) :
            getMessage("dialogTitleEditAVU");

        return (
            <Dialog
                open={open}
                onClose={this.props.closeAttrDialog}
                fullScreen
                disableBackdropClick
                disableEscapeKeyDown
                aria-labelledby={dialogTitleID}
                TransitionComponent={SlideUpTransition}
            >
                <AppBar className={classes.appBar}>
                    <Tooltip
                        title={error ? getMessage("errAVUEditFormTooltip") : ""}
                        placement="bottom-start"
                        enterDelay={200}
                    >
                        <Toolbar>
                            <IconButton id={build(formID, ids.BUTTONS.CLOSE)}
                                        color="inherit"
                                        aria-label={formatMessage(intl, "close")}
                                        disabled={!!error}
                                        onClick={this.props.closeAttrDialog}
                            >
                                <ArrowBack />
                            </IconButton>
                            <Typography id={dialogTitleID} variant="title" color="inherit" className={classes.flex}>
                                {title}
                            </Typography>
                        </Toolbar>
                    </Tooltip>
                </AppBar>
                <DialogContent>

                    <Field name={`${field}.attr`}
                           label={getMessage("attribute")}
                           id={build(formID, ids.AVU_ATTR)}
                           required={true}
                           autoFocus
                           margin="dense"
                           component={FormTextField}
                    />
                    <Field name={`${field}.value`}
                           label={getMessage("value")}
                           id={build(formID, ids.AVU_VALUE)}
                           component={FormTextField}
                    />
                    <Field name={`${field}.unit`}
                           label={getMessage("metadataUnitLabel")}
                           id={build(formID, ids.AVU_UNIT)}
                           component={FormTextField}
                    />

                    <Divider />

                    <ExpansionPanel defaultExpanded={avus && avus.length > 0}>
                        <ExpansionPanelSummary expandIcon={<ExpandMoreIcon id={build(formID, ids.BUTTONS.EXPAND, ids.AVU_GRID)} />}>
                            <Typography className={classes.heading}>{getMessage("metadataChildrenLabel")}</Typography>
                        </ExpansionPanelSummary>
                        <ExpansionPanelDetails>
                            <FieldArray name={`${field}.avus`}
                                        component={MetadataList}
                                        parentID={formID}
                                        onEditAVU={(index) => this.setState({editingAttrIndex: index})}
                            />
                        </ExpansionPanelDetails>
                    </ExpansionPanel>


                    <FieldArray name={`${field}.avus`}
                                component={FormDialogEditAVU}
                                editingAttrIndex={editingAttrIndex}
                                parentName={attr}
                                closeAttrDialog={() => this.setState({editingAttrIndex: -1})}
                    />
                </DialogContent>
            </Dialog>
        );
    }
}

EditAVU = withStyles(styles)(withI18N(injectIntl(EditAVU), intlData));

const FormDialogEditAVU = ({ fields, change, meta: { error }, editingAttrIndex, parentName, closeAttrDialog }) => (
    <Fragment>
        {fields.map((field, index) => (
            <EditAVU key={field}
                     field={field}
                     change={change}
                     error={error && error[index]}
                     avu={fields.get(index)}
                     open={editingAttrIndex === index}
                     parentName={parentName}
                     closeAttrDialog={closeAttrDialog}
            />
        ))
        }
    </Fragment>
);

export default FormDialogEditAVU;
