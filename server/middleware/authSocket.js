const jwt = require('jsonwebtoken');
const authConfig = require('../config/auth.json');

module.exports = (socket, next) => {
    const authorization = socket.handshake.headers.authorization;
    if (authorization) {
        jwt.verify(authorization, authConfig.secret, (err, decoded) => {
            if (err) return next(new Error('Authentication error'));
            socket.decoded = decoded;
            next();
        });
    } else {
        next(new Error('Authentication error'));
    }
}