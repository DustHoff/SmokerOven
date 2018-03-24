#!/usr/bin/env bash

BASE=/opt/smokeroven
mkdir -p ${BASE}/bin
mkdir -p ${BASE}/config
mkdir -p ${BASE}/logs

wget -O ${BASE}/bin/smokeroven http://dusthoff.github.io/scripts/smokeroven.sh
chmod a+x ${BASE}/bin/smokeroven
touch ${BASE}/config/application.yml

ln -s ${BASE}/bin/smokeroven /etc/init.d/smokeroven
update-rc.d smokeroven defaults

apt-get install libusb-1.0-0 libudev0 pm-utils
wget -O ${BASE}/brickd.deb http://download.tinkerforge.com/tools/brickd/linux/brickd_linux_latest_armhf.deb
dpkg -i ${BASE}/brickd.deb

DL_URL=$(curl -s https://api.github.com/repos/DustHoff/SmokerOven/releases/latest | grep browser_download_url | cut -d '"' -f 4)
wget -O ${BASE}/SmokerOvenApplication.jar "$DL_URL"

${BASE}/bin/smokeroven start
