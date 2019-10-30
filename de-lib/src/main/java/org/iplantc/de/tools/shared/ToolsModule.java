package org.iplantc.de.tools.shared;

/**
 * Created by sriram on 5/5/17.
 */
public interface ToolsModule {

    interface ToolIds {

        String TOOLS_VIEW = ".toolsView";

        String SHARING_VIEW = ".view";
        String SHARING_PERMS = ".permPanel";
        String SHARING_DLG = "toolSharingDlg";
        String DONE_BTN = ".doneBtn";
    }

    interface EditToolIds {
        String EDIT_DIALOG = "Edit";
        String TOOL_CPU = ".idToolCpu";
        String TOOL_MEM = ".idToolMem";
        String MIN_DISK_SPACE = ".minDiskSpace";
    }

    interface RequestToolIds {
        String TOOL_REQUEST_VIEW = ".toolRequestView";
        String toolLink = ".idToolLink";
        String toolUpld = ".idToolUpld";
        String toolSlt = ".idToolSlt";
        String testUpld = ".idTestUpld";
        String otherUpld = ".idOtherUpld";
        String otherSlt = ".idOtherSlt";
        String toolName = ".idToolName";
        String toolDesc = ".idToolDesc";
        String toolAttrib = ".idToolAttrib";
        String toolDoc = ".idToolDoc";
        String toolVer = ".idToolVer";
        String runInfo = ".idRunInfo";
        String otherInfo = ".idOtherInfo";
        String binUpld = ".idBinUpld";
        String testDataUpld = ".idTestDataUpld";
        String otherDataUpld = ".idOtherDataUpld";
    }
}
