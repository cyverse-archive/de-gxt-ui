import React, { Component } from 'react';
import AnalysesView from "../../../src/analysis/view/AnalysesView";

class AnalysesViewTest extends Component {
    render() {
        const analysesList = {
            "analyses": [{
                "description": "",
                "name": "Word_Count_analysis1",
                "can_share": true,
                "username": "sriram@iplantcollaborative.org",
                "app_id": "c7f05682-23c8-4182-b9a2-e09650a5f49b",
                "batch_status": {"total": 2, "completed": 0, "running": 0, "submitted": 2},
                "system_id": "de",
                "app_disabled": false,
                "batch": true,
                "enddate": "0",
                "status": "Submitted",
                "id": "5e18af30-a56f-11e8-b5fa-f64e9b87c109",
                "startdate": "1534875837334",
                "app_description": "Counts and summarizes the number of lines, words, and bytes in a target file",
                "notify": true,
                "resultfolderid": "/iplant/home/sriram/analyses/Word_Count_analysis1-2018-08-21-18-23-56.5",
                "app_name": "Word Count"
            }, {
                "description": "",
                "name": "test8",
                "can_share": true,
                "username": "sriram@iplantcollaborative.org",
                "app_id": "c7f05682-23c8-4182-b9a2-e09650a5f49b",
                "system_id": "de",
                "app_disabled": false,
                "batch": false,
                "enddate": "1533684920402",
                "status": "Failed",
                "id": "71380ffa-9a9a-11e8-9ac7-f64e9b87c109",
                "startdate": "1533684874626",
                "app_description": "Counts and summarizes the number of lines, words, and bytes in a target file",
                "wiki_url": "https://pods.iplantcollaborative.org/wiki/display/DEapps/Word%20Count",
                "notify": true,
                "resultfolderid": "/iplant/home/sriram/analyses/test8-2018-08-07-23-34-34.2",
                "app_name": "Word Count"
            }, {
                "description": "",
                "name": "test7",
                "can_share": true,
                "username": "sriram@iplantcollaborative.org",
                "app_id": "c7f05682-23c8-4182-b9a2-e09650a5f49b",
                "system_id": "de",
                "app_disabled": false,
                "batch": false,
                "enddate": "1533684906119",
                "status": "Failed",
                "id": "6be2da44-9a9a-11e8-a750-f64e9b87c109",
                "startdate": "1533684865675",
                "app_description": "Counts and summarizes the number of lines, words, and bytes in a target file",
                "wiki_url": "https://pods.iplantcollaborative.org/wiki/display/DEapps/Word%20Count",
                "notify": true,
                "resultfolderid": "/iplant/home/sriram/analyses/test7-2018-08-07-23-34-25.3",
                "app_name": "Word Count"
            }, {
                "description": "",
                "name": "test6",
                "can_share": true,
                "username": "sriram@iplantcollaborative.org",
                "app_id": "c7f05682-23c8-4182-b9a2-e09650a5f49b",
                "system_id": "de",
                "app_disabled": false,
                "batch": false,
                "enddate": "1533684883208",
                "status": "Failed",
                "id": "5f6ea6b2-9a9a-11e8-8372-f64e9b87c109",
                "startdate": "1533684844416",
                "app_description": "Counts and summarizes the number of lines, words, and bytes in a target file",
                "wiki_url": "https://pods.iplantcollaborative.org/wiki/display/DEapps/Word%20Count",
                "notify": true,
                "resultfolderid": "/iplant/home/sriram/analyses/test6-2018-08-07-23-34-04.0",
                "app_name": "Word Count"
            }, {
                "description": "",
                "name": "test5",
                "can_share": true,
                "username": "sriram@iplantcollaborative.org",
                "app_id": "c7f05682-23c8-4182-b9a2-e09650a5f49b",
                "system_id": "de",
                "app_disabled": false,
                "batch": false,
                "enddate": "1533677355533",
                "status": "Failed",
                "id": "cee0211e-9a87-11e8-89bc-f64e9b87c109",
                "startdate": "1533676871234",
                "app_description": "Counts and summarizes the number of lines, words, and bytes in a target file",
                "wiki_url": "https://pods.iplantcollaborative.org/wiki/display/DEapps/Word%20Count",
                "notify": true,
                "resultfolderid": "/iplant/home/sriram/analyses/test5-2018-08-07-21-21-10.8",
                "app_name": "Word Count"
            }, {
                "description": "",
                "name": "Word_Count_analysis1",
                "can_share": true,
                "username": "sriram@iplantcollaborative.org",
                "app_id": "c7f05682-23c8-4182-b9a2-e09650a5f49b",
                "system_id": "de",
                "app_disabled": false,
                "batch": false,
                "enddate": "1533314504293",
                "status": "Completed",
                "id": "fa2565b8-973b-11e8-9974-f64e9b87c101",
                "startdate": "1533314448712",
                "app_description": "Counts and summarizes the number of lines, words, and bytes in a target file",
                "wiki_url": "https://pods.iplantcollaborative.org/wiki/display/DEapps/Word%20Count",
                "notify": true,
                "resultfolderid": "/iplant/home/sriram/analyses/Word_Count_analysis1-2018-08-03-16-40-48.7",
                "app_name": "Word Count"
            }, {
                "description": "",
                "name": "Word_Count_analysis1",
                "can_share": true,
                "username": "sriram@iplantcollaborative.org",
                "app_id": "c7f05682-23c8-4182-b9a2-e09650a5f49b",
                "system_id": "de",
                "app_disabled": false,
                "batch": false,
                "enddate": "1533314504291",
                "status": "Completed",
                "id": "fa2565b8-973b-11e8-9974-f64e9b87c102",
                "startdate": "1533314448712",
                "app_description": "Counts and summarizes the number of lines, words, and bytes in a target file",
                "wiki_url": "https://pods.iplantcollaborative.org/wiki/display/DEapps/Word%20Count",
                "notify": true,
                "resultfolderid": "/iplant/home/sriram/analyses/Word_Count_analysis1-2018-08-03-16-40-48.7",
                "app_name": "Word Count"
            }, {
                "description": "",
                "name": "Word_Count_analysis1",
                "can_share": true,
                "username": "sriram@iplantcollaborative.org",
                "app_id": "c7f05682-23c8-4182-b9a2-e09650a5f49b",
                "system_id": "de",
                "app_disabled": false,
                "batch": false,
                "enddate": "1533314504295",
                "status": "Submitted",
                "id": "fa2565b8-973b-11e8-9974-f64e9b87c103",
                "startdate": "1533314448712",
                "app_description": "Counts and summarizes the number of lines, words, and bytes in a target file",
                "wiki_url": "https://pods.iplantcollaborative.org/wiki/display/DEapps/Word%20Count",
                "notify": true,
                "resultfolderid": "/iplant/home/sriram/analyses/Word_Count_analysis1-2018-08-03-16-40-48.7",
                "app_name": "Word Count"
            }, {
                "description": "",
                "name": "Word_Count_analysis1",
                "can_share": true,
                "username": "sriram@iplantcollaborative.org",
                "app_id": "c7f05682-23c8-4182-b9a2-e09650a5f49b",
                "system_id": "de",
                "app_disabled": false,
                "batch": false,
                "enddate": "1533314504297",
                "status": "Running",
                "id": "fa2565b8-973b-11e8-9974-f64e9b87c104",
                "startdate": "1533314448712",
                "app_description": "Counts and summarizes the number of lines, words, and bytes in a target file",
                "wiki_url": "https://pods.iplantcollaborative.org/wiki/display/DEapps/Word%20Count",
                "notify": true,
                "resultfolderid": "/iplant/home/sriram/analyses/Word_Count_analysis1-2018-08-03-16-40-48.7",
                "app_name": "Word Count"
            }, {
                "description": "",
                "name": "Word_Count_analysis1",
                "can_share": true,
                "username": "ipctest@iplantcollaborative.org",
                "app_id": "c7f05682-23c8-4182-b9a2-e09650a5f49b",
                "system_id": "de",
                "app_disabled": false,
                "batch": false,
                "enddate": "1533314504294",
                "status": "Completed",
                "id": "fa2565b8-973b-11e8-9974-f64e9b87c105",
                "startdate": "1533314448712",
                "app_description": "Counts and summarizes the number of lines, words, and bytes in a target file",
                "wiki_url": "https://pods.iplantcollaborative.org/wiki/display/DEapps/Word%20Count",
                "notify": true,
                "resultfolderid": "/iplant/home/sriram/analyses/Word_Count_analysis1-2018-08-03-16-40-48.7",
                "app_name": "Word Count"
            }],
            "timestamp": "1534875863833",
            "total": 10
        };
        const presenter = {
            getAnalyses: (rowsPerPage,
                          offset,
                          filter,
                          orderBy,
                          order,
                          resultCallback,
                          errorCallback) => {
                resultCallback(analysesList);
            }
        };

        return (
            <AnalysesView analysesList={analysesList}
                          username="sriram@iplantcollaborative.org"
                          presenter={presenter}/>
        );
    }
}

export default AnalysesViewTest;