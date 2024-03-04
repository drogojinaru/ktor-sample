cd $(dirname $0)
curl -H "Content-Range: bytes 42-1233/*" -v -F "file=@myfile.txt" "localhost:8080/file"