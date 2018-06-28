/**
 * @author psarando
 */
import React, { Component } from "react";

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
import TextField from "@material-ui/core/TextField";
import Typography from "@material-ui/core/Typography";
import { withStyles } from "@material-ui/core/styles";

import ContentRemove from "@material-ui/icons/Delete";
import ContentEdit from "@material-ui/icons/Edit";


class StringEditorDialog extends Component {
    constructor(props) {
        super(props);

        const { value } = props;
        this.state = { value };
    }

    componentWillReceiveProps(newProps) {
        const { value } = newProps;

        this.setState({ value });
    }

    render() {
        const { open, title, valueLabel, onSave, onClose } = this.props;
        const { value } = this.state;

        return (
            <Dialog
                open={open}
                disableBackdropClick
                disableEscapeKeyDown
                aria-labelledby="form-dialog-title"
            >
                <DialogTitle id="form-dialog-title">{title}</DialogTitle>
                <DialogContent>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="value"
                        label={valueLabel || "Value"}
                        value={value || ""}
                        onChange={event => this.setState({value: event.target.value})}
                        fullWidth
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={onClose} color="primary">
                        Cancel
                    </Button>
                    <Button onClick={() => onSave(value)} color="primary">
                        OK
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
        let values = this.props.values;

        const valuesMatch = v => (v === value);
        while (values.findIndex(valuesMatch) > -1) {
            value = this.newValue();
        }

        this.setState({selected: 0});

        this.props.onValuesChanged([
            value,
            ...values
        ]);
    };

    onValueRemoved = (index) => {
        let values = [...this.props.values];
        let { selected } = this.state;

        values.splice(index, 1);

        // fix selection
        if (index === selected) {
            selected = -1;
        } else if (index < selected) {
            selected--;
        }

        this.setState({selected});
        this.props.onValuesChanged(values);
    };

    onUpdateValue = (index, value) => {
        let values = [...this.props.values];

        values.splice(index, 1, value);

        this.props.onValuesChanged(values);
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
        let values = [...this.props.values];

        let [value] = values.splice(selected, 1);
        values.splice(selected + offset, 0, value);

        this.setState({selected: selected + offset});
        this.props.onValuesChanged(values);
    };

    render() {
        const { classes, title, helpLabel, columnLabel, values } = this.props;
        const { selected, editingIndex } = this.state;
        const editingValue = editingIndex >= 0 ? values[editingIndex] : "";

        return (
            <fieldset>
                <legend>{title}</legend>

                <Typography variant="subheading">{helpLabel}</Typography>

                <OrderedGridToolbar title={columnLabel}
                                    onAddItem={this.onAddValue}
                                    moveUp={this.moveUp}
                                    moveUpDisabled={selected <= 0}
                                    moveDown={this.moveDown}
                                    moveDownDisabled={selected < 0 || (values.length - 1) <= selected}
                />

                <div className={classes.attributeTableWrapper}>
                    <Table aria-labelledby="tableTitle">
                        <TableBody>
                            {values && values.map((value, index) => {
                                const isSelected = (index === selected);

                                return (
                                    <TableRow
                                        hover
                                        tabIndex={-1}
                                        key={value}
                                        selected={isSelected}
                                        onClick={() => this.handleSelect(index)}
                                    >
                                        <TableCell component="th" scope="row">
                                            {value}
                                        </TableCell>
                                        <TableCell padding="none">
                                            <IconButton aria-label="edit"
                                                        className={classes.button}
                                                        onClick={event => {
                                                            event.stopPropagation();
                                                            this.setState({editingIndex: index});
                                                        }}
                                            >
                                                <ContentEdit />
                                            </IconButton>
                                            <IconButton aria-label="delete"
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

                <StringEditorDialog open={editingIndex >= 0}
                                    title={title}
                                    valueLabel={columnLabel}
                                    value={editingValue}
                                    onClose={() => this.setState({editingIndex: -1})}
                                    onSave={(value, is_default) => {
                                        this.setState({editingIndex: -1});
                                        this.onUpdateValue(editingIndex, value, is_default);
                                    }}
                />

            </fieldset>
        );
    }
}

export default withStyles(styles)(StringListEditor);
