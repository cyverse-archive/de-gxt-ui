/**
 * @author psarando
 */
import React, { Component } from "react";
import { FastField, Field, getIn } from "formik";
import PropTypes from "prop-types";
import { injectIntl } from "react-intl";

import intlData from "../messages";
import styles from "../style";
import ids from "./ids";

import {
    build,
    DETableRow,
    FormCheckbox,
    FormCheckboxTableCell,
    formatMessage,
    getMessage,
    FormTextField,
    getFormError,
    withI18N,
} from "@cyverse-de/ui-lib";
import OrderedGridToolbar from "./OrderedGridToolbar";

import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    IconButton,
    Table,
    TableBody,
    TableCell,
    TableHead,
    withStyles,
} from "@material-ui/core";

import {
    Delete as ContentRemove,
    Edit as ContentEdit,
} from "@material-ui/icons";

const normalizeDefault = (enumValues, replace) => (event) => {
    event.stopPropagation();

    const is_default = event.target.checked;

    if (is_default) {
        enumValues.forEach((attrEnum, index) => {
            if (attrEnum.is_default) {
                replace(index, { ...attrEnum, is_default: false });
            }
        });
    }

    return is_default;
};

class AttributeEnumEditDialog extends Component {
    static propTypes = {
        onClose: PropTypes.func.isRequired,
    };

    render() {
        const {
            open,
            intl,
            field,
            error,
            replace,
            enumValues,
            onClose,
        } = this.props;

        const dialogID = build(ids.METADATA_TEMPLATE_FORM, field, ids.DIALOG);
        const dialogTitleID = build(dialogID, ids.TITLE);

        return (
            <Dialog
                open={open}
                disableBackdropClick
                disableEscapeKeyDown
                aria-labelledby={dialogTitleID}
            >
                <DialogTitle id={dialogTitleID}>
                    {getMessage("dialogTitleEditEnumValue")}
                </DialogTitle>
                <DialogContent>
                    <FastField
                        name={`${field}.value`}
                        label={formatMessage(intl, "value")}
                        id={build(dialogID, ids.ENUM_VALUE)}
                        required={true}
                        autoFocus
                        component={FormTextField}
                    />
                    <Field
                        name={`${field}.is_default`}
                        label={formatMessage(intl, "enumDefaultLabel")}
                        id={build(dialogID, ids.ENUM_VALUE_DEFAULT)}
                        color="primary"
                        component={FormCheckbox}
                        onClick={normalizeDefault(enumValues, replace)}
                    />
                </DialogContent>
                <DialogActions>
                    <Button
                        id={build(dialogID, ids.BUTTONS.CLOSE)}
                        color="primary"
                        disabled={!!error}
                        onClick={onClose}
                    >
                        {getMessage("done")}
                    </Button>
                </DialogActions>
            </Dialog>
        );
    }
}

class AttributeEnumEditGrid extends Component {
    constructor(props) {
        super(props);

        this.newEnumCount = 1;

        this.state = {
            selected: -1,
            editingEnumIndex: -1,
        };

        ["onAddEnum", "moveUp", "moveDown"].forEach(
            (methodName) => (this[methodName] = this[methodName].bind(this))
        );
    }

    newEnumValue() {
        return formatMessage(this.props.intl, "newValue", {
            count: this.newEnumCount++,
        });
    }

    onAddEnum() {
        const { name, form } = this.props;
        const attributeEnums = getIn(form.values, name) || [];

        let value = this.newEnumValue();

        const valuesMatch = (attrEnum) => attrEnum.value === value;
        while (attributeEnums.findIndex(valuesMatch) > -1) {
            value = this.newEnumValue();
        }

        this.setState({ selected: 0 });

        this.props.unshift({
            value,
            is_default: false,
        });
    }

    onEnumRemoved(index) {
        let { selected } = this.state;

        // fix selection
        if (index === selected) {
            selected = -1;
        } else if (index < selected) {
            selected--;
        }

        this.setState({ selected });

        this.props.remove(index);
    }

    handleSelect(index) {
        const { selected } = this.state;
        this.setState({ selected: selected === index ? -1 : index });
    }

    moveUp() {
        this.moveSelectedEnum(-1);
    }

    moveDown() {
        this.moveSelectedEnum(1);
    }

    moveSelectedEnum(offset) {
        const { selected } = this.state;

        this.setState({ selected: selected + offset });

        this.props.move(selected, selected + offset);
    }

