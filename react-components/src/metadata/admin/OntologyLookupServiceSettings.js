/**
 * @author psarando
 */
import React, { Component } from "react";
import { Field, FieldArray } from "redux-form";

import { FormSelectField } from "../../util/FormField";
import styles from "../style";
import StringListEditor from "./StringListEditor";

import Grid from '@material-ui/core/Grid';
import MenuItem from "@material-ui/core/MenuItem";
import { withStyles } from "@material-ui/core/styles";


const OLSEntityTypes = [
    "CLASS",
    "PROPERTY",
    "INDIVIDUAL",
    "ONTOLOGY",
];

const OLSEntityTypeMenuItems = OLSEntityTypes.map((type, index) => (<MenuItem key={index} value={type}>{type.toLowerCase()}</MenuItem>));

class OntologyLookupServiceSettings extends Component {
    render() {
        return (
            <Grid container
                  spacing={16}
                  direction="column"
                  justify="flex-start"
                  alignItems="stretch"
            >
                <Grid item>
                    <fieldset>
                        <legend>Entity Type</legend>

                        <Field name="type"
                               id="attrSettingsEntityType"
                               label="Restrict searches to an entity type:"
                               component={FormSelectField}
                        >
                            {OLSEntityTypeMenuItems}
                        </Field>
                    </fieldset>
                </Grid>

                <Grid item>
                    <FieldArray name="ontology"
                                component={StringListEditor}
                                title="Ontologies"
                                helpLabel="Restrict searches to a set of ontologies:"
                                columnLabel="OLS Ontology ID"
                    />
                </Grid>

                <Grid item>
                    <FieldArray name="childrenOf"
                                component={StringListEditor}
                                title="Children"
                                helpLabel="Restrict searches to all children of a given term (subclassOf/is-a relation only):"
                                columnLabel="IRI"
                    />
                </Grid>

                <Grid item>
                    <FieldArray name="allChildrenOf"
                                component={StringListEditor}
                                title="All Children"
                                helpLabel="Restrict searches to all children of a given term (subclassOf/is-a plus any hierarchical/transitive properties):"
                                columnLabel="IRI"
                    />
                </Grid>
            </Grid>
        );
    }
}

export default withStyles(styles)(OntologyLookupServiceSettings);
