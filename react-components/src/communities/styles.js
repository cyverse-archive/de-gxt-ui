import palette from "../util/CyVersePalette";

const styles = theme => ({
    column: {
        display: "flex",
        flexDirection: "column",
        width: "100%",
    },

    subjectSearch: {
        flexGrow: 1,
        flexBasis: '200px'
    },

    formItem: {
        margin: "5px",
    },

    toolbar: {
        backgroundColor: palette.lightGray,
    },

    toolbarItem: {
        marginRight: "15px",
    },

    textBlurb: {
        margin: '10px',
    },

    wrapper: {
        width: "100%",
        height: "100%",
    },

    loading: {
        position: 'absolute',
        top: '30%',
        left: '50%',
        color: palette.orange,
    }
});

export default styles;