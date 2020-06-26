exports.use = (io) => {
    const online = require('../controller/online');
    const user = require('../controller/user');
    
    io.on("connection", client => {
        const username = client.decoded.username;
        const userid = client.decoded.id;
        client.join(userid);
        if (!online.check(userid)) {
            client.broadcast.emit('newUser', user.findById(userid));
            console.log(`User online: ${username}`);
        }
        online.set(userid, client);


        client.emit('listUsers', {
            users: online.list(userid)
        });


        client.on("disconnect", () => {
            online.remove(userid);
            console.log(`User offline: ${username}`);
            if (!online.check(userid)) {
                client.broadcast.emit('dropUser', user.findById(userid));
            }
        });
    });
}