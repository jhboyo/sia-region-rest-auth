## 프로그램 실행 순서
### 1. Dockerfile로 이미지 만들기
image build with `Dockerfile`
```
docker build -t sia-rest-api-demo:0.0.3 .
```
### 2. docker-compose로 container 실행
deploy and run docker image with `docker-compose`
```
cd /{project root}/src/docker
docker-compose up
```
### 3. post api 호출하여 데이터 삽입 또는 pgadmin으로 insert 쿼리 실행
`post` http://localhost:8081/aois
```
$ curl -X POST "Content-Type: application/json; charset=utf-8" -d \
     '{
        "name": "북한산", "area": [
        {
        "x": 127.02,
        "y": 37.742
        },
        ... ]
     }'
```
### 4. get api 호출하여 데이터 조회
```
http://localhost:8081/regions/1/aois/intersects
```

***



## DB Table 작성 쿼리
++전달 받은 테이블에 serial id 값 추가++
++쿼리파일 경로: 프로젝트 루트 경로에 query.txt++

