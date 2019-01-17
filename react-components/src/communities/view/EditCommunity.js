import AddBtn from "../../data/search/queryBuilder/AddBtn";
import AppGridListing from "../../apps/listing/AppGridListing";
import build from "../../util/DebugIDUtil";
import CollaboratorListing from "../../collaborators/CollaboratorListing";
import { getMessage } from "../../util/I18NWrapper";
import ids from "../ids";
import messages from "../messages";
import styles from "../styles";
import SubjectSearchField from "../../collaborators/SubjectSearchField";
import withI18N from "../../util/I18NWrapper";

import CircularProgress from "@material-ui/core/CircularProgress/CircularProgress";
import PropTypes from "prop-types";
import React, { Component } from "react";
import TextField from "@material-ui/core/TextField";
import Toolbar from "@material-ui/core/Toolbar";
import { withStyles } from "@material-ui/core";

/**
 * @author aramsey
 *
 * A component that allows users to create or edit a community
 */
class EditCommunity extends Component {
    constructor(props) {
        super(props);

        this.state = {
            errors: {
                invalidName: false,
                emptyName: false,
                text: "",
            },
            name: "",
            description: "",
            admins: [],
            apps: [],
            loading: false,
        };

        [
            'handleDescChange',
            'handleNameChange',
            'onAdminSelected',
            'addAdmin',
            'handleRemoveApp',
            'removeApp',
            'handleRemoveAdmin',
            'removeAdmin',
            'onAddCommunityAppsClicked',
            'addApp',
            'validate',
            'isInvalid',
            'isDuplicateApp',
            'isDuplicateAdmin',
        ].forEach((fn) => this[fn] = this[fn].bind(this));
    }

    componentWillReceiveProps(nextProps) {
        if (!this.props.saveCommunity && nextProps.saveCommunity && !this.isInvalid()) {
            const {
                name, description, admins, apps
            } = this.state;
            const {community} = this.props;
            this.props.presenter.saveCommunity(community, name, description, admins, apps, () => {
                this.props.onCommunitySaved();
            })
        }
    }

    componentDidMount() {
        const {
            community,
            collaboratorsUtil,
        } = this.props;

        if (community) {
            this.setState({loading: true});
            let promises = [];

            let fetchAdminsPromise = new Promise((resolve, reject) => {
                    this.props.presenter.fetchCommunityAdmins(community, resolve, reject);
                }
            );

            let fetchAppsPromise = new Promise((resolve, reject) => {
                    this.props.presenter.fetchCommunityApps(community, resolve, reject);
                }
            );

            promises.push(fetchAdminsPromise, fetchAppsPromise);
            Promise.all(promises)
                .then(value => {
                    let communityName = collaboratorsUtil.getSubjectDisplayName(community);
                    this.setState({
                        errors: this.validate(communityName),
                        name: communityName,
                        description: community.description,
                        admins: value[0].members,
                        apps: value[1].apps,
                        loading: false,
                    })
                })
                .catch(error => {
                    console.log(error);
                    this.setState({loading: false});
                });
        } else {
            this.setState({
                errors: this.validate(null),
            });
        }
    }

    validate(name) {
        if (!name || name.length === 0) {
            return ({
                emptyName: true,
                text: getMessage('emptyField'),
            })
        } else if (name.includes(":")) {
            return ({
                invalidName: true,
                text: getMessage('invalidColonChar'),

            })
        } else {
            return ({
                invalidName: false,
                emptyName: false,
                text: "",
            })
        }
    }

    isInvalid() {
        const { errors } = this.state;
        return errors.emptyName || errors.invalidName;
    }

    onAddCommunityAppsClicked() {
        const {community} = this.props;
        this.props.presenter.onAddCommunityAppsClicked((app) => {
            if (app && !this.isDuplicateApp(app)) {
                if (community) {
                    this.setState({loading: true});
                    this.props.presenter.addAppToCommunity(app, community, () => {
                        this.addApp(app);
                        this.setState({loading: false});
                    })
                } else {
                    this.addApp(app)
                }
            }
        });
    }

    isDuplicateApp(app) {
        const {apps} = this.state;
        return apps.some((value) => value.id === app.id)
    }

    handleRemoveAdmin(subject) {
        const {community} = this.props;

        if (community) {
            this.setState({loading: true});
            this.props.presenter.removeCommunityAdmins(community, subject, () => {
                this.removeAdmin(subject);
                this.setState({loading: false});
            })
        } else {
            this.removeAdmin(subject)
        }
    }

    removeAdmin(subject) {
        const {admins} = this.state;
        this.setState({
            admins: admins.filter((value) => value.id !== subject.id)
        })
    }

