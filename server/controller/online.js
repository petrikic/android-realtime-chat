const user = require('./user');

let stackOnline = [];
let sockets = {};


const addUser = (userid, socket) => {
    sockets[userid] = socket;
}

const removeUser = (userid) => {
    delete sockets[userid];
}

const checkUser = (userid) => {
    return sockets[userid] && true;
}

const listUsers = (myId) => {
    objUsers = [];

    Object.keys(sockets).forEach(userid => {
        if (userid != myId) {
            usr = user.findById(userid);
            objUsers.push(usr);
        }
    });
    console.log(objUsers)
    return objUsers;
}

const getSocket = (user) => {
    return sockets[user];
}

exports.set = addUser;
exports.remove = removeUser;
exports.check = checkUser;
exports.list = listUsers;
exports.getSocket = getSocket;