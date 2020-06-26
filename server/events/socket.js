module.exports = (server) => {
    const io = require('socket.io')(server);
    const online = require('./online');
    const chat = require('./chat');


    

    online.use(io);
    chat.use(io);

    return io;
}