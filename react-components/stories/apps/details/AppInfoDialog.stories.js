import React, { Component } from "react";
import AppInfoDialog from "../../../src/apps/details/AppInfoDialog";
import { text } from '@storybook/addon-knobs';


class AppInfoDialogTest extends Component {
    render()  {
        const appDetals ={
            "is_favorite": true,
            "beta": false,
            "deleted": false,
            "disabled": false,
            "is_public": true,
            "can_run": true,
            "step_count": 1,
            "app_type": "DE",
            "description": "This App will add existing reference annotation information to newly assembled transcripts in GFF format.",
            "id": "676846d4-854a-11e4-980d-7f0fcca75dbb",
            "integrator_email": "rogerab@email.arizona.edu",
            "integrator_name": "Roger Barthelson",
            "name": "Annotate transcripts",
            "permission": "read",
            "system_id": "de",
            "wiki_url": "https://wiki.cyverse.org/wiki/display/DEapps/Word+Count",
            "edited_date": "1369428963000",
            "integration_date": "1369431889000",
            "references": ["", "http://trinityrnaseq.sourceforge.net/analysis/diff_expression_analysis.html"],
            "categories": [{
                "id": "f9f22c5a-09f5-4630-997c-4e3a00ae924b",
                "name": "Assembly Annotation"
            }],
            "suggested_categories": [{
                "id": "f9f22c5a-09f5-4630-997c-4e3a00ae924b",
                "name": "Assembly Annotation"
            }],
            "hierarchies": [{
                "iri": "http://edamontology.org/topic_0003",
                "label": "Topic",
                "subclasses": [{
                    "iri": "http://edamontology.org/topic_3307",
                    "label": "Computational biology",
                    "subclasses": [{
                        "iri": "http://edamontology.org/topic_3321",
                        "label": "Molecular genetics",
                        "subclasses": [{
                            "iri": "http://edamontology.org/topic_0203",
                            "label": "Gene expression",
                            "subclasses": [{
                                "iri": "http://edamontology.org/topic_3308",
                                "label": "Transcriptomics"
                            }]
                        }]
                    }]
                }, {
                    "iri": "http://edamontology.org/topic_3391",
                    "label": "Omics",
                    "subclasses": [{
                        "iri": "http://edamontology.org/topic_0622",
                        "label": "Genomics",
                        "subclasses": [{
                            "iri": "http://edamontology.org/topic_3308",
                            "label": "Transcriptomics"
                        }]
                    }]
                }, {
                    "iri": "http://edamontology.org/topic_3070",
                    "label": "Biology",
                    "subclasses": [{
                        "iri": "http://edamontology.org/topic_3053",
                        "label": "Genetics",
                        "subclasses": [{
                            "iri": "http://edamontology.org/topic_3321",
                            "label": "Molecular genetics",
                            "subclasses": [{
                                "iri": "http://edamontology.org/topic_0203",
                                "label": "Gene expression",
                                "subclasses": [{
                                    "iri": "http://edamontology.org/topic_3308",
                                    "label": "Transcriptomics"
                                }]
                            }]
                        }]
                    }]
                }]
            }, {
                "iri": "http://edamontology.org/operation_0004",
                "label": "Operation",
                "subclasses": [{
                    "iri": "http://edamontology.org/operation_0226",
                    "label": "Annotation",
                    "subclasses": [{
                        "iri": "http://edamontology.org/operation_0361",
                        "label": "Sequence annotation",
                        "subclasses": [{
                            "iri": "http://edamontology.org/operation_3672",
                            "label": "Gene functional annotation"
                        }]
                    }]
                }, {
                    "iri": "http://edamontology.org/operation_2428",
                    "label": "Validation",
                    "subclasses": [{
                        "iri": "http://edamontology.org/operation_3180",
                        "label": "Sequence assembly validation"
                    }]
                }, {
                    "iri": "http://edamontology.org/operation_2945",
                    "label": "Analysis",
                    "subclasses": [{
                        "iri": "http://edamontology.org/operation_2403",
                        "label": "Sequence analysis",
                        "subclasses": [{
                            "iri": "http://edamontology.org/operation_2478",
                            "label": "Nucleic acid sequence analysis",
                            "subclasses": [{
                                "iri": "http://edamontology.org/operation_3180",
                                "label": "Sequence assembly validation"
                            }]
                        }]
                    }, {
                        "iri": "http://edamontology.org/operation_2501",
                        "label": "Nucleic acid analysis",
                        "subclasses": [{
                            "iri": "http://edamontology.org/operation_2478",
                            "label": "Nucleic acid sequence analysis",
                            "subclasses": [{
                                "iri": "http://edamontology.org/operation_3180",
                                "label": "Sequence assembly validation"
                            }]
                        }]
                    }]
                }]
            }],
            "tools": [{
                "attribution": "rogerab",
                "description": "gffintersect",
                "id": "66fbef16-854a-11e4-9d48-ab603f97c137",
                "location": "/usr/local2/AnnotateTranscripts/annotate_transcripts",
                "name": "gffintersect_wrapper.pl",
                "type": "executable",
                "version": "1.0",
                "container": {
                    "image": {
                        "deprecated": true,
                        "name": "docker.cyverse.org/backwards-compat",
                        "tag": "latest",
                        "url": "https://registry.hub.docker.com/u/discoenv/backwards-compat"
                    }
                }
            }],
            "rating": {
                "average": 4,
                "total": 1,
                "user": 4
            },
            "job_stats": {},
            "pipeline_eligibility": {
                "is_valid": true,
                "reason": ""
            }
        };
        const presenter = {
            getAppDoc: (id, successCallback, errCallback) => {
                successCallback();
            },
            onAppFavoriteSelected: (appDetails, successCallback, errCallback) => {
                console.log("Favorite clicked!");
            },
            onAppRatingSelected: (appDetails, val, successCallback, errCallback) => {
                console.log("Rating value: " + val);
            },
            onAppRatingDeSelected: (appDetails, successCallback, errCallback) => {
                console.log("Rating deleted!");
            }
        };
        return (
            <AppInfoDialog baseDebugId="appDetails"
                           searchRegexPattern={text('Search Pattern', 'roger')}
                           app={appDetals}
                           dialogOpen={true}
                           presenter={presenter}
                           editable={false}/>
        )
    }
}
export default  AppInfoDialogTest;