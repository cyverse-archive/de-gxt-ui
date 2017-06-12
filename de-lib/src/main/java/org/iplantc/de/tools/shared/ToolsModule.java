package org.iplantc.de.tools.shared;

/**
 * Created by sriram on 5/5/17.
 */
public interface ToolsModule {

    interface ToolIds {

        String TOOLS_VIEW = ".toolsView";

        String MENU_TOOLS = ".toolsMenu";
        String MENU_ITEM_ADD_TOOLS = ".addTools";
        String MENU_ITEM_REQUEST_TOOL = ".requestTool";
        String MENU_ITEM_EDIT = ".editTool";
        String MENU_ITEM_DELETE = ".deleteTool";
        String MENU_ITEM_USE_IN_APPS = ".useInApps";

        String MENU_SHARE = ".shareMenu";
        String MENU_ITEM_SHARE_COLLABS = "shareCollabs";
        String MENU_ITEM_SHARE_PUBLIC = ".sharePublic";

        String MENU_ITEM_REFRESH = ".refresh";
    }

    interface EditToolIds {
        String EDIT_DIALOG = "Edit";
        String EDIT_VIEW = ".editView";
        String TOOL_NAME = ".idToolName";
        String TOOL_DESC = ".idToolDesc";
        String TOOL_VER = ".idToolVersion";
        String TOOL_IMG = ".idToolImg";
        String TOOL_TAG = ".idToolTag";
        String TOOL_URL = ".idToolUrl";
        String TOOL_CPU = ".idToolCpu";
        String TOOL_MEM = ".idToolMem";
        String TOOL_NW = ".idToolNw";
        String TOOL_TIME = ".idToolTime";
    }

    interface RequestToolIds {
        String TOOL_REQUEST = "ToolRequest";
        String TOOL_REQUEST_VIEW = ".toolRequestView";
        String toolLink = ".idToolLink";
        String toolUpld = ".idToolUpld";
        String toolSlt = ".idToolSlt";
        String testUpld = ".idTestUpld";
        String testSlt = ".idTestSlt";
        String otherUpld = ".idOtherUpld";
        String otherSlt = ".idOtherSlt";
        String toolName = ".idToolName";
        String toolDesc = ".idToolDesc";
        String toolAttrib = ".idToolAttrib";
        String binLink = ".idBinLink";
        String toolDoc = ".idToolDoc";
        String toolVer = ".idToolVer";
        String runInfo = ".idRunInfo";
        String otherInfo = ".idOtherInfo";
        String binUpld = ".idBinUpld";
        String testDataUpld = ".idTestDataUpld";
        String otherDataUpld = ".idOtherDataUpld";
        String TOOL_INFO_CELL = ".toolInfo";
    }
}
