import {StyleSheet} from "aphrodite";
export default StyleSheet.create({
    label: {
        fontWeight: 'bold',
        fontSize: 10,
        width: 90,
        paddingBottom: '0.5em'
    },
    value: {
        fontSize: 10,
        paddingLeft: 2,
        paddingBottom: '0.5em'
    },
    tagPanelStyle: {
        padding: 2,
        width: '95%',
        height: 100,
        margin: 2,
        overflow: 'scroll',
    },
    tagRemoveStyle: {
        fontWeight: 'bold',
        cursor: 'pointer',
        display: 'none',
        float: 'right',
        fontSize: 12,
        borderLeft: '1px solid',
        paddingLeft: '1px',
    },

    tagDivStyle: {
        maxWidth: '100%',
        border: '1px solid #d9d9d9',
        backgroundColor: '#DB6619',
        whiteSpace: 'nowrap',
        float: 'left',
        fontSize: 10,
        padding: 2,
        cursor: 'pointer',
        ':hover': {
            tagRemoveStyle: {
                display: 'block',
            }
        },
    },

    tagStyle: {
        float: 'left',
        paddingRight: 1,
        color: 'white',
    },

    tagListStyle: {
        fontSize: 'small',
        width: '100%'
    },

    infoTypeStyle: {
        width: '100px',
        fontSize: 'small',
        maxHeight: 100,
    },

    loadingStyle: {
        position: 'absolute',
        top: 200,
        left: 100,
        color: '#DB6619',
    },

})