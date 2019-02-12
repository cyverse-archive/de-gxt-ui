import build from "../../util/DebugIDUtil";
import CommunityFilter from "./CommunityFilter";
import ConfirmCloseDialog from "../../util/ConfirmCloseDialog";
import ids from "../ids";
import messages from "../messages";
import styles from "../styles";
import withI18N, { getMessage } from "../../util/I18NWrapper";

import AddIcon from "@material-ui/icons/Add";
import Button from "@material-ui/core/Button/Button";
import CircularProgress from "@material-ui/core/CircularProgress/CircularProgress";
import MenuItem from "@material-ui/core/MenuItem";
import PropTypes from "prop-types";
import React, { Component, Fragment } from "react";
import Select from "@material-ui/core/Select";
import Toolbar from "@material-ui/core/Toolbar";
import { withStyles } from "@material-ui/core/styles";

/**
 * @author aramsey
 *
 * A toolbar that is shown in the CommunitiesView
 */
class CommunitiesToolbar extends Component {

    constructor(props) {
        super(props);

        this.state = {
            deleteCommunity: false,
            leaveCommunity: false,
            joinCommunity: false,
            loading: false,
        };

        [
            'onDialogBtnClicked',
            'handleDialogConfirmed',
            'closeDialog',
        ].forEach((fn) => this[fn] = this[fn].bind(this));
    }

    onDialogBtnClicked(dialogName) {
        this.setState({[dialogName]: true})
    }

    closeDialog(dialogName) {
        this.setState({[dialogName]: false})
    }

    handleDialogConfirmed(dialogName) {
        const {community} = this.props;

        this.setState({loading: true});

        new Promise((resolve, reject) => {
            this.props.presenter[dialogName](community.name, resolve, reject);
        }).then(() => {
            this.closeDialog(dialogName);
            this.setState({loading: false});
            this.props.refreshListing();
        }).catch(() => {
            this.setState({loading: false});
        });
    }

