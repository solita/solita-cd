=========================================================
Jenkins Configuration Management with Ansible and Job DSL
=========================================================

This tutorial will show you how to manage the configuration of a Jenkins installation with Ansible_ and the Jenkins `Job DSL plugin`_. At the end of the tutorial, you'll have your Jenkins installation's configuration under version control and you'll be able to test changes to the configuration locally in a virtual machine before applying them to your CI server.

First we'll create a configuration for a Jenkins installation that runs on your computer in a virtual machine. Then we'll apply the same configuration to a Linux server, either updating the configuration of an existing Jenkins installation or creating a new installation.

This tutorial covers the basics of Vagrant_, Ansible_ and Jenkins `Job DSL`_. If you're proficient with all these tools, you can probably skip this tutorial and jump straight to the reference documentation of the :doc:`solita.jenkins` Ansible role.

.. contents::
   :backlinks: none
   :local:

-------------
Prerequisites
-------------

Before continuing with this tutorial, you need to have the following software installed:

- Git_
- Vagrant_

We'll be using Git via its command-line interface, but don't worry if you don't have experience with it – the instructions should be clear enough. (If they are not, please `create an issue`_!)

------------------------------
Setting up the Virtual Machine
------------------------------

One of the biggest benefits of configuration management is that we can practice and test our changes on a test server before applying them to a production server. Virtual machines work especially well as test servers, because if we make a mistake and get a server into a funny state, we can simply destroy it and recreate it in a known state.

We'll manage our virtual machines with a tool called Vagrant_, which allows us to describe what kind of machines we want with a simple text-based configuration file. Vagrant supports many different virtualization tools, but we'll be using VirtualBox_, which should have been installed along with Vagrant.

.. highlight:: sh

First we'll create a directory that will hold both our Jenkins configuration and the Vagrant configuration of the virtual machine that we use for testing. As a starting point, we'll use a `Ubuntu-based Vagrant configuration`_ that pre-installs Ansible_, which we'll need later in this tutorial. We need to clone it with Git, so open up a terminal (on Linux and OS X) or **Git Bash** (on Windows).

.. note ::

    The shell starts in your home directory (``/home/yourname``, ``/Users/yourname``, or ``C:\Users\yourname``). If you want to place your Jenkins configuration somewhere else, change the directory with ``cd``::

        # Linux and OS X
        cd /where/you/want/the/conf

    ::

        # Windows. Git Bash doesn't like the backslashes in Windows-style paths, so
        # we need to put the path in single quotes.
        cd 'C:\where\you\want\the\conf'

Clone the Vagrant configuration that we'll use as our starting point::

    git clone https://github.com/solita/vagrant-ansible.git jenkins

.. note ::

    The virtual machine is configured to use the IP address ``192.168.50.76``. If you want to use some other address, find ``192.168.50.76`` in the ``Vagrantfile`` and replace it with another value.

Now enter the cloned repository, which contains our Vagrant configuration, and start the virtual machine with ``vagrant up``::

    cd jenkins
    vagrant up

