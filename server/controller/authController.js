const router = require('express').Router();
const jwt = require('jsonwebtoken');
const user = require('./user');
const authenticator = require('../middleware/authenticator');

const authConfig = require('../config/auth.json');

const generateToken = (params = {}) => {
    return jwt.sign(params, authConfig.secret, {
        expiresIn: 20 //86400
    });
}

router.get('/validatetoken', authenticator, (req, res) => {
    res.status(200).send('OK');
} );


router.post('/register', (req, res) => {
    let usr = {
        username: req.body.username,
        password: req.body.password
    }
    if (usr.username.length < 4) {
        res.status(400).send({
            error: "Username is very small"
        });
    } else if (usr.password.length < 4) {
        res.status(400).send({
            error: "Password is very small"
        })
    } else if (user.findOne(usr.username)) {
        res.status(400).send({
            error: "Username already exists"
        });
    } else {
        user.insert(usr);
        const userInfo = user.findOne(usr.username);
        res.status(200).send({
            token: generateToken(userInfo)
        });
    }
});

router.post('/login', (req, res) => {
    const usr = {
        username: req.body.username,
        password: req.body.password
    }

    if (!user.findOne(usr.username))
        return res.status(401).send({
            error: 'User not found'
        });

    if (!user.find(usr))
        return res.status(403).send({
            error: 'Not authorized'
        });

    const userInfo = user.findOne(usr.username);

    res.status(200).send(generateToken(userInfo));

});

module.exports = router