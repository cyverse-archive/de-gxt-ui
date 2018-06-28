/**
 * @author psarando
 */
import React, { Component } from "react";

import styles from "../style";
import OrderedGridToolbar from "./OrderedGridToolbar";

import Button from "@material-ui/core/Button";
import Checkbox from "@material-ui/core/Checkbox";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogTitle from "@material-ui/core/DialogTitle";
import IconButton from "@material-ui/core/IconButton";
import InputLabel from "@material-ui/core/InputLabel";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TextField from "@material-ui/core/TextField";
import { withStyles } from "@material-ui/core/styles";

import ContentRemove from "@material-ui/icons/Delete";
import ContentEdit from "@material-ui/icons/Edit";

class AttributeEnumEditDialog extends Component {
    constructor(props) {
        super(props);

        const { value, is_default } = props;

        this.state = {
            value,
            is_default,
        };
    }

    componentWillReceiveProps(newProps) {
        const { value, is_default } = newProps;

        this.setState({
            value,
            is_default,
        });
    }

    render() {
        const { open, onSave, onClose } = this.props;
        const { value, is_default } = this.state;

        return (
            <Dialog
                open={open}
                disableBackdropClick
                disableEscapeKeyDown
                aria-labelledby="form-dialog-title"
            >
                <DialogTitle id="form-dialog-title">Edit Enum Selection</DialogTitle>
                <DialogContent>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="value"
                        label="Value"
                        value={value || ""}
                        onChange={event => this.setState({value: event.target.value})}
                        fullWidth
                    />
                    <Checkbox id="isDefault"
                              color="primary"
                              checked={!!is_default}
                              onChange={(event, checked) => this.setState({is_default: checked})}
                    />
                    <InputLabel htmlFor="isDefault">Default Selection?</InputLabel>
                </DialogContent>
                <DialogActions>
                    <Button onClick={onClose} color="primary">
                        Cancel
                    </Button>
                    <Button onClick={() => onSave(value, is_default)} color="primary">
                        OK
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
    }

    newEnumValue = () => `New value ${this.newEnumCount++}`;

    onAddEnum = () => {
        let value = this.newEnumValue();
        let attributeEnums = this.props.values;

        const valuesMatch = attrEnum => (attrEnum.value === value);
        while (attributeEnums.findIndex(valuesMatch) > -1) {
            value = this.newEnumValue();
        }

        this.setState({selected: 0});

        this.props.onValuesChanged([
            {
                value,
                is_default: false,
            },
            ...attributeEnums
        ]);
    };

    onEnumRemoved = (index) => {
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

    handleChange = (index, value, is_default) => {
        let values = [...this.props.values];
        const attrEnum = values[index];

        if (is_default) {
            let currentDefaultIndex = values.findIndex(value => value.is_default);

            if (currentDefaultIndex >= 0) {
                let currentDefault = values[currentDefaultIndex];
                values.splice(currentDefaultIndex, 1, {...currentDefault, is_default: false});
            }
        }

        values.splice(index, 1, {...attrEnum, value, is_default});

        this.props.onValuesChanged(values);
    };

    handleSelect = (index) => {
        const { selected } = this.state;
        this.setState({ selected: selected === index ? -1 : index });
    };

    moveUp = () => {
        this.moveSelectedEnum(-1);
    };

    moveDown = () => {
        this.moveSelectedEnum(1);
    };

    moveSelectedEnum = (offset) => {
        const { selected } = this.state;
        let values = [...this.props.values];

        let [attrEnum] = values.splice(selected, 1);
        values.splice(selected + offset, 0, attrEnum);

        this.setState({selected: selected + offset});
        this.props.onValuesChanged(values);
    };

    render() {
        const { classes, values } = this.props;
        const { selected, editingEnumIndex } = this.state;
        const editingEnum = editingEnumIndex >= 0 ? values[editingEnumIndex] : {};

        return (
            <div className={classes.attributeTableContainer}>

                <OrderedGridToolbar title="Values"
                                    onAddItem={this.onAddEnum}
                                    moveUp={this.moveUp}
                                    moveUpDisabled={selected <= 0}
                                    moveDown={this.moveDown}
                                    moveDownDisabled={selected < 0 || (values.length - 1) <= selected}
                />

                <div className={classes.attributeTableWrapper}>
                    <Table aria-labelledby="tableTitle">
                        <TableBody>
                            {values && values.map((attrEnum, index) => {
                                const isSelected = (index === selected);
                                const {
                                    value,
                                    is_default,
                                } = attrEnum;

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
                                        <TableCell padding="checkbox">
                                            <Checkbox color="primary"
                                                      checked={is_default}
                                                      onChange={(event, checked) => {
                                                          this.handleChange(index, value, checked);
                                                      }}
                                                      onClick={event => event.stopPropagation()}
                                            />
                                        </TableCell>
                                        <TableCell padding="none">
                                            <IconButton aria-label="edit"
                                                        className={classes.button}
                                                        onClick={event => {
                                                            event.stopPropagation();
                                                            this.setState({editingEnumIndex: index});
                                                        }}
                                            >
                                                <ContentEdit />
                                            </IconButton>
                                            <IconButton aria-label="delete"
                                                        classes={{root: classes.deleteIcon}}
                                                        onClick={event => {
                                                            event.stopPropagation();
                                                            this.onEnumRemoved(index);
                                                        }}
                                            >
                                                <ContentRemove />
                                            </IconButton>
                                        </TableCell>
                                    </TableRow>
                                );
                            })}
                        </TableBody>
                        <TableHead className={classes.tableHead}>
                            <TableRow>
                                <TableCell component="th" scope="row">Value</TableCell>
                                <TableCell padding="checkbox">Default?</TableCell>
                                <TableCell padding="none" />
                            </TableRow>
                        </TableHead>
                    </Table>
                </div>

                <AttributeEnumEditDialog open={editingEnumIndex >= 0}
                                         value={editingEnum.value}
                                         is_default={editingEnum.is_default}
                                         onClose={() => this.setState({editingEnumIndex: -1})}
                                         onSave={(value, is_default) => {
                                             this.setState({editingEnumIndex: -1});
                                             this.handleChange(editingEnumIndex, value, is_default);
                                         }}
                />

            </div>
        );
    }
}

export default withStyles(styles)(AttributeEnumEditGrid);