With this command, Vagrant downloads a disk image of an empty Ubuntu server, boots a virtual machine, and sets it up according to its configuration file (the ``Vagrantfile``). This will take several minutes, so now is good time to go grab a coffee (or a non-caffeinated beverage, if it's getting late and you don't want to mess up your night's sleep).

.. highlight:: text

If all goes well, the command's output should look something like this::

    Bringing machine 'default' up with 'virtualbox' provider...
    ==> default: Importing base box 'ubuntu/trusty64'...
    ==> default: Matching MAC address for NAT networking...
    ==> default: Checking if box 'ubuntu/trusty64' is up to date...
    ==> default: Setting the name of the VM: jenkins_default_1450333475157_82556
    ==> default: Clearing any previously set forwarded ports...
    ...
    blah blah blah
    ...
    ==> default: Collecting pycrypto>=2.6 (from ansible==2.0.0)
    ==> default: Collecting ecdsa>=0.11 (from paramiko->ansible==2.0.0)
    ==> default:   Using cached ecdsa-0.13-py2.py3-none-any.whl
    ==> default: Collecting MarkupSafe (from jinja2->ansible==2.0.0)
    ==> default: Installing collected packages: ecdsa, pycrypto, paramiko, MarkupSafe, jinja2, PyYAML, ansible
    ==> default:   Running setup.py install for ansible
    ==> default: Successfully installed MarkupSafe-0.23 PyYAML-3.11 ansible-2.0.0 ecdsa-0.13 jinja2-2.8 paramiko-1.16.0 pycrypto-2.6.1

.. highlight:: sh

Now that the virtual machine is up and running, we can connect to it with SSH_::

    vagrant ssh

.. highlight:: text

If everything's working, you should be greeted with the following output::

    Welcome to Ubuntu 14.04.3 LTS (GNU/Linux 3.13.0-71-generic x86_64)

     * Documentation:  https://help.ubuntu.com/

      System information as of Thu Dec 17 06:24:57 UTC 2015

      System load:  0.76              Processes:           80
      Usage of /:   3.4% of 39.34GB   Users logged in:     0
      Memory usage: 25%               IP address for eth0: 10.0.2.15
      Swap usage:   0%

      Graph this data and manage this system at:
        https://landscape.canonical.com/

      Get cloud support with Ubuntu Advantage Cloud Guest:
        http://www.ubuntu.com/business/services/cloud

    0 packages can be updated.
    0 updates are security updates.


    (ansible-1.9-env)vagrant@vagrant-ubuntu-trusty-64:/ansible$

.. highlight:: sh

You're now in a shell on the virtual machine, and any commands you type here are executed on the virtual machine, not on your own. To get back to the shell on your own machine, use the ``exit`` command in the virtual machine's shell::

    (ansible-1.9-env)vagrant@vagrant-ubuntu-trusty-64:/ansible$ exit
    logout
    Connection to 127.0.0.1 closed.

.. _vagrant-survival-guide:

Vagrant Survival Guide
======================

You can always get back to the virtual machine's shell by following the same steps we just used:

1. Enter the Jenkins configuration directory: ``cd /path/to/jenkins``
2. Start the virtual machine if it has been stopped: ``vagrant up``
3. Open a shell on the virtual machine: ``vagrant ssh``

If you don't need the virtual machine and want to free the memory and CPU resources it's using, you can stop it with the command ``vagrant halt``.

To free the disk space allocated to the virtual machine, you can destroy it with ``vagrant destroy``. Note that the virtual machine is not stored in the same directory as its ``Vagrantfile``, so just removing the ``jenkins`` directory won't destroy the virtual hard disk.

Finally, for advanced management and debugging, start **Oracle VM VirtualBox**, the VirtualBox management interface, from your operating system's menu. There you can see VirtualBox's logs or shut down and destroy a virtual machine even if you've lost Vagrant's bookkeeping files and can't manage the machine with Vagrant.

For more information, see the documentation for `Vagrant <https://docs.vagrantup.com/v2/>`__ and `VirtualBox <https://www.virtualbox.org/wiki/Documentation>`__.

-------------------------------
Installing Jenkins with Ansible
-------------------------------

We want our Jenkins installations to be as reproducible as possible, so our goal is to configure nothing by hand. Instead, we describe the configuration that a server should have and let Ansible_ make the required changes. These descriptions are written in the YAML_ format in files called playbooks_.

.. highlight:: yaml

Let's create a playbook that tells Ansible that the Vagrant-managed virtual machine should have Jenkins installed. Create a new file called ``jenkins.yml`` in the ``jenkins`` directory with the following contents::

    ---
    - hosts: vagrant        # (1)
      roles:                # (2)
        - solita.jenkins    # (3)

We won't go into details of Ansible playbooks -- for that, see `Ansible's documentation <playbooks_>`__ -- but here's a big-picture view of what the file contains:

1. We tell Ansible that we want to configure the server called "vagrant" (that's our virtual machine).

2. We want to apply some roles_ – reusable pieces of Ansible configuration – to the server.

3. The only role we need is :doc:`solita.jenkins`.

.. note ::

    YAML is very fussy about indentation, so make sure that you indent each line exactly as shown. You should also change your editor to only add spaces and never tabs.

Installing Dependencies
=======================

We use the role :doc:`solita.jenkins`, so we need to tell Ansible where it can be found. Create a file called ``requirements.yml`` in the ``jenkins`` directory::

    ---
    - src: https://github.com/solita/ansible-role-solita.jenkins.git
      name: solita.jenkins

Because Ansible does not officially support Windows, we'll be running it in our virtual machine, so :ref:`connect to it <vagrant-survival-guide>` with ``vagrant ssh``.

.. highlight:: sh

Now you can use `Ansible galaxy`_ to install the role with  the following command::

    ansible-galaxy install -p roles -r requirements.yml

.. note ::

    If you get a message saying ``[ERROR]: Unable to open requirements.yml``, you may have created the file in the wrong directory. Make sure you have a file called ``requirements.yml`` in the ``jenkins`` directory, next to the ``Vagrantfile``.

Running the Playbook
====================

Now that we have installed our playbook's dependencies (the :doc:`solita.jenkins` role), you can run the playbook::

    ansible-playbook -i environments/vagrant/inventory jenkins.yml

Once the playbook run is complete, Jenkins should be installed, running, and listening on the virtual machine's port 8080. The virtual machine's IP address is ``192.168.50.76``, so you can access the Jenkins installation with a web browser at http://192.168.50.76:8080/.

.. _Ansible: http://www.ansible.com/
.. _Ansible galaxy: http://docs.ansible.com/ansible/galaxy.html#the-ansible-galaxy-command-line-tool
.. _Job DSL: https://wiki.jenkins-ci.org/display/JENKINS/Job+DSL+Plugin
.. _Job DSL plugin: `Job DSL`_
.. _Git: http://www.git-scm.com/
.. _Vagrant: http://vagrantup.com/
.. _create an issue: https://github.com/solita/solita-cd/issues/new
.. _VirtualBox: https://www.virtualbox.org/
.. _SSH: https://en.wikipedia.org/wiki/Secure_Shell
.. _YAML: http://docs.ansible.com/ansible/YAMLSyntax.html
.. _playbooks: http://docs.ansible.com/ansible/playbooks.html
.. _roles: http://docs.ansible.com/ansible/playbooks_roles.html
.. _Ubuntu-based Vagrant configuration: https://github.com/solita/vagrant-ansible
