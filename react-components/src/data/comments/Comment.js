import React, { Component } from "react";
import { Typography, ListItem, Divider, IconButton } from "@material-ui/core";
import { Delete } from "@material-ui/icons";
import { getMessage, withI18N } from "@cyverse-de/ui-lib";
import messages from "./messages";

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
                        {getMessage("commentOn")} <b>{date}</b> {owner}{" "}
                        {getMessage("commentWrote")}:
                        <br />
                        {retracted ? (
                            <Typography color="error">
                                {getMessage("commentRetracted")}
                            </Typography>
                        ) : (
                            <Typography>{message}</Typography>
                        )}
                    </Typography>
                    <IconButton
                        variant="contained"
                        size="small"
                        className={classes.deleteIcon}
                        onClick={() => {
                            retractComment(id);
                        }}
                    >
                        <Delete />
                    </IconButton>
                </ListItem>
                <Divider />
            </>
        );
    }
}

export default withI18N(Comment, messages);
