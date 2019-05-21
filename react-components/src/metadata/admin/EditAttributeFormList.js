/**
 * @author psarando
 */
import React, { Component } from "react";

import { FastField, FieldArray, getIn } from "formik";
import PropTypes from "prop-types";
import { injectIntl } from "react-intl";

import constants from "../constants";
import intlData from "../messages";
import styles from "../style";
import ids from "./ids";

import {
    build,
    FormCheckbox,
    formatMessage,
    FormSelectField,
    FormTextField,
    getFormError,
    getMessage,
    withI18N,
} from "@cyverse-de/ui-lib";
import AttributeEnumEditGrid from "./AttributeEnumEditGrid";
import OntologyLookupServiceSettings from "./OntologyLookupServiceSettings";

import Divider from "@material-ui/core/Divider";
import ExpansionPanel from "@material-ui/core/ExpansionPanel";
import ExpansionPanelSummary from "@material-ui/core/ExpansionPanelSummary";
import ExpansionPanelDetails from "@material-ui/core/ExpansionPanelDetails";
import Fab from "@material-ui/core/Fab";
import Grid from "@material-ui/core/Grid";
import IconButton from "@material-ui/core/IconButton";
import MenuItem from "@material-ui/core/MenuItem";
import Typography from "@material-ui/core/Typography";
import { withStyles } from "@material-ui/core/styles";

import ContentAdd from "@material-ui/icons/Add";
import ContentRemove from "@material-ui/icons/Delete";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import KeyboardArrowDown from "@material-ui/icons/KeyboardArrowDown";
import KeyboardArrowUp from "@material-ui/icons/KeyboardArrowUp";

const AttributeTypeMenuItems = [
    constants.ATTRIBUTE_TYPE.STRING,
    constants.ATTRIBUTE_TYPE.TIMESTAMP,
    constants.ATTRIBUTE_TYPE.BOOLEAN,
    constants.ATTRIBUTE_TYPE.NUMBER,
    constants.ATTRIBUTE_TYPE.INTEGER,
    constants.ATTRIBUTE_TYPE.MULTILINE_TEXT,
    constants.ATTRIBUTE_TYPE.URL,
    constants.ATTRIBUTE_TYPE.ENUM,
    constants.ATTRIBUTE_TYPE.ONTOLOGY_TERM_OLS,
    constants.ATTRIBUTE_TYPE.ONTOLOGY_TERM_UAT,
    constants.ATTRIBUTE_TYPE.GROUPING,
].map((type, index) => (
    <MenuItem key={index} value={type}>
        {type}
    </MenuItem>
));

class EditAttribute extends Component {
    constructor(props) {
        super(props);

        this.state = {
            editingAttrIndex: -1,
        };

        ["normalizeType"].forEach(
            (methodName) => (this[methodName] = this[methodName].bind(this))
        );
    }

    static propTypes = {
        onAttributeRemoved: PropTypes.func.isRequired,
        moveUp: PropTypes.func.isRequired,
        moveDown: PropTypes.func.isRequired,
        moveUpDisabled: PropTypes.bool,
        moveDownDisabled: PropTypes.bool,
        attribute: PropTypes.shape({
            name: PropTypes.string.isRequired,
            description: PropTypes.string.isRequired,
            type: PropTypes.string.isRequired,
            required: PropTypes.bool,
            settings: PropTypes.shape({
                type: PropTypes.string,
            }),
        }).isRequired,
    };

