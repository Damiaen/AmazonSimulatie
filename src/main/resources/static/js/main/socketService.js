class socketService {

    constructor(w) {
        this._worldObjectManger = w;
    }

    connect() {
        this.socket = new WebSocket("ws://" + window.location.hostname + ":" + window.location.port + "/connectToSimulation");

        // Connection opened
        this.socket.addEventListener('open', (e) => {
            this.socket.binaryType = 'arraybuffer'; // important
            console.log('Connected to server with following event: ', e);
        });

        this.socket.onmessage = e => {
            let command = this.parseCommand(e.data);
            // console.log('Recieved command:', command);
            if (command.command === 'object_update') {
                this._worldObjectManger.updateObject(command);
            }

            //this._worldObjectManger.updateWorldPosition(command);
        };
        this.socket.onclose = e => {
            console.log('Error connecting. Clearing worldObjects and attempting to reconnect.', e.reason);
            setTimeout(() => {
                this.connect();
            }, 2000);
        };
    };

    parseCommand(input = "") {
        return JSON.parse(input);
    }

}

export {socketService};