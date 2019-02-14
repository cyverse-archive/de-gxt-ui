/**
 * @author psarando
 */
import React, { Component } from "react";

import { getIn } from 'formik';
import PropTypes from "prop-types";
import { injectIntl } from "react-intl";

import build from "../util/DebugIDUtil";
import withI18N, { formatMessage, getMessage } from "../util/I18NWrapper";
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
import ContentView from "@material-ui/icons/List";

class MetadataGridToolbar extends Component {
    static propTypes = {
        onAddAVU: PropTypes.func.isRequired,
    };

    render() {
        const { parentID, editable, classes, intl, onAddAVU } = this.props;

        return (
            <Toolbar
                className={classes.root}
            >
                {editable &&
                <div className={classes.actions}>
                    <Button id={build(parentID, ids.BUTTONS.ADD)}
                            variant="fab"
                            mini
                            color="primary"
                            aria-label={formatMessage(intl, "addMetadata")}
                            onClick={onAddAVU}
                    >
                        <ContentAdd/>
                    </Button>
                </div>}
                <div className={classes.title}>
                    <Typography id={build(parentID, ids.TITLE)} variant="title">
                        {getMessage("avus")}
                    </Typography>
                </div>
            </Toolbar>
        );
    }
}

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
    static propTypes = {
        onRequestSort: PropTypes.func.isRequired,
    };

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

        [
            "onAddAVU",
            "handleRequestSort",
        ].forEach(methodName => (this[methodName] = this[methodName].bind(this)));
    }

    newAttrName() {
        return `New attribute ${this.newAttrCount++}`;
    }

    onAddAVU() {
        const { form, name, unshift } = this.props;
        const avus = getIn(form.values, name) || [];

        let newName = this.newAttrName();

        const namesMatch = avu => (avu.attr === newName);
        while (avus.findIndex(namesMatch) > -1) {
            newName = this.newAttrName();
        }

        unshift({
            attr: newName,
            value: "",
            unit: "",
        });
    }

    handleRequestSort(event, property) {
        const { name, form: { setFieldValue, values } } = this.props;
        const avus = [...getIn(values, name)];

        const orderBy = property;
        let order = 'asc';

        if (this.state.orderBy === property && this.state.order === order) {
            order = 'desc';
        }

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

        setFieldValue(name, avus);
    }

    render() {
        const {
            parentID,
            editable,
            classes,
            intl,
            name,
            form: {
                values,
            },
        } = this.props;

        const { order, orderBy } = this.state;

        const avus = getIn(values, name);
        const tableID = build(parentID, ids.AVU_GRID);

        return (
            <div className={classes.metadataTemplateContainer}>

                <MetadataGridToolbar parentID={tableID}
                                     editable={editable}
                                     onAddAVU={this.onAddAVU}
                />

                <div className={classes.tableWrapper}>
                    <Table aria-labelledby={build(tableID, ids.TITLE)}>
                        <TableBody>
                            {avus && avus.map((avu, index) => {
                                const {
                                    attr,
                                    value,
                                    unit,
                                    avus,
                                } = avu;

                                const field = `${name}[${index}]`;

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
                                                {editable ? <ContentEdit/> : <ContentView/>}
                                            </IconButton>
                                            {editable &&
                                            <IconButton id={build(rowID, ids.BUTTONS.DELETE)}
                                                        aria-label={formatMessage(intl, "delete")}
                                                        classes={{ root: classes.deleteIcon }}
                                                        onClick={event => {
                                                            event.stopPropagation();
                                                            this.props.remove(index);
                                                        }}
                                            >
                                                <ContentRemove/>
                                            </IconButton>}
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
                            rowCount={avus ? avus.length : 0}
                        />
                    </Table>
                </div>
            </div>
        );
    }
}

export default withStyles(styles)(withI18N(injectIntl(MetadataList), intlData));
