/**
 * @author psarando
 */
import React, { Component } from "react";
import { Field } from "redux-form";

import styles from "../style";
import OrderedGridToolbar from "./OrderedGridToolbar";

import Checkbox from "@material-ui/core/Checkbox";
import IconButton from "@material-ui/core/IconButton";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import { withStyles } from "@material-ui/core/styles";

import ContentRemove from "@material-ui/icons/Delete";
import ContentEdit from "@material-ui/icons/Edit";

const FormCheckboxTableCell = ({ input, ...custom }) => (
    <TableCell padding="checkbox">
        <Checkbox color="primary"
                  checked={!!input.value}
                  onClick={event => event.stopPropagation()}
                  onChange={input.onChange}
                  {...custom}
        />
    </TableCell>
);

const columnData = [
    {
        id: "name",
        label: "Attribute",
        component: "th",
        scope: "row",
    },
    { id: "description", label: "Description" },
    { id: "type", label: "Type", padding: "none" },
    { id: "attributes", label: "Child Attributes", padding: "none", numeric: true },
    { id: "required", label: "Required", padding: "checkbox" },
];

class AttributeGridHeader extends Component {
    render() {
        const { classes } = this.props;

        return (
            <TableHead className={classes.tableHead}>
                <TableRow>
                    {columnData.map(column => {
                        return (
                            <TableCell
                                component={column.component}
                                scope={column.scope}
                                padding={column.padding ? column.padding : "default"}
                                numeric={column.numeric}
                                key={column.id}
                            >
                                {column.label}
                            </TableCell>
                        );
                    }, this)}
                    <TableCell padding="none" />
                </TableRow>
            </TableHead>
        );
    }
}

AttributeGridHeader = withStyles(styles)(AttributeGridHeader);

class TemplateAttributeList extends Component {
    constructor(props) {
        super(props);

        this.newAttrCount = 1;

        this.state = {
            selected: -1,
        };
    }

    onAddAttribute = () => {
        const fields = this.props.fields;
        const attributes = fields.getAll() || [];

        let name = `New attribute ${this.newAttrCount++}`;

        const namesMatch = attr => (attr.name === name);
        while (attributes.findIndex(namesMatch) > -1) {
            name = `New attribute ${this.newAttrCount++}`;
        }

        this.setState({
            selected: 0,
        });

        fields.unshift({
            name: name,
            description: "",
            type: "String",
            required: false,
        });
    };

    onAttributeRemoved = (index) => {
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
        const { selected } = this.state;

        this.setState({selected: selected - 1});

        this.props.fields.move(selected, selected - 1);
    };

    moveDown = () => {
        const { selected } = this.state;

        this.setState({selected: selected + 1});

        this.props.fields.move(selected, selected + 1);
    };

    render() {
        const { classes, fields } = this.props;
        const { selected } = this.state;

        return (
            <div className={classes.attributeTableContainer}>

                <OrderedGridToolbar title="Attributes"
                                    onAddItem={this.onAddAttribute}
                                    moveUp={this.moveUp}
                                    moveUpDisabled={selected <= 0}
                                    moveDown={this.moveDown}
                                    moveDownDisabled={selected < 0 || (fields.length - 1) <= selected}
                />

                <div className={classes.attributeTableWrapper}>
                    <Table aria-labelledby="tableTitle">
                        <TableBody>
                            {fields && fields.map((field, index) => {
                                const isSelected = index === selected;
                                const {
                                    name,
                                    description,
                                    type,
                                    attributes,
                                } = fields.get(index);

                                return (
                                    <TableRow
                                        hover
                                        tabIndex={-1}
                                        key={field}
                                        selected={isSelected}
                                        onClick={() => this.handleSelect(index)}
                                    >
                                        <TableCell component="th" scope="row">
                                            {name}
                                        </TableCell>
                                        <TableCell>{description}</TableCell>
                                        <TableCell padding="none">{type}</TableCell>
                                        <TableCell padding="none" numeric>{attributes ? attributes.length : 0}</TableCell>
                                        <Field name={`${field}.required`}
                                               component={FormCheckboxTableCell}
                                        />
                                        <TableCell padding="none">
                                            <IconButton aria-label="edit"
                                                        className={classes.button}
                                                        onClick={event => {
                                                            event.stopPropagation();
                                                            this.props.onEditAttr(index);
                                                        }}
                                            >
                                                <ContentEdit />
                                            </IconButton>
                                            <IconButton aria-label="delete"
                                                        classes={{root: classes.deleteIcon}}
                                                        onClick={event => {
                                                            event.stopPropagation();
                                                            this.onAttributeRemoved(index);
                                                        }}
                                            >
                                                <ContentRemove />
                                            </IconButton>
                                        </TableCell>
                                    </TableRow>
                                );
                            })}
                        </TableBody>
                        <AttributeGridHeader/>
                    </Table>
                </div>
            </div>
        );
    }
}

export default withStyles(styles)(TemplateAttributeList);
