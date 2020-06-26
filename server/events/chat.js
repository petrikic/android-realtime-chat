const { sign } = require("jsonwebtoken");

exports.use = (io) => {
    
    io.on('connection', client => {
        client.on('sendMessage', message => {
            signMessage = {
                text: message.text,
                senderId: client.decoded.userid,
                timestamp: Date.now(),
                isLeft: false
            }
            client.emit('receiveMessage', signMessage);
        });
    });
}