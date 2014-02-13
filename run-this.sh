#insert the test data
mongoimport -d MCC -c testEdges --file testDBEdges.json
mongoimport -d MCC -c testNodes --file testDBNodes.json
