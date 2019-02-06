/**
 *
 * @author Sriram
 *
 **/
import React from "react";
import TableCell from "@material-ui/core/TableCell";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableSortLabel from "@material-ui/core/TableSortLabel";
import Checkbox from "@material-ui/core/Checkbox";
import Tooltip from "@material-ui/core/Tooltip";
import Color from "../CyVersePalette";
import PropTypes from "prop-types";
import build from "../../util/DebugIDUtil";
import { withStyles } from "@material-ui/core/styles";
import exStyles from "./style";

class EnhancedTableHead extends React.Component {
    createSortHandler = property => event => {
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
            rowInPage
        } = this.props;

        let isInDeterminate = numSelected > 0 && numSelected !== rowInPage;
        return (
            <TableHead>
                <TableRow>
                    {selectable && (
                        <TableCell padding={padding ? padding : "default"}
                                   className={classes.checkbox_cell}>
                            <Checkbox
                                indeterminate={isInDeterminate}
                                checked={numSelected === rowInPage}
                                onChange={onSelectAllClick}
                                className={classes.column_heading}
                            />
                        </TableCell>
                    )
                    }
                    {columnData.map(column => {
                        return (
                            <TableCell
                                key={column.key ? column.key : column.name}
                                variant="head"
                                numeric={column.numeric}
                                padding={padding || "default"}
                                sortDirection={orderBy === column.name ? order : false}
                                className={classes.column_heading}
                                id={build(this.props.baseId,
                                    this.props.ids[column.name.replace(/\s/g, "_").toUpperCase()])}
                            >
                                {column.enableSorting ? (
                                        <Tooltip
                                            title="Sort"
                                            placement={column.numeric ? 'bottom-end' : 'bottom-start'}
                                            enterDelay={300}
                                        >
                                            <TableSortLabel
                                                active={column.key ?
                                                    orderBy === column.key :
                                                    orderBy === column.name}
                                                direction={order.toLowerCase()}
                                                onClick={this.createSortHandler(column.key ?
                                                    column.key :
                                                    column.name)}
                                                style={{color: Color.white}}
                                            >
                                                {column.name}
                                            </TableSortLabel>
                                        </Tooltip>
                                    ) : (
                                        column.name
                                    )}
                            </TableCell>
                        )
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
    rowCount: PropTypes.number,
    baseId: PropTypes.string.isRequired,
    ids:  PropTypes.object.isRequired,
    columnData: PropTypes.arrayOf(
        PropTypes.shape({
            name: PropTypes.string,
            numeric: PropTypes.bool,
            enableSorting: PropTypes.bool,
            key: PropTypes.string
        })),
    padding: PropTypes.string,
};

EnhancedTableHead.defaultProps = {
    selectable: false,
};
export default withStyles(exStyles)(EnhancedTableHead);