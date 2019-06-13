import { palette } from "@cyverse-de/ui-lib";

export default {
    searchButton: {
        display: "flex",
        flexDirection: "row-reverse",
        margin: "5px",
    },

    buttonBar: {
        display: "flex",
        flexDirection: "row",
        justifyContent: "flex-end",
    },

    form: {
        padding: "10px 5px 10px 15px",
        borderStyle: "solid",
        borderWidth: "1px",
        borderColor: palette.gray,
        minWidth: "500px",
        minHeight: "300px",
        maxHeight: "300px",
        overflow: "scroll",
    },

    condition: {
        paddingLeft: "20px",
        marginTop: "5px",
    },

    fileSize: {
        width: "100px",
        display: "inline-flex",
        flexDirection: "column",
        alignItems: "center",
    },

    permissionUsers: {
        marginTop: "5px",
        padding: "5px",
    },

    userChip: {
        margin: "2px",
    },

    autocompleteField: {
        display: "inline-block",
        width: "250px",
        marginTop: "12px",
    },

    conditionButton: {
        margin: "0px 5px 0px 5px",
    },
};
