FROM solita/ansible-ssh:2.0.2.0

COPY id_rsa.pub /tmp/
RUN cat /tmp/id_rsa.pub >> /home/ansible/.ssh/authorized_keys && rm -f /tmp/id_rsa.pub

RUN mkdir -p /etc/ansible
COPY requirements.yml /etc/ansible/

ENV REFRESHED_AT 2016-04-25

RUN ansible-galaxy install -r /etc/ansible/requirements.yml
