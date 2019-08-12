import React, { Component } from "react";
import PropTypes from "prop-types";

import Clause from "./queryBuilder/Clause";
import ids from "./ids";
import messages from "./messages";
import SaveSearchButton from "./SaveSearchButton";
import styles from "./styles";
import { validate } from "./queryBuilder/Validations";

import { build, getMessage, withI18N } from "@cyverse-de/ui-lib";
import { Field, Form, withFormik } from "formik";
import { Button, withStyles } from "@material-ui/core";

/**
 * A form which allows users to build their own custom data search queries
 */
class QueryBuilder extends Component {
    constructor(props) {
        super(props);

        this.handleSaveSearch = this.handleSaveSearch.bind(this);
    }

    handleSaveSearch(values) {
        const { initialValues } = this.props;
        let originalName = initialValues ? initialValues.label : null;
        this.props.presenter.onSaveSearch(values, originalName);
    }

    render() {
        const {
            handleSubmit,
            classes,
            parentId,
            collaboratorsUtil,
            presenter,
        } = this.props;

        return (
            <Form className={classes.form}>
                <Field
                    root={true}
                    parentId={parentId}
                    presenter={presenter}
                    collaboratorsUtil={collaboratorsUtil}
                    component={Clause}
                />
                <div className={classes.buttonBar}>
                    <Field
                        name="label"
                        render={(props) => (
                            <SaveSearchButton
                                handleSave={this.handleSaveSearch}
                                parentId={parentId}
                                {...props}
                            />
                        )}
                    />
                    <Button
                        variant="contained"
                        type="submit"
                        onClick={handleSubmit}
                        id={build(parentId, ids.searchBtn)}
                        className={classes.searchButton}
                    >
                        {getMessage("searchBtn")}
                    </Button>
                </div>
            </Form>
        );
    }
}

const handleSubmit = (values, { props }) => {
    props.presenter.onSearchBtnClicked(values);
};

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

export default withFormik({
    mapPropsToValues: ({ initialValues }) => ({ ...initialValues }),
    validate,
    handleSubmit,
})(withStyles(styles)(withI18N(QueryBuilder, messages)));
