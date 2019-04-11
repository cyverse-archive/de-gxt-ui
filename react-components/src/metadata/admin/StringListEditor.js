/**
 * @author psarando
 */
import React, { Component } from "react";

import { FastField, getIn } from "formik";
import PropTypes from "prop-types";
import { injectIntl } from "react-intl";

import build from "../../util/DebugIDUtil";
import withI18N, { formatMessage, getMessage } from "../../util/I18NWrapper";
import intlData from "../messages";
import styles from "../style";
import ids from "./ids";

import { FormTextField } from "../../util/FormField";
import OrderedGridToolbar from "./OrderedGridToolbar";

import Button from "@material-ui/core/Button";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogTitle from "@material-ui/core/DialogTitle";
import IconButton from "@material-ui/core/IconButton";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableRow from "@material-ui/core/TableRow";
import Typography from "@material-ui/core/Typography";
import { withStyles } from "@material-ui/core/styles";

import ContentRemove from "@material-ui/icons/Delete";
import ContentEdit from "@material-ui/icons/Edit";

class StringEditorDialog extends Component {
    static propTypes = {
        onClose: PropTypes.func.isRequired,
    };

    render() {
        const {
            open,
            parentID,
            title,
            valueLabel,
            field,
            onClose,
        } = this.props;

        const fieldID = build(parentID, field);
        const dialogID = build(fieldID, ids.DIALOG);
        const dialogTitleID = build(dialogID, ids.TITLE);

        return (
            <Dialog
                open={open}
                disableBackdropClick
                disableEscapeKeyDown
                aria-labelledby={dialogTitleID}
            >
                <DialogTitle id={dialogTitleID}>{title}</DialogTitle>
                <DialogContent>
                    <FastField
                        name={field}
                        component={FormTextField}
                        id={fieldID}
                        label={valueLabel || getMessage("value")}
                        autoFocus
                    />
                </DialogContent>
                <DialogActions>
                    <Button
                        id={build(dialogID, ids.BUTTONS.CLOSE)}
                        color="primary"
                        onClick={onClose}
                    >
                        {getMessage("done")}
                    </Button>
                </DialogActions>
            </Dialog>
        );
    }
}

class StringListEditor extends Component {
    constructor(props) {
        super(props);

        this.newValueCount = 1;

        this.state = {
            selected: -1,
            editingIndex: -1,
        };

        ["onAddValue", "moveUp", "moveDown"].forEach(
            (methodName) => (this[methodName] = this[methodName].bind(this))
        );
    }

    newValue() {
        return formatMessage(this.props.intl, "newValue", {
            count: this.newValueCount++,
        });
    }

    onAddValue() {
        const { name, form } = this.props;
        const values = getIn(form.values, name) || [];

        let value = this.newValue();

        const valuesMatch = (v) => v === value;
        while (values.findIndex(valuesMatch) > -1) {
            value = this.newValue();
        }

        this.setState({ selected: 0 });

        this.props.unshift(value);
    }

    onValueRemoved(index) {
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
        this.moveSelectedValue(-1);
    }

    moveDown() {
        this.moveSelectedValue(1);
    }

    moveSelectedValue(offset) {
        const { selected } = this.state;

        this.setState({ selected: selected + offset });

        this.props.move(selected, selected + offset);
    }

    render() {
        const {
            classes,
            intl,
            parentID,
            title,
            helpLabel,
            columnLabel,
            name,
            form: { values },
        } = this.props;
        const { selected, editingIndex } = this.state;

        const listValues = getIn(values, name) || [];

        return (
            <fieldset>
                <legend>{title}</legend>

                <Typography variant="subtitle1">{helpLabel}</Typography>

                <OrderedGridToolbar
                    title={columnLabel}
                    parentID={parentID}
                    onAddItem={this.onAddValue}
                    moveUp={this.moveUp}
                    moveUpDisabled={selected <= 0}
                    moveDown={this.moveDown}
                    moveDownDisabled={
                        !listValues ||
                        selected < 0 ||
                        listValues.length - 1 <= selected
                    }
                />

                <div className={classes.attributeTableWrapper}>
                    <Table aria-labelledby={build(parentID, ids.TITLE)}>
                        <TableBody>
                            {listValues &&
                                listValues.map((value, index) => {
                                    const isSelected = index === selected;
                                    const field = `${name}[${index}]`;
                                    const rowID = build(
                                        ids.METADATA_TEMPLATE_FORM,
                                        field
                                    );

                                    return (
                                        <TableRow
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
                                                            editingIndex: index,
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
                                                        this.onValueRemoved(
                                                            index
                                                        );
                                                    }}
                                                >
                                                    <ContentRemove />
                                                </IconButton>
                                            </TableCell>
                                        </TableRow>
                                    );
                                })}
                        </TableBody>
                    </Table>
                </div>

                {listValues &&
                    listValues.map((value, index) => {
                        const field = `${name}[${index}]`;

                        return (
                            <StringEditorDialog
                                key={field}
                                open={editingIndex === index}
                                parentID={parentID}
                                field={field}
                                title={title}
                                valueLabel={columnLabel}
                                onClose={() =>
                                    this.setState({ editingIndex: -1 })
                                }
                            />
                        );
                    })}
            </fieldset>
        );
    }
}

export default withStyles(styles)(
    withI18N(injectIntl(StringListEditor), intlData)
);
