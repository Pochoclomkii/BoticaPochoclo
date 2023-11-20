const WebSocket = require('ws');
const crypto = require('crypto');

const wss = new WebSocket.Server({ port: 8088 });

wss.on('connection', function connection(ws) {
  const dh = crypto.createDiffieHellman(2048);
  const serverKeys = dh.generateKeys();

  ws.send(JSON.stringify({ public_key: serverKeys.toString('hex') }));

  ws.on('message', function incoming(message) {
    try {
      const data = JSON.parse(message);
      const clientPublicKey = Buffer.from(data.public_key, 'hex');
      const sharedKey = dh.computeSecret(clientPublicKey);

      const cipher = crypto.createCipheriv('aes-256-ctr', sharedKey, Buffer.alloc(16, 0));
      let encrypted = cipher.update(data.message, 'utf8', 'hex');
      encrypted += cipher.final('hex');

      console.log('Mensaje cifrado:', encrypted);

      // Simular el envío del mensaje cifrado al cliente
      ws.send(JSON.stringify({ encrypted_message: encrypted }));
    } catch (error) {
      console.error('Error:', error);
    }
  });
});