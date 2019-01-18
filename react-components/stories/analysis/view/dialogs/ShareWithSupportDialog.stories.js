/**
 *  @author sriram
 *
 **/
import React, { Component } from 'react';
import ShareWithSupportDialog from "../../../../src/analysis/view/dialogs/ShareWithSupportDialog";

class ShareWithSupportDialogTest extends Component {
    render() {
        const analysis = {
            "description": "testing comments",
            "name": "test8",
            "can_share": true,
            "username": "sriram@iplantcollaborative.org",
            "app_id": "c7f05682-23c8-4182-b9a2-e09650a5f49b",
            "system_id": "de",
            "app_disabled": false,
            "batch": false,
            "enddate": "1533684920402",
            "status": "Completed",
            "id": "71380ffa-9a9a-11e8-9ac7-f64e9b87c109",
            "startdate": "1533684874626",
            "app_description": "Counts and summarizes the number of lines, words, and bytes in a target file",
            "wiki_url": "https://pods.iplantcollaborative.org/wiki/display/DEapps/Word%20Count",
            "notify": true,
            "resultfolderid": "/iplant/home/sriram/analyses/test8-2018-08-07-23-34-34.2",
            "app_name": "Word Count"
        };
        return (
            <ShareWithSupportDialog analysis={analysis} dialogOpen={true} userName="ipctest" email="ipctest@cyverse.org"/>
        );
    }
}

export default ShareWithSupportDialogTest;