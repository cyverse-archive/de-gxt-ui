/**
 * @author psarando
 */
import React, { Component } from "react";
import { Field, FieldArray, FormSection } from "redux-form";
import { injectIntl } from "react-intl";

import build from "../../util/DebugIDUtil";
import withI18N, { getMessage, formatMessage } from "../../util/I18NWrapper";
import intlData from "../messages";
import styles from "../style";
import ids from "./ids";

import {
    FormCheckbox,
    FormSelectField,
    FormTextField,
} from "../../util/FormField";
import AttributeEnumEditGrid from "./AttributeEnumEditGrid";
import OntologyLookupServiceSettings from "./OntologyLookupServiceSettings";

import Button from "@material-ui/core/Button";
import Divider from "@material-ui/core/Divider";
import ExpansionPanel from "@material-ui/core/ExpansionPanel";
import ExpansionPanelSummary from "@material-ui/core/ExpansionPanelSummary";
import ExpansionPanelDetails from "@material-ui/core/ExpansionPanelDetails";
import Grid from '@material-ui/core/Grid';
import IconButton from "@material-ui/core/IconButton";
import MenuItem from "@material-ui/core/MenuItem";
import Typography from "@material-ui/core/Typography";
import { withStyles } from "@material-ui/core/styles";

import ContentAdd from "@material-ui/icons/Add";
import ContentRemove from "@material-ui/icons/Delete";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import KeyboardArrowDown from "@material-ui/icons/KeyboardArrowDown";
import KeyboardArrowUp from "@material-ui/icons/KeyboardArrowUp";

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
    "Grouping",
];

const AttributeTypeMenuItems = AttributeTypes.map((type, index) => (<MenuItem key={index} value={type}>{type}</MenuItem>));

class EditAttribute extends Component {
    constructor(props) {
        super(props);

        this.state = {
            editingAttrIndex: -1,
        };
    }

    normalizeType = (type) => {
        const { change, field, attribute: { settings } } = this.props;

        if (type === "OLS Ontology Term" && (!settings || !settings.type)) {
            change(`${field}.settings`, {...settings, type: "CLASS"});
        }

        return type;
    };

    render() {
        const {
            classes,
            intl,
            onAttributeRemoved,
            moveUp,
            moveDown,
            moveUpDisabled,
            moveDownDisabled,
            attribute: { name, description, type },
            // from redux-form
            change, field, error,
        } = this.props;

        const formID = build(ids.METADATA_TEMPLATE_FORM, field, ids.DIALOG);
        const dialogTitleID = build(formID, ids.TITLE);

        return (
            <ExpansionPanel aria-labelledby={dialogTitleID}>
                <ExpansionPanelSummary expandIcon={<ExpandMoreIcon id={build(formID, ids.BUTTONS.EXPAND)} />}>
                    <IconButton id={build(formID, ids.BUTTONS.DELETE)}
                                aria-label={formatMessage(intl, "delete")}
                                classes={{root: classes.deleteIcon}}
                                onClick={event => {
                                    event.stopPropagation();
                                    onAttributeRemoved();
                                }}
                    >
                        <ContentRemove />
                    </IconButton>
                    <div className={classes.title}>
                        <Typography id={dialogTitleID} variant="title" color="inherit" >
                            {name}
                        </Typography>
                        <Typography id={build(formID, error ? ids.TITLE_ERR : ids.TITLE_SUB)}
                                    variant="subheading"
                                    noWrap
                                    className={error ? classes.errorSubTitle : null}
                        >
                            {error || description}
                        </Typography>
                    </div>
                    <div className={classes.spacer} />
                    <div className={classes.actions}>
                        <Button id={build(formID, ids.BUTTONS.MOVE_UP)}
                                variant="fab"
                                mini
                                color="secondary"
                                aria-label={formatMessage(intl, "moveUp")}
                                className={classes.button}
                                disabled={moveUpDisabled}
                                onClick={(event) => {
                                    event.stopPropagation();
                                    moveUp();
                                }}
                        >
                            <KeyboardArrowUp />
                        </Button>
                    </div>
                    <div className={classes.actions}>
                        <Button id={build(formID, ids.BUTTONS.MOVE_DOWN)}
                                variant="fab"
                                mini
                                color="secondary"
                                aria-label={formatMessage(intl, "moveDown")}
                                className={classes.button}
                                disabled={moveDownDisabled}
                                onClick={(event) => {
                                    event.stopPropagation();
                                    moveDown();
                                }}
                        >
                            <KeyboardArrowDown />
                        </Button>
                    </div>
                </ExpansionPanelSummary>
                <ExpansionPanelDetails>
                    <Grid container
                          spacing={16}
                          direction="column"
                          justify="flex-start"
                          alignItems="stretch"
                    >

                        <Grid item>
                            <Field name={`${field}.name`}
                                   label={getMessage("attrNameLabel")}
                                   id={build(formID, ids.ATTR_NAME)}
                                   required={true}
                                   margin="dense"
                                   component={FormTextField}
                            />
                        </Grid>
                        <Grid item>
                            <Field name={`${field}.description`}
                                   label={getMessage("description")}
                                   id={build(formID, ids.ATTR_DESCRIPTION)}
                                   component={FormTextField}
                            />
                        </Grid>
                        <Grid item>

                            <Field name={`${field}.type`}
                                   label={getMessage("attrTypeLabel")}
                                   id={build(formID, ids.ATTR_TYPE)}
                                   component={FormSelectField}
                                   normalize={this.normalizeType}
                            >
                                {AttributeTypeMenuItems}
                            </Field>
                        </Grid>
                        <Grid item>

                            <Field name={`${field}.required`}
                                   label={getMessage("attrRequiredLabel")}
                                   id={build(formID, ids.ATTR_REQUIRED)}
                                   color="primary"
                                   component={FormCheckbox}
                            />
                        </Grid>
                        <Grid item>

                            <Divider />
                        </Grid>

                        {type === "Enum" &&
                        <Grid item>
                            <ExpansionPanel defaultExpanded>
                                <ExpansionPanelSummary expandIcon={<ExpandMoreIcon id={build(formID, ids.BUTTONS.EXPAND, ids.ENUM_VALUES_GRID)} />}>
                                    <Typography className={classes.heading}>{getMessage("enumValues")}</Typography>
                                </ExpansionPanelSummary>
                                <ExpansionPanelDetails>
                                    <FieldArray name={`${field}.values`}
                                                component={AttributeEnumEditGrid}
                                                parentID={formID}
                                                change={change}
                                    />
                                </ExpansionPanelDetails>
                            </ExpansionPanel>
                        </Grid>
                        }

                        {type === "OLS Ontology Term" &&
                        <Grid item>
                            <ExpansionPanel defaultExpanded>
                                <ExpansionPanelSummary expandIcon={<ExpandMoreIcon id={build(formID, ids.BUTTONS.EXPAND, ids.OLS_PARAMS_EDIT_DIALOG)} />}>
                                    <Typography className={classes.heading}>{getMessage("olsQueryParams")}</Typography>
                                </ExpansionPanelSummary>
                                <ExpansionPanelDetails>
                                    <FormSection name={`${field}.settings`}
                                                 component={OntologyLookupServiceSettings}
                                                 parentID={formID}
                                    />
                                </ExpansionPanelDetails>
                            </ExpansionPanel>
                        </Grid>
                        }

                        <Grid item>
                            <FieldArray name={`${field}.attributes`}
                                        component={EditAttributeFormList}
                                        change={change}
                            />
                        </Grid>
                    </Grid>
                </ExpansionPanelDetails>
            </ExpansionPanel>
        );
    }
}