    handleRemoveApp(app) {
        const {community} = this.props;
        if (community) {
            this.setState({loading: true});
            this.props.presenter.removeCommunityApps(community, app, () => {
                this.removeApp(app);
                this.setState({loading: false});
            })
        } else {
            this.removeApp(app)
        }
    }

    removeApp(app) {
        const {apps} = this.state;
        this.setState({
            apps: apps.filter((value) => value.id !== app.id)
        })
    }

    handleDescChange(event) {
        let value = event.target.value;
        this.setState({
            description: value
        });
    }

    handleNameChange(event) {
        let value = event.target.value;
        this.setState({
            errors: this.validate(value),
            name: value
        });
    }

    onAdminSelected(subject) {
        const {community} = this.props;

        if (!this.isDuplicateAdmin(subject)) {
            if (community) {
                this.setState({loading: true});
                this.props.presenter.addCommunityAdmin(community, subject, () => {
                    this.addAdmin(subject);
                    this.setState({loading: false});
                });
            } else {
                this.addAdmin(subject);
            }
        }
    }

    isDuplicateAdmin(subject) {
        const {admins} = this.state;
        return admins.some((value) => value.id === subject.id)
    }

    addAdmin(subject) {
        let newAdmins = [];
        this.setState({
            admins: newAdmins.concat(this.state.admins, subject),
        })
    }

    addApp(app) {
        let newApps = [];
        this.setState({
            apps: newApps.concat(this.state.apps, app)
        })
    }

    render() {
        const {
            admins,
            apps,
            name,
            errors,
            loading,
        } = this.state;

        const {
            isCommunityAdmin,
            parentId,
            collaboratorsUtil,
            presenter,
            classes,
        } = this.props;

        const toolbarId = build(parentId, ids.EDIT.TOOLBAR);

        return (
            <div className={classes.wrapper}>
                <form className={classes.column}>
                    {loading &&
                    <CircularProgress size={30} classes={{root: classes.loading}} thickness={7}/>
                    }
                    <TextField id={build(parentId, ids.EDIT.NAME)}
                               error={this.isInvalid()}
                               required
                               label={getMessage('nameField')}
                               value={name}
                               className={classes.formItem}
                               disabled={!isCommunityAdmin}
                               helperText={errors.text}
                               onChange={this.handleNameChange}/>
                    <TextField id={build(parentId, ids.EDIT.DESCRIPTION)}
                               multiline
                               label={getMessage('descriptionField')}
                               value={this.state.description}
                               className={classes.formItem}
                               disabled={!isCommunityAdmin}
                               onChange={this.handleDescChange}/>
                    <fieldset className={classes.formItem}>
                        <legend>{getMessage('communityAdmins')}</legend>
                        {isCommunityAdmin &&
                        <Toolbar>
                            <div className={classes.subjectSearch}>
                                <SubjectSearchField collaboratorsUtil={collaboratorsUtil}
                                                    presenter={presenter}
                                                    parentId={parentId}
                                                    onSelect={this.onAdminSelected}/>
                            </div>
                        </Toolbar>
                        }
                        <CollaboratorListing parentId={parentId}
                                             collaboratorsUtil={collaboratorsUtil}
                                             data={admins}
                                             deletable={isCommunityAdmin}
                                             onDeleteCollaborator={this.handleRemoveAdmin}/>
                    </fieldset>
                    <fieldset className={classes.formItem}>
                        <legend>{getMessage('apps')}</legend>
                        {isCommunityAdmin &&
                        <Toolbar id={toolbarId}>
                            <AddBtn onClick={this.onAddCommunityAppsClicked}/>
                        </Toolbar>
                        }
                        <AppGridListing parentId={parentId}
                                        data={apps}
                                        selectable={false}
                                        deletable={isCommunityAdmin}
                                        onRemoveApp={this.handleRemoveApp}/>
                    </fieldset>
                </form>
            </div>
        )
    }
}

EditCommunity.propTypes = {
    parentId: PropTypes.string.isRequired,
    collaboratorsUtil: PropTypes.object.isRequired,
    community: PropTypes.object,
    isCommunityAdmin: PropTypes.bool.isRequired,
    saveCommunity: PropTypes.bool.isRequired,
    onCommunitySaved: PropTypes.func.isRequired,
    presenter: PropTypes.shape({
        fetchCommunityAdmins: PropTypes.func.isRequired,
        fetchCommunityApps: PropTypes.func.isRequired,
        searchCollaborators: PropTypes.func.isRequired,
        removeCommunityApps: PropTypes.func.isRequired,
        removeCommunityAdmins: PropTypes.func.isRequired,
        onAddCommunityAppsClicked: PropTypes.func.isRequired,
        addCommunityAdmin: PropTypes.func.isRequired,
        addAppToCommunity: PropTypes.func.isRequired,
    })
};

export default withStyles(styles)(withI18N(EditCommunity, messages));