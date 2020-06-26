module.exports = (server) => {
    const io = require('socket.io')(server);
    const online = require('./online');
    const chat = require('./chat');

    io.on('connection', client => {
        console.log(`Cliente conectado: ${client.id}`);
        img = 'https://scontent.ffor2-1.fna.fbcdn.net/v/t1.0-9/p960x960/72265143_1305686996280534_3504091622977568768_o.jpg?_nc_cat=106&_nc_sid=85a577&_nc_ohc=_0qK6cM8UPQAX_TGl2J&_nc_ht=scontent.ffor2-1.fna&_nc_tp=6&oh=945c62b01c967c828ece22f951dd286b&oe=5F194522';
        url = 'https://habitusinvestimentos.com.br/wp-content/uploads/2019/12/facebook-avatar.jpg';
        users = {users: [{
                id: '12332',
                username: "User1",
                urlPhoto: img
            },
            {
                id: 12348,
                username: "User2",
                urlPhoto: url
            },
            {
                id: '12332',
                username: "User1",
                urlPhoto: img
            },
            {
                id: 12348,
                username: "User2",
                urlPhoto: url
            },
            {
                id: '12332',
                username: "User1",
                urlPhoto: img
            },
            {
                id: 12348,
                username: "User2",
                urlPhoto: url
            },
            {
                id: '12332',
                username: "User1",
                urlPhoto: img
            },
            {
                id: 12348,
                username: "User2",
                urlPhoto: url
            },
            {
                id: '12332',
                username: "User1",
                urlPhoto: img
            },
            {
                id: 12348,
                username: "User2",
                urlPhoto: url
            }]
        }

        client.emit('listUsers', users);
        
        client.on('teste', value => {
            //client.emit('listUsers', users);
            //console.log(value);
        });
    });

    online.use(io);
    chat.use(io);

    return io;
}