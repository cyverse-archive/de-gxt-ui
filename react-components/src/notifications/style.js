import Color from "../util/CyVersePalette";

export default (theme) => ({
    table: {
        overflow: "auto",
        height: "75%",
    },
    tableHead: {
        backgroundColor: Color.blue,
        position: "sticky",
        top: 0,
    },
    container: {
        width: "100%",
        height: "100%",
        marginTop: 0,
        overflow: "auto",
    },
    toolbarButton: {
        marginRight: 20,
        textTransform: "none",
    },
    toolbar: {
        backgroundColor: Color.lightGray,
        borderBottom: "solid 2px",
        borderColor: Color.gray,
        height: 55,
    },
    notification: {
        textDecoration: "underline",
        cursor: "pointer",
    },
    unSeenNotificationBackground: {
        backgroundColor: Color.lightBlue,
    },
    dropDown: {
        margin: 3,
        height: 40,
        flexDirection: "unset",
    },
    dropDownLabel: {
        paddingLeft: 5,
        fontSize: 10,
    },
});
