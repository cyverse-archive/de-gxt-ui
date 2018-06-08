import build from '../../util/DebugIDUtil';
import ids from './ids';
import messages from './messages';
import withI18N, { getMessage } from "../../util/I18NWrapper";

import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import PropTypes from 'prop-types';
import React, {Component} from 'react';
import TextField from '@material-ui/core/TextField';

/**
 * @author aramsey
 * A button which opens a dialog for naming an advanced search query and saving it
 */
class SaveSearchButton extends Component {
    constructor(props) {
        super(props);

        this.state = {
            open: false
        };

        this.handleClose = this.handleClose.bind(this);
        this.handleOpen = this.handleOpen.bind(this);
        this.handleSave = this.handleSave.bind(this);
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

    render() {
        let {
            value,
            onChange,
            parentId,
            disabled
        } = this.props;

        return (
            <div>
                <Button variant="raised"
                        id={build(parentId, ids.saveSearchBtn)}
                        disabled={disabled ? disabled : false}
                        onClick={this.handleOpen}>
                    {getMessage('saveSearchBtn')}
                </Button>
                <Dialog open={this.state.open}
                        onClose={this.handleClose}>
                    <DialogTitle id={ids.saveSearchDlg}>
                        {getMessage('saveSearchTitle')}
                    </DialogTitle>
                    <DialogContent>
                        <TextField id={ids.saveTextField}
                                   label={value ? getMessage('filterName') : getMessage('requiredField')}
                                   value={value}
                                   onChange={onChange}/>
                    </DialogContent>
                    <DialogActions>
                        <Button variant="flat"
                                disabled={!value}
                                id={ids.saveBtn}
                                color="primary"
                                onClick={this.handleSave}>
                            {getMessage('saveBtn')}
                        </Button>,
                        <Button variant="flat"
                                id={ids.cancelBtn}
                                color="primary"
                                onClick={this.handleClose}>
                            {getMessage('cancelBtn')}
                        </Button>
                    </DialogActions>
                </Dialog>
            </div>
        )
    }
}

SaveSearchButton.propTypes = {
    parentId: PropTypes.string,
    disabled: PropTypes.bool,
    handleSave: PropTypes.func.isRequired,
    value: PropTypes.string.isRequired,
    onChange: PropTypes.func.isRequired
};

export default withI18N(SaveSearchButton, messages);