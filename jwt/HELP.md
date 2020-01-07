


Get Auth token

curl -X POST http://localhost:8080/auth/signin -H "Content-Type:application/json" -d "{\"username\":\"user\", \"password\":\"password\"}"


Access API


curl -X GET http://localhost:8080/me -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTUyNDY0OTI4OSwiZXhwIjoxNTI0NjUyODg5fQ.Lj1w6vPJNdJbcY6cAhO3DbkgCAqpG7lzztzUeKMyNyE"