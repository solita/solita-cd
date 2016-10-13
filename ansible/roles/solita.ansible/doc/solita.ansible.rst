==============
solita.ansible
==============

An Ansible role for installing Ansible.

.. contents::
   :backlinks: none
   :local:

-------
Example
-------

.. highlight:: yaml

::

    # playbook.yml
    ---
    - hosts: servers
      vars:
        solita_ansible_version: 2.1.0.0
      roles:
         - solita.ansible

------------
Installation
------------

.. highlight:: yaml

You can install this role with ansible-galaxy_. First add the following lines
to your ``requirements.yml``:

.. code-block:: yaml
   :emphasize-lines: 3-4

    # requirements.yml
    ---
    - src: https://github.com/solita/ansible-role-solita.ansible.git
      version: v1.1.0
      name: solita.ansible

.. highlight:: sh

Then run ``ansible-galaxy`` to install the role::

    ansible-galaxy install -p path/to/your/roles -r requirements.yml

---------------
Ansible version
---------------

By default, the role will install the latest version of Ansible. To install a
specific version, set the variable ``solita_ansible_version``.

Examples
========

.. highlight:: yaml

::

    # playbook.yml
    ---
    - hosts: servers
      vars:
        solita_ansible_version: 2.1.0.0
      roles:
         - solita.ansible

-----
Roles
-----

To install roles, list them in a `requirements file`_ called
``requirements.yml`` next to your playbook. The contents of
``/etc/ansible/roles`` will be updated to match ``requirements.yml``.

.. note ::

    ``/etc/ansible/roles`` will be removed before installing the new roles, so
    if you create ``requirements.yml``, make sure to list all your roles!

If you don't have a ``requirements.yml``, no changes will be made to
``/etc/ansible/roles``.

You can change the path to the requirements file by setting the variable
``solita_ansible_requirements_file``. Set it to an empty string to disable role
installation.

Examples
========

.. highlight:: yaml

Change the path to the requirements file::

    # playbook.yml
    ---
    - hosts: servers
      vars:
        solita_ansible_requirements_file: roles.yml
      roles:
         - solita.ansible

Disable role installation::

    # playbook.yml
    ---
    - hosts: servers
      vars:
        solita_ansible_requirements_file: ""
      roles:
         - solita.ansible

.. _ansible-galaxy: http://docs.ansible.com/ansible/galaxy.html#the-ansible-galaxy-command-line-tool
.. _requirements file: http://docs.ansible.com/ansible/galaxy.html#installing-multiple-roles-from-a-file
