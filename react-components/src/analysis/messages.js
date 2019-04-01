var intlData = {
  locales: "en-US",
  messages: {
    emptyValue: "-",
    goOutputFolder: "Go to output folder",
    goOutputFolderOf: "Go to output folder of",
    viewParam: "View Parameters",
    relaunch: "Relaunch...",
    analysisInfo: "View Analysis Info",
    share: "Share with collaborators...",
    completeAndSave: "Complete and Save Outputs",
    cancel: "Cancel",
    ok: "Ok",
    delete: "Delete",
    rename: "Rename...",
    updateComments: "Update Comments...",
    analyses: "Analyses",
    refresh: "Refresh",
    analysisInfoTitle: "Analysis Info",
    okBtnText: "OK",
    cancelBtnText: "Cancel",
    comments: "Comments",
    analysisParamTitle: "Viewing parameters for {name}",
    renameDlgHeader: "Rename Analysis",
    renamePrompt: "Rename",
    commentsDlgHeader: "Comments",
    commentsPrompt: "Comments...",
    search: "Search...",
    analysis: "Analysis",
    analysisId: "Analysis ID",
    app: "App",
    currentStatus: "Current Status",
    outputFolder: "Output folder",
    startDate: "Start date",
    endDate: "End date",
    user: "User",
    name: "Name",
    email: "Email",
    submit: "Submit",
    noOutput: "Analysis completed but I got no output.",
    unExpectedOutput: "Analysis completed but I got unexpected output.",
    outputConditionHeader: "Select Output condition:",
    saveToFile: "Save to file",
    type: "App Type",
    viewFilter: "View",
    needHelp: "I still need help!",
    noAnalysis: "No analysis to display!",
    htDetails: "View HT analyses details",
    viewAll: "View All analyses",
    analysisInfoDlgTitle: "Analysis Info",
    analysesExecDeleteWarning:
      "This will remove the selected analyses and the parameters information associated with those analyses. Outputs can still be viewed in the Data window within the folder created by these analyses.",

    shareDisclaimer:
      '<span style="overflow:auto;">I agree to <a href="https://wiki.cyverse.org/wiki/x/KBfHAQ" target="_blank">share my analysis</a> details, output file(s) and logs with CyVerse Support. If you want any of the <a href="https://wiki.cyverse.org/wiki/x/XRfHAQ" target="_blank">Science Informaticians</a> to recuse themselves from assisting with this problem, please list their name(s) in comments.</span>',
    agaveRunning:
      "<h4> My Analysis never completes </h4>\n" +
      '<p style="margin:10px;">HPC apps that use the Agave API will display as "Running" while the data is being transferred. If your analysis has a very large input file or output file, part of the run time may be due to data transfer. Certain apps have very long run times, depending on the input. Please check the app documentation to estimate how long you it should take for your analysis to complete.</p>\n' +
      "\n",
    agaveSubmitted:
      "<h4> My analysis has been in the Submitted state for a long time. </h4>\n" +
      '<p style="margin:10px;">This application runs on XSEDE, the U.S. national supercomputing network. Wait time for your analysis to submit and run may be up to several days.\n' +
      'Please do not resubmit your analysis. See accessing <a href="https://wiki.cyverse.org/wiki/x/MYOdAQ" target="_blank">XSEDE</a> and queues for more information.</p>\n' +
      "\n",
    completedNoOutput:
      "<h4> My analysis completed but no output</h4>\n" +
      '<p style="margin:10px;">If there are only <a href="https://wiki.cyverse.org/wiki/x/GVi" target="_blank">log files</a>, the application did not run successfully. Check your <a href="https://wiki.cyverse.org/wiki/x/GVi" target="_blank">log files</a> (<b>condor-stderr-0 and condor-stdout-0</b>) under logs directory. If you see the message similar to input file not found, the cause could be due:</p>\n' +
      '<p style="padding-left:10px;margin:10px;"> * <b>Special characters in the file name:</b> Rename the file, excluding any of the <a href="https://wiki.cyverse.org/wiki/x/O6ui" target="_blank"> special characters </a> and relaunch the analysis.</p>\n' +
      '<p style="padding-left:10px;margin:10px;"> * <b>Corrupted input file:</b> Try to run the app again using another input file. Start with the <a href="https://wiki.cyverse.org/wiki/x/pAOZ" target="_blank"> example file </a> supplied with the app. You may need to return to your original data source (e.g., sequencing center) and request a new copy of the input data.</p>\n',
    completedUnExpectedOutput:
      "<h4> My analysis completed but generated unexpected output</h4>\n" +
      '<p style="margin:10px;">If your output files did not return expected results:</p>\n' +
      '<p style="padding-left:10px;margin:10px;">* <b>Invalid parameters:</b> Each tool will report errors differently. You will need to search the <a href="https://wiki.cyverse.org/wiki/x/GVi">log files</a> for an error message. Start by searching the stdout file, which lists all of the outputs from the tool. Also check the <a href="https://wiki.cyverse.org/wiki/x/pAOZ" target="_blank">app manual</a> and the tool\'s documentation. </p>\n' +
      '<p style="padding-left:10px;margin:10px;">* <b>Problem with app:</b>\n' +
      '<p style="margin:10px;">\n' +
      '<p style="padding-left:20px;margin:10px;">* <b>If the app is <a href="https://wiki.cyverse.org/wiki/x/6gGO#UsingtheAppsWindowandSubmittinganAnalysis-AppStatus" target="_blank">public</a>:</b> Please post a <a href="https://wiki.cyverse.org/wiki/x/zi2" target="_blank">comment</a>.</p>\n' +
      '<p style="padding-left:20px;margin:10px;">* <b>If the app is <a href="https://wiki.cyverse.org/wiki/x/6gGO#UsingtheAppsWindowandSubmittinganAnalysis-AppStatus" target="_blank">private</a>:</b> Validate with several real datasets. Contact the original author of the <a href="https://wiki.cyverse.org/wiki/x/pAOZ" target="_blank">tool</a>.</p>\n' +
      "</p>\n",
    condorRunning:
      "<h4> My analysis never completes </h4>\n" +
      '<p style="padding-left:10px;margin:10px;"> * Is the input consistent with the intended functionality of the app? Please read the <a href="https://wiki.cyverse.org/wiki/x/pAOZ" target="_blank">app manual</a>\n' +
      "    for limits on the size of input files an app can normally handle and an estimate\n" +
      "    of how long it should take for your analysis to complete.</p>\n" +
      '<p style="padding-left:10px;margin:10px;"> * If you are trying to run an analysis with input that is much larger than the app can handle, it may\n' +
      "    not complete. Do not try to relaunch this analysis. If you would like\n" +
      '    to suggest a new app for additional functionality, please use <a href="http://ask.iplantcollaborative.org" target="_blank">forums</a>.</p>\n',
    condorSubmitted:
      "<h4>My analysis has been in the Submitted state for a long time.</h4>\n" +
      '<p style="margin:10px;">Your analysis is submitted and is queued for running. You may experience long wait times for analysis submissions after maintenance or periods of peak demand.\n' +
      "    Please allow 24 hours for your analysis to start running.</p>\n",
    failed:
      "<h4> My analysis failed </h4>\n" +
      '<p style="margin:10px;">There are several common causes for a failed analysis.</p>\n' +
      '<p style="margin:10px;">When there is a scheduled DE maintenance, all submitted analyses will be killed. If your analysis fails on a maintenance day, please relaunch the analysis.</p>\n' +
      '<p style="margin:10px;">Problem with the input file(s):</p>\n' +
      '<p style="margin:10px;">\n' +
      '    <p style="padding-left:10px;margin:10px;">* <b>Special characters in the file name:</b> Rename the file, excluding any of the <a href="https://wiki.cyverse.org/wiki/x/O6ui" target="_blank">special characters</a>, and relaunch.</p>\n' +
      '    <p style="padding-left:10px;margin:10px;">* <b>Incorrect input file format:</b> Read the <a href="https://wiki.cyverse.org/wiki/x/pAOZ" target="_blank">app manual</a>, reformat the input file, and relaunch.</p>\n' +
      '    <p style="padding-left:10px;margin:10px;">* <b>Corrupted input file:</b> Try to run the app again using different input file. Start with the <a href="https://wiki.cyverse.org/wiki/x/jwy" target="_blank">example file</a> supplied with the app. You may need to return to your original data source (e.g., sequencing center) and request a new copy of the input data.</p>\n' +
      "</p>\n" +
      '<p style="margin:10px;">Other common reasons for analysis failure:</p>\n' +
      '<p style="margin:10px;">\n' +
      '<p style="padding-left:10px;margin:10px;">* <b>Invalid parameters:</b> Each tool will report errors differently. You will need to search the log files for an error message. Start by searching the (stdout) file, which lists all of the output from the tool. Also check the app documentation, including the original documentation for the tool.</p>\n' +
      '<p style="padding-left:10px;margin:10px;">* <b>Problem with app: </b> </p>\n' +
      '<p style="margin:10px;">\n' +
      '            <p style="padding-left:20px;margin:10px;">* <b>If the app is <a href="https://wiki.cyverse.org/wiki/x/6gGO#UsingtheAppsWindowandSubmittinganAnalysis-AppStatus" target="_blank">public</a>:</b> Please post a <a href="https://wiki.cyverse.org/wiki/display/DEmanual/Using+App+Ratings+and+App+Comments" target="_blank">comment</a>.</p>\n' +
      '            <p style="padding-left:20px;margin:10px;">* <b>If the app is <a href="https://wiki.cyverse.org/wiki/x/6gGO#UsingtheAppsWindowandSubmittinganAnalysis-AppStatus" target="_blank">private</a>:</b> Validate with several real datasets. Contact the original author of the <a href="https://wiki.cyverse.org/wiki/display/DEmanual/Viewing+App+and+Tool+Information" target="_blank">tool</a>.</p>\n' +
      "</p>\n" +
      "</p>\n"
  }
};

export default intlData;
