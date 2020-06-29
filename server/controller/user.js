const db = require('../database/dao');
const bcrypt = require('bcryptjs');


const insertUser = (user) => {
    user.password = bcrypt.hashSync(user.password, 10);
    user.urlPhoto = '/unknow.jpg';
    db.insert('users', user, (result) =>{
        if(result.error){
            throw result.error;
        }
    });
}

const deleteUser = (user) => {
    const SQL_DELETE = `DELETE FROM users WHERE username = "${user.username}";`
    db.run(SQL_DELETE,(result) => {
        if(result.error){
            throw result.error;
        }
    });
}

const updateUser = (user) => {
    db.update('users', user, `username = "${user.username}"`);
}

const findOne = (username) => {
    let SQL_SELECT_ONE = `SELECT id, username, urlPhoto FROM users
                                WHERE username = "${username}";`;
    return result = db.run(SQL_SELECT_ONE)[0];
}

const findById = (userid) => {
    let SQL_SELECT_ONE = `SELECT id, username, urlPhoto FROM users
                                WHERE id = "${userid}";`;
    return result = db.run(SQL_SELECT_ONE)[0];
}

const findUser = (user) => {
    let SQL_SELECT_USER = `SELECT * FROM users
                                WHERE username = "${user.username}";`;
    let result = db.run(SQL_SELECT_USER)[0];
    return result && bcrypt.compareSync(user.password, result.password);
}

const listUsers = () => {
    let SQL_QUERY = `SELECT username FROM users;`;
    return db.run(SQL_QUERY);
}

exports.insert = insertUser;
exports.delete = deleteUser;
exports.update = updateUser;
exports.find = findUser;
exports.findOne = findOne;
exports.findById = findById;
exports.list = listUsers;