## Vertx CRUD

simple http/websocket server for crud operations

some things were complicated but it's only for benchmark

## WHAT IS IT?
First of all this is primarily for benchmark tests

Well, also if you were looking for some code examples using Vert.x,you can re-create a refactoring by yourself

##Prerequisites

Used **PostgreSQL** as default database, but you can also use MySQL

[Install]("https://www.postgresql.org/download/"") PostgreSQL and run script resources/pg_scripts.sql,
create user and password the same as configured at resources/reference.conf or modify

JVM options
```
-server
-Xms1g
-Xmx2g
-Xss1m
-XX:+AggressiveOpts
-XX:+UseNUMA                                      
-XX:+UseParallelGC     
-XX:+UseCompressedOops
-XX:+TieredCompilation 
-XX:MaxGCPauseMillis=100
-XX:ParallelGCThreads=20
-XX:ReservedCodeCacheSize=128m
```

sysctl.conf (for Linux)
```
net.ipv4.tcp_wmem = 4096 87380 4161536

net.ipv4.tcp_rmem = 4096 87380 4161536

net.ipv4.tcp_mem = 786432 2097152 3145728

fs.file-max = 2000000

fs.nr_open = 2000000
net.ipv4.tcp_syncookies = 1

net.ipv4.tcp_tw_reuse = 1

net.ipv4.tcp_tw_recycle = 1

net.ipv4.tcp_fin_timeout = 30
net.ipv4.tcp_timestamps=0
net.ipv4.tcp_max_syn_backlog = 8192      

net.ipv4.ip_local_port_range = 1024 65535

net.ipv4.tcp_keepalive_time = 1200  
```

## Application usage

### Find or create
* **GET**
```
http://localhost:8081/api/user/some_user?email=some@mail.com&name=Some
```

### Insert new
* **PUT**
```
http://localhost:8081/api/user/some_user?email=some@mail.com&name=Some
```

### Remove
* **DELETE**
```
http://localhost:8081/api/user/some_user
```

### Update
* **POST**
```
http://localhost:8081/api/user/some_user

{ 
    "msg": 
           {
                "name": "Some",
                "email": "some@mail.com",   
                "nickname": "some_user"   
           }
}

```

### Websocket API

```
Connect ws://localhost:8082/api) you receive:

{
  "msg" : "Successfully connected to Websocket API"
}

You wanna find or create some user (assume you already connected ws://localhost:8082/api):

Send frame to ws://localhost:8082/api:

{ 
    "method": "findOrCreate",

    "msg": 
           {
                "name": "Some",
                "email": "some@mail.com",   
                "nickname": "some_user"   
           }
}

All available methods can be viewed by connecting to: ws://localhost:8082/info

```

### Benchmark

[wrk](https://github.com/wg/wrk) used for benchmark

```
wrk -t8 -c256 -d30s 'http://localhost:8081/api/user/some_user?email=some@mail.com&name=Some'
```

where 

**-t** - number of threads = number of cores

**-c** - number of connections

**-d** - duration in seconds

## Author

* **cat-ai@github.com** - *Initial work*