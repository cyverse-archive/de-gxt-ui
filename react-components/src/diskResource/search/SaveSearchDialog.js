import React, {Component} from 'react';

import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';
import PropTypes from 'prop-types';
import RaisedButton from 'material-ui/RaisedButton';
import TextField from 'material-ui/TextField';

/**
 * @author aramsey
 * A dialog for naming an advanced search query and saving it
 */
class SaveSearchDialog extends Component {
    constructor(props) {
        super(props);

        this.state = {
            open: false,
            name: props.originalName
        };

        this.handleClose = this.handleClose.bind(this);
        this.handleOpen = this.handleOpen.bind(this);
        this.handleSave = this.handleSave.bind(this);
        this.handleValueChange = this.handleValueChange.bind(this);
    }

    handleValueChange(event) {
        let value = event.target.value;

        this.setState(function () {
            return {
                name: value
            }
        })
    }

    handleSave() {
        let name = this.state.name;
        let originalName = this.props.originalName;

        if (name.length > 0) {
            this.props.handleSave(originalName, name);
            this.setState(function () {
                return {
                    open: false,
                    name: ''
                }
            })
        }
    }

    handleOpen() {
        this.setState(function () {
            return {
                open: true
            }
        })
    }

    handleClose() {
        this.setState(function () {
            return {
                open: false
            }
        })
    }

    render() {
        let appearance = this.props.appearance;
        let actions = [
            <FlatButton label={appearance.saveBtn()}
                        primary={true}
                        onClick={this.handleSave}/>,
            <FlatButton label={appearance.cancelBtn()}
                        primary={true}
                        onClick={this.handleClose}/>
        ];

        return (
            <div>
                <RaisedButton label={appearance.saveSearchBtnText()}
                              onClick={this.handleOpen}/>
                <Dialog title={appearance.saveSearchTitle()}
                        actions={actions}
                        open={this.state.open}
                        onRequestClose={this.handleClose}>
                    <TextField hintText={appearance.filterName()}
                               errorText={appearance.requiredField()}
                               value={this.state.name}
                               onChange={this.handleValueChange}/>
                </Dialog>
            </div>
        )
    }
}

SaveSearchDialog.propTypes = {
    appearance: PropTypes.object.isRequired,
    handleSave: PropTypes.func.isRequired,
    originalName: PropTypes.string.isRequired
};

export default SaveSearchDialog;