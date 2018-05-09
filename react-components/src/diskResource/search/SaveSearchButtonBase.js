import React, {Component} from 'react';

import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';
import PropTypes from 'prop-types';
import RaisedButton from 'material-ui/RaisedButton';
import TextField from 'material-ui/TextField';
import ids from './ids';
import {getMessage} from '../../util/hasI18N';

/**
 * @author aramsey
 * A dialog for naming an advanced search query and saving it
 */
class SaveSearchButtonBase extends Component {
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
        this.getFullId = this.getFullId.bind(this);
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

    getFullId(id) {
        let parentId = this.props.parentId;
        return parentId ? parentId + id : id;
    }

    render() {
        let actions = [
            <FlatButton id={ids.saveBtn}
                        label={getMessage('saveBtn')}
                        primary={true}
                        onClick={this.handleSave}/>,
            <FlatButton id={ids.cancelBtn}
                        label={getMessage('cancelBtn')}
                        primary={true}
                        onClick={this.handleClose}/>
        ];

        return (
            <div>
                <RaisedButton id={this.getFullId(ids.saveSearchBtn)}
                              label={getMessage('saveSearchBtn')}
                              onClick={this.handleOpen}/>
                <Dialog id={ids.saveSearchDlg}
                        title={getMessage('saveSearchTitle')}
                        actions={actions}
                        open={this.state.open}
                        onRequestClose={this.handleClose}>
                    <TextField id={ids.saveTextField}
                               hintText={getMessage('filterName')}
                               errorText={getMessage('requiredField')}
                               value={this.state.name}
                               onChange={this.handleValueChange}/>
                </Dialog>
            </div>
        )
    }
}

SaveSearchButtonBase.propTypes = {
    parentId: PropTypes.string,
    handleSave: PropTypes.func.isRequired,
    originalName: PropTypes.string.isRequired
};

export default SaveSearchButtonBase;