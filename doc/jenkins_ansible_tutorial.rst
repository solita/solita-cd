=========================================================
Jenkins Configuration Management with Ansible and Job DSL
=========================================================

This tutorial will show you how to manage the configuration of a Jenkins installation with Ansible_ and `Jenkins Job DSL`_. At the end of the tutorial, you'll have your Jenkins installation's configuration under version control and you'll be able to test changes to the configuration locally in a virtual machine before applying them to your CI server.

First we'll create a configuration for a Jenkins installation that runs on your computer in a virtual machine. Then we'll apply the same configuration to a Linux server, either updating the configuration of an existing Jenkins installation or creating a new installation.

This tutorial covers the basics of Vagrant_, Ansible_ and `Jenkins Job DSL`_. If you're proficient with all these tools, you can probably skip this tutorial and jump straight to the reference documentation of the :doc:`solita.jenkins` Ansible role.

.. toctree::
   :maxdepth: 1

   jenkins_ansible_prerequisites
   jenkins_ansible_vm
   jenkins_ansible_installation
   jenkins_ansible_configuration

.. _Ansible: http://www.ansible.com/
.. _Jenkins Job DSL: https://wiki.jenkins-ci.org/display/JENKINS/Job+DSL+Plugin
.. _Vagrant: http://vagrantup.com/
