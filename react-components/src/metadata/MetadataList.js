/**
 * @author psarando
 */
import React, { Component } from "react";
import { injectIntl } from "react-intl";

import build from "../util/DebugIDUtil";
import withI18N, { getMessage, formatMessage } from "../util/I18NWrapper";
import ids from "./ids";
import intlData from "./messages";
import styles from "./style";

import Button from "@material-ui/core/Button";
import IconButton from "@material-ui/core/IconButton";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableSortLabel from "@material-ui/core/TableSortLabel";
import Toolbar from "@material-ui/core/Toolbar";
import Tooltip from "@material-ui/core/Tooltip";
import Typography from "@material-ui/core/Typography";
import { withStyles } from "@material-ui/core/styles";

import ContentAdd from "@material-ui/icons/Add";
import ContentRemove from "@material-ui/icons/Delete";
import ContentEdit from "@material-ui/icons/Edit";

let MetadataGridToolbar = props => {
    const { parentID, classes, intl, onAddAVU } = props;

    return (
        <Toolbar
            className={classes.root}
        >
            <div className={classes.actions}>
                <Button id={build(parentID, ids.BUTTONS.ADD)}
                        variant="fab"
                        mini
                        color="primary"
                        aria-label={formatMessage(intl, "addMetadata")}
                        onClick={onAddAVU}
                >
                    <ContentAdd />
                </Button>
            </div>
            <div className={classes.title}>
                <Typography id={build(parentID, ids.TITLE)} variant="title">
                    {getMessage("avus")}
                </Typography>
            </div>
        </Toolbar>
    );
};

MetadataGridToolbar = withStyles(styles)(withI18N(injectIntl(MetadataGridToolbar), intlData));

const columnData = [
    {
        id: "attr",
        label: getMessage("attribute"),
        component: "th",
        scope: "row",
    },
    { id: "value", label: getMessage("value") },
    { id: "unit", label: getMessage("metadataUnitLabel") },
    { id: "avus", label: getMessage("metadataChildrenLabel"), padding: "none", numeric: true },
];

class MetadataGridHeader extends React.Component {
    createSortHandler = property => event => {
        this.props.onRequestSort(event, property);
    };

    render() {
        const { parentID, classes, order, orderBy } = this.props;

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
                                sortDirection={orderBy === column.id ? order : false}
                            >
                                <Tooltip
                                    title={getMessage("sort")}
                                    placement="bottom-start"
                                    enterDelay={300}
                                >
                                    <TableSortLabel
                                        id={build(parentID, ids.COL_HEADER, column.id)}
                                        active={orderBy === column.id}
                                        direction={order}
                                        onClick={this.createSortHandler(column.id)}
                                    >
                                        {column.label}
                                    </TableSortLabel>
                                </Tooltip>
                            </TableCell>
                        );
                    }, this)}
                    <TableCell padding="none" />
                </TableRow>
            </TableHead>
        );
    }
}

MetadataGridHeader = withStyles(styles)(MetadataGridHeader);

class MetadataList extends Component {
    constructor(props) {
        super(props);

        this.newAttrCount = 1;

        this.state = {
            orderBy: props.orderBy,
            order: props.order || "asc",
        };
    }

    newAttrName = () => `New attribute ${this.newAttrCount++}`;

    onAddAVU = () => {
        const fields = this.props.fields;
        const avus = fields.getAll() || [];

        let name = this.newAttrName();

        const namesMatch = avu => (avu.attr === name);
        while (avus.findIndex(namesMatch) > -1) {
            name = this.newAttrName();
        }

        fields.unshift({
            attr: name,
            value: "",
            unit: "",
        });
    };

    handleRequestSort = (event, property) => {
        const orderBy = property;
        let order = 'asc';

        if (this.state.orderBy === property && this.state.order === order) {
            order = 'desc';
        }

        const fields = this.props.fields;
        const avus = [...fields.getAll()];

        const comparator = (a, b) => (a < b ? -1 : 1);
        avus.sort((a, b) => {
            let aVal = a[orderBy];
            let bVal = b[orderBy];

            if (orderBy === "avus") {
                aVal = aVal ? aVal.length : 0;
                bVal = bVal ? bVal.length : 0;
            }

            return order === 'desc' ? comparator(bVal, aVal) : comparator(aVal, bVal);
        });

        this.setState({ order, orderBy });

        this.props.change(fields.name, avus);
    };

    render() {
        const { parentID, classes, intl, fields } = this.props;
        const { order, orderBy } = this.state;

        const tableID = build(parentID, ids.AVU_GRID);

        return (
            <div className={classes.metadataTemplateContainer}>

                <MetadataGridToolbar parentID={tableID} onAddAVU={this.onAddAVU} />

                <div className={classes.tableWrapper}>
                    <Table aria-labelledby={build(tableID, ids.TITLE)}>
                        <TableBody>
                            {fields && fields.map((field, index) => {
                                const {
                                    attr,
                                    value,
                                    unit,
                                    avus,
                                } = fields.get(index);

                                const rowID = build(ids.EDIT_METADATA_FORM, field);

                                return (
                                    <TableRow
                                        hover
                                        tabIndex={-1}
                                        key={field}
                                    >
                                        <TableCell component="th" scope="row">
                                            {attr}
                                        </TableCell>
                                        <TableCell>{value}</TableCell>
                                        <TableCell>{unit}</TableCell>
                                        <TableCell padding="none" numeric>{avus ? avus.length : 0}</TableCell>
                                        <TableCell padding="none">
                                            <IconButton id={build(rowID, ids.BUTTONS.EDIT)}
                                                        aria-label={formatMessage(intl, "edit")}
                                                        className={classes.button}
                                                        onClick={event => {
                                                            event.stopPropagation();
                                                            this.props.onEditAVU(index);
                                                        }}
                                            >
                                                <ContentEdit />
                                            </IconButton>
                                            <IconButton id={build(rowID, ids.BUTTONS.DELETE)}
                                                        aria-label={formatMessage(intl, "delete")}
                                                        classes={{root: classes.deleteIcon}}
                                                        onClick={event => {
                                                            event.stopPropagation();
                                                            this.props.fields.remove(index);
                                                        }}
                                            >
                                                <ContentRemove />
                                            </IconButton>
                                        </TableCell>
                                    </TableRow>
                                );
                            })}
                        </TableBody>
                        <MetadataGridHeader
                            parentID={tableID}
                            order={order}
                            orderBy={orderBy}
                            onRequestSort={this.handleRequestSort}
                            rowCount={fields.length}
                        />
                    </Table>
                </div>
            </div>
        );
    }
}

export default withStyles(styles)(withI18N(injectIntl(MetadataList), intlData));
