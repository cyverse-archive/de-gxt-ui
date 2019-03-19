/**
 * @author psarando
 */
import React, { Component } from "react";

import { getIn } from 'formik';
import PropTypes from "prop-types";
import { injectIntl } from "react-intl";

import build from "../util/DebugIDUtil";
import withI18N, { formatMessage, getMessage } from "../util/I18NWrapper";
import { stableSort, getSorting } from "../util/table/TableSort";
import ids from "./ids";
import intlData from "./messages";
import styles from "./style";

import EnhancedTableHead from "../util/table/EnhancedTableHead";

import Checkbox from "@material-ui/core/Checkbox";
import Fab from "@material-ui/core/Fab";
import Grid from '@material-ui/core/Grid';
import IconButton from "@material-ui/core/IconButton";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableRow from "@material-ui/core/TableRow";
import Toolbar from "@material-ui/core/Toolbar";
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
                    <Fab id={build(parentID, ids.BUTTONS.ADD)}
                         size="small"
                         color="primary"
                         aria-label={formatMessage(intl, "addMetadata")}
                         onClick={onAddAVU}
                    >
                        <ContentAdd/>
                    </Fab>
                </div>}
                <div className={classes.title}>
                    <Typography id={build(parentID, ids.TITLE)} variant="h6">
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
        id: build(ids.COL_HEADER, ids.AVU_ATTR),
        key: "attr",
        name: getMessage("attribute"),
        enableSorting: true,
    },
    {
        id: build(ids.COL_HEADER, ids.AVU_VALUE),
        key: "value",
        name: getMessage("value"),
        enableSorting: true,
    },
    {
        id: build(ids.COL_HEADER, ids.AVU_UNIT),
        key: "unit",
        name: getMessage("metadataUnitLabel"),
        enableSorting: true,
    },
    {
        id: build(ids.COL_HEADER, ids.AVU_AVUS),
        key: "avus",
        name: getMessage("metadataChildrenLabel"),
        enableSorting: true,
        numeric: true,
        padding: "none",
    },
    {
        id: build(ids.COL_HEADER, ids.COL_ACTIONS),
        key: "actions",
        enableSorting: false,
        padding: "none",
    },
];

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
        return formatMessage(this.props.intl, "newAttrName", {count: this.newAttrCount++});
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
        const orderBy = property;
        let order = "asc";

        if (this.state.orderBy === property && this.state.order === order) {
            order = "desc";
        }

        this.setState({ order, orderBy });
    }

    render() {
        const {
            parentID,
            editable,
            selectable,
            onSelectAVU,
            onSelectAllClick,
            avusSelected,
            rowsInPage,
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

        // A copy of the list of AVUs, but including only each AVU's sortable fields,
        // along with a TableRow component for rendering within the returned Table below.
        // This will allow the TableRows to be sorted without altering the original AVU list,
        // which also allows each AVU's original index in the metadata model to be preserved
        // and used in the form editing functions.
        const unsortedAVURows = (avus && avus.map((avu, index) => {
            const {
                attr,
                value,
                unit,
                avus,
            } = avu;

            const field = `${name}[${index}]`;

            const rowID = build(ids.EDIT_METADATA_FORM, field);

            const selected = avusSelected && avusSelected.includes(avu);

            // This returned object should include each sortable field defined by columnData above,
            // in addition to the TableRow component.
            return {
                attr,
                value,
                unit,
                // This field only requires the length of the original AVU's children, for TableRow sorting purposes.
                avus: avus ? avus.length : 0,
                // Include the TableRow component that will be rendered within the final Table component.
                tableRow: (
                    <TableRow
                        hover
                        tabIndex={-1}
                        key={field}
                        selected={selected}
                    >
                        {selectable &&
                        <TableCell padding="checkbox">
                            <Checkbox checked={selected}
                                      onChange={(event, checked) => onSelectAVU(avu, checked)}
                            />
                        </TableCell>}
                        <TableCell component="th" scope="row">
                            {attr}
                        </TableCell>
                        <TableCell>{value}</TableCell>
                        <TableCell>{unit}</TableCell>
                        <TableCell padding="none" align="right">{avus ? avus.length : 0}</TableCell>
                        <TableCell padding="none">
                            <Grid container
                                  spacing={0}
                                  wrap="nowrap"
                                  direction="row"
                                  justify="center"
                                  alignItems="center"
                            >
                                <Grid item>
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
                                </Grid>
                                {editable &&
                                <Grid item>
                                    <IconButton id={build(rowID, ids.BUTTONS.DELETE)}
                                                aria-label={formatMessage(intl, "delete")}
                                                classes={{ root: classes.deleteIcon }}
                                                onClick={event => {
                                                    event.stopPropagation();
                                                    this.props.remove(index);
                                                }}
                                    >
                                        <ContentRemove/>
                                    </IconButton>
                                </Grid>}
                            </Grid>
                        </TableCell>
                    </TableRow>
                ),
            };
        }));

        // Only sort the TableRows if sorting has been requested.
        const sortedAVURows = orderBy ? stableSort(unsortedAVURows, getSorting(order, orderBy)) : unsortedAVURows;

        return (
            <div className={classes.metadataTemplateContainer}>

                <MetadataGridToolbar parentID={tableID}
                                     editable={editable}
                                     onAddAVU={this.onAddAVU}
                />

                <div className={classes.tableWrapper}>
                    <Table aria-labelledby={build(tableID, ids.TITLE)}>
                        <TableBody>
                            {sortedAVURows && sortedAVURows.map(row => row.tableRow)}
                        </TableBody>
                        <EnhancedTableHead baseId={tableID}
                                           columnData={columnData}
                                           order={order}
                                           orderBy={orderBy}
                                           onRequestSort={this.handleRequestSort}
                                           selectable={selectable}
                                           onSelectAllClick={onSelectAllClick}
                                           numSelected={avusSelected ? avusSelected.length : 0}
                                           rowsInPage={rowsInPage}
                        />
                    </Table>
                </div>
            </div>
        );
    }
}

export default withStyles(styles)(withI18N(injectIntl(MetadataList), intlData));
