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
import Button from '@material-ui/core/Button';
import ContentAdd from '@material-ui/icons/Add';
import ContentEdit from '@material-ui/icons/Edit';
import TextField from '@material-ui/core/TextField';

import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';

import Slide from '@material-ui/core/Slide';

import Divider from '@material-ui/core/Divider';
import AppBar from '@material-ui/core/AppBar';
import IconButton from '@material-ui/core/IconButton';
import CloseIcon from '@material-ui/icons/Close';

import FormControl from '@material-ui/core/FormControl';
import InputLabel from '@material-ui/core/InputLabel';
import Input from '@material-ui/core/Input';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';

import KeyboardArrowDown from "@material-ui/icons/KeyboardArrowDown";
import KeyboardArrowUp from "@material-ui/icons/KeyboardArrowUp";

import ExpansionPanel from "@material-ui/core/ExpansionPanel";
import ExpansionPanelSummary from "@material-ui/core/ExpansionPanelSummary";
import ExpansionPanelDetails from "@material-ui/core/ExpansionPanelDetails";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";

const AttributeTypes = [
    "String",
    "Timestamp",
    "Boolean",
    "Number",
    "Integer",
    "Multiline Text",
    "URL/URI",
    "Enum",
    "OLS Ontology Term",
    "UAT Ontology Term",
];

const AttributeTypeMenuItems = AttributeTypes.map((type, index) => (<MenuItem key={index} value={type}>{type}</MenuItem>));

const Transition = (props) => {
    return <Slide direction="up" {...props} />;
};

const styles = theme => ({
    attributeTableContainer: {
        width: "100%",
        height: "100%",
    },
    attributeTableWrapper: {
        overflow: "auto",
        height: "80%",
    },
    appBar: {
        position: "relative",
    },
    flex: {
        flex: 1,
    },
    tableHead: {
        backgroundColor: "#e2e2e2",
        position: "sticky",
        top: 0
    },
    deleteIcon: {
        "&:hover": {
            backgroundColor: theme.palette.error.dark,
        },
    },
});

const toolbarStyles = theme => ({
    root: {
        paddingLeft: theme.spacing.unit,
        paddingRight: theme.spacing.unit,
    },
    spacer: {
        flex: '1 1 100%',
    },
    actions: {
        color: theme.palette.text.secondary,
    },
    title: {
        paddingLeft: theme.spacing.unit,
        flex: '0 0 auto',
    },
});

let OrderedGridToolbar = props => {
    const {
        classes,
        title,
        onAddItem,
        moveUp,
        moveDown,
        moveUpDisabled,
        moveDownDisabled,
    } = props;

    return (
        <Toolbar
            className={classes.root}
        >
            <div className={classes.actions}>
                <Button variant="fab"
                        mini
                        color="primary"
                        aria-label="add row"
                        onClick={onAddItem}
                >
                    <ContentAdd />
                </Button>
            </div>
            <div className={classes.title}>
                <Typography variant="title" id="tableTitle">
                    {title}
                </Typography>
            </div>
            <div className={classes.spacer} />
            <div className={classes.actions}>
                <Button variant="fab"
                        mini
                        color="secondary"
                        aria-label="move up"
                        className={classes.button}
                        disabled={moveUpDisabled}
                        onClick={() => moveUp()}
                >
                    <KeyboardArrowUp />
                </Button>
            </div>
            <div className={classes.actions}>
                <Button variant="fab"
                        mini
                        color="secondary"
                        aria-label="move down"
                        className={classes.button}
                        disabled={moveDownDisabled}
                        onClick={() => moveDown()}
                >
                    <KeyboardArrowDown />
                </Button>
            </div>
        </Toolbar>
    );
};

OrderedGridToolbar = withStyles(toolbarStyles)(OrderedGridToolbar);

class AttributeEnumEditDialog extends React.Component {
    constructor(props) {
        super(props);

        const { value, is_default } = props;

        this.state = {
            value,
            is_default,
        };
    }

    componentWillReceiveProps(newProps) {
        const { value, is_default } = newProps;

        this.setState({
            value,
            is_default,
        });
    }

    render() {
        const { open, onSave, onClose } = this.props;
        const { value, is_default } = this.state;

        return (
            <Dialog
                open={open}
                disableBackdropClick
                disableEscapeKeyDown
                aria-labelledby="form-dialog-title"
            >
                <DialogTitle id="form-dialog-title">Edit Enum Selection</DialogTitle>
                <DialogContent>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="value"
                        label="Value"
                        value={value || ""}
                        onChange={event => this.setState({value: event.target.value})}
                        fullWidth
                    />
                    <Checkbox id="isDefault"
                              color="primary"
                              checked={!!is_default}
                              onChange={(event, checked) => this.setState({is_default: checked})}
                    />
                    <InputLabel htmlFor="isDefault">Default Selection?</InputLabel>
                </DialogContent>
                <DialogActions>
                    <Button onClick={onClose} color="primary">
                        Cancel
                    </Button>
                    <Button onClick={() => onSave(value, is_default)} color="primary">
                        OK
                    </Button>
                </DialogActions>
            </Dialog>
        );
    }
}

