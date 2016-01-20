===================
Configuring Jenkins
===================

The Jenkins installation we just created (in :doc:`jenkins_ansible_installation`) is now in its default settings. To change the settings, we set variables_ in the playbook.

By default the Jenkins installation is unsecured and anonymous users can act as administrators. Let's secure the installation and add a password-protected user account.

We can see in the :ref:`documentation <solita.jenkins_security>` of the :doc:`solita.jenkins` role that we need to set the variables ``solita_jenkins_security_realm`` and ``solita_jenkins_users``. We can do that by changing the playbook ``jenkins.yml`` like this (the lines 3 to 6 are new):

.. code-block:: yaml
   :linenos:
   :emphasize-lines: 3-6

   ---
   - hosts: vagrant
     vars:
       solita_jenkins_security_realm: jenkins
       solita_jenkins_users:
         - some_user
     roles:
       - solita.jenkins

.. highlight:: sh

To apply the changes to the Jenkins installation, run the playbook just as before::

    ansible-playbook -i environments/vagrant/inventory jenkins.yml

If you now try to access Jenkins, you will be redirected to a login screen:

.. image:: /images/jenkins_ansible_login.png

To log in as ``some_user``, we have to know the user's password. When :doc:`solita.jenkins` creates a new user, it writes the user's default password into a file called ``solita_jenkins_default_password`` next to the `Ansible inventory file`_ (the file we passed to ``ansible-playbook`` with the ``-i`` option). You can print the file's contents with ``cat`` on the virtual machine::

    cat environments/vagrant/solita_jenkins_default_password/some_user

Alternatively, since the virtual machine's ``/ansible`` directory is synchronized with the ``jenkins`` directory on your machine, you can just open the file with a text editor.

Open the file and read the password, and you should be able to use it to log in as ``some_user``.

---------------------
Further configuration
---------------------

:doc:`solita.jenkins` can do much more than just add users. See its :doc:`documentation <solita.jenkins>` for all of the configuration options.

.. _Ansible inventory file: http://docs.ansible.com/ansible/intro_inventory.html
.. _variables: http://docs.ansible.com/ansible/playbooks_variables.html
