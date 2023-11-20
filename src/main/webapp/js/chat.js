const WebSocket = require('ws');
const wss = new WebSocket.Server({port: 8088});

const users = new Set();

wss.on('connection', function connection(ws) {
    users.add(ws);

    ws.on('message', function incoming(message) {
        // Aquí recibes el mensaje, asumiendo que es un string en formato JSON
        const receivedMessage = JSON.parse(message);

        // Envías el mensaje a todos los clientes conectados, incluido el usuario y el mensaje
        users.forEach(function each(user) {
            if (user !== ws && user.readyState === WebSocket.OPEN) {
                const decryptedMessage = decryptMessage(receivedMessage.message);
                user.send(`${receivedMessage.user}: ${decryptedMessage}`);
                user.send(`${receivedMessage.user}: ${JSON.stringify(receivedMessage.message)}`);
            }
        });

    });

    ws.on('close', function () {
        users.delete(ws);
    });
});