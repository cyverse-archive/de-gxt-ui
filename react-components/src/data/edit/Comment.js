import React, {Component} from "react";
import Typography from '@material-ui/core/Typography';
import ListItem from '@material-ui/core/ListItem';


class Comment extends Component{
    state = {
        anchorEl: null
    };
    constructor(props){
        super(props);
        this.handleClick = this.handleClick.bind(this);

    }

    handleClick = name => event => {
        this.setState({
            [name]: event.target.value,
        });
    };

    render(){
        const {date, owner, message, id, retracted} = this.props;

        return (
            <div id={id}>
                <ListItem button onClick={this.handleClick}>
                    <Typography>
                        On <b>{date}</b> {owner} wrote:
                        <br />
                        {retracted ? <Typography color="error">Retracted</Typography> :
                            <Typography>{message}</Typography>}

                    </Typography>
                </ListItem>
                <hr />
            </div>
        )
    }

}

export default Comment