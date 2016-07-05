=================================
The anatomy of a Magritte project
=================================

Now we'll take a closer look at the files and directories that make up a
Magritte project.

.. note::

    This default structure is just a starting point for your project. Feel free
    to reorganize and change it in any way you want.

.. contents::
   :backlinks: none
   :local:

-------
Ansible
-------

``ansible`` and ``ansible-playbook``

    Wrappers that run the corresponding Ansible_ commands in a Docker
    container.

``ansible.cfg``

    The `Ansible configuration file`_.

``requirements.yml``

    An `Ansible Galaxy requirements file`_ that lists the `Ansible roles`_ that
    the project depends on. After changing this file, destroy and recreate the
    Ansible container by running ``docker/YOURPROJECT-ansible/destroy.sh`` and
    ``docker/YOURPROJECT-ansible/start.sh``.

``*.yml``

    The rest of the YAML files in the project directory are `Ansible
    playbooks`_ that describe how to provision the servers and deploy
    applications to them.

.. note::

    .. highlight:: sh

    Always begin all Ansible commands with ``./`` to use the Ansible wrappers
    and not your local version of Ansible::

        ./ansible -m ping all

-----------------------
Imagination and reality
-----------------------

Coming soon!

-----------------
Docker containers
-----------------

Coming soon!

----------------------
Jenkins jobs and views
----------------------

Coming soon!

---------------
Support scripts
---------------

``.magritte``

    Utility scripts that are called or sourced by the other scripts. There
    should be no need to modify the files in this directory.

.. _Ansible: https://www.ansible.com
.. _Ansible configuration file: http://docs.ansible.com/ansible/intro_configuration.html
.. _Ansible roles: http://docs.ansible.com/ansible/playbooks_roles.html
.. _Ansible galaxy requirements file: http://docs.ansible.com/ansible/galaxy.html#advanced-control-over-role-requirements-files
.. _Ansible playbooks: http://docs.ansible.com/ansible/playbooks.html
