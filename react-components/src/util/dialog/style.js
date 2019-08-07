import Color from "../CyVersePalette";

export default (theme) => ({
    dialogCloseButton: {
        position: 'absolute',
        right: 0,
        top: 10,
        color: Color.white,
        '&:hover': {
            backgroundColor: Color.lightBlue,
        },
    },
    header: {
        backgroundColor: Color.darkBlue
    },
    title: {
        color: Color.white
    }
});