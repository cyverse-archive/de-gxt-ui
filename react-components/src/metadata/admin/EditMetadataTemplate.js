/**
 * @author psarando
 */
import React, { Component } from "react";

import styles from "../style";

import EditAttribute from "./EditAttribute";
import SlideUpTransition from "./SlideUpTransition";
import TemplateAttributeList from "./TemplateAttributeList";

import AppBar from "@material-ui/core/AppBar";
import Button from "@material-ui/core/Button";
import Checkbox from "@material-ui/core/Checkbox";
import Dialog from "@material-ui/core/Dialog";
import DialogContent from "@material-ui/core/DialogContent";
import Divider from "@material-ui/core/Divider";
import IconButton from "@material-ui/core/IconButton";
import InputLabel from "@material-ui/core/InputLabel";
import TextField from "@material-ui/core/TextField";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import { withStyles } from "@material-ui/core/styles";

import CloseIcon from "@material-ui/icons/Close";

class EditMetadataTemplate extends Component {
    constructor(props) {
        super(props);


        const { name, description, deleted } = props.metadataTemplate;

        let attributes = [...props.metadataTemplate.attributes];

        this.state = {
            name: name,
            description: description,
            deleted: deleted,
            attributes: attributes,
            editingAttrIndex: -1,
        };
    }

    onSaveTemplate = () => {
        const {
            name,
            description,
            deleted,
            attributes,
        } = this.state;

        this.props.presenter.onSaveTemplate({
            name,
            description,
            deleted,
            attributes,
        });
    };

    handleChange = key => event => {
        this.setState({
            [key]: event.target.value,
        });
    };

    onAttributesChanged = (attributes) => {
        this.setState({attributes});
    };

    onAttributeUpdated = (index, attr) => {
        let attributes = [...this.state.attributes];
        attributes.splice(index, 1, attr);

        this.setState({attributes: attributes, editingAttrIndex: -1});
    };

    render() {
        const { classes, open } = this.props;
        const { name, description, deleted, attributes, editingAttrIndex } = this.state;
        const editingAttr = editingAttrIndex >= 0 ? attributes[editingAttrIndex] : {};

        return (
            <Dialog open={open}
                    onClose={this.props.presenter.closeTemplateInfoDialog}
                    fullScreen
                    disableBackdropClick
                    disableEscapeKeyDown
                    aria-labelledby="form-dialog-title"
                    TransitionComponent={SlideUpTransition}
            >
                <AppBar className={classes.appBar}>
                    <Toolbar>
                        <IconButton color="inherit" onClick={this.props.presenter.closeTemplateInfoDialog} aria-label="Close">
                            <CloseIcon />
                        </IconButton>
                        <Typography id="form-dialog-title" variant="title" color="inherit" className={classes.flex}>
                            Edit Metadata Template
                        </Typography>
                        <Button onClick={this.onSaveTemplate} color="inherit">
                            {this.props.saveText}
                        </Button>
                    </Toolbar>
                </AppBar>

                <DialogContent>
                    <TextField autoFocus
                               margin="dense"
                               id="name"
                               label="Name"
                               value={name}
                               onChange={this.handleChange("name")}
                               fullWidth
                    />
                    <TextField id="description"
                               label="Description"
                               value={description}
                               onChange={this.handleChange("description")}
                               fullWidth
                    />

                    <Checkbox id="deleted"
                              color="primary"
                              checked={deleted}
                              onChange={(event, checked) => this.setState({deleted: checked})}
                    />
                    <InputLabel htmlFor="deleted">Mark as Deleted?</InputLabel>

                    <Divider />

                    <TemplateAttributeList attributes={attributes}
                                           onAttributesChanged={this.onAttributesChanged}
                                           onAttributeUpdated={this.onAttributeUpdated}
                                           onEditAttr={(index) => this.setState({editingAttrIndex: index})}
                    />

                    <EditAttribute attribute={editingAttr}
                                   open={editingAttrIndex >= 0}
                                   parentName={name}
                                   saveAttr={(attr) => this.onAttributeUpdated(editingAttrIndex, attr)}
                                   closeAttrDialog={() => this.setState({editingAttrIndex: -1})}
                    />
                </DialogContent>
            </Dialog>
        );
    }
}

export default withStyles(styles)(EditMetadataTemplate);
