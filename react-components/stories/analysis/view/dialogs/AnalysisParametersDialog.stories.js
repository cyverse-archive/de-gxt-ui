import React, { Component } from 'react';
import AnalysisParametersDialog from "../../../../src/analysis/view/dialogs/AnalysisParametersDialog";

class AnalysisParametersDialogTest extends Component {

    render() {
        const parameterList = {
            "app_id": "5c13a988-bc75-11e4-8418-ef0021a84d3d",
            "system_id": "de",
            "parameters": [{
                "param_type": "FileInput",
                "param_id": "5c2d52f2-bc75-11e4-8e85-93c6a9fde6c6",
                "info_type": "NucleotideOrPeptideSequence",
                "is_default_value": false,
                "param_name": "Genome FASTA file",
                "param_value": {"value": "/iplant/home/rogerab/testfiles/BAfiles/BAgenomeRay41.fa"},
                "is_visible": true,
                "full_param_id": "5c14fcd4-bc75-11e4-a45f-0ba310ad6a4e_5c2d52f2-bc75-11e4-8e85-93c6a9fde6c6",
                "data_format": "Unspecified"
            }, {
                "param_type": "Flag",
                "param_id": "5c3135fc-bc75-11e4-8818-e3bcb90c4207",
                "info_type": "",
                "is_default_value": true,
                "param_name": "Verbose output",
                "param_value": {"value": "true"},
                "is_visible": true,
                "full_param_id": "5c14fcd4-bc75-11e4-a45f-0ba310ad6a4e_5c3135fc-bc75-11e4-8818-e3bcb90c4207",
                "data_format": ""
            }, {
                "param_type": "Flag",
                "param_id": "5c331a7a-bc75-11e4-8725-afa165a0d548",
                "info_type": "",
                "is_default_value": true,
                "param_name": "Extended output",
                "param_value": {"value": "false"},
                "is_visible": true,
                "full_param_id": "5c14fcd4-bc75-11e4-a45f-0ba310ad6a4e_5c331a7a-bc75-11e4-8725-afa165a0d548",
                "data_format": ""
            }, {
                "param_type": "Flag",
                "param_id": "5c34e440-bc75-11e4-87c5-6f64247f970f",
                "info_type": "",
                "is_default_value": true,
                "param_name": "Keep temporary files",
                "param_value": {"value": "false"},
                "is_visible": true,
                "full_param_id": "5c14fcd4-bc75-11e4-a45f-0ba310ad6a4e_5c34e440-bc75-11e4-87c5-6f64247f970f",
                "data_format": ""
            }, {
                "param_type": "TextSelection",
                "param_id": "5c36af0a-bc75-11e4-a385-277dc4e6672b",
                "info_type": "",
                "is_default_value": false,
                "param_name": "Intron size optimization",
                "param_value": {
                    "value": {
                        "isDefault": true,
                        "display": "No optimization (intron lengthu003D5,000)",
                        "id": "5c376b8e-bc75-11e4-92d6-03cb3e4f2155",
                        "name": "-v",
                        "value": ""
                    }
                },
                "is_visible": true,
                "full_param_id": "5c14fcd4-bc75-11e4-a45f-0ba310ad6a4e_5c36af0a-bc75-11e4-a385-277dc4e6672b",
                "data_format": ""
            }, {
                "param_type": "MultiFileOutput",
                "param_id": "5c3a5d44-bc75-11e4-9a78-53fcfeff6dad",
                "info_type": "File",
                "is_default_value": true,
                "param_name": "Output file prefix",
                "param_value": {"value": "Output_file_prefix"},
                "is_visible": true,
                "full_param_id": "5c14fcd4-bc75-11e4-a45f-0ba310ad6a4e_5c3a5d44-bc75-11e4-9a78-53fcfeff6dad",
                "data_format": "Unspecified"
            }]
        };

        return (
            <AnalysisParametersDialog parameters={parameterList.parameters}
                                      analysisName="Test_analysis1"
                                      dialogOpen={true}/>
        );
    }
}

export default AnalysisParametersDialogTest;