import React from "react";
import { makeStyles, withStyles } from "@material-ui/core/styles";
import InputLabel from "@material-ui/core/InputLabel";
import MenuItem from "@material-ui/core/MenuItem";
import FormControl from "@material-ui/core/FormControl";
import Select from "@material-ui/core/Select";
import styles from "../AllStatsStyle";
import ids from "./AllStatsIDs";

const useStyles = makeStyles((theme) => ({
    formControl: {
        margin: theme.spacing(1),
        minWidth: 120,
    },
    selectEmpty: {
        marginTop: theme.spacing(2),
    },
}));

function SimpleSelect(props) {
    const classes = props;
    const [age, setAge] = React.useState("");

    const inputLabel = React.useRef("");
    const [labelWidth, setLabelWidth] = React.useState(0);

    React.useEffect(() => {
        setLabelWidth(inputLabel.current.offsetWidth);
    }, []);

    const handleChange = (event) => {
        setAge(event.target.value);
    };

    return (
        <div>
            <FormControl className={props.id}>
                <InputLabel>App Count</InputLabel>
                <Select id={props.id} value={age} onChange={handleChange}>
                    <MenuItem value={10}>10</MenuItem>
                    <MenuItem value={20}>20</MenuItem>
                    <MenuItem value={30}>30</MenuItem>
                    <MenuItem value={50}>50</MenuItem>
                    <MenuItem value={100}>100</MenuItem>
                    <MenuItem value={500}>500</MenuItem>
                    <MenuItem value={1000}>1000</MenuItem>
                </Select>
            </FormControl>
        </div>
    );
}

export default withStyles(styles)(SimpleSelect);
