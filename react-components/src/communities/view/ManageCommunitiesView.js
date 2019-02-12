import CommunitiesToolbar from "./CommunitiesToolbar";
import CommunityFilter from "./CommunityFilter";
import CommunityListing from "./CommunityListing";
import PrivilegeTypes from "../PrivilegeTypes";
import styles from "../styles";

import PropTypes from "prop-types";
import React, { Component } from "react";
import EditCommunityDialog from "./EditCommunityDialog";
import { withStyles } from "@material-ui/core/styles";

/**
 * @author aramsey
 *
 * A component that displays communities so a user can manage the communities they belong to
 * and view all communities
 */
class ManageCommunitiesView extends Component {
    constructor(props) {
        super(props);

        this.state = {
            loadingListing: false,
            communityType: CommunityFilter.MY_COMMUNITIES,
            communitiesList: [],
            editDlgOpen: false,
            isCommunityAdmin: false,
            isCommunityMember: false,
            selectedCommunity: null
        };

        [
            'handleCommunityFilterChange',
            'onCreateCommunityClicked',
            'handleCloseEditDlg',
            'onCommunityClicked',
            'onCommunitySelected',
            'onCommunitySaved',
            'refreshCommunityList',
        ].forEach((fn) => this[fn] = this[fn].bind(this));
    }

    componentDidMount() {
        this.handleCommunityFilterChange(CommunityFilter.MY_COMMUNITIES);
    }

    handleCommunityFilterChange(selection) {
        this.setState({communityType: selection});

        this.refreshCommunityList(selection);
    }

    refreshCommunityList(selection) {
        if (!selection) {
            selection = this.state.communityType;
        }

        this.setState({loadingListing: true});

        if (selection === CommunityFilter.MY_COMMUNITIES) {
            new Promise((resolve, reject) => {
                this.props.presenter.fetchMyCommunities(resolve, reject);
            }).then(listing => {
                this.setState({
                    communitiesList: listing.groups,
                    loadingListing: false,
                })
            }).catch(() => {
                this.setState({loadingListing: false})
            });
        } else {
            new Promise((resolve, reject) => {
                this.props.presenter.fetchAllCommunities(resolve, reject);
            }).then(listing => {
                this.setState({
                    communitiesList: listing.groups,
                    loadingListing: false,
                })
            }).catch(() => this.setState({loadingListing: false}));
        }
    }

    onCreateCommunityClicked() {
        this.setState({
            editDlgOpen: true,
            isCommunityAdmin: true,
            isCommunityMember: true,
            selectedCommunity: null
        })
    }

    onCommunityClicked(community) {
        const privileges = community.privileges;
        this.setState({
            isCommunityAdmin: privileges && privileges.includes(PrivilegeTypes.ADMIN),
            isCommunityMember: community.member,
            editDlgOpen: true,
            selectedCommunity: community
        })
    }

    onCommunitySelected(community) {
        const privileges = community.privileges;
        this.setState({
            selectedCommunity: community,
            isCommunityAdmin: privileges && privileges.includes(PrivilegeTypes.ADMIN),
            isCommunityMember: community.member,
        })
    }

    onCommunitySaved(community) {
        this.setState({selectedCommunity: community})
    }

    handleCloseEditDlg() {
        this.setState({editDlgOpen: false});
        this.refreshCommunityList();
    }

    render() {
        const {
            communitiesList,
            editDlgOpen,
            isCommunityAdmin,
            isCommunityMember,
            selectedCommunity,
            loadingListing,
        } = this.state;

        const {
            parentId,
            collaboratorsUtil,
            presenter,
            currentUser,
            classes,
        } = this.props;

        return (
            <div className={classes.wrapper}>
                <CommunitiesToolbar parentId={parentId}
                                    community={selectedCommunity}
                                    isCommunityAdmin={isCommunityAdmin}
                                    isMember={isCommunityMember}
                                    presenter={presenter}
                                    collaboratorsUtil={collaboratorsUtil}
                                    currentCommunityType={this.state.communityType}
                                    onCreateCommunityClicked={this.onCreateCommunityClicked}
                                    refreshListing={this.refreshCommunityList}
                                    handleCommunityFilterChange={(event) => this.handleCommunityFilterChange(event.target.value)}/>
                <CommunityListing collaboratorsUtil={collaboratorsUtil}
                                  parentId={parentId}
                                  data={communitiesList}
                                  loading={loadingListing}
                                  onCommunityClicked={this.onCommunityClicked}
                                  onCommunitySelected={this.onCommunitySelected}/>
                <EditCommunityDialog open={editDlgOpen}
                                     collaboratorsUtil={collaboratorsUtil}
                                     presenter={presenter}
                                     currentUser={currentUser}
                                     community={selectedCommunity}
                                     isCommunityAdmin={isCommunityAdmin}
                                     isMember={isCommunityMember}
                                     onCommunitySaved={this.onCommunitySaved}
                                     onClose={this.handleCloseEditDlg}/>
            </div>
        )
    }
}

ManageCommunitiesView.propTypes = {
    parentId: PropTypes.string,
    collaboratorsUtil: PropTypes.object.isRequired,
    currentUser: PropTypes.shape({
        name: PropTypes.string.isRequired,
        id: PropTypes.string.isRequired,
    }),
    presenter: PropTypes.shape({
        fetchMyCommunities: PropTypes.func.isRequired,
        fetchAllCommunities: PropTypes.func.isRequired,
        fetchCommunityAdmins: PropTypes.func.isRequired,
        fetchCommunityApps: PropTypes.func.isRequired,
        getCommunityAdmins: PropTypes.func.isRequired,
        getCommunityMembers: PropTypes.func.isRequired,
        searchCollaborators: PropTypes.func.isRequired,
    })
};

export default withStyles(styles)(ManageCommunitiesView);