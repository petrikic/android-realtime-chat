const { sign } = require('jsonwebtoken');
const online = require('../controller/online');
const messageDB = require('../controller/message');

exports.use = (io) => {
    
    io.on('connection', client => {
        let messages = messageDB.getMessages(client.decoded.id);
        if(messages){
            messages.forEach(message => {
                client.emit('receiveMessage', message);
            });
            messageDB.deleteMessages(client.decoded.id);
        }
        client.on('sendMessage', message => {
            let receiver = online.getSocket(message.receiverId);
            let timestamp = Date.now();
            let senderMessage = {
                text: message.text,
                userReferenceId: message.receiverId,
                receiverId: message.receiverId,
                senderId: client.decoded.id,
                timestamp: timestamp
            }
            let receiverMessage = {
                text: message.text,
                userReferenceId: client.decoded.id,
                receiverId: message.receiverId,
                senderId: client.decoded.id,
                receiverId: message.receiverId,
                timestamp: timestamp
            }
            client.emit('receiveMessage', senderMessage);
            if(online.check(message.receiverId)){
                receiver.emit('receiveMessage', receiverMessage);
            }
            else {
                messageDB.insert(receiverMessage);            
            }
        });
    });
}