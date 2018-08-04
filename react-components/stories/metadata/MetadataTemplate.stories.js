import React, { Component } from "react";

import EditMetadataTemplate from "../../src/metadata/admin/EditMetadataTemplate";

const presenter = (logger) => ({
    onSaveTemplate: (template, resolve, reject) => {
        setTimeout(() => {
                logger(template);
                resolve(template);
            },
            1500
        );
    },
    closeTemplateInfoDialog: () => logger("dialog closed."),
});

const nestedAttrMetadataTemplate = {
    "id": "91334572-5e13-11e8-acc0-f64e9b87c109",
    "name": "Test Metadata Template",
    "description": "Template Description",
    "deleted": false,
    "created_on": "2018-05-21T12:00:00Z",
    "modified_on": "2018-05-25T19:23:42Z",
    "attributes": [
        {
            "name": "String Attr",
            "description": "String Description",
            "type": "String",
            "required": true,
            "attributes": [
                {
                    "name": "String Sub-Attr",
                    "description": "String Description",
                    "type": "String",
                    "required": true,
                    "attributes": [
                        {
                            "name": "Sub-String Sub-Attr",
                            "description": "String Description",
                            "type": "String",
                            "required": true,
                        },
                    ],
                },
                {
                    "name": "URL/URI Sub-Attr",
                    "description": "URL/URI Description",
                    "type": "URL/URI",
                    "required": false,
                },
            ],
        },
        {
            "name": "Timestamp Attr",
            "description": "Timestamp Description",
            "type": "Timestamp",
            "required": false,
        },
        {
            "name": "Boolean Attr",
            "description": "Boolean Description",
            "type": "Boolean",
            "required": false,
        },
        {
            "name": "Number Attr",
            "description": "Number Description",
            "type": "Number",
            "required": false,
        },
        {
            "name": "Integer Attr",
            "description": "Integer Description",
            "type": "Integer",
            "required": true,
        },
        {
            "name": "Multiline Text Attr",
            "description": "Multiline Text Description",
            "type": "Multiline Text",
            "required": false,
        },
        {
            "name": "URL/URI Attr",
            "description": "URL/URI Description",
            "type": "URL/URI",
            "required": false,
        },
        {
            "name": "Enum Attr",
            "description": "Enum Description",
            "type": "Enum",
            "required": false,
            "values": [
                {
                    "value": "choice 1",
                    "is_default": false,
                },
                {
                    "value": "choice 2",
                    "is_default": true,
                },
                {
                    "value": "choice 3",
                    "is_default": false,
                },
            ],
        },
        {
            "name": "OLS Ontology Term Attr",
            "description": "OLS Ontology Term Description",
            "type": "OLS Ontology Term",
            "required": false,
            "settings": {
                "allChildrenOf": [],
                "childrenOf": [],
                "ontology": [
                    "edam"
                ],
                "type": "CLASS"
            },
        },
        {
            "name": "UAT Ontology Term Attr",
            "description": "UAT Ontology",
            "type": "UAT Ontology Term",
            "required": false,
        },
    ],
};

class EditNestedAttrMetadataTemplateTest extends Component {
    render () {
        const logger = this.props.logger || ((template) => {
            console.log(template);
        });

        return (
            <EditMetadataTemplate open presenter={presenter(logger)} initialValues={nestedAttrMetadataTemplate} />
        );
    }
}

