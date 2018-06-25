/**
 * @author psarando
 */
import React, { Component } from "react";

import styles from "../style";

import AttributeEnumEditGrid from "./AttributeEnumEditGrid";
import OntologyLookupServiceSettings from "./OntologyLookupServiceSettings";
import SlideUpTransition from "./SlideUpTransition";
import TemplateAttributeList from "./TemplateAttributeList";

import AppBar from "@material-ui/core/AppBar";
import Button from "@material-ui/core/Button";
import Checkbox from "@material-ui/core/Checkbox";
import Dialog from "@material-ui/core/Dialog";
import DialogContent from "@material-ui/core/DialogContent";
import Divider from "@material-ui/core/Divider";
import ExpansionPanel from "@material-ui/core/ExpansionPanel";
import ExpansionPanelSummary from "@material-ui/core/ExpansionPanelSummary";
import ExpansionPanelDetails from "@material-ui/core/ExpansionPanelDetails";
import FormControl from "@material-ui/core/FormControl";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import IconButton from "@material-ui/core/IconButton";
import Input from "@material-ui/core/Input";
import InputLabel from "@material-ui/core/InputLabel";
import MenuItem from "@material-ui/core/MenuItem";
import Select from "@material-ui/core/Select";
import TextField from "@material-ui/core/TextField";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import { withStyles } from "@material-ui/core/styles";

import CloseIcon from "@material-ui/icons/Close";
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

        const { name, description, type, required, values, settings, attributes } = props.attribute;
        this.state = {
            name,
            description,
            type,
            required,
            values: values || [],
            settings: settings || {},
            attributes: attributes || [],
            editingAttrIndex: -1,
        };

        this.EditAttributeChild = withStyles(styles)(EditAttribute);
    }

    componentWillReceiveProps(newProps) {
        const { attribute } = newProps;
        const { name, description, type, required, values, settings, attributes } = attribute;

        this.setState({
            name,
            description,
            type,
            required,
            values: values || [],
            settings: settings || {},
            attributes: attributes || [],
        });
    }

    saveAttr = () => {
        const {
            name,
            description,
            type,
            required,
            values,
            settings,
            attributes,
        } = this.state;

        this.props.saveAttr({
            ...this.props.attribute,
            name,
            description,
            type,
            required,
            values,
            settings,
            attributes,
        });
    };

    handleChange = key => event => {
        this.setState({
            [key]: event.target.value,
        });
    };

    onAttributesChanged = (attributes) => {
        this.setState({attributes});
    };

    onAttributeUpdated = (index, attr) => {
        let attributes = [...this.state.attributes];
        attributes.splice(index, 1, attr);

        this.setState({attributes: attributes, editingAttrIndex: -1});
    };

    render() {
        const { classes, open, parentName } = this.props;
        const { name, description, type, required, attributes, values, settings, editingAttrIndex } = this.state;
        const editingAttr = editingAttrIndex >= 0 ? attributes[editingAttrIndex] : {};

        const EditAttributeChild = this.EditAttributeChild;

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
                            <CloseIcon />
                        </IconButton>
                        <Typography variant="title" color="inherit" className={classes.flex}>
                            Edit Attribute for {parentName}
                        </Typography>
                        <Button color="inherit" onClick={this.saveAttr}>
                            Save
                        </Button>
                    </Toolbar>
                </AppBar>
                <DialogContent>

                    <TextField id="name"
                               label="Name"
                               value={name || ""}
                               onChange={this.handleChange("name")}
                               margin="dense"
                               autoFocus
                               fullWidth
                    />
                    <TextField id="description"
                               label="Description"
                               value={description || ""}
                               onChange={this.handleChange("description")}
                               fullWidth
                    />

                    <FormControl fullWidth>
                        <InputLabel htmlFor="attr-type">Type</InputLabel>
                        <Select
                            value={type || ""}
                            onChange={this.handleChange("type")}
                            input={<Input id="attr-type" />}
                        >
                            {AttributeTypeMenuItems}
                        </Select>
                    </FormControl>

                    <FormControlLabel
                        control={
                            <Checkbox id="required"
                                      color="primary"
                                      checked={!!required}
                                      onChange={(event, checked) => this.setState({required: checked})}
                            />
                        }
                        label="Required?"
                    />

                    <Divider />

                    {type === "Enum" &&
                    <ExpansionPanel defaultExpanded>
                        <ExpansionPanelSummary expandIcon={<ExpandMoreIcon />}>
                            <Typography className={classes.heading}>Enum Values</Typography>
                        </ExpansionPanelSummary>
                        <ExpansionPanelDetails>
                            <AttributeEnumEditGrid values={values}
                                                   onValuesChanged={(values) => this.setState({values})}/>
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
                            <TemplateAttributeList attributes={attributes}
                                                   onAttributesChanged={this.onAttributesChanged}
                                                   onAttributeUpdated={this.onAttributeUpdated}
                                                   onEditAttr={(index) => this.setState({editingAttrIndex: index})}
                            />
                        </ExpansionPanelDetails>
                    </ExpansionPanel>

                    <EditAttributeChild attribute={editingAttr}
                                        open={editingAttrIndex >= 0}
                                        parentName={name}
                                        saveAttr={(attr) => this.onAttributeUpdated(editingAttrIndex, attr)}
                                        closeAttrDialog={() => this.setState({editingAttrIndex: -1})}
                    />
                </DialogContent>
            </Dialog>
        );
    }
}

export default withStyles(styles)(EditAttribute);
