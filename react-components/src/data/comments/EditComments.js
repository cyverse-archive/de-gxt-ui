import React, { Component } from "react";
import { injectIntl } from "react-intl";
import exStyles from "./style.js";
import DotMenu from "./DotMenu.js";
import Comment from "./Comment.js";
import constants from "../../constants";
import messages from "./messages";
import { Add } from "@material-ui/icons";
import {
    Dialog,
    DialogContent,
    TextField,
    Fab,
    List,
    withStyles,
    Divider,
    DialogActions,
    FormControl,
} from "@material-ui/core";
import {
    DEDialogHeader,
    palette,
    formatDate,
    stableSort,
    getSorting,
    getMessage,
    withI18N,
    LoadingMask,
    formatMessage,
} from "@cyverse-de/ui-lib";

class EditComments extends Component {
    constructor(props) {
        super(props);
        this.state = {
            open: true,
            commentList: null,
            loading: false,
            commentText: null,
        };

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
    getComments(input) {
        this.setState({ loading: true });
        new Promise((resolve, reject) => {
            this.props.presenter.getComments(input, resolve, reject);
        })
            .then((commentList) => {
                this.setState({ commentList: commentList });
                this.setState({ loading: false });
            })
            .catch((error) => {
                console.log(error);
                this.setState({ loading: false });
            });
    }

    retractComment(commentId) {
        this.setState({ loading: true });
        new Promise((resolve, reject) => {
            this.props.presenter.retractComment(commentId, resolve, reject);
        })
            .then((commentList) => {
                this.setState({ commentList: commentList });
                this.setState({ loading: false });
            })
            .catch((error) => {
                console.log(error);
                this.setState({ loading: false });
            });
    }

    createComment(commentText) {
        this.setState({ loading: true });
        new Promise(() => {
            this.props.presenter.createComment(commentText);
        })
            .then((commentList) => {
                this.setState({ commentList: commentList });
                this.setState({ loading: false });
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
        let temp = stableSort(
            this.state.commentList,
            getSorting("asc", "post_time")
        );
        this.setState({ commentList: temp });
    };

    handleSortLeastRecent = () => {
        let temp = stableSort(
            this.state.commentList,
            getSorting("desc", "post_time")
        );
        this.setState({ commentList: temp });
    };

    handleSortOwner = () => {
        let temp = stableSort(
            this.state.commentList,
            getSorting("asc", "commenter")
        );
        this.setState({ commentList: temp });
    };

    handleChange(event) {
        this.setState({ commentText: event.target.value });
    }

    render() {
        const { classes, intl } = this.props;

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
                      classes={classes}
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
                        heading={formatMessage(intl, "editCommentsTitle", {
                            fileName: this.props.fileName,
                        })}
                        onClose={this.handleClose}
                    />
                    <Divider />

                    <DotMenu
                        handleSortMostRecent={this.handleSortMostRecent}
                        handleSortLeastRecent={this.handleSortLeastRecent}
                        handleSortOwner={this.handleSortOwner}
                        className={classes.dropDownDots}
                    />

                    <DialogContent
                        id="editCommentsCommentList"
                        className={classes.dContent}
                    >
                        <LoadingMask loading={this.state.loading}>
                            <List component="nav">{commentItems}</List>
                        </LoadingMask>
                    </DialogContent>

                    <TextField
                        value={this.state.commentText}
                        onChange={this.handleChange}
                        className={classes.addCommentTextField}
                        label={getMessage("addCommentTextField")}
                        margin="dense"
                        multiline
                        variant="filled"
                        rows="5"
                        id="addCommentTextField"
                    />

                    <DialogActions>
                        <Fab
                            size="medium"
                            color="primary"
                            aria-label={formatMessage(intl, "addFab")}
                            onClick={() => {
                                this.createComment(this.state.commentText);
                            }}
                            className={classes.addCommentButton}
                        >
                            <Add />
                        </Fab>
                    </DialogActions>
                </Dialog>
            </div>
        );
    }
}

export default withI18N(
    injectIntl(withStyles(exStyles)(EditComments)),
    messages
);