    normalizeType(type) {
        const {
            setFieldValue,
            field,
            attribute: { settings },
        } = this.props;

        const isOLSType = type === constants.ATTRIBUTE_TYPE.ONTOLOGY_TERM_OLS;

        if (isOLSType && (!settings || !settings.type)) {
            setFieldValue(`${field}.settings`, { ...settings, type: "CLASS" });
        }

        return type;
    }

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
            field,
            touched,
            errors,
        } = this.props;

        const formID = build(ids.METADATA_TEMPLATE_FORM, field);
        const dialogTitleID = build(formID, ids.TITLE);
        const attrErrors = getFormError(field, touched, errors);
        const error = attrErrors && attrErrors.error;

        return (
            <ExpansionPanel aria-labelledby={dialogTitleID}>
                <ExpansionPanelSummary
                    expandIcon={
                        <ExpandMoreIcon
                            id={build(formID, ids.BUTTONS.EXPAND)}
                        />
                    }
                >
                    <IconButton
                        id={build(formID, ids.BUTTONS.DELETE)}
                        aria-label={formatMessage(intl, "delete")}
                        classes={{ root: classes.deleteIcon }}
                        onClick={(event) => {
                            event.stopPropagation();
                            onAttributeRemoved();
                        }}
                    >
                        <ContentRemove />
                    </IconButton>
                    <div className={classes.title}>
                        <Typography
                            id={dialogTitleID}
                            variant="h6"
                            color="inherit"
                        >
                            {name}
                        </Typography>
                        <Typography
                            id={build(
                                formID,
                                error ? ids.TITLE_ERR : ids.TITLE_SUB
                            )}
                            variant="subtitle1"
                            noWrap
                            className={error ? classes.errorSubTitle : null}
                        >
                            {error || description}
                        </Typography>
                    </div>
                    <div className={classes.spacer} />
                    <div className={classes.actions}>
                        <Fab
                            id={build(formID, ids.BUTTONS.MOVE_UP)}
                            size="small"
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
                        </Fab>
                    </div>
                    <div className={classes.actions}>
                        <Fab
                            id={build(formID, ids.BUTTONS.MOVE_DOWN)}
                            size="small"
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
                        </Fab>
                    </div>
                </ExpansionPanelSummary>
                <ExpansionPanelDetails>
                    <Grid
                        container
                        spacing={16}
                        direction="column"
                        justify="flex-start"
                        alignItems="stretch"
                    >
                        <Grid item>
                            <FastField
                                name={`${field}.name`}
                                label={getMessage("attrNameLabel")}
                                id={build(formID, ids.ATTR_NAME)}
                                required={true}
                                component={FormTextField}
                            />
                        </Grid>
                        <Grid item>
                            <FastField
                                name={`${field}.description`}
                                label={getMessage("description")}
                                id={build(formID, ids.ATTR_DESCRIPTION)}
                                component={FormTextField}
                            />
                        </Grid>
                        <Grid item>
                            <FastField
                                name={`${field}.type`}
                                label={getMessage("attrTypeLabel")}
                                id={build(formID, ids.ATTR_TYPE)}
                                render={({
                                    field: { onChange, ...field },
                                    ...props
                                }) => (
                                    <FormSelectField
                                        {...props}
                                        field={field}
                                        onChange={(event) => {
                                            this.normalizeType(
                                                event.target.value
                                            );
                                            onChange(event);
                                        }}
                                    >
                                        {AttributeTypeMenuItems}
                                    </FormSelectField>
                                )}
                            />
                        </Grid>
                        <Grid item>
                            <FastField
                                name={`${field}.required`}
                                label={getMessage("attrRequiredLabel")}
                                id={build(formID, ids.ATTR_REQUIRED)}
                                color="primary"
                                component={FormCheckbox}
                            />
                        </Grid>
                        <Grid item>
                            <Divider />
                        </Grid>

                        {type === constants.ATTRIBUTE_TYPE.ENUM && (
                            <Grid item>
                                <ExpansionPanel defaultExpanded>
                                    <ExpansionPanelSummary
                                        expandIcon={
                                            <ExpandMoreIcon
                                                id={build(
                                                    formID,
                                                    ids.BUTTONS.EXPAND,
                                                    ids.ENUM_VALUES_GRID
                                                )}
                                            />
                                        }
                                    >
                                        <Typography variant="subtitle1">
                                            {getMessage("enumValues")}
                                        </Typography>
                                    </ExpansionPanelSummary>
                                    <ExpansionPanelDetails>
                                        <FieldArray
                                            name={`${field}.values`}
                                            render={(arrayHelpers) => (
                                                <AttributeEnumEditGrid
                                                    {...arrayHelpers}
                                                    parentID={formID}
                                                />
                                            )}
                                        />
                                    </ExpansionPanelDetails>
                                </ExpansionPanel>
                            </Grid>
                        )}

                        {type ===
                            constants.ATTRIBUTE_TYPE.ONTOLOGY_TERM_OLS && (
                            <Grid item>
                                <ExpansionPanel defaultExpanded>
                                    <ExpansionPanelSummary
                                        expandIcon={
                                            <ExpandMoreIcon
                                                id={build(
                                                    formID,
                                                    ids.BUTTONS.EXPAND,
                                                    ids.OLS_PARAMS_EDIT_DIALOG
                                                )}
                                            />
                                        }
                                    >
                                        <Typography variant="subtitle1">
                                            {getMessage("olsQueryParams")}
                                        </Typography>
                                    </ExpansionPanelSummary>
                                    <ExpansionPanelDetails>
                                        <OntologyLookupServiceSettings
                                            field={`${field}.settings`}
                                            parentID={formID}
                                        />
                                    </ExpansionPanelDetails>
                                </ExpansionPanel>
                            </Grid>
                        )}

                        <Grid item>
                            <FieldArray
                                name={`${field}.attributes`}
                                component={EditAttributeFormList}
                            />
                        </Grid>
                    </Grid>
                </ExpansionPanelDetails>
            </ExpansionPanel>
        );
    }
}

