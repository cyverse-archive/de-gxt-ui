/**
 *
 * @author Sriram
 *
 **/
import React, { Component } from "react";
import TablePaginationActions from "../../util/table/TablePaginationActions";
import injectSheet from "react-jss";
import exStyles from "../style";
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
import ToolbarSeparator from "@material-ui/core/Toolbar";
import constants from "../../constants";
import intlData from "../messages";
import withI18N, { getMessage } from "../../util/I18NWrapper";
import Checkbox from "@material-ui/core/Checkbox";
import EnhancedTableHead from "../../util/table/EnhancedTableHead";
import Color from "../../util/CyVersePalette";
import Select from "@material-ui/core/Select";
import InputLabel from "@material-ui/core/InputLabel";
import MenuItem from "@material-ui/core/MenuItem";
import FormControl from "@material-ui/core/FormControl";
import RefreshIcon from "@material-ui/icons/Refresh";
import DeleteIcon from "@material-ui/icons/Delete";
import CheckIcon from "@material-ui/icons/Check";

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
            total: 0,
            page: 0,
            rowsPerPage: 100,
            selected: [],
            order: 'asc',
            orderBy: 'Date',
            filter: 'All',
        }
    }

    componentDidMount() {
        const {rowsPerPage, page}  = this.state;
        this.props.presenter.getNotifications(rowsPerPage, page, (notifications, total) => {
                this.setState({
                    loading: false,
                    data: notifications.messages,
                    total: total,
                })
            }, (errorCode, errorMessage) => {
                this.setState({
                    loading: false,
                });
            },
        )
    }

    handleChangePage = (event, page) => {
        this.setState({page});
    };

    handleFilterChange = event => {
        console.log("filter==>" + event.target.value);
        this.setState({filter: event.target.value});
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
            this.setState(state => ({selected: state.data.map(n => n.message.id)}));
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
        const {classes} = this.props;
        const {data, rowsPerPage, page, order, orderBy, selected, total} = this.state;

        return (
            <div className={classes.container}>
                {this.state.loading &&
                <CircularProgress size={30} className={classes.loadingStyle} thickness={7}/>
                }
                <Toolbar style={{
                    backgroundColor: Color.lightGray,
                    borderBottom: 'solid 2px',
                    borderColor: Color.gray,
                }}>
                    <ToolbarGroup>
                        <form autoComplete="off">
                            <FormControl>
                                <InputLabel htmlFor="filer-simple">Filter</InputLabel>
                                <Select
                                    value={this.state.filter}
                                    onChange={this.handleFilterChange}
                                    inputProps={{
                                        name: 'filter',
                                        id: 'filter-simple',
                                    }}>
                                    <MenuItem value="New">{getMessage("new")}</MenuItem>
                                    <MenuItem value="All">{getMessage("all")}</MenuItem>
                                    <MenuItem
                                        value="Analysis">{getMessage("analysis")}</MenuItem>
                                    <MenuItem value="Data">{getMessage("data")}</MenuItem>
                                    <MenuItem value="Tool Request">{getMessage("tool")}</MenuItem>
                                    <MenuItem value="Apps">{getMessage("apps")}</MenuItem>
                                    <MenuItem
                                        value="Permanent ID Request">{getMessage("permId")}</MenuItem>
                                    <MenuItem value="Team">{getMessage("team")}</MenuItem>
                                </Select>
                            </FormControl>
                        </form>
                        <ToolbarSeparator/>
                        <Button variant="raised" size="small"
                                style={{marginRight: 20}}><RefreshIcon />{getMessage("refresh")}
                        </Button>
                        <Button variant="raised" size="small"
                                style={{marginRight: 20}}><CheckIcon />{getMessage("markSeen")}</Button>
                        <ToolbarSeparator/>
                        <Button variant="raised" size="small"
                                style={{marginRight: 20}}><DeleteIcon />{getMessage("delete")}
                        </Button>
                        
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
                            rowCount={total}
                            columnData={columnData}
                        />
                        <TableBody>
                            {data.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map(n => {
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
                    count={total}
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
export default injectSheet(exStyles)(withI18N(NotificationView, intlData));