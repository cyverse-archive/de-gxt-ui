import React, { Component } from "react";
import PropTypes from "prop-types";

import {
    build,
    DEDialogHeader,
    getMessage,
    withI18N,
} from "@cyverse-de/ui-lib";

import EditCommunity from "./EditCommunity";

import ids from "../ids";
import messages from "../messages";
import styles from "../styles";

import {
    withStyles,
    Button,
    Dialog,
    DialogActions,
    DialogContent,
} from "@material-ui/core";

/**
 * @author aramsey
 * A dialog that displays the EditCommunity form
 */
class EditCommunityDialog extends Component {
    constructor(props) {
        super(props);

        this.state = {
            saveCommunity: false,
            isSelectAppsDlgOpen: false,
        };

        [
            "onDialogBtnClicked",
            "handleCloseEditDialog",
            "cancelSave",
            "isSelectAppsDlgOpen",
        ].forEach((fn) => (this[fn] = this[fn].bind(this)));
    }

    onDialogBtnClicked(dialogName) {
        this.setState({ [dialogName]: true });
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

    isSelectAppsDlgOpen(open) {
        this.setState({ isSelectAppsDlgOpen: open });
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
            classes,
        } = this.props;

        const { saveCommunity, isSelectAppsDlgOpen } = this.state;

        const communityName =
            community !== null
                ? collaboratorsUtil.getSubjectDisplayName(community)
                : null;

        return (
            <Dialog
                open={open}
                fullWidth={true}
                maxWidth="lg"
                onClose={onClose}
                classes={{ root: isSelectAppsDlgOpen ? classes.hidden : null }}
                id={ids.EDIT_DLG}
            >
                <DEDialogHeader
                    heading={
                        <DialogHeader
                            community={community}
                            communityName={communityName}
                            isCommunityAdmin={isCommunityAdmin}
                        />
                    }
                    messages={messages.messages}
                    onClose={onClose}
                />
                <DialogContent>
                    <EditCommunity
                        parentId={ids.EDIT_DLG}
                        community={community}
                        isCommunityAdmin={isCommunityAdmin}
                        isMember={isMember}
                        collaboratorsUtil={collaboratorsUtil}
                        currentUser={currentUser}
                        presenter={presenter}
                        saveCommunity={saveCommunity}
                        cancelSave={this.cancelSave}
                        isSelectAppsDlgOpen={this.isSelectAppsDlgOpen}
                        onCommunitySaved={onCommunitySaved}
                        handleCloseEditDlg={this.handleCloseEditDialog}
                    />
                </DialogContent>
                <DialogActions>
                    <Button
                        variant="contained"
                        id={build(ids.EDIT_DLG, ids.BUTTONS.CANCEL)}
                        onClick={this.handleCloseEditDialog}
                    >
                        {getMessage("cancel")}
                    </Button>
                    {isCommunityAdmin && (
                        <Button
                            variant="contained"
                            id={build(ids.EDIT_DLG, ids.BUTTONS.OK)}
                            onClick={() =>
                                this.onDialogBtnClicked("saveCommunity")
                            }
                        >
                            {getMessage("ok")}
                        </Button>
                    )}
                </DialogActions>
            </Dialog>
        );
    }
}

function DialogHeader(props) {
    const { community, communityName, isCommunityAdmin } = props;

    if (community === null) {
        return getMessage("createCommunityTitle");
    } else if (community && isCommunityAdmin) {
        return getMessage("editCommunityTitle", {
            values: { name: communityName },
        });
    } else {
        return communityName;
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
