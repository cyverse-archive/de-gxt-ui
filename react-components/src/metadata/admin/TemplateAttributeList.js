/**
 * @author psarando
 */
import React, { Component } from "react";

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
        let name = `New attribute ${this.newAttrCount++}`;
        let attributes = this.props.attributes;

        const namesMatch = attr => (attr.name === name);
        while (attributes.findIndex(namesMatch) > -1) {
            name = `New attribute ${this.newAttrCount++}`;
        }

        this.setState({
            selected: 0,
        });

        this.props.onAttributesChanged([
            {
                name: name,
                description: "",
                type: "String",
                required: false,
            },
            ...attributes
        ]);
    };

    onAttributeRemoved = (index) => {
        let { attributes } = this.props;
        let { selected } = this.state;

        attributes = [...attributes];
        attributes.splice(index, 1);

        // fix selection
        if (index === selected) {
            selected = -1;
        } else if (index < selected) {
            selected--;
        }

        this.setState({selected});
        this.props.onAttributesChanged(attributes);
    };

    onRequiredChecked = (index, attr, checked) => {
        this.props.onAttributeUpdated(index, {
            ...attr,
            required: checked,
        });
    };

    handleSelect = (index) => {
        const { selected } = this.state;
        this.setState({ selected: selected === index ? -1 : index });
    };

    moveUp = () => {
        const { selected } = this.state;
        let attributes = [...this.props.attributes];

        let [attr] = attributes.splice(selected, 1);
        attributes.splice(selected - 1, 0, attr);

        this.setState({selected: selected - 1});
        this.props.onAttributesChanged(attributes);
    };

    moveDown = () => {
        const { selected } = this.state;
        let attributes = [...this.props.attributes];

        let [attr] = attributes.splice(selected, 1);
        attributes.splice(selected + 1, 0, attr);

        this.setState({selected: selected + 1});
        this.props.onAttributesChanged(attributes);
    };

    render() {
        const { classes, attributes } = this.props;
        const { selected } = this.state;

        return (
            <div className={classes.attributeTableContainer}>

                <OrderedGridToolbar title="Attributes"
                                    onAddItem={this.onAddAttribute}
                                    moveUp={this.moveUp}
                                    moveUpDisabled={selected <= 0}
                                    moveDown={this.moveDown}
                                    moveDownDisabled={selected < 0 || (attributes.length - 1) <= selected}
                />

                <div className={classes.attributeTableWrapper}>
                    <Table aria-labelledby="tableTitle">
                        <TableBody>
                            {attributes && attributes.map((attribute, index) => {
                                const isSelected = index === selected;
                                const {
                                    name,
                                    description,
                                    type,
                                    required,
                                    attributes,
                                } = attribute;

                                return (
                                    <TableRow
                                        hover
                                        tabIndex={-1}
                                        key={name}
                                        selected={isSelected}
                                        onClick={() => this.handleSelect(index)}
                                    >
                                        <TableCell component="th" scope="row">
                                            {name}
                                        </TableCell>
                                        <TableCell>{description}</TableCell>
                                        <TableCell padding="none">{type}</TableCell>
                                        <TableCell padding="none" numeric>{attributes ? attributes.length : 0}</TableCell>
                                        <TableCell padding="checkbox">
                                            <Checkbox color="primary"
                                                      checked={required}
                                                      onClick={event => event.stopPropagation()}
                                                      onChange={(event, checked) => {
                                                          this.onRequiredChecked(index, attribute, checked);
                                                      }}
                                            />
                                        </TableCell>
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