EditAttribute = withStyles(styles)(withI18N(injectIntl(EditAttribute), intlData));

class EditAttributeFormList extends Component {
    constructor(props) {
        super(props);

        this.newAttrCount = 1;
    }

    onAddAttribute = () => {
        const fields = this.props.fields;
        const attributes = fields.getAll() || [];

        let name = `New attribute ${this.newAttrCount++}`;

        const namesMatch = attr => (attr.name === name);
        while (attributes.findIndex(namesMatch) > -1) {
            name = `New attribute ${this.newAttrCount++}`;
        }

        fields.unshift({
            name: name,
            description: "",
            type: "String",
            required: false,
        });
    };

    onAttributeRemoved = (index) => {
        this.props.fields.remove(index);
    };

    moveUp = (index) => {
        this.moveSelectedAttr(index, -1);
    };

    moveDown = (index) => {
        this.moveSelectedAttr(index, 1);
    };

    moveSelectedAttr = (index, offset) => {
        this.props.fields.move(index, index + offset);
    };

    render () {
        const { classes, intl, fields, change, meta: { error } } = this.props;

        return (
            <ExpansionPanel defaultExpanded={fields && fields.length > 0}>
                <ExpansionPanelSummary expandIcon={<ExpandMoreIcon id={build(fields.name, ids.BUTTONS.EXPAND, ids.ATTR_GRID)} />}>
                    <div className={classes.actions}>
                        <Button variant="fab"
                                mini
                                color="primary"
                                id={build(fields.name, ids.BUTTONS.ADD)}
                                aria-label={formatMessage(intl, "addRow")}
                                onClick={(event) => {
                                    event.stopPropagation();
                                    this.onAddAttribute();
                                }}
                        >
                            <ContentAdd />
                        </Button>
                    </div>
                    <Typography className={classes.title}>{getMessage("attributes")}</Typography>
                </ExpansionPanelSummary>
                <ExpansionPanelDetails>
                    <Grid container
                          spacing={16}
                          direction="column"
                          justify="flex-start"
                          alignItems="stretch"
                    >
                        {fields.map((field, index) => (
                            <Grid item key={field}>
                                <EditAttribute field={field}
                                               change={change}
                                               error={error && error[index]}
                                               attribute={fields.get(index)}
                                               onAddAttribute={this.onAddAttribute}
                                               onAttributeRemoved={() => this.onAttributeRemoved(index)}
                                               moveUp={() => this.moveUp(index)}
                                               moveUpDisabled={index <= 0}
                                               moveDown={() => this.moveDown(index)}
                                               moveDownDisabled={index < 0 || (fields.length - 1) <= index}
                                />
                            </Grid>
                        ))}
                    </Grid>
                </ExpansionPanelDetails>
            </ExpansionPanel>
        );
    }
}

EditAttributeFormList = withStyles(styles)(withI18N(injectIntl(EditAttributeFormList), intlData));

export default EditAttributeFormList;