    render() {
        const {
            parentId,
            currentCommunityType,
            handleCommunityFilterChange,
            onCreateCommunityClicked,
            classes,
            community,
            collaboratorsUtil,
            isCommunityAdmin,
            isMember,
        } = this.props;

        const {loading} = this.state;

        let toolbarId = build(parentId, ids.TOOLBAR);
        const communityName = community !== null ? collaboratorsUtil.getSubjectDisplayName(community) : null;

        return (
            <Fragment>
                <Toolbar id={toolbarId} classes={{root: classes.toolbar}}>
                    <Select value={currentCommunityType}
                            onChange={handleCommunityFilterChange}>
                        <MenuItem
                            value={CommunityFilter.MY_COMMUNITIES}>{getMessage('myCommunities')}</MenuItem>
                        <MenuItem
                            value={CommunityFilter.ALL_COMMUNITIES}>{getMessage('allCommunities')}</MenuItem>
                    </Select>
                    <Button variant="raised"
                            id={build(parentId, ids.BUTTONS.CREATE)}
                            className={classes.toolbarItem}
                            onClick={onCreateCommunityClicked}>
                        <AddIcon/>
                        {getMessage('createCommunity')}
                    </Button>
                    <Button variant="raised"
                            disabled={!community || isMember}
                            className={classes.toolbarItem}
                            id={build(toolbarId, ids.BUTTONS.JOIN_COMMUNITY)}
                            onClick={() => this.onDialogBtnClicked('joinCommunity')}>
                        {getMessage('joinCommunity')}
                    </Button>
                    <Button variant="raised"
                            disabled={!community || !isMember}
                            className={classes.toolbarItem}
                            id={build(toolbarId, ids.BUTTONS.LEAVE_COMMUNITY)}
                            onClick={() => this.onDialogBtnClicked('leaveCommunity')}>
                        {getMessage('leaveCommunity')}
                    </Button>
                    <Button variant="raised"
                            disabled={!community || !isCommunityAdmin}
                            className={classes.toolbarItem}
                            id={build(toolbarId, ids.BUTTONS.DELETE_COMMUNITY)}
                            onClick={() => this.onDialogBtnClicked('deleteCommunity')}>
                        {getMessage('deleteCommunity')}
                    </Button>
                </Toolbar>

                <ConfirmCloseDialog open={this.state.deleteCommunity}
                                    parentId={ids.CONFIRM_DELETE_DLG}
                                    onConfirm={() => this.handleDialogConfirmed('deleteCommunity')}
                                    confirmLabel={getMessage('yes')}
                                    onCancel={() => this.closeDialog('deleteCommunity')}
                                    cancelLabel={getMessage('cancel')}
                                    onClose={() => this.closeDialog('deleteCommunity')}
                                    closeLabel={getMessage('no')}
                                    title={getMessage('confirmDeleteCommunityTitle', {values: {name: communityName}})}
                                    dialogContent={
                                        <div>
                                            {loading &&
                                            <CircularProgress size={30}
                                                              classes={{root: classes.loading}}
                                                              thickness={7}/>
                                            }
                                            {getMessage('confirmDeleteCommunity')}
                                        </div>
                                    }/>

                <ConfirmCloseDialog open={this.state.joinCommunity}
                                    parentId={ids.CONFIRM_JOIN_DLG}
                                    onConfirm={() => this.handleDialogConfirmed('joinCommunity')}
                                    confirmLabel={getMessage('yes')}
                                    onCancel={() => this.closeDialog('joinCommunity')}
                                    cancelLabel={getMessage('cancel')}
                                    onClose={() => this.closeDialog('joinCommunity')}
                                    closeLabel={getMessage('no')}
                                    title={getMessage('confirmJoinCommunityTitle', {values: {name: communityName}})}
                                    dialogContent={
                                        <div>
                                            {loading &&
                                            <CircularProgress size={30}
                                                              classes={{root: classes.loading}}
                                                              thickness={7}/>
                                            }
                                            {getMessage('confirmJoinCommunity')}
                                        </div>
                                    }/>

                <ConfirmCloseDialog open={this.state.leaveCommunity}
                                    parentId={ids.CONFIRM_LEAVE_DLG}
                                    onConfirm={() => this.handleDialogConfirmed('leaveCommunity')}
                                    confirmLabel={getMessage('yes')}
                                    onCancel={() => this.closeDialog('leaveCommunity')}
                                    cancelLabel={getMessage('cancel')}
                                    onClose={() => this.closeDialog('leaveCommunity')}
                                    closeLabel={getMessage('no')}
                                    title={getMessage('confirmLeaveCommunityTitle', {values: {name: communityName}})}
                                    dialogContent={
                                        <div>
                                            {loading &&
                                            <CircularProgress size={30}
                                                              classes={{root: classes.loading}}
                                                              thickness={7}/>
                                            }
                                            {getMessage('confirmLeaveCommunity')}
                                        </div>
                                    }/>
            </Fragment>
        )
    }
}

CommunitiesToolbar.propTypes = {
    parentId: PropTypes.string.isRequired,
    currentCommunityType: PropTypes.string.isRequired,
    handleCommunityFilterChange: PropTypes.func.isRequired,
    onCreateCommunityClicked: PropTypes.func.isRequired,
    presenter: PropTypes.shape({
        deleteCommunity: PropTypes.func.isRequired,
        joinCommunity: PropTypes.func.isRequired,
        leaveCommunity: PropTypes.func.isRequired,
    }),
    community: PropTypes.object.isRequired,
    isCommunityAdmin: PropTypes.bool.isRequired,
    isMember: PropTypes.bool.isRequired,
    collaboratorsUtil: PropTypes.object.isRequired,
    refreshListing: PropTypes.func.isRequired,
};

export default withStyles(styles)(withI18N(CommunitiesToolbar, messages));