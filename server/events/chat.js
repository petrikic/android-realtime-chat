const { sign } = require('jsonwebtoken');
const online = require('../controller/online');

exports.use = (io) => {
    
    io.on('connection', client => {
        client.on('sendMessage', message => {
            let timestamp = Date.now();
            let senderMessage = {
                text: message.text,
                userReferenceId: message.receiverId,
                senderId: client.decoded.id,
                timestamp: timestamp
            }
            let receiverMessage = {
                text: message.text,
                userReferenceId: client.decoded.id,
                senderId: client.decoded.id,
                timestamp: timestamp
                
            }
            let receiver = online.getSocket(message.receiverId);
            client.emit('receiveMessage', senderMessage);
            receiver.emit('receiveMessage', receiverMessage);
        });
    });
}