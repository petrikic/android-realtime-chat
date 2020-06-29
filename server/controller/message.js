const db = require('../database/dao');

const insertMessage = (message) => {
    db.insert('messages', message, (result) =>{
        if(result.error){
            throw result.error;
        }
    });
}

const getMessages = (uuid) => {
    const SQL_SELECT_MESSAGES = `SELECT * FROM messages
                                WHERE receiverId = "${uuid}";`;
    return result = db.run(SQL_SELECT_MESSAGES);
}

const deleteMessages = (uuid) => {
    const SQL_DELETE_MESSAGES = `DELETE FROM messages
                                WHERE receiverId = "${uuid}";`;
    db.run(SQL_DELETE_MESSAGES);
}

exports.insert = insertMessage;
exports.getMessages = getMessages;
exports.deleteMessages = deleteMessages;