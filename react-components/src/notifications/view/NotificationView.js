/**
 *
 * @author Sriram
 *
 **/
import React, { Component } from "react";
import TablePaginationActions from "../../util/pagination/TablePaginationActions";
import injectSheet from "react-jss";
import exStyles from "./style";


const columnData = [
    {name: "notificationCategory", numeric: false},
    {name: "message", numeric: false},
    {name: "timeStamp", numeric: false},
];

class NotificationView extends Component {
    constructor(props) {
        super(props);
        this.state = {
            data: [],
            loading: true,
            page: 0,
            rowsPerPage: 100,
        }
    }

    handleChangePage = (event, page) => {
        this.setState({page});
    };

    handleChangeRowsPerPage = event => {
        this.setState({rowsPerPage: event.target.value});
    };

    render() {
        const {classes} = this.props;
        const {data, rowsPerPage, page, order, orderBy} = this.state;

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
                        <TableHead>
                            <TableRow hover>
                                {columnData.map(column => (
                                    <TableCell className={classes.tableHead}
                                               key={column.name}
                                               numeric={column.numeric}
                                               sortDirection={orderBy === column.name ? order : false}
                                    >
                                    </TableCell>
                                ))}
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {data.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map(n => {
                                return (
                                    <TableRow hover key={n.message.id}>
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
                    count={data.length}
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