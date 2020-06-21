module.exports = (server) => {
    const io = require('socket.io')(server);

    io.on('connection', client => {
        console.log(`Cliente conectado: ${client.id}`);
    });

    return io;

}