    render() {
        const {
            classes,
            intl,
            parentID,
            name,
            replace,
            form: { touched, errors, values },
        } = this.props;
        const { selected, editingEnumIndex } = this.state;
        const attributeEnums = getIn(values, name);
        const attrErrors = getIn(errors, name);
        const error = Array.isArray(attrErrors) ? null : attrErrors;

        const tableID = build(parentID, ids.ENUM_VALUES_GRID);

        return (
            <div className={classes.attributeTableContainer}>
                <OrderedGridToolbar
                    title={getMessage("enumValues")}
                    error={error}
                    parentID={tableID}
                    onAddItem={this.onAddEnum}
                    moveUp={this.moveUp}
                    moveUpDisabled={selected <= 0}
                    moveDown={this.moveDown}
                    moveDownDisabled={
                        !attributeEnums ||
                        selected < 0 ||
                        attributeEnums.length - 1 <= selected
                    }
                />

                <div className={classes.attributeTableWrapper}>
                    <Table aria-labelledby={build(tableID, ids.TITLE)}>
                        <TableBody>
                            {attributeEnums &&
                                attributeEnums.map(({ value }, index) => {
                                    const isSelected = index === selected;
                                    const field = `${name}[${index}]`;

                                    const rowID = build(
                                        ids.METADATA_TEMPLATE_FORM,
                                        field
                                    );

                                    return (
                                        <DETableRow
                                            hover
                                            tabIndex={-1}
                                            key={field}
                                            selected={isSelected}
                                            onClick={() =>
                                                this.handleSelect(index)
                                            }
                                        >
                                            <TableCell
                                                component="th"
                                                scope="row"
                                            >
                                                {value}
                                            </TableCell>
                                            <Field
                                                name={`${field}.is_default`}
                                                id={build(
                                                    rowID,
                                                    ids.ENUM_VALUE_DEFAULT
                                                )}
                                                component={
                                                    FormCheckboxTableCell
                                                }
                                                onClick={normalizeDefault(
                                                    attributeEnums,
                                                    replace
                                                )}
                                            />
                                            <TableCell padding="none">
                                                <IconButton
                                                    id={build(
                                                        rowID,
                                                        ids.BUTTONS.EDIT
                                                    )}
                                                    aria-label={formatMessage(
                                                        intl,
                                                        "edit"
                                                    )}
                                                    className={classes.button}
                                                    onClick={(event) => {
                                                        event.stopPropagation();
                                                        this.setState({
                                                            editingEnumIndex: index,
                                                        });
                                                    }}
                                                >
                                                    <ContentEdit />
                                                </IconButton>
                                                <IconButton
                                                    id={build(
                                                        rowID,
                                                        ids.BUTTONS.DELETE
                                                    )}
                                                    aria-label={formatMessage(
                                                        intl,
                                                        "delete"
                                                    )}
                                                    classes={{
                                                        root:
                                                            classes.deleteIcon,
                                                    }}
                                                    onClick={(event) => {
                                                        event.stopPropagation();
                                                        this.onEnumRemoved(
                                                            index
                                                        );
                                                    }}
                                                >
                                                    <ContentRemove />
                                                </IconButton>
                                            </TableCell>
                                        </DETableRow>
                                    );
                                })}
                        </TableBody>
                        <TableHead className={classes.tableHead}>
                            <DETableRow>
                                <TableCell component="th" scope="row">
                                    {getMessage("value")}
                                </TableCell>
                                <TableCell padding="checkbox">
                                    {getMessage("default")}
                                </TableCell>
                                <TableCell padding="none" />
                            </DETableRow>
                        </TableHead>
                    </Table>
                </div>

                {attributeEnums &&
                    attributeEnums.map((value, index) => {
                        const field = `${name}[${index}]`;
                        const attrEnumError = getFormError(
                            field,
                            touched,
                            errors
                        );
                        const error = attrEnumError && attrEnumError.error;

                        return (
                            <AttributeEnumEditDialog
                                key={field}
                                open={editingEnumIndex === index}
                                field={field}
                                error={error}
                                replace={replace}
                                enumValues={attributeEnums}
                                intl={intl}
                                onClose={() =>
                                    this.setState({ editingEnumIndex: -1 })
                                }
                            />
                        );
                    })}
            </div>
        );
    }
}

export default withStyles(styles)(
    withI18N(injectIntl(AttributeEnumEditGrid), intlData)
);
