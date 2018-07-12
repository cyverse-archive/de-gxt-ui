/**
 * @author psarando
 */
import React, { Component } from "react";
import { Field } from "redux-form";
import { injectIntl } from "react-intl";

import withI18N, { getMessage, formatMessage } from "../../util/I18NWrapper";
import intlData from "../messages";
import styles from "../style";
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
import { FormTextField } from "../../util/FormField";


class StringEditorDialog extends Component {
    render() {
        const { open, title, valueLabel, field, onClose } = this.props;

        return (
            <Dialog
                open={open}
                disableBackdropClick
                disableEscapeKeyDown
                aria-labelledby="form-dialog-title"
            >
                <DialogTitle id="form-dialog-title">{title}</DialogTitle>
                <DialogContent>
                    <Field name={field}
                           component={FormTextField}
                           id="value"
                           label={valueLabel || getMessage("value")}
                           margin="dense"
                           autoFocus
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={onClose} color="primary">
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
    }

    newValue = () => `New value ${this.newValueCount++}`;

    onAddValue = () => {
        let value = this.newValue();
        const fields = this.props.fields;
        const values = fields.getAll() || [];

        const valuesMatch = v => (v === value);
        while (values.findIndex(valuesMatch) > -1) {
            value = this.newValue();
        }

        this.setState({selected: 0});

        this.props.fields.unshift(value);
    };

    onValueRemoved = (index) => {
        let { selected } = this.state;

        // fix selection
        if (index === selected) {
            selected = -1;
        } else if (index < selected) {
            selected--;
        }

        this.setState({selected});

        this.props.fields.remove(index);
    };

    handleSelect = (index) => {
        const { selected } = this.state;
        this.setState({ selected: selected === index ? -1 : index });
    };

    moveUp = () => {
        this.moveSelectedValue(-1);
    };

    moveDown = () => {
        this.moveSelectedValue(1);
    };

    moveSelectedValue = (offset) => {
        const { selected } = this.state;

        this.setState({selected: selected + offset});

        this.props.fields.move(selected, selected + offset);
    };

    render() {
        const { classes, intl, title, helpLabel, columnLabel, fields } = this.props;
        const { selected, editingIndex } = this.state;

        return (
            <fieldset>
                <legend>{title}</legend>

                <Typography variant="subheading">{helpLabel}</Typography>

                <OrderedGridToolbar title={columnLabel}
                                    onAddItem={this.onAddValue}
                                    moveUp={this.moveUp}
                                    moveUpDisabled={selected <= 0}
                                    moveDown={this.moveDown}
                                    moveDownDisabled={selected < 0 || (fields.length - 1) <= selected}
                />

                <div className={classes.attributeTableWrapper}>
                    <Table aria-labelledby="tableTitle">
                        <TableBody>
                            {fields && fields.map((field, index) => {
                                const isSelected = (index === selected);
                                const value = fields.get(index);

                                return (
                                    <TableRow
                                        hover
                                        tabIndex={-1}
                                        key={field}
                                        selected={isSelected}
                                        onClick={() => this.handleSelect(index)}
                                    >
                                        <TableCell component="th" scope="row">
                                            {value}
                                        </TableCell>
                                        <TableCell padding="none">
                                            <IconButton aria-label={formatMessage(intl, "edit")}
                                                        className={classes.button}
                                                        onClick={event => {
                                                            event.stopPropagation();
                                                            this.setState({editingIndex: index});
                                                        }}
                                            >
                                                <ContentEdit />
                                            </IconButton>
                                            <IconButton aria-label={formatMessage(intl, "delete")}
                                                        classes={{root: classes.deleteIcon}}
                                                        onClick={event => {
                                                            event.stopPropagation();
                                                            this.onValueRemoved(index);
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

                {fields && fields.map((field, index) =>
                    <StringEditorDialog key={field}
                                        open={editingIndex === index}
                                        field={field}
                                        title={title}
                                        valueLabel={columnLabel}
                                        onClose={() => this.setState({editingIndex: -1})}
                    />
                )}

            </fieldset>
        );
    }
}

export default withStyles(styles)(withI18N(injectIntl(StringListEditor), intlData));
