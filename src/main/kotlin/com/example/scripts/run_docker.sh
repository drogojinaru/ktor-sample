cd $(dirname $0)
#docker run -i loadimpact/k6 run --vus 5 --duration 20s - <script.js
docker run -i --rm -v .:/app -w /app grafana/k6 run script.js
