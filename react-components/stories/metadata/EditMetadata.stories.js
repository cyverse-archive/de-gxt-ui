import React, { Component } from "react";

import EditMetadata from "../../src/metadata/EditMetadata"

class EditMetadataTest extends Component {
    render () {
        const logger = this.props.logger || ((metadata) => {
            console.log(metadata);
        });

        const presenter = {
            onSaveMetadata: logger,
            closeEditMetadataDialog: () => logger("dialog closed."),
        };

        const metadata = {
            "avus": [
                {
                    "id": "1",
                    "attr": "Integer Attr",
                    "value": "",
                    "unit": "unit1",
                    "avus": [
                        {
                            "id": "3",
                            "attr": "attr3",
                            "value": "value3",
                            "unit": "unit3",
                            "avus": [
                                {
                                    "id": "5",
                                    "attr": "attr5",
                                    "value": "value5",
                                    "unit": "unit5",
                                },
                            ],
                        },
                    ],
                },
                {
                    "id": "2",
                    "attr": "String Attr",
                    "value": "value2",
                    "unit": "unit2",
                    "avus": [
                        {
                            "id": "4",
                            "attr": "attr4",
                            "value": "value4",
                            "unit": "unit4",
                        },
                    ],
                },
                {
                    "id": "6",
                    "attr": "String Attr",
                    "value": "",
                    "unit": "",
                },
            ]
        };

        return (
            <EditMetadata saveText="Save" presenter={presenter} open={true} initialValues={metadata} />
        );
    }
}

export default EditMetadataTest;
