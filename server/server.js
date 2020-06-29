const express = require('express');
const bodyParser = require('body-parser');
const auth = require('./controller/authController');
const authSocket = require('./middleware/authSocket')
const multer = require('multer');
const upload = multer({dest: 'uploads/'});

const app = express();
const server = require('http').createServer(app);
const io = require('./events/socket')(server);


app.use(express.static(__dirname + '/uploads'));
app.use(express.static('public'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
    extended: false
}));

io.use(authSocket);


app.post('/upload', upload.single('image'), (req, res) => {
    console.log(req.body);
})
app.use('/auth', auth);

server.listen(3000, () => {
    console.log('Server listen on http://localhost:3000');
});