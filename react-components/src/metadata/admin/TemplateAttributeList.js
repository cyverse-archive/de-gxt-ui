/**
 * @author psarando
 */
import React, { Component } from "react";
import { Field } from "redux-form";
import { injectIntl } from "react-intl";

import withI18N, { getMessage, formatMessage } from "../../util/I18NWrapper";
import { FormCheckboxTableCell } from "../../util/FormField";
import intlData from "../messages";
import styles from "../style";
import OrderedGridToolbar from "./OrderedGridToolbar";

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
        label: getMessage("attribute"),
        component: "th",
        scope: "row",
    },
    { id: "description", label: getMessage("description") },
    { id: "type", label: getMessage("attrTypeLabel"), padding: "none" },
    { id: "attributes", label: getMessage("attrChildrenLabel"), padding: "none", numeric: true },
    { id: "required", label: getMessage("required"), padding: "checkbox" },
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
        this.moveSelectedAttr(-1);
    };

    moveDown = () => {
        this.moveSelectedAttr(1);
    };

    moveSelectedAttr = (offset) => {
        const { selected } = this.state;

        this.setState({selected: selected + offset});

        this.props.fields.move(selected, selected + offset);
    };

    render() {
        const { classes, intl, fields, meta: { error } } = this.props;
        const { selected } = this.state;

        return (
            <div className={classes.attributeTableContainer}>

                <OrderedGridToolbar title={getMessage("attributes")}
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
                                        className={error && error[index] ? classes.attributeTableRowError : null}
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
                                            <IconButton aria-label={formatMessage(intl, "edit")}
                                                        className={classes.button}
                                                        onClick={event => {
                                                            event.stopPropagation();
                                                            this.props.onEditAttr(index);
                                                        }}
                                            >
                                                <ContentEdit />
                                            </IconButton>
                                            <IconButton aria-label={formatMessage(intl, "delete")}
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

export default withStyles(styles)(withI18N(injectIntl(TemplateAttributeList), intlData));
