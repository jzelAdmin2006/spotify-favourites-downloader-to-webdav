#!/bin/sh

echo '{ "clientId": "'"$CLIENT_ID"'", "clientSecret": "'"$CLIENT_SECRET"'" }' > /app/config/spotify/config.json
echo "$DEEZER_ARL" > /app/config/.arl
java -jar /app/app.jar
