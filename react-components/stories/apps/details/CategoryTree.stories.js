/**
 * @author psarando
 */
import React, { Component } from 'react';
import CategoryTree from '../../../src/apps/details/CategoryTree';

class CategoryTreeTest extends Component {
    render() {
        const logger = this.props.logger || ((selection) => {
            console.log(selection);
        });

        const presenter = {
            onDetailsCategoryClicked: logger
        };

        const app = {
            hierarchies: [
                {
                    "iri": "http://edamontology.org/topic_0003",
                    "label": "Topic",
                    "subclasses": [
                        {
                            "iri": "http://edamontology.org/topic_3068",
                            "label": "Literature and reference",
                            "subclasses": [
                                {
                                    "iri": "http://edamontology.org/topic_0218",
                                    "label": "Text mining"
                                }
                            ]
                        },
                        {
                            "iri": "http://edamontology.org/topic_0605",
                            "label": "Informatics",
                            "subclasses": [
                                {
                                    "iri": "http://edamontology.org/topic_3071",
                                    "label": "Data management"
                                }
                            ]
                        }
                    ]
                },
                {
                    "iri": "http://edamontology.org/operation_0004",
                    "label": "Operation",
                    "subclasses": [
                        {
                            "iri": "http://edamontology.org/operation_2409",
                            "label": "Data handling",
                            "subclasses": [
                                {
                                    "iri": "http://edamontology.org/operation_3096",
                                    "label": "Editing"
                                }
                            ]
                        }
                    ]
                }
            ]
        };

        return ( <CategoryTree app={app} presenter={presenter} /> );
    }
}

export default CategoryTreeTest;
