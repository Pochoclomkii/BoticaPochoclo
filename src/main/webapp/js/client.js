const ws = new WebSocket('ws://localhost:8088');

ws.onopen = function() {
  console.log('Conexión establecida.');
  sendMessage(); // Llama a tu función para enviar mensajes aquí
};

ws.onmessage = function(event) {
  // Manejar mensajes entrantes
};

function sendMessage() {
  if (ws.readyState === WebSocket.OPEN) {
    // Verifica si la conexión está abierta
    ws.send('Mensaje a enviar'); // Envía el mensaje como texto plano
  } else {
    console.error('La conexión WebSocket no está abierta todavía.');
  }
}