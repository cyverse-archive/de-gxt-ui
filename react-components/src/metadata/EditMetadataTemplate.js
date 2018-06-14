/**
 * @author psarando
 */
import React, { Component } from 'react';
import { withStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import Checkbox from '@material-ui/core/Checkbox';
import ContentRemove from '@material-ui/icons/Delete';
import { lighten } from '@material-ui/core/styles/colorManipulator';
import Button from '@material-ui/core/Button';
import ContentAdd from '@material-ui/icons/Add';
import ContentEdit from '@material-ui/icons/Edit';
import TextField from '@material-ui/core/TextField';

import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';

import KeyboardArrowDown from "@material-ui/icons/KeyboardArrowDown";
import KeyboardArrowUp from "@material-ui/icons/KeyboardArrowUp";

const styles = theme => ({
    metadataTemplateContainer: {
        width: "100%",
        height: "100%",
        marginTop: theme.spacing.unit * 3,
    },
    tableWrapper: {
        overflow: 'auto',
        height: "70%",
    },
});

class TemplateInfoDialog extends React.Component {
    constructor(props) {
        super(props);

        const { open, name, description } = props;

        this.state = {
            open,
            name,
            description,
        };
    }

    componentWillReceiveProps(newProps) {
        const { open, name, description } = newProps;

        this.setState({
            open,
            name,
            description,
        });
    }

    handleChange = key => event => {
        this.setState({
            [key]: event.target.value,
        });
    };


    handleSave = () => {
        const { onTemplateInfoDialogSave } = this.props;
        const { name, description } = this.state;

        onTemplateInfoDialogSave(name, description);
    };

    render() {
        const { name, description, open } = this.state;

        return (
            <Dialog
                open={open}
                onClose={this.props.closeTemplateInfoDialog}
                disableBackdropClick
                disableEscapeKeyDown
                aria-labelledby="form-dialog-title"
            >
                <DialogTitle id="form-dialog-title">Edit Template Info</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Edit Metadata Template name and description:
                    </DialogContentText>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="name"
                        label="Name"
                        value={name}
                        onChange={this.handleChange("name")}
                        fullWidth
                    />
                    <TextField
                        id="description"
                        label="Description"
                        value={description}
                        onChange={this.handleChange("description")}
                        fullWidth
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={this.props.closeTemplateInfoDialog} color="primary">
                        Cancel
                    </Button>
                    <Button onClick={this.handleSave} color="primary">
                        OK
                    </Button>
                </DialogActions>
            </Dialog>
        );
    }
}

const toolbarStyles = theme => ({
    root: {
        paddingRight: theme.spacing.unit,
    },
    highlight:
        theme.palette.type === 'light'
            ? {
                color: theme.palette.secondary.main,
                backgroundColor: lighten(theme.palette.secondary.light, 0.85),
            }
            : {
                color: theme.palette.text.primary,
                backgroundColor: theme.palette.secondary.dark,
            },
    spacer: {
        flex: '1 1 100%',
    },
    actions: {
        color: theme.palette.text.secondary,
    },
    title: {
        flex: '0 0 auto',
    },
});

let AttributeGridToolbar = props => {
    const { templateName, description, onAddAttribute, openTemplateInfoDialog, classes } = props;

    return (
        <Toolbar
            className={classes.root}
        >
            <div className={classes.actions}>
                <Button variant="fab"
                        mini
                        color="primary"
                        aria-label="edit template info"
                        onClick={openTemplateInfoDialog}
                >
                    <ContentEdit />
                </Button>
            </div>
            <div className={classes.title}>
                <Typography variant="title" id="tableTitle">
                    {templateName}
                </Typography>
                <Typography color="inherit" variant="subheading">
                    {description}
                </Typography>
            </div>
            <div className={classes.spacer} />
            <div className={classes.actions}>
                <Button variant="fab"
                        mini
                        color="primary"
                        aria-label="move up"
                        className={classes.button}
                        disabled={props.moveUpDisabled}
                        onClick={() => props.moveUp()}
                >
                    <KeyboardArrowUp />
                </Button>
            </div>
            <div className={classes.actions}>
                <Button variant="fab"
                        mini
                        color="primary"
                        aria-label="move down"
                        className={classes.button}
                        disabled={props.moveDownDisabled}
                        onClick={() => props.moveDown()}
                >
                    <KeyboardArrowDown />
                </Button>
            </div>
            <div className={classes.actions}>
                <Button variant="fab"
                        mini
                        color="primary"
                        aria-label="add attribute"
                        onClick={onAddAttribute}
                >
                    <ContentAdd />
                </Button>
            </div>
        </Toolbar>
    );
};

AttributeGridToolbar = withStyles(toolbarStyles)(AttributeGridToolbar);

const headerStyles = theme => ({
    tableHead: {
        backgroundColor: "#e2e2e2",
        position: "sticky",
        top: 0
    },
});

const columnData = [
    {
        id: "name",
        label: "Attribute",
        component: "th",
        scope: "row",
        padding: "none",
    },
    { id: "description", label: "Description" },
    { id: "type", label: "Type" },
    { id: "attributes", label: "Child Attributes", numeric: true },
    { id: "required", label: "Required", padding: "checkbox" },
];

class AttributeGridHeader extends React.Component {
    render() {
        const { classes } = this.props;

        return (
            <TableHead className={classes.tableHead}>
                <TableRow>
                    {columnData.map(column => {
                        return (
                            <TableCell
                                component={column.component}
                                scope={column.scope}
                                padding={column.padding ? column.padding : "default"}
                                numeric={column.numeric}
                                key={column.id}
                            >
                                {column.label}
                            </TableCell>
                        );
                    }, this)}
                    <TableCell padding="none" />
                    <TableCell padding="none" />
                </TableRow>
            </TableHead>
        );
    }
}

AttributeGridHeader = withStyles(headerStyles)(AttributeGridHeader);

class EditMetadataTemplate extends Component {
    constructor(props) {
        super(props);

        this.newAttrCount = 1;

        const { name, description, deleted } = props.metadataTemplate;

        let attributes = [...props.metadataTemplate.attributes];

        this.state = {
            name: name,
            description: description,
            deleted: deleted,
            attributes: attributes,
            selected: -1,
            templateInfoDialogOpen: false,
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

    onTemplateInfoDialogSave = (name, description) => {
        this.setState({
            name,
            description,
            templateInfoDialogOpen: false,
        });
    };

    closeTemplateInfoDialog = () => {
        this.setState({
            templateInfoDialogOpen: false,
        });
    };

    openTemplateInfoDialog = () => {
        this.setState({
            templateInfoDialogOpen: true,
        });
    };

    onAddAttribute = () => {
        let name = `New attribute ${this.newAttrCount++}`;
        let attributes = this.state.attributes;

        const namesMatch = attr => (attr.name === name);
        while (attributes.findIndex(namesMatch) > -1) {
            name = `New attribute ${this.newAttrCount++}`;
        }

        this.setState({
            selected: 0,
            attributes: [
                {
                    name: name,
                    description: "",
                    type: "String",
                    required: false,
                },
                ...attributes
            ],
        });
    };

    onAttributeRemoved = (index) => {
        let { selected, attributes } = this.state;

        attributes = [...attributes];
        attributes.splice(index, 1);

        // fix selection
        if (index === selected) {
            selected = -1;
        } else if (index < selected) {
            selected--;
        }

        this.setState({selected, attributes});
    };

    onAttributeUpdated = (index, attr) => {
        let attributes = [...this.state.attributes];
        attributes.splice(index, 1, attr);

        this.setState({attributes: attributes});
    };

    onRequiredChecked = (index, attr, checked) => {
        this.onAttributeUpdated(index, {
            ...attr,
            required: checked,
        });
    };

    handleSelect = (index) => {
        const { selected } = this.state;
        this.setState({ selected: selected === index ? -1 : index });
    };

    moveUp = () => {
        const { selected } = this.state;
        let attributes = [...this.state.attributes];

        let [attr] = attributes.splice(selected, 1);
        attributes.splice(selected - 1, 0, attr);

        this.setState({attributes: attributes, selected: selected - 1});
    };

    moveDown = () => {
        const { selected } = this.state;
        let attributes = [...this.state.attributes];

        let [attr] = attributes.splice(selected, 1);
        attributes.splice(selected + 1, 0, attr);

        this.setState({attributes: attributes, selected: selected + 1});
    };

    render() {
        const { classes } = this.props;
        const { name, description, attributes, selected, templateInfoDialogOpen } = this.state;

        return (
            <div className={classes.metadataTemplateContainer}>
                <TemplateInfoDialog open={templateInfoDialogOpen}
                                    name={name}
                                    description={description}
                                    onTemplateInfoDialogSave={this.onTemplateInfoDialogSave}
                                    closeTemplateInfoDialog={this.closeTemplateInfoDialog}/>

                <AttributeGridToolbar templateName={name}
                                      description={description}
                                      onAddAttribute={this.onAddAttribute}
                                      openTemplateInfoDialog={this.openTemplateInfoDialog}
                                      moveUp={this.moveUp}
                                      moveUpDisabled={selected <= 0}
                                      moveDown={this.moveDown}
                                      moveDownDisabled={selected < 0 || (attributes.length - 1) <= selected}
                />

                <div className={classes.tableWrapper}>
                    <Table aria-labelledby="tableTitle">
                        <TableBody>
                            {attributes && attributes.map((attribute, index) => {
                                const isSelected = index === selected;
                                const {
                                    name,
                                    description,
                                    type,
                                    required,
                                    attributes,
                                } = attribute;

                                return (
                                    <TableRow
                                        hover
                                        tabIndex={-1}
                                        key={name}
                                        selected={isSelected}
                                        onClick={() => this.handleSelect(index)}
                                    >
                                        <TableCell component="th" scope="row" padding="none">
                                            {name}
                                        </TableCell>
                                        <TableCell>{description}</TableCell>
                                        <TableCell>{type}</TableCell>
                                        <TableCell numeric>{attributes ? attributes.length : 0}</TableCell>
                                        <TableCell padding="checkbox">
                                            <Checkbox checked={required}
                                                      onClick={event => event.stopPropagation()}
                                                      onChange={(event, checked) => {
                                                          this.onRequiredChecked(index, attribute, checked);
                                                      }}
                                            />
                                        </TableCell>
                                        <TableCell padding="none">
                                            <Button variant="fab"
                                                    mini
                                                    color="primary"
                                                    aria-label="edit"
                                                    className={classes.button}
                                                    onClick={event => event.stopPropagation()}
                                            >
                                                <ContentEdit />
                                            </Button>
                                        </TableCell>
                                        <TableCell padding="none">
                                            <Button variant="fab"
                                                    mini
                                                    color="secondary"
                                                    aria-label="delete"
                                                    className={classes.button}
                                                    onClick={event => {
                                                        event.stopPropagation();
                                                        this.onAttributeRemoved(index);
                                                    }}
                                            >
                                                <ContentRemove />
                                            </Button>
                                        </TableCell>
                                    </TableRow>
                                );
                            })}
                        </TableBody>
                        <AttributeGridHeader/>
                    </Table>
                </div>

                <Button variant="raised" onClick={this.onSaveTemplate} fullWidth={true}>{
                    this.props.saveText
                }</Button>
            </div>
        );
    }
}

export default withStyles(styles)(EditMetadataTemplate);