class AttributeEnumEditGrid extends React.Component {
    constructor(props) {
        super(props);

        this.newEnumCount = 1;

        this.state = {
            selected: -1,
            editingEnumIndex: -1,
        };
    }

    newEnumValue = () => `New value ${this.newEnumCount++}`;

    onAddEnum = () => {
        let value = this.newEnumValue();
        let attributeEnums = this.props.values;

        const valuesMatch = attrEnum => (attrEnum.value === value);
        while (attributeEnums.findIndex(valuesMatch) > -1) {
            value = this.newEnumValue();
        }

        this.setState({selected: 0});

        this.props.onValuesChanged([
            {
                value,
                is_default: false,
            },
            ...attributeEnums
        ]);
    };

    onEnumRemoved = (index) => {
        let values = [...this.props.values];
        let { selected } = this.state;

        values.splice(index, 1);

        // fix selection
        if (index === selected) {
            selected = -1;
        } else if (index < selected) {
            selected--;
        }

        this.setState({selected});
        this.props.onValuesChanged(values);
    };

    handleChange = (index, value, is_default) => {
        let values = [...this.props.values];
        const attrEnum = values[index];

        if (is_default) {
            let currentDefaultIndex = values.findIndex(value => value.is_default);

            if (currentDefaultIndex >= 0) {
                let currentDefault = values[currentDefaultIndex];
                values.splice(currentDefaultIndex, 1, {...currentDefault, is_default: false});
            }
        }

        values.splice(index, 1, {...attrEnum, value, is_default});

        this.props.onValuesChanged(values);
    };

    handleSelect = (index) => {
        const { selected } = this.state;
        this.setState({ selected: selected === index ? -1 : index });
    };

    moveUp = () => {
        this.moveSelectedEnum(-1);
    };

    moveDown = () => {
        this.moveSelectedEnum(1);
    };

    moveSelectedEnum = (offset) => {
        const { selected } = this.state;
        let values = [...this.props.values];

        let [attrEnum] = values.splice(selected, 1);
        values.splice(selected + offset, 0, attrEnum);

        this.setState({selected: selected + offset});
        this.props.onValuesChanged(values);
    };

    render() {
        const { classes, values } = this.props;
        const { selected, editingEnumIndex } = this.state;
        const editingEnum = editingEnumIndex >= 0 ? values[editingEnumIndex] : {};

        return (
            <div className={classes.attributeTableContainer}>

                <OrderedGridToolbar title="Values"
                                    onAddItem={this.onAddEnum}
                                    moveUp={this.moveUp}
                                    moveUpDisabled={selected <= 0}
                                    moveDown={this.moveDown}
                                    moveDownDisabled={selected < 0 || (values.length - 1) <= selected}
                />

                <div className={classes.attributeTableWrapper}>
                    <Table aria-labelledby="tableTitle">
                        <TableBody>
                            {values && values.map((attrEnum, index) => {
                                const isSelected = (index === selected);
                                const {
                                    value,
                                    is_default,
                                } = attrEnum;

                                return (
                                    <TableRow
                                        hover
                                        tabIndex={-1}
                                        key={value}
                                        selected={isSelected}
                                        onClick={() => this.handleSelect(index)}
                                    >
                                        <TableCell component="th" scope="row">
                                            {value}
                                        </TableCell>
                                        <TableCell padding="checkbox">
                                            <Checkbox color="primary"
                                                      checked={is_default}
                                                      onChange={(event, checked) => {
                                                          this.handleChange(index, value, checked);
                                                      }}
                                                      onClick={event => event.stopPropagation()}
                                            />
                                        </TableCell>
                                        <TableCell padding="none">
                                            <IconButton aria-label="edit"
                                                        className={classes.button}
                                                        onClick={event => {
                                                            event.stopPropagation();
                                                            this.setState({editingEnumIndex: index});
                                                        }}
                                            >
                                                <ContentEdit />
                                            </IconButton>
                                            <IconButton aria-label="delete"
                                                        classes={{root: classes.deleteIcon}}
                                                        onClick={event => {
                                                            event.stopPropagation();
                                                            this.onEnumRemoved(index);
                                                        }}
                                            >
                                                <ContentRemove />
                                            </IconButton>
                                        </TableCell>
                                    </TableRow>
                                );
                            })}
                        </TableBody>
                        <TableHead className={classes.tableHead}>
                            <TableRow>
                                <TableCell component="th" scope="row">Value</TableCell>
                                <TableCell padding="checkbox">Default?</TableCell>
                                <TableCell padding="none" />
                            </TableRow>
                        </TableHead>
                    </Table>
                </div>

                <AttributeEnumEditDialog open={editingEnumIndex >= 0}
                                         value={editingEnum.value}
                                         is_default={editingEnum.is_default}
                                         onClose={() => this.setState({editingEnumIndex: -1})}
                                         onSave={(value, is_default) => {
                                             this.setState({editingEnumIndex: -1});
                                             this.handleChange(editingEnumIndex, value, is_default);
                                         }}
                />

            </div>
        );
    }
}

