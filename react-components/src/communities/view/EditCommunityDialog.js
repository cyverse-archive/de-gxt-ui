import build from "../../util/DebugIDUtil";
import DEDialogHeader from "../../util/dialog/DEDialogHeader";
import EditCommunity from "./EditCommunity";
import { getMessage } from "../../util/I18NWrapper";
import ids from "../ids";
import messages from "../messages";
import styles from "../styles";
import withI18N from "../../util/I18NWrapper";

import Button from "@material-ui/core/Button/Button";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import PropTypes from "prop-types";
import React, { Component } from "react";
import { withStyles } from "@material-ui/core";

/**
 * @author aramsey
 * A dialog that displays the EditCommunity form
 */
class EditCommunityDialog extends Component {
    constructor(props) {
        super(props);

        this.state = {
            saveCommunity: false
        };

        [
            'onDialogBtnClicked',
            'handleCloseEditDialog',
            'cancelSave',
        ].forEach((fn) => this[fn] = this[fn].bind(this));
    }

    onDialogBtnClicked(dialogName) {
        this.setState({[dialogName]: true})
    }

    handleCloseEditDialog() {
        this.setState({
            saveCommunity: false,
        });

        this.props.onClose();
    }

    cancelSave() {
        this.setState({
            saveCommunity: false,
        });
    }

    render() {
        const {
            community,
            isCommunityAdmin,
            isMember,
            onClose,
            open,
            collaboratorsUtil,
            presenter,
            currentUser,
            onCommunitySaved,
        } = this.props;

        const {
            saveCommunity
        } = this.state;

        const communityName = community !== null ? collaboratorsUtil.getSubjectDisplayName(community) : null;

        return (
            <Dialog open={open}
                    fullWidth={true}
                    maxWidth='lg'
                    onClose={onClose}
                    id={ids.EDIT_DLG}>
                <DEDialogHeader heading={<DialogHeader community={community}
                                                       communityName={communityName}
                                                       isCommunityAdmin={isCommunityAdmin}/>}
                                messages={messages.messages}
                                onClose={onClose}>
                </DEDialogHeader>
                <DialogContent>
                    <EditCommunity parentId={ids.EDIT_DLG}
                                   community={community}
                                   isCommunityAdmin={isCommunityAdmin}
                                   isMember={isMember}
                                   collaboratorsUtil={collaboratorsUtil}
                                   currentUser={currentUser}
                                   presenter={presenter}
                                   saveCommunity={saveCommunity}
                                   cancelSave={this.cancelSave}
                                   onCommunitySaved={onCommunitySaved}
                                   handleCloseEditDlg={this.handleCloseEditDialog}/>
                </DialogContent>
                <DialogActions>
                    {isCommunityAdmin &&
                    <Button variant="raised"
                            id={build(ids.EDIT_DLG, ids.BUTTONS.OK)}
                            onClick={() => this.onDialogBtnClicked('saveCommunity')}>
                        {getMessage('ok')}
                    </Button>
                    }
                    <Button variant="raised"
                            id={build(ids.EDIT_DLG, ids.BUTTONS.CANCEL)}
                            onClick={this.handleCloseEditDialog}>
                        {getMessage('cancel')}
                    </Button>
                </DialogActions>
            </Dialog>
        )
    }
}

function DialogHeader(props) {
    const {
        community,
        communityName,
        isCommunityAdmin,
    } = props;

    if (community === null) {
        return (
            getMessage('createCommunityTitle')
        );
    } else if (community && isCommunityAdmin) {
        return (
            getMessage('editCommunityTitle', {values: {name: communityName}})
        );
    } else {
        return (
            communityName
        );
    }
}

EditCommunityDialog.propTypes = {
    collaboratorsUtil: PropTypes.object.isRequired,
    onClose: PropTypes.func.isRequired,
    open: PropTypes.bool.isRequired,
    onCommunitySaved: PropTypes.func.isRequired,
    presenter: PropTypes.shape({
        deleteCommunity: PropTypes.func.isRequired,
        joinCommunity: PropTypes.func.isRequired,
        leaveCommunity: PropTypes.func.isRequired,
    }),
    currentUser: PropTypes.shape({
        name: PropTypes.string.isRequired,
        id: PropTypes.string.isRequired,
    }),
    community: PropTypes.object,
    isCommunityAdmin: PropTypes.bool.isRequired,
    isMember: PropTypes.bool.isRequired,
};

export default withStyles(styles)(withI18N(EditCommunityDialog, messages));