const dataciteMetadataTemplate = {
    "id": "ae75bc42-45ec-11e5-801c-43dab0dfe096",
    "name": "DOI - DC 4.1",
    "description": "DataCite 4.1 metadata template for DOI submissions",
    "deleted": false,
    "attributes": [
        {
            "id": "5e0c923a-7415-11e8-8326-008cfa5ae621",
            "name": "title",
            "description": "A name or title by which the dataset or resource is known.",
            "required": true,
            "type": "String",
            "attributes": [
                {
                    "name": "titleType",
                    "description": "",
                    "required": false,
                    "type": "String"
                },
                {
                    "name": "xml:lang",
                    "description": "",
                    "required": false,
                    "type": "Enum",
                    "values": [
                        {
                            "value": "en-us",
                            "is_default": true
                        }
                    ]
                }
            ]
        },
        {
            "id": "5e0f2ed2-7415-11e8-8326-008cfa5ae621",
            "name": "creator",
            "description": "The main researcher involved in producing the data, or the lead author of the publication in priority order. May be a corporate/institutional or personal name. Additional Creator fields may be added. Use the Contributor field to add names of co-contributors.",
            "required": true,
            "type": "String",
            "attributes": [
                {
                    "id": "5e10ea38-7415-11e8-8326-008cfa5ae621",
                    "name": "affiliation",
                    "description": "The organizational or institutional affiliation of the creator.",
                    "required": true,
                    "type": "String"
                },
                {
                    "id": "5e129e64-7415-11e8-8326-008cfa5ae621",
                    "name": "nameIdentifier",
                    "description": "ORCID of Creator, if known. ORCID provides a persistent digital identifier that distinguishes you from every other researcher. See DOI FAQ page.",
                    "required": false,
                    "type": "String",
                    "attributes": [
                        {
                            "id": "5e143ada-7415-11e8-8326-008cfa5ae621",
                            "name": "nameIdentifierScheme",
                            "description": "",
                            "required": false,
                            "type": "Enum",
                            "values": [
                                {
                                    "id": "5e1595a6-7415-11e8-8326-008cfa5ae621",
                                    "is_default": true,
                                    "value": "ORCID"
                                }
                            ]
                        },
                        {
                            "name": "schemeURI",
                            "description": "",
                            "required": false,
                            "type": "URL/URI"
                        }
                    ]
                }
            ]
        },
        {
            "id": "5e165c7a-7415-11e8-8326-008cfa5ae621",
            "name": "publisher",
            "description": "Publisher of the dataset or resource.",
            "required": true,
            "type": "Enum",
            "values": [
                {
                    "id": "5e1785c8-7415-11e8-8326-008cfa5ae621",
                    "is_default": true,
                    "value": "CyVerse Data Commons"
                }
            ]
        },
        {
            "id": "5e183252-7415-11e8-8326-008cfa5ae621",
            "name": "publicationYear",
            "description": "Year the DOI is issued. Leave blank. Will be filled in after DOI or ARK is issued.",
            "required": false,
            "type": "String"
        },
        {
            "id": "d7817198-7416-11e8-a7d5-008cfa5ae621",
            "name": "resourceType",
            "description": "A very brief, preferably one or two word, description of the type of resource for which you are requesting a DOI. This should be more specific than resourceType",
            "required": true,
            "type": "String",
            "attributes": [
                {
                    "name": "resourceTypeGeneral",
                    "description": "",
                    "required": false,
                    "type": "String"
                }
            ]
        },
        {
            "id": "d782bbe8-7416-11e8-a7d5-008cfa5ae621",
            "name": "contributor",
            "description": "The institution or person responsible for collecting, managing, distributing, or otherwise contributing to the development of the resource. If more than one, add rows for contributorName and contributorType under User Metadata.",
            "required": false,
            "type": "String",
            "attributes": [
                {
                    "id": "d783f6b6-7416-11e8-a7d5-008cfa5ae621",
                    "name": "contributorType",
                    "description": "Select a role for the contributor.",
                    "required": false,
                    "type": "Enum",
                    "values": [
                        {
                            "id": "d785092a-7416-11e8-a7d5-008cfa5ae621",
                            "is_default": false,
                            "value": "ContactPerson"
                        },
                        {
                            "id": "d7855d26-7416-11e8-a7d5-008cfa5ae621",
                            "is_default": false,
                            "value": "DataCollector"
                        },
                        {
                            "id": "d785b032-7416-11e8-a7d5-008cfa5ae621",
                            "is_default": false,
                            "value": "DataCurator"
                        },
                        {
                            "id": "d7860190-7416-11e8-a7d5-008cfa5ae621",
                            "is_default": false,
                            "value": "DataManager"
                        },
                        {
                            "id": "d78652b2-7416-11e8-a7d5-008cfa5ae621",
                            "is_default": false,
                            "value": "Distributor"
                        },
                        {
                            "id": "d786a334-7416-11e8-a7d5-008cfa5ae621",
                            "is_default": false,
                            "value": "Editor"
                        },
                        {
                            "id": "d786ef06-7416-11e8-a7d5-008cfa5ae621",
                            "is_default": false,
                            "value": "Funder"
                        },
                        {
                            "id": "d7873b46-7416-11e8-a7d5-008cfa5ae621",
                            "is_default": false,
                            "value": "HostingInstitution"
                        },
                        {
                            "id": "d7879532-7416-11e8-a7d5-008cfa5ae621",
                            "is_default": false,
                            "value": "Producer"
                        },
                        {
                            "id": "d787eed8-7416-11e8-a7d5-008cfa5ae621",
                            "is_default": false,
                            "value": "ProjectLeader"
                        },
                        {
                            "id": "d7883eba-7416-11e8-a7d5-008cfa5ae621",
                            "is_default": false,
                            "value": "ProjectManager"
                        },
                        {
                            "id": "d78888a2-7416-11e8-a7d5-008cfa5ae621",
                            "is_default": false,
                            "value": "ProjectMember"
                        },
                        {
                            "id": "d788d3d4-7416-11e8-a7d5-008cfa5ae621",
                            "is_default": false,
                            "value": "RegistrationAgency"
                        },
                        {
                            "id": "d7892140-7416-11e8-a7d5-008cfa5ae621",
                            "is_default": false,
                            "value": "RegistrationAuthority"
                        },
                        {
                            "id": "d7896eca-7416-11e8-a7d5-008cfa5ae621",
                            "is_default": false,
                            "value": "RelatedPerson"
                        },
                        {
                            "id": "d789c370-7416-11e8-a7d5-008cfa5ae621",
                            "is_default": false,
                            "value": "Researcher"
                        },
                        {
                            "id": "d78a17f8-7416-11e8-a7d5-008cfa5ae621",
                            "is_default": false,
                            "value": "ResearchGroup"
                        },
                        {
                            "id": "d78a671c-7416-11e8-a7d5-008cfa5ae621",
                            "is_default": false,
                            "value": "RightsHolder"
                        },
                        {
                            "id": "d78ab32a-7416-11e8-a7d5-008cfa5ae621",
                            "is_default": false,
                            "value": "Sponsor"
                        },
                        {
                            "id": "d78b0460-7416-11e8-a7d5-008cfa5ae621",
                            "is_default": false,
                            "value": " Supervisor"
                        },
                        {
                            "id": "d78b547e-7416-11e8-a7d5-008cfa5ae621",
                            "is_default": false,
                            "value": "WorkPackageLeader"
                        },
                        {
                            "id": "d78ba208-7416-11e8-a7d5-008cfa5ae621",
                            "is_default": false,
                            "value": "Other"
                        }
                    ]
                },
                {
                    "name": "nameIdentifier",
                    "description": "ORCID of Contributor, if known. ORCID provides a persistent digital identifier that distinguishes the contributor from every other researcher. See DOI FAQ page.",
                    "required": false,
                    "type": "String",
                    "attributes": [
                        {
                            "name": "nameIdentifierScheme",
                            "description": "",
                            "required": false,
                            "type": "Enum",
                            "values": [
                                {
                                    "is_default": true,
                                    "value": "ORCID"
                                }
                            ]
                        },
                        {
                            "name": "schemeURI",
                            "description": "",
                            "required": false,
                            "type": "URL/URI"
                        }
                    ]
                }
            ]
        },
        {
            "id": "b2aa45a4-7419-11e8-ad87-008cfa5ae621",
            "name": "description",
            "description": "A description of the dataset or resource and how to use it. Generally not the same as the abstract of the project or corresponding publication.",
            "required": true,
            "type": "String",
            "attributes": [
                {
                    "id": "b2abb1dc-7419-11e8-ad87-008cfa5ae621",
                    "name": "descriptionType",
                    "description": "Select Abstract",
                    "required": false,
                    "type": "Enum",
                    "values": [
                        {
                            "id": "b2accf72-7419-11e8-ad87-008cfa5ae621",
                            "is_default": true,
                            "value": "Abstract"
                        }
                    ]
                },
                {
                    "name": "xml:lang",
                    "description": "",
                    "required": false,
                    "type": "Enum",
                    "values": [
                        {
                            "value": "en-us",
                            "is_default": true
                        }
                    ]
                }
            ]
        },
        {
            "id": "d78c4456-7416-11e8-a7d5-008cfa5ae621",
            "name": "subject",
            "description": "Subject, keyword, classification code, or key phrase describing the resource. Seperate words or phrases with commas.",
            "required": true,
            "type": "String",
            "attributes": [
                {
                    "name": "schemeURI",
                    "description": "",
                    "required": false,
                    "type": "URL/URI"
                },
                {
                    "name": "subjectScheme",
                    "description": "",
                    "required": false,
                    "type": "String"
                },
                {
                    "name": "xml:lang",
                    "description": "",
                    "required": false,
                    "type": "Enum",
                    "values": [
                        {
                            "value": "en-us",
                            "is_default": true
                        }
                    ]
                }
            ]
        },
        {
            "id": "732f12fc-7418-11e8-ad87-008cfa5ae621",
            "name": "identifier",
            "description": "Leave blank. Will be filled in after DOI is issued",
            "required": false,
            "type": "String",
            "attributes": [
                {
                    "id": "73308b14-7418-11e8-ad87-008cfa5ae621",
                    "name": "identifierType",
                    "description": "DOI is the only option",
                    "required": true,
                    "type": "Enum",
                    "values": [
                        {
                            "id": "7331cdf8-7418-11e8-ad87-008cfa5ae621",
                            "is_default": true,
                            "value": "DOI"
                        }
                    ]
                }
            ]
        },
        {
            "id": "2a253512-741a-11e8-ad87-008cfa5ae621",
            "name": "Analysis_tool",
            "description": "Add a link (URL) to a tool that can be used with this datasets, such as a DE app or Atmosphere image.",
            "required": false,
            "type": "URL/URI"
        },
        {
            "id": "b2b4adfa-7419-11e8-ad87-008cfa5ae621",
            "name": "rights",
            "description": "All CyVerse Curated Data in the Data Commons is open access. You can choose between ODC PDDL for non-copyrightable materials (i.e., data only) or CC0 for copyrightable material (Workflows, White Papers, Project Documents). More information is available at https://wiki.cyverse.org/wiki/display/DC/Permanent+Identifier+FAQs#PermanentIdentifierFAQs-WhichlicensecanIusetopublishmydata?",
            "required": true,
            "type": "Enum",
            "values": [
                {
                    "id": "907ea31e-741c-11e8-ad87-008cfa5ae621",
                    "is_default": false,
                    "value": "CC0"
                },
                {
                    "id": "b2b5f624-7419-11e8-ad87-008cfa5ae621",
                    "is_default": true,
                    "value": "ODC PDDL"
                }
            ],
            "attributes": [
                {
                    "name": "rightsURI",
                    "description": "",
                    "required": false,
                    "type": "URL/URI"
                }
            ]
        },
        {
            "id": "52b91254-74b1-11e8-b2bf-008cfa5ae621",
            "name": "Version",
            "description": "Test",
            "required": false,
            "type": "String"
        },
        {
            "id": "730d04be-7418-11e8-ad87-008cfa5ae621",
            "name": "alternateIdentifier",
            "description": "An identifier or identifiers other than the primary Identifier applied to the resource being registered. This may be any alphanumeric string which is unique within its domain of issue. May be used for local identifiers. AlternateIdentifier should be used for another identifier of the same instance (same location, same file). For multiple identifiers, add more fields.",
            "required": false,
            "type": "String",
            "attributes": [
                {
                    "id": "730b7996-7418-11e8-ad87-008cfa5ae621",
                    "name": "alternateIdentifierType",
                    "description": "The type of the AlternateIdentifier as free text",
                    "required": false,
                    "type": "String"
                }
            ]
        },
        {
            "id": "7309ed60-7418-11e8-ad87-008cfa5ae621",
            "name": "relatedIdentifier",
            "description": "Identifiers of related resources. These must be globally unique identifiers. For multiple identifiers, add more fields.",
            "required": false,
            "type": "String",
            "attributes": [
                {
                    "id": "730810f8-7418-11e8-ad87-008cfa5ae621",
                    "name": "relatedIdentifierType",
                    "description": "The type of the related identifier. Select from list.",
                    "required": false,
                    "type": "Enum",
                    "values": [
                        {
                            "id": "9085f33a-741c-11e8-ad87-008cfa5ae621",
                            "is_default": false,
                            "value": "arXiv"
                        }
                    ]
                },
                {
                    "id": "b2bf8edc-7419-11e8-ad87-008cfa5ae621",
                    "name": "relationType",
                    "description": "Description of the relationship of the resource being registered (for which you are requesting a DOI) and the related resource. Select from list.",
                    "required": false,
                    "type": "Enum",
                    "values": [
                        {
                            "id": "9087c098-741c-11e8-ad87-008cfa5ae621",
                            "is_default": false,
                            "value": "IsReviewedBy"
                        }
                    ]
                }
            ]
        },
        {
            "name": "geoLocation",
            "description": "The spatial limits or description of a geographic location.",
            "required": false,
            "type": "Grouping",
            "attributes": [
                {
                    "id": "9089c956-741c-11e8-ad87-008cfa5ae621",
                    "name": "geoLocationPlace",
                    "description": "Description of a geographic location in free text, such as a place name.",
                    "required": false,
                    "type": "String"
                },
                {
                    "name": "geoLocationPoint",
                    "description": "A point location in space. A point contains a single latitude-longitude pair.",
                    "type": "Grouping",
                    "attributes": [
                        {
                            "id": "2a26dcd2-741a-11e8-ad87-008cfa5ae621",
                            "name": "pointLongitude",
                            "description": "A longitude point location in space.",
                            "required": false,
                            "type": "Number"
                        },
                        {
                            "name": "pointLatitude",
                            "description": "A latitude point location in space.",
                            "required": false,
                            "type": "Number"
                        }
                    ]
                },
                {
                    "name": "geoLocationBox",
                    "description": "The spatial limits of a place. A box contains two latitude-longitude pairs (opposite corners of the box).",
                    "type": "Grouping",
                    "attributes": [
                        {
                            "id": "b289d436-7419-11e8-ad87-008cfa5ae621",
                            "name": "northBoundLatitude",
                            "description": "The north bound of a spatial limit of a place.",
                            "required": false,
                            "type": "Number"
                        },
                        {
                            "name": "southBoundLatitude",
                            "description": "The south bound of a spatial limit of a place.",
                            "required": false,
                            "type": "Number"
                        },
                        {
                            "name": "eastBoundLongitude",
                            "description": "The east bound of a spatial limit of a place.",
                            "required": false,
                            "type": "Number"
                        },
                        {
                            "name": "westBoundLongitude",
                            "description": "The west bound of a spatial limit of a place.",
                            "required": false,
                            "type": "Number"
                        }
                    ]
                }
            ]
        },
        {
            "id": "b2c0eff2-7419-11e8-ad87-008cfa5ae621",
            "name": "compressed_data",
            "description": "Does this dataset contain compressed (e.g., zip) files?",
            "required": false,
            "type": "Boolean"
        },
        {
            "id": "52c9e192-74b1-11e8-b2bf-008cfa5ae621",
            "name": "FundingReferenceName",
            "description": "Test",
            "required": false,
            "type": "String"
        },
        {
            "id": "52caf1f4-74b1-11e8-b2bf-008cfa5ae621",
            "name": "FundingReferenceIdentifier",
            "description": "Test",
            "required": false,
            "type": "String"
        },
        {
            "id": "b2c24b22-7419-11e8-ad87-008cfa5ae621",
            "name": "reuse_or_citation_conditions",
            "description": "A standard citation for your dataset will be generated from other metadata, but here you may include specific instructions on how to cite or reuse your dataset. ",
            "required": false,
            "type": "String"
        },
        {
            "id": "52cdad72-74b1-11e8-b2bf-008cfa5ae621",
            "name": "Language",
            "description": "Test",
            "required": false,
            "type": "String"
        },
        {
            "id": "52cec45a-74b1-11e8-b2bf-008cfa5ae621",
            "name": "Size",
            "description": "This should autofill",
            "required": false,
            "type": "String"
        },
        {
            "id": "52cfd6c4-74b1-11e8-b2bf-008cfa5ae621",
            "name": "Format",
            "description": "",
            "required": false,
            "type": "String"
        },
        {
            "id": "b2c39842-7419-11e8-ad87-008cfa5ae621",
            "name": "is_deprecated",
            "description": "Has this published dataset been deprecated? If true, there should be a link to the replacement dataset.",
            "required": false,
            "type": "Boolean"
        }
    ]
};

class EditDataCiteMetadataTemplateTest extends Component {
    render () {
        const logger = this.props.logger || ((template) => {
            console.log(template);
        });

        return (
            <EditMetadataTemplate open presenter={presenter(logger)} initialValues={dataciteMetadataTemplate} />
        );
    }
}

export {
    EditNestedAttrMetadataTemplateTest,
    EditDataCiteMetadataTemplateTest,
};
