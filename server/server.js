const express = require('express');
const bodyParser = require('body-parser');
const auth = require('./controller/authController');
const authSocket = require('./middleware/authSocket')

const app = express();
const server = require('http').createServer(app);
const io = require('./events/socket')(server);

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended: false
}));

io.use(authSocket);

app.use('/auth', auth);

server.listen(3000, () => {
    console.log('Server listen on http://localhost:3000');
});