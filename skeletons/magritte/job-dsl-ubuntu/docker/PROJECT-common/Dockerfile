FROM solita/ubuntu-systemd-ssh:16.04

COPY id_rsa.pub /tmp/
RUN cat /tmp/id_rsa.pub >> /root/.ssh/authorized_keys && rm -f /tmp/id_rsa.pub

RUN apt-get update && \
  apt-get install -y \
    python \
    sudo
