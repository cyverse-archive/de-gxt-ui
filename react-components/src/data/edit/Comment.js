import React, { Component } from "react";
import Typography from "@material-ui/core/Typography";
import ListItem from "@material-ui/core/ListItem";
import DeleteIcon from "@material-ui/icons/Delete";
import Fab from "@material-ui/core/Fab";
import Divider from '@material-ui/core/Divider';


class Comment extends Component {
    constructor(props) {
        super(props);
    }
    render() {
        const {
            date,
            owner,
            message,
            id,
            retracted,
            classes,
            retractComment,
        } = this.props;

        return (
            <>
                <ListItem id={id}>
                    <Typography className={classes.commentText}>
                        On <b>{date}</b> {owner} wrote:
                        <br />
                        {retracted ? (
                            <Typography color="error">Retracted</Typography>
                        ) : (
                            <Typography>{message}</Typography>
                        )}
                    </Typography>
                    <Fab
                        variant="contained"
                        size="small"
                        className={classes.deleteButton}
                        onClick={() => {
                            retractComment(this.props.id);
                        }}
                    >
                        <DeleteIcon />
                    </Fab>
                </ListItem>
                <Divider />
            </>
        );
    }
}

export default Comment;
