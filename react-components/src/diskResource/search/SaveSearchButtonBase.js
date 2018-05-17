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
 * A button which opens a dialog for naming an advanced search query and saving it
 */
class SaveSearchButtonBase extends Component {
    constructor(props) {
        super(props);

        this.state = {
            open: false
        };

        this.handleClose = this.handleClose.bind(this);
        this.handleOpen = this.handleOpen.bind(this);
        this.handleSave = this.handleSave.bind(this);
        this.getFullId = this.getFullId.bind(this);
    }

    handleSave() {
        this.props.handleSave();
        this.setState({open: false})
    }

    handleOpen() {
        this.setState({open: true})
    }

    handleClose() {
        this.setState({open: false})
    }

    getFullId(id) {
        let parentId = this.props.parentId;
        return parentId ? parentId + id : id;
    }

    render() {
        let { value, onChange } = this.props;
        let disabled = this.props.disabled;

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
                              disabled={disabled ? disabled : false}
                              label={getMessage('saveSearchBtn')}
                              onClick={this.handleOpen}/>
                <Dialog id={ids.saveSearchDlg}
                        title={getMessage('saveSearchTitle')}
                        actions={actions}
                        open={this.state.open}
                        onRequestClose={this.handleClose}>
                    <TextField id={ids.saveTextField}
                               hintText={getMessage('filterName')}
                               errorText={value ? null : getMessage('requiredField')}
                               value={value}
                               onChange={onChange}/>
                </Dialog>
            </div>
        )
    }
}

SaveSearchButtonBase.propTypes = {
    parentId: PropTypes.string,
    disabled: PropTypes.bool,
    handleSave: PropTypes.func.isRequired,
    value: PropTypes.string.isRequired,
    onChange: PropTypes.func.isRequired
};

export default SaveSearchButtonBase;