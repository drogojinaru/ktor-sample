curl -X PUT -d '{"id": "100", "title": "How to use Ubuntu", "author" : "Mr. LinuxHappy", "price": 180.5}' \
-H "Content-Type: application/json" "localhost:8080/books"