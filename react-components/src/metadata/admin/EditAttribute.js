/**
 * @author psarando
 */
import React, { Component, Fragment } from "react";
import { Field, FieldArray } from "redux-form";

import {
    FormCheckbox,
    FormSelectField,
    FormTextField,
} from "../../util/FormField";

import styles from "../style";
import AttributeEnumEditGrid from "./AttributeEnumEditGrid";
import OntologyLookupServiceSettings from "./OntologyLookupServiceSettings";
import SlideUpTransition from "./SlideUpTransition";
import TemplateAttributeList from "./TemplateAttributeList";

import AppBar from "@material-ui/core/AppBar";
import Dialog from "@material-ui/core/Dialog";
import DialogContent from "@material-ui/core/DialogContent";
import Divider from "@material-ui/core/Divider";
import ExpansionPanel from "@material-ui/core/ExpansionPanel";
import ExpansionPanelSummary from "@material-ui/core/ExpansionPanelSummary";
import ExpansionPanelDetails from "@material-ui/core/ExpansionPanelDetails";
import IconButton from "@material-ui/core/IconButton";
import MenuItem from "@material-ui/core/MenuItem";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import { withStyles } from "@material-ui/core/styles";

import ArrowBack from "@material-ui/icons/ArrowBack";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";

const AttributeTypes = [
    "String",
    "Timestamp",
    "Boolean",
    "Number",
    "Integer",
    "Multiline Text",
    "URL/URI",
    "Enum",
    "OLS Ontology Term",
    "UAT Ontology Term",
];

const AttributeTypeMenuItems = AttributeTypes.map((type, index) => (<MenuItem key={index} value={type}>{type}</MenuItem>));

class EditAttribute extends Component {
    constructor(props) {
        super(props);

        const { settings } = props.attribute;
        this.state = {
            settings: settings || {},
            editingAttrIndex: -1,
        };
    }

    componentWillReceiveProps(newProps) {
        const { attribute } = newProps;
        const { settings } = attribute;

        this.setState({
            settings: settings || {},
        });
    }

    render() {
        const { classes, change, field, attribute, open, parentName } = this.props;
        const { name, type, attributes } = attribute;
        const { settings, editingAttrIndex } = this.state;

        return (
            <Dialog
                open={open}
                onClose={this.props.closeAttrDialog}
                fullScreen
                disableBackdropClick
                disableEscapeKeyDown
                aria-labelledby="form-dialog-title"
                TransitionComponent={SlideUpTransition}
            >
                <AppBar className={classes.appBar}>
                    <Toolbar>
                        <IconButton color="inherit" onClick={this.props.closeAttrDialog} aria-label="Close">
                            <ArrowBack />
                        </IconButton>
                        <Typography variant="title" color="inherit" className={classes.flex}>
                            Edit Attribute for {parentName}
                        </Typography>
                    </Toolbar>
                </AppBar>
                <DialogContent>

                    <Field name={`${field}.name`}
                           label="Name"
                           id="attrName"
                           autoFocus
                           margin="dense"
                           component={FormTextField}
                    />
                    <Field name={`${field}.description`}
                           label="Description"
                           id="attrDescription"
                           component={FormTextField}
                    />

                    <Field name={`${field}.type`}
                           label="Type"
                           id="attrType"
                           component={FormSelectField}
                    >
                        {AttributeTypeMenuItems}
                    </Field>

                    <Field name={`${field}.required`}
                           label="Required?"
                           id="attrRequired"
                           color="primary"
                           component={FormCheckbox}
                    />

                    <Divider />

                    {type === "Enum" &&
                    <ExpansionPanel defaultExpanded>
                        <ExpansionPanelSummary expandIcon={<ExpandMoreIcon />}>
                            <Typography className={classes.heading}>Enum Values</Typography>
                        </ExpansionPanelSummary>
                        <ExpansionPanelDetails>
                            <FieldArray name={`${field}.values`}
                                        component={AttributeEnumEditGrid}
                                        change={change}
                            />
                        </ExpansionPanelDetails>
                    </ExpansionPanel>
                    }

                    {type === "OLS Ontology Term" &&
                    <ExpansionPanel defaultExpanded>
                        <ExpansionPanelSummary expandIcon={<ExpandMoreIcon />}>
                            <Typography className={classes.heading}>Ontology Lookup Service Query Params</Typography>
                        </ExpansionPanelSummary>
                        <ExpansionPanelDetails>
                            <OntologyLookupServiceSettings settings={settings}
                                                           onSettingsChanged={(settings) => this.setState({settings})}/>
                        </ExpansionPanelDetails>
                    </ExpansionPanel>
                    }

                    <ExpansionPanel defaultExpanded={attributes && attributes.length > 0}>
                        <ExpansionPanelSummary expandIcon={<ExpandMoreIcon />}>
                            <Typography className={classes.heading}>Attributes</Typography>
                        </ExpansionPanelSummary>
                        <ExpansionPanelDetails>
                            <FieldArray name={`${field}.attributes`}
                                        component={TemplateAttributeList}
                                        onEditAttr={(index) => this.setState({editingAttrIndex: index})}
                            />
                        </ExpansionPanelDetails>
                    </ExpansionPanel>


                    <FieldArray name={`${field}.attributes`}
                                component={FormDialogEditAttribute}
                                editingAttrIndex={editingAttrIndex}
                                parentName={name}
                                closeAttrDialog={() => this.setState({editingAttrIndex: -1})}
                    />
                </DialogContent>
            </Dialog>
        );
    }
}

EditAttribute = withStyles(styles)(EditAttribute);

const FormDialogEditAttribute = ({ fields, change, editingAttrIndex, parentName, closeAttrDialog }) => (
    <Fragment>
        {fields.map((field, index) => (
            <EditAttribute key={field}
                           field={field}
                           change={change}
                           attribute={fields.get(index)}
                           open={editingAttrIndex === index}
                           parentName={parentName}
                           closeAttrDialog={closeAttrDialog}
            />
        ))
        }
    </Fragment>
);

export default FormDialogEditAttribute;
