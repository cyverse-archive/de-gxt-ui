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
                                id={build(this.props.baseId, this.props.ids[column.name.toUpperCase()])}
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
        })),
};

EnhancedTableHead.defaultProps = {
    selectable: false,
};
export default EnhancedTableHead;