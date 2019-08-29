import React, { Component } from "react";
import ViceLogsViewer from "../../../../src/analysis/view/dialogs/ViceLogsViewer";

class ViceLogsViewerTest extends Component {
    constructor(props) {
        super(props);
        this.state = { mask: true };
    }
    render() {
        window.setTimeout(() => {
            this.setState({ mask: false });
        }, 3000);
        const logs =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Vehicula ipsum a arcu cursus vitae congue mauris rhoncus. Adipiscing enim eu turpis egestas pretium. Velit dignissim sodales ut eu sem integer vitae. Nunc non blandit massa enim nec dui nunc mattis. Mollis nunc sed id semper risus in. Malesuada fames ac turpis egestas integer eget aliquet nibh praesent. Diam sit amet nisl suscipit adipiscing bibendum est ultricies. Sem fringilla ut morbi tincidunt augue interdum velit euismod in. Quam vulputate dignissim suspendisse in. Tristique senectus et netus et malesuada fames ac. Aliquet nec ullamcorper sit amet risus nullam eget felis. Porttitor lacus luctus accumsan tortor posuere ac. Nunc sed augue lacus viverra vitae congue eu. Etiam erat velit scelerisque in dictum.\n" +
            "\n" +
            "Et magnis dis parturient montes nascetur. Facilisis gravida neque convallis a cras semper. Ultrices vitae auctor eu augue ut. Non consectetur a erat nam at lectus urna. Felis eget velit aliquet sagittis id consectetur purus. Blandit aliquam etiam erat velit scelerisque in dictum non consectetur. Quisque non tellus orci ac auctor augue mauris augue. Facilisis magna etiam tempor orci eu lobortis elementum. Egestas pretium aenean pharetra magna ac placerat vestibulum lectus. Pellentesque massa placerat duis ultricies. Turpis in eu mi bibendum neque. A cras semper auctor neque vitae tempus. Tincidunt lobortis feugiat vivamus at augue eget arcu dictum varius. Congue eu consequat ac felis. Cras ornare arcu dui vivamus arcu felis bibendum. Cursus vitae congue mauris rhoncus aenean vel elit scelerisque mauris. Id venenatis a condimentum vitae sapien. Risus sed vulputate odio ut. Id aliquet lectus proin nibh. Et ligula ullamcorper malesuada proin libero nunc.\n" +
            "\n" +
            "Et tortor consequat id porta nibh venenatis cras. Eu augue ut lectus arcu bibendum at. Odio euismod lacinia at quis risus sed vulputate. Ac turpis egestas integer eget aliquet. Interdum varius sit amet mattis. Pretium lectus quam id leo in vitae turpis massa sed. Lacus vestibulum sed arcu non. Ultricies tristique nulla aliquet enim tortor at auctor urna nunc. Tortor at auctor urna nunc id. Fames ac turpis egestas sed tempus urna. Et egestas quis ipsum suspendisse ultrices gravida.\n" +
            "\n" +
            "Accumsan in nisl nisi scelerisque eu ultrices vitae auctor. Non pulvinar neque laoreet suspendisse interdum consectetur. Pellentesque dignissim enim sit amet venenatis urna cursus eget. Nec dui nunc mattis enim ut. Ultricies integer quis auctor elit sed. Nisl pretium fusce id velit ut tortor pretium viverra. Viverra orci sagittis eu volutpat odio facilisis mauris. Sed libero enim sed faucibus. Sed blandit libero volutpat sed cras ornare. Sed egestas egestas fringilla phasellus faucibus scelerisque eleifend. Scelerisque felis imperdiet proin fermentum leo vel. Massa tempor nec feugiat nisl pretium fusce id. Id aliquet risus feugiat in. In est ante in nibh mauris cursus mattis.\n" +
            "\n" +
            "Quis enim lobortis scelerisque fermentum dui faucibus in. Vulputate eu scelerisque felis imperdiet proin. Libero enim sed faucibus turpis. Vel risus commodo viverra maecenas accumsan lacus vel. Diam in arcu cursus euismod quis viverra nibh cras pulvinar. Quis eleifend quam adipiscing vitae. Egestas pretium aenean pharetra magna ac placerat vestibulum lectus mauris. Blandit cursus risus at ultrices mi tempus. Ut tellus elementum sagittis vitae. Sapien pellentesque habitant morbi tristique senectus. Non consectetur a erat nam at lectus urna. Pretium vulputate sapien nec sagittis aliquam malesuada bibendum arcu. Sagittis purus sit amet volutpat consequat mauris nunc. Habitasse platea dictumst quisque sagittis purus sit amet. Risus at ultrices mi tempus imperdiet. Etiam non quam lacus suspendisse faucibus interdum posuere lorem ipsum. Scelerisque varius morbi enim nunc. Tincidunt nunc pulvinar sapien et ligula ullamcorper malesuada proin.";
        return (
            <ViceLogsViewer
                logs={logs}
                dialogOpen={true}
                analysisName="test analyses"
                loading={this.state.mask}
            />
        );
    }
}

export default ViceLogsViewerTest;
