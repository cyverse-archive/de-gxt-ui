import CommunitiesToolbar from "./CommunitiesToolbar";
import CommunityListing from "./CommunityListing";

import PropTypes from "prop-types";
import React, { Component } from "react";
import EditCommunityDialog from "./EditCommunityDialog";

/**
 * @author aramsey
 *
 * A component that displays communities so a user can manage the communities they belong to
 * and view all communities
 */
class CommunitiesView extends Component {
    constructor(props) {
        super(props);

        this.state = {
            communityType: "MyCommunities",
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
            'onCommunityClicked'
        ].forEach((fn) => this[fn] = this[fn].bind(this));
    }

    componentDidMount() {
        this.handleCommunityFilterChange("MyCommunities");
    }

    handleCommunityFilterChange(selection) {
        this.setState({communityType: selection});

        if (selection === "MyCommunities") {
            this.props.presenter.fetchMyCommunities((listing) => {this.setState({communitiesList: listing.groups})});
        } else {
            this.props.presenter.fetchAllCommunities((listing) => this.setState({communitiesList: listing.groups}));
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
        this.props.presenter.fetchCommunityPrivileges(community, (isCommunityAdmin, isCommunityMember) => this.setState({
            isCommunityAdmin: isCommunityAdmin,
            isCommunityMember: isCommunityMember,
            editDlgOpen: true,
            selectedCommunity: community
        }));
    }

    handleCloseEditDlg() {
        this.setState({editDlgOpen: false})
    }

    render() {
        const {
            communitiesList,
            editDlgOpen,
            isCommunityAdmin,
            isCommunityMember,
            selectedCommunity
        } = this.state;

        const {
            parentId,
            collaboratorsUtil,
            presenter
        } = this.props;

        return (
            <div>
                <CommunitiesToolbar parentId={parentId}
                                    currentCommunityType={this.state.communityType}
                                    onCreateCommunityClicked={this.onCreateCommunityClicked}
                                    handleCommunityFilterChange={(event) => this.handleCommunityFilterChange(event.target.value)}/>
                <CommunityListing collaboratorsUtil={collaboratorsUtil}
                                  parentId={parentId}
                                  data={communitiesList}
                                  onCommunityClicked={this.onCommunityClicked}/>
                <EditCommunityDialog open={editDlgOpen}
                                     collaboratorsUtil={collaboratorsUtil}
                                     presenter={presenter}
                                     community={selectedCommunity}
                                     isCommunityAdmin={isCommunityAdmin}
                                     isMember={isCommunityMember}
                                     onClose={this.handleCloseEditDlg}/>
            </div>
        )
    }
}

CommunitiesView.propTypes = {
    parentId: PropTypes.string,
    collaboratorsUtil: PropTypes.object.isRequired,
    presenter: PropTypes.shape({
        fetchMyCommunities: PropTypes.func.isRequired,
        fetchAllCommunities: PropTypes.func.isRequired,
        fetchCommunityAdmins: PropTypes.func.isRequired,
        fetchCommunityApps: PropTypes.func.isRequired,
        fetchCommunityPrivileges: PropTypes.func.isRequired,
        searchCollaborators: PropTypes.func.isRequired,
    })
};

export default CommunitiesView;