AttributeEnumEditGrid = withStyles(styles)(AttributeEnumEditGrid);

const columnData = [
    {
        id: "name",
        label: "Attribute",
        component: "th",
        scope: "row",
    },
    { id: "description", label: "Description" },
    { id: "type", label: "Type", padding: "none" },
    { id: "attributes", label: "Child Attributes", padding: "none", numeric: true },
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
                </TableRow>
            </TableHead>
        );
    }
}

AttributeGridHeader = withStyles(styles)(AttributeGridHeader);

class TemplateAttributeList extends Component {
    constructor(props) {
        super(props);

        this.newAttrCount = 1;

        this.state = {
            selected: -1,
        };
    }

    onAddAttribute = () => {
        let name = `New attribute ${this.newAttrCount++}`;
        let attributes = this.props.attributes;

        const namesMatch = attr => (attr.name === name);
        while (attributes.findIndex(namesMatch) > -1) {
            name = `New attribute ${this.newAttrCount++}`;
        }

        this.setState({
            selected: 0,
        });

        this.props.onAttributesChanged([
            {
                name: name,
                description: "",
                type: "String",
                required: false,
            },
            ...attributes
        ]);
    };

    onAttributeRemoved = (index) => {
        let { attributes } = this.props;
        let { selected } = this.state;

        attributes = [...attributes];
        attributes.splice(index, 1);

        // fix selection
        if (index === selected) {
            selected = -1;
        } else if (index < selected) {
            selected--;
        }

        this.setState({selected});
        this.props.onAttributesChanged(attributes);
    };

    onRequiredChecked = (index, attr, checked) => {
        this.props.onAttributeUpdated(index, {
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
        let attributes = [...this.props.attributes];

        let [attr] = attributes.splice(selected, 1);
        attributes.splice(selected - 1, 0, attr);

        this.setState({selected: selected - 1});
        this.props.onAttributesChanged(attributes);
    };

    moveDown = () => {
        const { selected } = this.state;
        let attributes = [...this.props.attributes];

        let [attr] = attributes.splice(selected, 1);
        attributes.splice(selected + 1, 0, attr);

        this.setState({selected: selected + 1});
        this.props.onAttributesChanged(attributes);
    };

    render() {
        const { classes, attributes } = this.props;
        const { selected } = this.state;

        return (
            <div className={classes.attributeTableContainer}>

                <OrderedGridToolbar title="Attributes"
                                    onAddItem={this.onAddAttribute}
                                    moveUp={this.moveUp}
                                    moveUpDisabled={selected <= 0}
                                    moveDown={this.moveDown}
                                    moveDownDisabled={selected < 0 || (attributes.length - 1) <= selected}
                />

                <div className={classes.attributeTableWrapper}>
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
                                        <TableCell component="th" scope="row">
                                            {name}
                                        </TableCell>
                                        <TableCell>{description}</TableCell>
                                        <TableCell padding="none">{type}</TableCell>
                                        <TableCell padding="none" numeric>{attributes ? attributes.length : 0}</TableCell>
                                        <TableCell padding="checkbox">
                                            <Checkbox color="primary"
                                                      checked={required}
                                                      onClick={event => event.stopPropagation()}
                                                      onChange={(event, checked) => {
                                                          this.onRequiredChecked(index, attribute, checked);
                                                      }}
                                            />
                                        </TableCell>
                                        <TableCell padding="none">
                                            <IconButton aria-label="edit"
                                                        className={classes.button}
                                                        onClick={event => {
                                                            event.stopPropagation();
                                                            this.props.onEditAttr(index);
                                                        }}
                                            >
                                                <ContentEdit />
                                            </IconButton>
                                            <IconButton aria-label="delete"
                                                        classes={{root: classes.deleteIcon}}
                                                        onClick={event => {
                                                            event.stopPropagation();
                                                            this.onAttributeRemoved(index);
                                                        }}
                                            >
                                                <ContentRemove />
                                            </IconButton>
                                        </TableCell>
                                    </TableRow>
                                );
                            })}
                        </TableBody>
                        <AttributeGridHeader/>
                    </Table>
                </div>
            </div>
        );
    }
}

