import React, {Component} from "react";
import Dialog from '@material-ui/core/Dialog';
import Button from '@material-ui/core/Button';
import DeleteIcon from '@material-ui/icons/Delete';
import { withStyles } from '@material-ui/core/styles';
import DEDialogHeader from  "./../../util/dialog/DEDialogHeader.js"
import DialogContent from '@material-ui/core/DialogContent';
import exStyles from "./style.js"
import DotMenu from "./DotMenu.js"
import Comment from "./Comment.js"
import TextField from '@material-ui/core/TextField'
import Fab from '@material-ui/core/Fab'
import AddIcon from '@material-ui/icons/Add'
import CyVersePalette from "../../util/CyVersePalette";
import List from '@material-ui/core/List';


class EditComments extends Component{
    state = {
        anchorEl: null,
    };
    constructor(props){
        super(props);
        this.state = {
            open: true,
            commentList : null,
        };
        this.handleClose = this.handleClose.bind(this);
        this.handleSortMostRecent = this.handleSortMostRecent.bind(this);
        this.handleSortLeastRecent = this.handleSortLeastRecent.bind(this);
        this.handleSortOwner = this.handleSortOwner.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.createComment = this.createComment.bind(this);
    }

    componentDidMount(){
        this.getComments(this.props.fileName)
    }
    getComments(input, callback){
        new Promise((resolve, reject) => {
            this.props.presenter.getComments(input, resolve, reject);
        }).then(commentList => {
            console.log("here");
            console.log(commentList);
            this.setState({commentList : commentList});
        }).catch(error => {
            console.log(error);
            this.setState({loading : false});
        })

    }

    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({open: true})
    }

    handleClose = () => {
        this.setState({open : false});
    };

    handleSortMostRecent = () => {
        console.log("Sort ascending");
        let swapped;
        let aux = 0;
        let temp = this.state.commentList;
        swapped = true;
        while(swapped){
            swapped = false;
            for(let i = 1; i < temp.length; i++){
                if(temp[i - 1].date > temp[i].date){
                    swapped = true;
                    aux = temp[i - 1];
                    temp[i - 1] = temp[i];
                    temp[i] = aux;
                }
            }

        }
        this.setState({commentList : temp});

    };

    handleSortLeastRecent = () => {
        console.log("Sort descending");
        let swapped;
        let aux = 0;
        let temp = this.state.commentList;
        swapped = true;
        while(swapped){
            swapped = false;
            for(let i = 1; i < temp.length; i++){
                if(temp[i - 1].date < temp[i].date){
                    swapped = true;
                    aux = temp[i - 1];
                    temp[i - 1] = temp[i];
                    temp[i] = aux;
                }
            }

        }
        this.setState({commentList : temp});
    };

    handleSortOwner = () => {
        console.log("Sort User");
        let swapped;
        let aux = 0;
        let temp = this.state.commentList;
        swapped = true;
        while(swapped){
            swapped = false;
            for(let i = 1; i < temp.length; i++){
                if(temp[i - 1].owner.charAt(0) > temp[i].owner.charAt(0)){
                    swapped = true;
                    aux = temp[i - 1];
                    temp[i - 1] = temp[i];
                    temp[i] = aux;
                }
            }

        }
        this.setState({commentList : temp});
    };

    handleChange = name => event => {
        this.setState({
            [name]: event.target.value,
        });
    };

    createComment = () => {
      const text = document.getElementById("addCommentTextField").value;
      console.log(text);
    };


    render(){
        const {classes} = this.props;
        let commentItems = this.state.commentList ? this.state.commentList.map((comment, index) =>
            <Comment message={comment.message} id={comment.id} retracted={comment.retracted}
                    date={comment.date} owner={comment.owner}/>
    ) : [];
        return(
            <div className={classes.root}>
                <Dialog
                    open={this.state.open}
                    onClose={this.handleClose}
                    id={"edit-comments-dialog"}
                    className={classes.main}
                >
                    <DEDialogHeader
                        id={"edit-comments-dialog-title"}
                        heading={"Comments"}
                        onClose={this.handleClose}
                    >

                    </DEDialogHeader>
                    <hr></hr>

                    <Button variant="contained" color="secondary" className={classes.deleteButton}

                    >
                        Retract
                        <DeleteIcon />
                    </Button>

                        <label id={"editCommentsDropDownLabel"} className={classes.dropDownLabel}>  Comments</label>
                        <DotMenu
                            handleSortMostRecent={this.handleSortMostRecent}
                            handleSortLeastRecent={this.handleSortLeastRecent}
                            handleSortOwner={this.handleSortOwner}
                            className={this.props.classes.dropDownDots}
                        />

                    <DialogContent id="editCommentsCommentList" className={classes.dContent}>
                        <List component="nav">
                            {commentItems}
                        </List>
                    </DialogContent>
                    <TextField
                        value={this.state.multiline}
                        onChange={this.handleChange('multiline')}
                        className={classes.addCommentTextField}
                        label="Add a Comment"
                        margin="normal"
                        multiline
                        variant="filled"
                        rows="5"
                        id="addCommentTextField"
                    />
                    <Fab
                        size="medium"
                        color={CyVersePalette.blue}
                        aria-label="Add"
                        onClick={this.createComment}
                        className={classes.addCommentButton}
                    >
                        <AddIcon/>
                    </Fab>
                </Dialog>
            </div>
        );
    }
}

export default withStyles(exStyles)(EditComments)

