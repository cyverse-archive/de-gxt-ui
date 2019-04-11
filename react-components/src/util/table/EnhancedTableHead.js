/**
 * @author Sriram, psarando
 */
import React from "react";
import PropTypes from "prop-types";

import Color from "../CyVersePalette";
import build from "../DebugIDUtil";

import exStyles from "./style";

import Checkbox from "@material-ui/core/Checkbox";
import TableCell from "@material-ui/core/TableCell";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableSortLabel from "@material-ui/core/TableSortLabel";
import Tooltip from "@material-ui/core/Tooltip";
import { withStyles } from "@material-ui/core/styles";

class EnhancedTableHead extends React.Component {
    createSortHandler = (property) => (event) => {
        this.props.onRequestSort(event, property);
    };

    render() {
        const {
            onSelectAllClick,
            order,
            orderBy,
            numSelected,
            columnData,
            selectable,
            classes,
            padding,
            rowsInPage,
        } = this.props;

        let isInDeterminate = numSelected > 0 && numSelected !== rowsInPage;
        return (
            <TableHead>
                <TableRow>
                    {selectable && (
                        <TableCell
                            padding={padding ? padding : "checkbox"}
                            className={classes.checkbox_cell}
                        >
                            <Checkbox
                                indeterminate={isInDeterminate}
                                checked={numSelected === rowsInPage}
                                onChange={onSelectAllClick}
                                className={classes.column_heading}
                            />
                        </TableCell>
                    )}
                    {columnData.map((column) => {
                        const key = column.key || column.name;

                        return (
                            <TableCell
                                key={key}
                                id={build(this.props.baseId, column.id)}
                                variant="head"
                                align={column.numeric ? "right" : "inherit"}
                                padding={column.padding || padding || "default"}
                                sortDirection={orderBy === key ? order : false}
                                className={classes.column_heading}
                            >
                                {column.enableSorting ? (
                                    <Tooltip
                                        title="Sort"
                                        placement={
                                            column.numeric
                                                ? "bottom-end"
                                                : "bottom-start"
                                        }
                                        enterDelay={300}
                                    >
                                        <TableSortLabel
                                            active={orderBy === key}
                                            direction={order.toLowerCase()}
                                            onClick={
                                                this.props.onRequestSort &&
                                                this.createSortHandler(key)
                                            }
                                            style={{ color: Color.white }}
                                        >
                                            {column.name}
                                        </TableSortLabel>
                                    </Tooltip>
                                ) : (
                                    column.name
                                )}
                            </TableCell>
                        );
                    }, this)}
                </TableRow>
            </TableHead>
        );
    }
}

EnhancedTableHead.propTypes = {
    selectable: PropTypes.bool,
    numSelected: PropTypes.number,
    onRequestSort: PropTypes.func,
    onSelectAllClick: PropTypes.func,
    order: PropTypes.string,
    orderBy: PropTypes.string,
    rowsInPage: PropTypes.number,
    baseId: PropTypes.string.isRequired,
    columnData: PropTypes.arrayOf(
        PropTypes.shape({
            id: PropTypes.string,
            name: PropTypes.oneOfType([PropTypes.string, PropTypes.object]),
            padding: PropTypes.string,
            numeric: PropTypes.bool,
            enableSorting: PropTypes.bool,
            key: PropTypes.string,
        })
    ),
    padding: PropTypes.string,
};

EnhancedTableHead.defaultProps = {
    selectable: false,
};

export default withStyles(exStyles)(EnhancedTableHead);
