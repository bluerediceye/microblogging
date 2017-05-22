## How To Build 
- mvn clean package docker:build
- docker run -p 80:8080 --name microblogging -d fr/microblogging

## Where to find Dockerfile
- It can be found from microblogging/docker/Dockerfile. Please make sure docker-entrypoint.sh is in the same directory.

## Where is API docs
- It can be found from http://{host}:{port}/swagger-ui.html

## What's the connection credentials
- They are hard-coded at this moment. e.g. admin/admin

## REST API examples
- Create a post
  ----  
      curl -X POST \
      http://localhost:8080/post/ \
      -H 'accept: */*' \
      -H 'authorization: Basic bWluZzptaW5n' \
      -H 'cache-control: no-cache' \
      -H 'content-type: application/json' \
      -H 'postman-token: 02f458c8-3317-2ce2-8bfd-01e6b9381a8e' \
      -d '{
      "title": "my first post",
      "body": "This is my first post",
      "userId": "1234"}'
   ----  
- Update a post
   ----  
      curl -X PUT \
       http://localhost:8080/post/592165178bc27a3ca1bdec0b \
      -H 'accept: */*' \
      -H 'authorization: Basic bWluZzptaW5n' \
      -H 'cache-control: no-cache' \
      -H 'content-type: application/json' \
      -H 'postman-token: fdb00b64-3dc2-f41d-1118-0befb207a7f6' \
      -d '{
       "title": "my modified post",
       "body": "This is my modified post",
       "userId": "1234"}'
   ----
- Retrieve a post by Id
  ----
      curl -X GET \
      http://localhost:8080/post/592013088bc27a1f3d93ffe3 \
      -H 'authorization: Basic YWRtaW46YWRtaW4=' \
      -H 'cache-control: no-cache' \
      -H 'postman-token: 2b2f3a96-9d42-3ab9-13ec-61cbe5bddbe9'
  ----
- Retrieve posts by user Id and timestamp
  ----
      curl -X GET \
      'http://localhost:8080/user/1234?start=2017-05-21T10%3A23%3A39' \
      -H 'authorization: Basic bWluZzptaW5n' \
      -H 'cache-control: no-cache' \
      -H 'content-type: application/json' \
      -H 'postman-token: 25d50631-2304-1f50-2ce0-e40164574368' \
      -d '{
      "id": "591f5e9495ee58171862d175",
      "body": "hello hel he",
      "timestamp": "2017-05-19T09:07:32",
      "userId": "MingLi",
      "ratings": null}'
   ----
- Delete a post
    ---- 
        curl -X DELETE \
         http://localhost:8080/post/592013088bc27a1f3d93ffe3 \
         -H 'authorization: Basic YWRtaW46YWRtaW4=' \
         -H 'cache-control: no-cache' \
         -H 'postman-token: b77f8735-33d4-adda-202f-0c5764baaef5' \
    ----
- Fulltext search posts
    ----
         curl -X GET \
        'http://localhost:8080/post/search?query=hel' \
        -H 'authorization: Basic bWluZzptaW5n' \
        -H 'cache-control: no-cache' \
        -H 'content-type: application/json' \
        -H 'postman-token: 38761f90-5dfd-5cdb-46c4-18ce4a1a5f58' \
        -d '{
        "id": "591f5e9495ee58171862d175",
        "body": "hello hel he",
        "timestamp": "2017-05-19T09:07:32",
        "userId": "MingLi",
        "ratings": null     
    ----
- Rate a post
    ----
       curl -X POST \
       http://localhost:8080/post/592013088bc27a1f3d93ffe3/rating \
       -H 'authorization: Basic bWluZzptaW5n' \
       -H 'cache-control: no-cache' \
       -H 'content-type: application/json' \
       -H 'postman-token: 79c84794-b9d6-605f-2cac-857b0cdeff94' \
       -d '{
     	"userId":"123453333",
     	"rate": 3}'
     ----
- Retrieve rates of a post
    ----
    	curl -X GET \
        http://localhost:8080/post/592013088bc27a1f3d93ffe3/ratings \
        -H 'authorization: Basic bWluZzptaW5n' \
        -H 'cache-control: no-cache' \
        -H 'postman-token: 084fb261-0c42-8693-92c5-e08e3aaed148'
    ----