#!/usr/bin/env bash

BASE=/opt/smokeroven
mkdir -p ${BASE}/bin
mkdir -p ${BASE}/config
mkdir -p ${BASE}/logs

wget -o ${BASE}/bin/smokeroven http://dusthoff.github.io/scripts/smokeroven.sh
touch ${BASE}/config/application.yml

ln -s ${BASE}/bin/smokeroven /etc/init.d/smokeroven
update-rc.d smokeroven defaults
${BASE}/bin/smokeroven start
