#install Mongo for travisCI
sudo mkdir -p /data/db 
sudo apt-key adv --keyserver keyserver.ubuntu.com --recv 7F0CEB10 #install new version
echo 'deb http://downloads-distro.mongodb.org/repo/ubuntu-upstart dist 10gen' | sudo tee /etc/apt/sources.list.d/10gen.list
sudo apt-get update
sudo apt-get install -y mongodb-10gen
sudo mongod & > mongod.out &

#insert the test data
mongoimport -d MCC -c testEdges --file testDBEdges.json
mongoimport -d MCC -c testNodes --file testDBNodes.json
