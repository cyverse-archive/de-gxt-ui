/**
 *
 * @author Sriram
 *
 **/
import React, {Component} from "react";
import TablePaginationActions from "../../util/table/TablePaginationActions";
import injectSheet from "react-jss";
import exStyles from "./style";
import Button from "@material-ui/core/Button";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TablePagination from "@material-ui/core/TablePagination";
import TableRow from "@material-ui/core/TableRow";
import CircularProgress from "@material-ui/core/CircularProgress";
import moment from "moment";
import Toolbar from "@material-ui/core/Toolbar";
import ToolbarGroup from "@material-ui/core/Toolbar";
import constants from "../../constants";
import {getMessage} from "../../util/I18NWrapper";
import Checkbox from "@material-ui/core/Checkbox";
import EnhancedTableHead from "../../util/table/EnhancedTablehead";

const columnData = [
    {name: "Category", numeric: false},
    {name: "Message", numeric: false},
    {name: "Date", numeric: false},
];

class NotificationView extends Component {
    constructor(props) {
        super(props);
        this.state = {
            data: [],
            loading: true,
            page: 0,
            rowsPerPage: 100,
            selected: [],
            order: 'asc',
            orderBy: 'Date'
        }
    }

    handleChangePage = (event, page) => {
        this.setState({page});
    };

    handleChangeRowsPerPage = event => {
        this.setState({rowsPerPage: event.target.value});
    };

    handleClick = (event, id) => {
        const {selected} = this.state;
        const selectedIndex = selected.indexOf(id);
        let newSelected = [];

        if (selectedIndex === -1) {
            newSelected = newSelected.concat(selected, id);
        } else if (selectedIndex === 0) {
            newSelected = newSelected.concat(selected.slice(1));
        } else if (selectedIndex === selected.length - 1) {
            newSelected = newSelected.concat(selected.slice(0, -1));
        } else if (selectedIndex > 0) {
            newSelected = newSelected.concat(
                selected.slice(0, selectedIndex),
                selected.slice(selectedIndex + 1),
            );
        }

        this.setState({selected: newSelected});
    };

    handleSelectAllClick = (event, checked) => {
        if (checked) {
            this.setState(state => ({selected: state.data.map(n => n.id)}));
            return;
        }
        this.setState({selected: []});
    };

    handleRequestSort = (event, property) => {
        const orderBy = property;
        let order = 'desc';

        if (this.state.orderBy === property && this.state.order === 'desc') {
            order = 'asc';
        }

        this.setState({order, orderBy});
    };

    isSelected = id => this.state.selected.indexOf(id) !== -1;

    render() {
        const {classes, data} = this.props;
        const {rowsPerPage, page, order, orderBy, selected} = this.state;

        return (
            <div className={classes.container}>
                {this.state.loading &&
                <CircularProgress size={30} className={classes.loadingStyle} thickness={7}/>
                }

                <Toolbar>
                    <ToolbarGroup>
                        <Button variant="raised">Refresh</Button>
                    </ToolbarGroup>
                </Toolbar>

                <div className={classes.table}>
                    <Table>
                        <EnhancedTableHead
                            numSelected={selected.length}
                            order={order}
                            orderBy={orderBy}
                            onSelectAllClick={this.handleSelectAllClick}
                            onRequestSort={this.handleRequestSort}
                            rowCount={data.length}
                            columnData={columnData}
                        />
                        <TableBody>
                            {data.messages.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map(n => {
                                const isSelected = this.isSelected(n.message.id);
                                return (
                                    <TableRow onClick={event => this.handleClick(event, n.message.id)}
                                              role="checkbox"
                                              aria-checked={isSelected}
                                              tabIndex={-1}
                                              selected={isSelected}
                                              hover
                                              key={n.message.id}>
                                        <TableCell padding="checkbox">
                                            <Checkbox checked={isSelected}/>
                                        </TableCell>
                                        <TableCell>{n.type}</TableCell>
                                        <TableCell>{(n.message.text)}</TableCell>
                                        <TableCell>{(n.message.timestamp) ? moment(n.message.timestamp, "x").format(
                                                constants.DATE_FORMAT) :
                                            getMessage("emptyValue")} </TableCell>
                                    </TableRow>
                                );
                            })}
                        </TableBody>
                    </Table>
                </div>

                <TablePagination
                    component="div"
                    colSpan={3}
                    count={data.messages.length}
                    rowsPerPage={rowsPerPage}
                    page={page}
                    onChangePage={this.handleChangePage}
                    onChangeRowsPerPage={this.handleChangeRowsPerPage}
                    ActionsComponent={TablePaginationActions}
                    rowsPerPageOptions={[100, 500, 1000]}
                />
            </div>
        )
    }

}
export default injectSheet(exStyles)(NotificationView);