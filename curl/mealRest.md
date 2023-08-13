## get all

curl http://localhost:8080/topjava/rest/meals

## get between

curl http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-31&startTime=13:00&endDate=2020-01-31&endTime=20:00

## get

curl http://localhost:8080/topjava/rest/meals/100003

## create

curl -X POST
-H 'Content-Type: application/json'
-d '{"dateTime":"2023-08-13T13:00","description":"food", "calories":500}'
http://localhost:8080/topjava/rest/meals

## update

curl -X PUT
-H 'Content-Type: application/json'
-d '{"dateTime":"2023-08-13T15:00","description":"Updated", "calories":"1300"}'
http://localhost:8080/topjava/rest/meals/100007

## delete

curl -X DELETE http://localhost:8080/topjava/rest/meals/100007