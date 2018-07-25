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

class EnhancedTableHead extends React.Component {
    createSortHandler = property => event => {
        this.props.onRequestSort(event, property);
    };

    render() {
        const {onSelectAllClick, order, orderBy, numSelected, rowCount, columnData, selectable} = this.props;

        return (
            <TableHead>
                <TableRow>
                    {selectable && (
                        <TableCell padding="checkbox"
                                   style={{backgroundColor: Color.blue, position: "sticky", top: 0}}>
                            <Checkbox
                                indeterminate={numSelected > 0 && numSelected < rowCount}
                                checked={numSelected === rowCount}
                                onChange={onSelectAllClick}
                                style={{
                                    backgroundColor: Color.blue,
                                    position: "sticky",
                                    top: 0,
                                    color: Color.white,
                                }}
                            />
                        </TableCell>
                    )
                    }
                    {columnData.map(column => {
                        return (
                            <TableCell
                                key={column.name}
                                numeric={column.numeric}
                                padding={column.disablePadding ? 'none' : 'default'}
                                sortDirection={orderBy === column.name ? order : false}
                                style={{
                                    backgroundColor: Color.blue,
                                    position: "sticky",
                                    top: 0,
                                    color: Color.white,
                                }}
                            >
                                {column.enableSorting ? (
                                        <Tooltip
                                            title="Sort"
                                            placement={column.numeric ? 'bottom-end' : 'bottom-start'}
                                            enterDelay={300}
                                        >
                                            <TableSortLabel
                                                active={orderBy === column.name}
                                                direction={order.toLowerCase()}
                                                onClick={this.createSortHandler(column.name)}
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
    selectable: PropTypes.bool.isRequired,
    numSelected: PropTypes.number.isRequired,
    onRequestSort: PropTypes.func.isRequired,
    onSelectAllClick: PropTypes.func.isRequired,
    order: PropTypes.string.isRequired,
    orderBy: PropTypes.string.isRequired,
    rowCount: PropTypes.number.isRequired,
};

export default EnhancedTableHead;