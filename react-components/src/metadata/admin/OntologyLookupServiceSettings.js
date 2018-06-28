/**
 * @author psarando
 */
import React, { Component } from "react";

import styles from "../style";
import StringListEditor from "./StringListEditor";

import FormControl from "@material-ui/core/FormControl";
import Grid from '@material-ui/core/Grid';
import Input from "@material-ui/core/Input";
import InputLabel from "@material-ui/core/InputLabel";
import MenuItem from "@material-ui/core/MenuItem";
import Select from "@material-ui/core/Select";
import { withStyles } from "@material-ui/core/styles";


const OLSEntityTypes = [
    "CLASS",
    "PROPERTY",
    "INDIVIDUAL",
    "ONTOLOGY",
];

const OLSEntityTypeMenuItems = OLSEntityTypes.map((type, index) => (<MenuItem key={index} value={type}>{type.toLowerCase()}</MenuItem>));

class OntologyLookupServiceSettings extends Component {
    handleChange = (key, value) => {
        this.props.onSettingsChanged({
            ...this.props.settings,
            [key]: value,
        });
    };

    render() {
        const { settings } = this.props;
        const { type, ontology, childrenOf, allChildrenOf } = settings;

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

                        <FormControl fullWidth>
                            <InputLabel htmlFor="entity-type">Restrict searches to an entity type:</InputLabel>
                            <Select
                                value={type || "CLASS"}
                                onChange={event => this.handleChange("type", event.target.value)}
                                input={<Input id="entity-type" />}
                            >
                                {OLSEntityTypeMenuItems}
                            </Select>
                        </FormControl>
                    </fieldset>
                </Grid>

                <Grid item>
                    <StringListEditor title="Ontologies"
                                      helpLabel="Restrict searches to a set of ontologies:"
                                      columnLabel="OLS Ontology ID"
                                      values={ontology || []}
                                      onValuesChanged={ontology => this.handleChange("ontology", ontology)}
                    />
                </Grid>

                <Grid item>
                    <StringListEditor title="Children"
                                      helpLabel="Restrict searches to all children of a given term (subclassOf/is-a relation only):"
                                      columnLabel="IRI"
                                      values={childrenOf || []}
                                      onValuesChanged={childrenOf => this.handleChange("childrenOf", childrenOf)}
                    />
                </Grid>

                <Grid item>
                    <StringListEditor title="All Children"
                                      helpLabel="Restrict searches to all children of a given term (subclassOf/is-a plus any hierarchical/transitive properties):"
                                      columnLabel="IRI"
                                      values={allChildrenOf || []}
                                      onValuesChanged={allChildrenOf => this.handleChange("allChildrenOf", allChildrenOf)}
                    />
                </Grid>
            </Grid>
        );
    }
}

export default withStyles(styles)(OntologyLookupServiceSettings);
