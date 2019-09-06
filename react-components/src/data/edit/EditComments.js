import React, { Component } from "react";
import { DEDialogHeader, palette, formatDate } from "@cyverse-de/ui-lib";
import Dialog from "@material-ui/core/Dialog";
import { withStyles } from "@material-ui/core/styles";
import DialogContent from "@material-ui/core/DialogContent";
import exStyles from "./style.js";
import DotMenu from "./DotMenu.js";
import Comment from "./Comment.js";
import TextField from "@material-ui/core/TextField";
import Fab from "@material-ui/core/Fab";
import AddIcon from "@material-ui/icons/Add";
import List from "@material-ui/core/List";
import constants from "../../constants";
import stableSort from "@cyverse-de/ui-lib";

let COMMENTS = "Comments";
const ADD_A_COMMENT = "Add a Comment";

class EditComments extends Component {
    constructor(props) {
        super(props);
        this.state = {
            open: true,
            commentList: null,
            loading: false,
            multiline: false,
            commentText: null,
        };

        COMMENTS = "Edit " + this.props.fileName + " Comments";
        this.handleClose = this.handleClose.bind(this);
        this.handleSortMostRecent = this.handleSortMostRecent.bind(this);
        this.handleSortLeastRecent = this.handleSortLeastRecent.bind(this);
        this.handleSortOwner = this.handleSortOwner.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.createComment = this.createComment.bind(this);
        this.retractComment = this.retractComment.bind(this);
        this.getComments = this.getComments.bind(this);
    }

    componentDidMount() {
        this.getComments(this.props.fileName);
    }
    getComments(input, callback) {
        new Promise((resolve, reject) => {
            this.props.presenter.getComments(input, resolve, reject);
        })
            .then((commentList) => {
                this.setState({ commentList: commentList });
            })
            .catch((error) => {
                console.log(error);
                this.setState({ loading: false });
            });
    }

    retractComment(commentId, callback) {
        new Promise((resolve, reject) => {
            this.props.presenter.retractComment(commentId, resolve, reject);
        })
            .then((commentList) => {
                this.setState({ commentList: commentList });
            })
            .catch((error) => {
                console.log(error);
                this.setState({ loading: false });
            });
    }

    createComment(commentText, callback) {
        new Promise((resolve, reject) => {
            this.props.presenter.createComment(commentText);
        })
            .then((commentList) => {
                this.setState({ commentList: commentList });
            })
            .catch((error) => {
                console.log(error);
                this.setState({ loading: false });
            });
    }

    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({ open: true });
    }

    handleClose = () => {
        this.setState({ open: false });
    };

    handleSortMostRecent = () => {
        let swapped;
        let aux = 0;
        let temp = this.state.commentList;
        swapped = true;
        while (swapped) {
            swapped = false;
            for (let i = 1; i < temp.length; i++) {
                if (temp[i - 1].date > temp[i].date) {
                    swapped = true;
                    aux = temp[i - 1];
                    temp[i - 1] = temp[i];
                    temp[i] = aux;
                }
            }
        }
        this.setState({ commentList: temp });
    };

    handleSortLeastRecent = () => {
        let swapped;
        let aux = 0;
        let temp = this.state.commentList;
        swapped = true;
        while (swapped) {
            swapped = false;
            for (let i = 1; i < temp.length; i++) {
                if (temp[i - 1].date < temp[i].date) {
                    swapped = true;
                    aux = temp[i - 1];
                    temp[i - 1] = temp[i];
                    temp[i] = aux;
                }
            }
        }
        this.setState({ commentList: temp });
    };

    handleSortOwner = () => {
        let swapped;
        let aux = 0;
        let temp = this.state.commentList;
        swapped = true;
        while (swapped) {
            swapped = false;
            for (let i = 1; i < temp.length; i++) {
                if (temp[i - 1].owner.charAt(0) > temp[i].owner.charAt(0)) {
                    swapped = true;
                    aux = temp[i - 1];
                    temp[i - 1] = temp[i];
                    temp[i] = aux;
                }
            }
        }

        this.setState({ commentList: temp });
    };

    handleChange(event) {
        this.setState({ commentText: event.target.value });
    }

    render() {
        const { classes } = this.props;
        let commentItems = this.state.commentList
            ? this.state.commentList.map((comment, index) => (
                  <Comment
                      message={comment.comment}
                      id={comment.id}
                      retracted={comment.retracted}
                      date={formatDate(
                          comment.post_time,
                          constants.DATE_FORMAT
                      )}
                      owner={comment.commenter}
                      classes={this.props.classes}
                      retractComment={this.retractComment}
                  />
              ))
            : [];
        return (
            <div className={classes.root}>
                <Dialog
                    open={this.state.open}
                    onClose={this.handleClose}
                    id={"edit-comments-dialog"}
                    className={classes.main}
                >
                    <DEDialogHeader
                        id={"edit-comments-dialog-title"}
                        heading={COMMENTS}
                        onClose={this.handleClose}
                    />
                    <hr />

                    <DotMenu
                        handleSortMostRecent={this.handleSortMostRecent}
                        handleSortLeastRecent={this.handleSortLeastRecent}
                        handleSortOwner={this.handleSortOwner}
                        className={this.props.classes.dropDownDots}
                    />

                    <DialogContent
                        id="editCommentsCommentList"
                        className={classes.dContent}
                    >
                        <List component="nav">{commentItems}</List>
                    </DialogContent>
                    <TextField
                        value={this.state.commentText}
                        onChange={this.handleChange}
                        className={classes.addCommentTextField}
                        label={ADD_A_COMMENT}
                        margin="normal"
                        multiline
                        variant="filled"
                        rows="5"
                        id="addCommentTextField"
                    />
                    <Fab
                        size="medium"
                        color={palette.blue}
                        aria-label="Add"
                        onClick={() => {
                            this.createComment(this.state.commentText);
                        }}
                        className={classes.addCommentButton}
                    >
                        <AddIcon />
                    </Fab>
                </Dialog>
            </div>
        );
    }
}

export default withStyles(exStyles)(EditComments);