EditAttribute = withStyles(styles)(
    withI18N(injectIntl(EditAttribute), intlData)
);

class EditAttributeFormList extends Component {
    constructor(props) {
        super(props);

        this.newAttrCount = 1;

        ["onAddAttribute"].forEach(
            (methodName) => (this[methodName] = this[methodName].bind(this))
        );
    }

    newAttrName() {
        return formatMessage(this.props.intl, "newAttrName", {
            count: this.newAttrCount++,
        });
    }

    onAddAttribute() {
        const { form, name, unshift } = this.props;
        const attributes = getIn(form.values, name) || [];

        let newName = this.newAttrName();

        const namesMatch = (attr) => attr.name === newName;
        while (attributes.findIndex(namesMatch) > -1) {
            newName = this.newAttrName();
        }

        unshift({
            name: newName,
            description: "",
            type: constants.ATTRIBUTE_TYPE.STRING,
            required: false,
        });
    }

    onAttributeRemoved(index) {
        this.props.remove(index);
    }

    moveUp(index) {
        this.moveSelectedAttr(index, -1);
    }

    moveDown(index) {
        this.moveSelectedAttr(index, 1);
    }

    moveSelectedAttr(index, offset) {
        this.props.move(index, index + offset);
    }

    render() {
        const {
            classes,
            intl,
            name,
            form: { touched, errors, values, setFieldValue },
        } = this.props;

        const attributes = getIn(values, name);

        return (
            <ExpansionPanel
                defaultExpanded={attributes && attributes.length > 0}
            >
                <ExpansionPanelSummary
                    expandIcon={
                        <ExpandMoreIcon
                            id={build(name, ids.BUTTONS.EXPAND, ids.ATTR_GRID)}
                        />
                    }
                >
                    <div className={classes.actions}>
                        <Fab
                            size="small"
                            color="primary"
                            id={build(name, ids.BUTTONS.ADD)}
                            aria-label={formatMessage(intl, "addRow")}
                            onClick={(event) => {
                                event.stopPropagation();
                                this.onAddAttribute();
                            }}
                        >
                            <ContentAdd />
                        </Fab>
                    </div>
                    <Typography className={classes.title}>
                        {getMessage("attributes")}
                    </Typography>
                </ExpansionPanelSummary>
                <ExpansionPanelDetails>
                    <Grid
                        container
                        spacing={16}
                        direction="column"
                        justify="flex-start"
                        alignItems="stretch"
                    >
                        {attributes &&
                            attributes.map((attribute, index) => {
                                const attrFieldName = `${name}[${index}]`;
                                return (
                                    <Grid item key={attrFieldName}>
                                        <EditAttribute
                                            field={attrFieldName}
                                            setFieldValue={setFieldValue}
                                            touched={touched}
                                            errors={errors}
                                            attribute={attribute}
                                            onAddAttribute={this.onAddAttribute}
                                            onAttributeRemoved={() =>
                                                this.onAttributeRemoved(index)
                                            }
                                            moveUp={() => this.moveUp(index)}
                                            moveUpDisabled={index <= 0}
                                            moveDown={() =>
                                                this.moveDown(index)
                                            }
                                            moveDownDisabled={
                                                index < 0 ||
                                                attributes.length - 1 <= index
                                            }
                                        />
                                    </Grid>
                                );
                            })}
                    </Grid>
                </ExpansionPanelDetails>
            </ExpansionPanel>
        );
    }
}

EditAttributeFormList = withStyles(styles)(
    withI18N(injectIntl(EditAttributeFormList), intlData)
);

export default EditAttributeFormList;
