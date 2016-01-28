====================================================
Prerequisites for development environment
====================================================

Before continuing with this tutorial, you need to have the following software installed:

- Git_
- Vagrant_

We'll be using Git via its command-line interface, but don't worry if you don't have experience with it – the instructions should be clear enough. (If they are not, please `create an issue <https://github.com/solita/solita-cd/issues/new>`_!)

.. _Git: http://www.git-scm.com/
.. _Vagrant: http://vagrantup.com/

====================================================
Prerequisites for production environment
====================================================

When you are happy with the development instance you may want to deploy the jenkins to a production server for real work. To do this you will need a linux server with:

- Ansible_
- Working internet connection (at least until installation is done)
- Roughly 2Gb memory
- Enough free disk for your planned project build artifact history
- Reachable VCS-server for your actual project to continously integrate
- Preferably a SMTP-server_ for Jenkins notification emails.

.. _Ansible http://www.liquidweb.com/kb/how-to-install-ansible-on-centos-7-via-yum/
.. _SMTP-server https://en.wikipedia.org/wiki/Simple_Mail_Transfer_Protocol
