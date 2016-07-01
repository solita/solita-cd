FROM solita/centos-systemd-ssh:7

COPY id_rsa.pub /tmp/
RUN cat /tmp/id_rsa.pub >> /root/.ssh/authorized_keys && rm -f /tmp/id_rsa.pub

RUN yum install -y \
    python \
    sudo \
    initscripts
