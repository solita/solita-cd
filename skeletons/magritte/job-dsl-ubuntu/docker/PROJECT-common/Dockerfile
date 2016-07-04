FROM solita/ubuntu-systemd-ssh:16.04

RUN apt-get update && \
  apt-get install -y \
    python \
    sudo

RUN useradd -m ansible -s /bin/bash && \
  echo 'ansible ALL = (ALL:ALL) NOPASSWD: ALL' >/etc/sudoers.d/ansible

COPY id_rsa.pub /tmp/
RUN mkdir /home/ansible/.ssh && \
  cat /tmp/id_rsa.pub >>/home/ansible/.ssh/authorized_keys && rm -f /tmp/id_rsa.pub && \
  chmod 700 /home/ansible/.ssh && \
  chmod 600 /home/ansible/.ssh/authorized_keys && \
  chown -R ansible:ansible /home/ansible/.ssh
