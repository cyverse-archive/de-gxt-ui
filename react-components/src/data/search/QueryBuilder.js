import React, { Component } from "react";
import PropTypes from "prop-types";

import Condition from "./queryBuilder/Condition";
import ids from "./ids";
import messages from "./messages";
import SaveSearchButton from "./SaveSearchButton";
import styles from "./styles";
import withStoreProvider from "../../util/StoreProvider";
import { build, getMessage, withI18N } from "@cyverse-de/ui-lib";

import Button from "@material-ui/core/Button";
import { reduxForm, Field } from "redux-form";
import { withStyles } from "@material-ui/core/styles";

/**
 * A form which allows users to build their own custom data search queries
 */
class QueryBuilder extends Component {
    constructor(props) {
        super(props);

        this.handleSubmitForm = this.handleSubmitForm.bind(this);
        this.handleSaveSearch = this.handleSaveSearch.bind(this);
        this.getOriginalName = this.getOriginalName.bind(this);
    }

    handleSubmitForm(values) {
        this.props.presenter.onSearchBtnClicked(values);
    }

    handleSaveSearch(values) {
        let originalName = this.getOriginalName();
        this.props.presenter.onSaveSearch(values, originalName);
    }

    getOriginalName() {
        let { initialValues } = this.props;
        return initialValues ? initialValues.label : null;
    }

    render() {
        const { handleSubmit, classes, parentId } = this.props;

        return (
            <div className={classes.form}>
                <Condition
                    root={true}
                    parentId={parentId}
                    helperProps={this.props}
                />
                <div className={classes.buttonBar}>
                    <Field
                        name="label"
                        originalName={this.getOriginalName()}
                        parentId={parentId}
                        handleSave={handleSubmit(this.handleSaveSearch)}
                        component={renderSaveSearchBtn}
                    />
                    <Button
                        variant="contained"
                        id={build(parentId, ids.searchBtn)}
                        className={classes.searchButton}
                        onClick={handleSubmit(this.handleSubmitForm)}
                    >
                        {getMessage("searchBtn")}
                    </Button>
                </div>
            </div>
        );
    }
}

QueryBuilder.propTypes = {
    presenter: PropTypes.shape({
        onAddTagSelected: PropTypes.func.isRequired,
        onEditTagSelected: PropTypes.func.isRequired,
        fetchTagSuggestions: PropTypes.func.isRequired,
        onSaveSearch: PropTypes.func.isRequired,
        onSearchBtnClicked: PropTypes.func.isRequired,
        searchCollaborators: PropTypes.func.isRequired,
    }),
    parentId: PropTypes.string.isRequired,
    collaboratorsUtil: PropTypes.shape({
        isTeam: PropTypes.func,
        isCollaboratorList: PropTypes.func,
        getSubjectDisplayName: PropTypes.func,
    }),
};

function renderSaveSearchBtn(props) {
    const { input, disabled, ...custom } = props;
    return <SaveSearchButton disabled={disabled} {...input} {...custom} />;
}

export default withStoreProvider(
    reduxForm({
        form: "dataQueryBuilder",
        enableReinitialize: true,
    })(withStyles(styles)(withI18N(QueryBuilder, messages)))
);
