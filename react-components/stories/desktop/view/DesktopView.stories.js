/**
 *
 * @author sriram
 *
 */
import React, {Component} from "react";
import DesktopView from "../../../src/desktop/view/DesktopView";

class DesktopViewTest extends  Component {
    render() {
        const windowConfigs = [{
            "tag": "0",
            "type": "DATA",
            "windowTitle": "Data",
            "minimized": false,

        },
            {
                "tag": "",
                "type": "APPS",
                "windowTitle": "Apps",
                "minimized": true,

            }];
        
        const notifications = {
            "total": "30",
            "unseen_total": "10",
            "messages": [{
                "type": "analysis",
                "user": "sriram",
                "subject": "Word_Count_analysis1 status changed.",
                "email": false,
                "email_template": "analysis_status_change",
                "payload": {
                    "description": "",
                    "analysisresultsfolder": "/iplant/home/sriram/analyses/Word_Count_analysis1-2018-03-07-18-55-17.9",
                    "name": "Word_Count_analysis1",
                    "username": "sriram@iplantcollaborative.org",
                    "app_id": "c7f05682-23c8-4182-b9a2-e09650a5f49b",
                    "analysisstartdate": "2018-03-07T18:55:18.171Z",
                    "system_id": "de",
                    "app_disabled": false,
                    "analysisstatus": "Submitted",
                    "batch": false,
                    "status": "Submitted",
                    "id": "142b48c4-2239-11e8-8c2f-008cfa5ae621",
                    "analysisname": "Word_Count_analysis1",
                    "startdate": "1520448918171",
                    "app_description": "Counts and summarizes the number of lines, words, and bytes in a target file",
                    "action": "job_status_change",
                    "analysisdescription": "",
                    "wiki_url": "https://pods.iplantcollaborative.org/wiki/display/DEapps/Word%20Count",
                    "notify": true,
                    "resultfolderid": "/iplant/home/sriram/analyses/Word_Count_analysis1-2018-03-07-18-55-17.9",
                    "email_address": "sriram@iplantcollaborative.org",
                    "user": "sriram",
                    "app_name": "Word Count"
                },
                "message": {
                    "id": "CB9C08F4-EC38-44F7-9342-1B85916A9029",
                    "timestamp": "1520448918196",
                    "text": "Word_Count_analysis1 submitted"
                },
                "seen": false,
                "deleted": false
            }, {
                "type": "analysis",
                "user": "sriram",
                "subject": "Word_Count_analysis1 status changed.",
                "email": false,
                "email_template": "analysis_status_change",
                "payload": {
                    "description": "",
                    "analysisresultsfolder": "/iplant/home/sriram/analyses/Word_Count_analysis1-2018-03-07-18-55-17.9",
                    "name": "Word_Count_analysis1",
                    "can_share": false,
                    "username": "sriram@iplantcollaborative.org",
                    "app_id": "c7f05682-23c8-4182-b9a2-e09650a5f49b",
                    "analysisstartdate": "2018-03-07T18:55:17.899Z",
                    "system_id": "de",
                    "app_disabled": false,
                    "analysisstatus": "Running",
                    "batch": false,
                    "enddate": "0",
                    "status": "Running",
                    "id": "142b48c4-2239-11e8-8c2f-008cfa5ae621",
                    "analysisname": "Word_Count_analysis1",
                    "startdate": "1520448917899",
                    "app_description": "Counts and summarizes the number of lines, words, and bytes in a target file",
                    "action": "job_status_change",
                    "analysisdescription": "",
                    "wiki_url": "https://pods.iplantcollaborative.org/wiki/display/DEapps/Word%20Count",
                    "notify": true,
                    "resultfolderid": "/iplant/home/sriram/analyses/Word_Count_analysis1-2018-03-07-18-55-17.9",
                    "email_address": "sriram@iplantcollaborative.org",
                    "user": "sriram",
                    "app_name": "Word Count"
                },
                "message": {
                    "id": "A51B9A72-4B9A-4E57-B80B-EFEF9517BE32",
                    "timestamp": "1520466625572",
                    "text": "Word_Count_analysis1 running"
                },
                "seen": true,
                "deleted": false
            }, {
                "type": "analysis",
                "user": "sriram",
                "subject": "Word_Count_analysis1 status changed.",
                "email": true,
                "email_template": "analysis_status_change",
                "payload": {
                    "description": "",
                    "analysisresultsfolder": "/iplant/home/sriram/analyses/Word_Count_analysis1-2018-03-07-18-55-17.9",
                    "name": "Word_Count_analysis1",
                    "can_share": false,
                    "username": "sriram@iplantcollaborative.org",
                    "app_id": "c7f05682-23c8-4182-b9a2-e09650a5f49b",
                    "analysisstartdate": "2018-03-07T18:55:17.899Z",
                    "system_id": "de",
                    "app_disabled": false,
                    "analysisstatus": "Completed",
                    "batch": false,
                    "enddate": "1520466629681",
                    "status": "Completed",
                    "id": "142b48c4-2239-11e8-8c2f-008cfa5ae621",
                    "analysisname": "Word_Count_analysis1",
                    "startdate": "1520448917899",
                    "app_description": "Counts and summarizes the number of lines, words, and bytes in a target file",
                    "action": "job_status_change",
                    "analysisdescription": "",
                    "wiki_url": "https://pods.iplantcollaborative.org/wiki/display/DEapps/Word%20Count",
                    "notify": true,
                    "resultfolderid": "/iplant/home/sriram/analyses/Word_Count_analysis1-2018-03-07-18-55-17.9",
                    "email_address": "sriram@iplantcollaborative.org",
                    "user": "sriram",
                    "app_name": "Word Count"
                },
                "message": {
                    "id": "7BCDBA22-8018-418B-8219-E8B4E15DC0EF",
                    "timestamp": "1520466630351",
                    "text": "Word_Count_analysis1 completed"
                },
                "seen": true,
                "deleted": false
            }, {
                "type": "analysis",
                "user": "sriram",
                "subject": "Word_Count_analysis1 status changed.",
                "email": false,
                "email_template": "analysis_status_change",
                "payload": {
                    "description": "",
                    "analysisresultsfolder": "/iplant/home/sriram/analyses/Word_Count_analysis1-2018-04-10-22-57-26.0",
                    "name": "Word_Count_analysis1",
                    "username": "sriram@iplantcollaborative.org",
                    "app_id": "c7f05682-23c8-4182-b9a2-e09650a5f49b",
                    "analysisstartdate": "2018-04-10T22:57:26.476Z",
                    "system_id": "de",
                    "app_disabled": false,
                    "analysisstatus": "Submitted",
                    "batch": false,
                    "status": "Submitted",
                    "id": "89c1925c-3d12-11e8-b82d-008cfa5ae621",
                    "analysisname": "Word_Count_analysis1",
                    "startdate": "1523401046476",
                    "app_description": "Counts and summarizes the number of lines, words, and bytes in a target file",
                    "action": "job_status_change",
                    "analysisdescription": "",
                    "wiki_url": "https://pods.iplantcollaborative.org/wiki/display/DEapps/Word%20Count",
                    "notify": true,
                    "resultfolderid": "/iplant/home/sriram/analyses/Word_Count_analysis1-2018-04-10-22-57-26.0",
                    "email_address": "sriram@iplantcollaborative.org",
                    "user": "sriram",
                    "app_name": "Word Count"
                },
                "message": {
                    "id": "9134191C-7A0F-4FFB-ADED-3E0683628522",
                    "timestamp": "1523401046522",
                    "text": "Word_Count_analysis1 submitted"
                },
                "seen": true,
                "deleted": false
            }, {
                "type": "analysis",
                "user": "sriram",
                "subject": "Word_Count_analysis1 status changed.",
                "email": false,
                "email_template": "analysis_status_change",
                "payload": {
                    "description": "",
                    "analysisresultsfolder": "/iplant/home/sriram/analyses/Word_Count_analysis1-2018-04-10-22-57-26.0",
                    "name": "Word_Count_analysis1",
                    "can_share": false,
                    "username": "sriram@iplantcollaborative.org",
                    "app_id": "c7f05682-23c8-4182-b9a2-e09650a5f49b",
                    "analysisstartdate": "2018-04-10T22:57:26.021Z",
                    "system_id": "de",
                    "app_disabled": false,
                    "analysisstatus": "Running",
                    "batch": false,
                    "enddate": "0",
                    "status": "Running",
                    "id": "89c1925c-3d12-11e8-b82d-008cfa5ae621",
                    "analysisname": "Word_Count_analysis1",
                    "startdate": "1523401046021",
                    "app_description": "Counts and summarizes the number of lines, words, and bytes in a target file",
                    "action": "job_status_change",
                    "analysisdescription": "",
                    "wiki_url": "https://pods.iplantcollaborative.org/wiki/display/DEapps/Word%20Count",
                    "notify": true,
                    "resultfolderid": "/iplant/home/sriram/analyses/Word_Count_analysis1-2018-04-10-22-57-26.0",
                    "email_address": "sriram@iplantcollaborative.org",
                    "user": "sriram",
                    "app_name": "Word Count"
                },
                "message": {
                    "id": "47BBA55D-CE35-467D-89A5-4DD6047A46C1",
                    "timestamp": "1523401053001",
                    "text": "Word_Count_analysis1 running"
                },
                "seen": true,
                "deleted": false
            }, {
                "type": "analysis",
                "user": "sriram",
                "subject": "Word_Count_analysis1 status changed.",
                "email": true,
                "email_template": "analysis_status_change",
                "payload": {
                    "description": "",
                    "analysisresultsfolder": "/iplant/home/sriram/analyses/Word_Count_analysis1-2018-04-10-22-57-26.0",
                    "name": "Word_Count_analysis1",
                    "can_share": false,
                    "username": "sriram@iplantcollaborative.org",
                    "app_id": "c7f05682-23c8-4182-b9a2-e09650a5f49b",
                    "analysisstartdate": "2018-04-10T22:57:26.021Z",
                    "system_id": "de",
                    "app_disabled": false,
                    "analysisstatus": "Completed",
                    "batch": false,
                    "enddate": "1523401108061",
                    "status": "Completed",
                    "id": "89c1925c-3d12-11e8-b82d-008cfa5ae621",
                    "analysisname": "Word_Count_analysis1",
                    "startdate": "1523401046021",
                    "app_description": "Counts and summarizes the number of lines, words, and bytes in a target file",
                    "action": "job_status_change",
                    "analysisdescription": "",
                    "wiki_url": "https://pods.iplantcollaborative.org/wiki/display/DEapps/Word%20Count",
                    "notify": true,
                    "resultfolderid": "/iplant/home/sriram/analyses/Word_Count_analysis1-2018-04-10-22-57-26.0",
                    "email_address": "sriram@iplantcollaborative.org",
                    "user": "sriram",
                    "app_name": "Word Count"
                },
                "message": {
                    "id": "CB9F7CB0-EA6F-4D68-A9AF-F4CC4BC2416C",
                    "timestamp": "1523401108846",
                    "text": "Word_Count_analysis1 completed"
                },
                "seen": true,
                "deleted": false
            }, {
                "type": "Data",
                "user": "sriram",
                "subject": "test-multi-input uploaded successfully.",
                "email": false,
                "email_template": null,
                "payload": {
                    "action": "UPLOAD_COMPLETE",
                    "data": {
                        "infoType": "multi-input-path-list",
                        "path": "/iplant/home/sriram/test-multi-input",
                        "parentFolderId": "/iplant/home/sriram",
                        "date-created": "1523899955000",
                        "md5": "251671dc356f0a332ea573d6ce5ffd46",
                        "permission": "own",
                        "date-modified": "1523899955000",
                        "file-size": "95",
                        "label": "test-multi-input",
                        "id": "26f563e0-419c-11e8-a13d-1a5a300ff36f",
                        "content-type": "text/plain",
                        "sourceUrl": "test-multi-input"
                    }
                },
                "message": {
                    "id": "21EF3C78-E623-4B05-8B65-E801CE5DCAD6",
                    "timestamp": "1523899957449",
                    "text": "test-multi-input uploaded successfully."
                },
                "seen": false,
                "deleted": false
            }, {
                "type": "analysis",
                "user": "sriram",
                "subject": "Word_Count_analysis1 status changed.",
                "email": false,
                "email_template": "analysis_status_change",
                "payload": {
                    "description": "",
                    "analysisresultsfolder": "/iplant/home/sriram/analyses/Word_Count_analysis1-2018-04-19-17-33-22.0",
                    "name": "Word_Count_analysis1",
                    "username": "sriram@iplantcollaborative.org",
                    "app_id": "c7f05682-23c8-4182-b9a2-e09650a5f49b",
                    "analysisstartdate": "2018-04-19T17:33:23.605Z",
                    "system_id": "de",
                    "app_disabled": false,
                    "analysisstatus": "Submitted",
                    "batch": false,
                    "status": "Submitted",
                    "id": "c29d387e-43f7-11e8-a4f4-f64e9b87c109",
                    "analysisname": "Word_Count_analysis1",
                    "startdate": "1524159203605",
                    "app_description": "Counts and summarizes the number of lines, words, and bytes in a target file",
                    "action": "job_status_change",
                    "analysisdescription": "",
                    "wiki_url": "https://pods.iplantcollaborative.org/wiki/display/DEapps/Word%20Count",
                    "notify": true,
                    "resultfolderid": "/iplant/home/sriram/analyses/Word_Count_analysis1-2018-04-19-17-33-22.0",
                    "email_address": "sriram@iplantcollaborative.org",
                    "user": "sriram",
                    "app_name": "Word Count"
                },
                "message": {
                    "id": "7F10AB9A-C104-49C5-A96D-8F0C5BFAA566",
                    "timestamp": "1524159203780",
                    "text": "Word_Count_analysis1 submitted"
                },
                "seen": true,
                "deleted": false
            }, {
                "type": "analysis",
                "user": "sriram",
                "subject": "Word_Count_analysis1 status changed.",
                "email": false,
                "email_template": "analysis_status_change",
                "payload": {
                    "description": "",
                    "analysisresultsfolder": "/iplant/home/sriram/analyses/Word_Count_analysis1-2018-04-19-17-33-22.0",
                    "name": "Word_Count_analysis1",
                    "can_share": false,
                    "username": "sriram@iplantcollaborative.org",
                    "app_id": "c7f05682-23c8-4182-b9a2-e09650a5f49b",
                    "analysisstartdate": "2018-04-19T17:33:22.087Z",
                    "system_id": "de",
                    "app_disabled": false,
                    "analysisstatus": "Running",
                    "batch": false,
                    "enddate": "0",
                    "status": "Running",
                    "id": "c29d387e-43f7-11e8-a4f4-f64e9b87c109",
                    "analysisname": "Word_Count_analysis1",
                    "startdate": "1524159202087",
                    "app_description": "Counts and summarizes the number of lines, words, and bytes in a target file",
                    "action": "job_status_change",
                    "analysisdescription": "",
                    "wiki_url": "https://pods.iplantcollaborative.org/wiki/display/DEapps/Word%20Count",
                    "notify": true,
                    "resultfolderid": "/iplant/home/sriram/analyses/Word_Count_analysis1-2018-04-19-17-33-22.0",
                    "email_address": "sriram@iplantcollaborative.org",
                    "user": "sriram",
                    "app_name": "Word Count"
                },
                "message": {
                    "id": "05F9B2C7-AE85-4957-9C2E-787442112F93",
                    "timestamp": "1524159223492",
                    "text": "Word_Count_analysis1 running"
                },
                "seen": true,
                "deleted": false
            }, {
                "type": "analysis",
                "user": "sriram",
                "subject": "Word_Count_analysis1 status changed.",
                "email": true,
                "email_template": "analysis_status_change",
                "payload": {
                    "description": "",
                    "analysisresultsfolder": "/iplant/home/sriram/analyses/Word_Count_analysis1-2018-04-19-17-33-22.0",
                    "name": "Word_Count_analysis1",
                    "can_share": false,
                    "username": "sriram@iplantcollaborative.org",
                    "app_id": "c7f05682-23c8-4182-b9a2-e09650a5f49b",
                    "analysisstartdate": "2018-04-19T17:33:22.087Z",
                    "system_id": "de",
                    "app_disabled": false,
                    "analysisstatus": "Completed",
                    "batch": false,
                    "enddate": "1524159931295",
                    "status": "Completed",
                    "id": "c29d387e-43f7-11e8-a4f4-f64e9b87c109",
                    "analysisname": "Word_Count_analysis1",
                    "startdate": "1524159202087",
                    "app_description": "Counts and summarizes the number of lines, words, and bytes in a target file",
                    "action": "job_status_change",
                    "analysisdescription": "",
                    "wiki_url": "https://pods.iplantcollaborative.org/wiki/display/DEapps/Word%20Count",
                    "notify": true,
                    "resultfolderid": "/iplant/home/sriram/analyses/Word_Count_analysis1-2018-04-19-17-33-22.0",
                    "email_address": "sriram@iplantcollaborative.org",
                    "user": "sriram",
                    "app_name": "Word Count"
                },
                "message": {
                    "id": "9CBEB717-925A-4C38-B417-45A962327ED3",
                    "timestamp": "1524159931444",
                    "text": "Word_Count_analysis1 completed"
                },
                "seen": true,
                "deleted": false
            }]
        };
        const presenter = {
            getNotifications: (resultCallback) => {
                resultCallback(notifications)
            },
        };
        return (
            <DesktopView presenter={presenter} windowConfigList={windowConfigs}/>
        );
    }
}

export default DesktopViewTest;
