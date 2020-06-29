const sqlite = require('sqlite-sync');
sqlite.connect(__dirname + '/database.db3');


const SQL_CREATE = `CREATE TABLE IF NOT EXISTS Users(
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        username TEXT UNIQUE,
                        password TEXT,
                        urlPhoto
                    );`;

const SQL_CREATE_MESSAGES = `CREATE TABLE IF NOT EXISTS messages(
                        text TEXT,
                        userReferenceId INTEGER,
                        senderId INTEGER,
                        receiverId INTEGER,
                        timestamp INTEGER
                    );`;

sqlite.run(SQL_CREATE);
sqlite.run(SQL_CREATE_MESSAGES);

module.exports = sqlite;