TemplateAttributeList = withStyles(styles)(TemplateAttributeList);

class EditAttribute extends Component {
    constructor(props) {
        super(props);

        const { name, description, type, required, values, attributes } = props.attribute;
        this.state = {
            name,
            description,
            type,
            required,
            values: values || [],
            attributes: attributes || [],
            editingAttrIndex: -1,
        };

        this.EditAttributeChild = withStyles(styles)(EditAttribute);
    }

    componentWillReceiveProps(newProps) {
        const { attribute } = newProps;
        const { name, description, type, required, values, attributes } = attribute;

        this.setState({
            name,
            description,
            type,
            required,
            values: values || [],
            attributes: attributes || [],
        });
    }

    saveAttr = () => {
        const {
            name,
            description,
            type,
            required,
            values,
            attributes,
        } = this.state;

        this.props.saveAttr({
            ...this.props.attribute,
            name,
            description,
            type,
            required,
            values,
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
        const { classes, open, parentName } = this.props;
        const { name, description, type, required, attributes, values, editingAttrIndex } = this.state;
        const editingAttr = editingAttrIndex >= 0 ? attributes[editingAttrIndex] : {};

        const EditAttributeChild = this.EditAttributeChild;

        return (
            <Dialog
                open={open}
                onClose={this.props.closeAttrDialog}
                fullScreen
                disableBackdropClick
                disableEscapeKeyDown
                aria-labelledby="form-dialog-title"
                TransitionComponent={Transition}
            >
                <AppBar className={classes.appBar}>
                    <Toolbar>
                        <IconButton color="inherit" onClick={this.props.closeAttrDialog} aria-label="Close">
                            <CloseIcon />
                        </IconButton>
                        <Typography variant="title" color="inherit" className={classes.flex}>
                            Edit Attribute for {parentName}
                        </Typography>
                        <Button color="inherit" onClick={this.saveAttr}>
                            Save
                        </Button>
                    </Toolbar>
                </AppBar>
                <DialogContent>

                    <TextField id="name"
                               label="Name"
                               value={name || ""}
                               onChange={this.handleChange("name")}
                               margin="dense"
                               autoFocus
                               fullWidth
                    />
                    <TextField id="description"
                               label="Description"
                               value={description || ""}
                               onChange={this.handleChange("description")}
                               fullWidth
                    />

                    <FormControl fullWidth>
                        <InputLabel htmlFor="attr-type">Type</InputLabel>
                        <Select
                            value={type || ""}
                            onChange={this.handleChange("type")}
                            input={<Input id="attr-type" />}
                        >
                            {AttributeTypeMenuItems}
                        </Select>
                    </FormControl>

                    <Checkbox id="required"
                              color="primary"
                              checked={!!required}
                              onChange={(event, checked) => this.setState({required: checked})}
                    />
                    <InputLabel htmlFor="required">Required</InputLabel>

                    <Divider />

                    {type === "Enum" &&
                    <ExpansionPanel defaultExpanded>
                        <ExpansionPanelSummary expandIcon={<ExpandMoreIcon />}>
                            <Typography className={classes.heading}>Enum Values</Typography>
                        </ExpansionPanelSummary>
                        <ExpansionPanelDetails>
                            <AttributeEnumEditGrid values={values}
                                                   onValuesChanged={(values) => this.setState({values})}/>
                        </ExpansionPanelDetails>
                    </ExpansionPanel>
                    }

                    <ExpansionPanel defaultExpanded={attributes && attributes.length > 0}>
                        <ExpansionPanelSummary expandIcon={<ExpandMoreIcon />}>
                            <Typography className={classes.heading}>Attributes</Typography>
                        </ExpansionPanelSummary>
                        <ExpansionPanelDetails>
                            <TemplateAttributeList attributes={attributes}
                                                   onAttributesChanged={this.onAttributesChanged}
                                                   onAttributeUpdated={this.onAttributeUpdated}
                                                   onEditAttr={(index) => this.setState({editingAttrIndex: index})}
                            />
                        </ExpansionPanelDetails>
                    </ExpansionPanel>

                    <EditAttributeChild attribute={editingAttr}
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

EditAttribute = withStyles(styles)(EditAttribute);

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
                    TransitionComponent={Transition}
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
