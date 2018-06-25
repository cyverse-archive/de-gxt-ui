import React, { Component } from "react";

import EditMetadataTemplate from "../../src/metadata/admin/EditMetadataTemplate";

const metadataTemplate = {
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

class EditMetadataTemplateTest extends Component {
    render () {
        const logger = this.props.logger || ((template) => {
            console.log(template);
        });

        const presenter = {
            onSaveTemplate: logger,
            closeTemplateInfoDialog: () => logger("dialog closed."),
        };

        return (
            <EditMetadataTemplate open saveText="Save" presenter={presenter} initialValues={metadataTemplate} />
        );
    }
}

export default EditMetadataTemplateTest;
