class socketService {

    constructor(w) {
        this._worldObjectManger = w;
    }

    async connect() {
        this.socket = new WebSocket("ws://" + window.location.hostname + ":" + window.location.port + "/connectToSimulation");
        // Connection opened
        this.socket.addEventListener('open', (e) => {
            this.socket.binaryType = 'arraybuffer'; // important
            console.log('Connected to server with following event: ', e);
        });

        // Do something based on command
        this.socket.onmessage = async e => {
            let command = this.parseCommand(e.data);
            if (command.command === 'object_update') {
                await this._worldObjectManger.updateObject(command);
            }
        };

        // If socket disconnects try to reconnect
        this.socket.onclose = e => {
            console.log('Error connecting. Clearing worldObjects and attempting to reconnect.', e.reason);
            this._worldObjectManger.clearWorld();
            setTimeout(() => {
                this.connect();
            }, 1000);
        };
    };

    parseCommand(input = "") {
        return JSON.parse(input);
    }

}

export {socketService};