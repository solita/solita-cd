================
Quickstart guide
================

By following these instructions, you should have a minimal deployment pipeline
running on your machine in no time.

.. contents::
   :backlinks: none
   :local:

-------------
Prerequisites
-------------

Before we start, you'll need to have two pieces of software installed on your
machine:

- Git_, for getting Magritte.

- Docker_ (at least version 1.11.2) for running Ansible_ and testing the pipeline without polluting your
  machine.

.. note ::

    At the moment moment **Docker for Mac** and **Docker for Windows** (both in beta)
    are unsupported! Please use the **Docker Toolbox** instead.

.. highlight:: sh

Start the Docker Quickstart Terminal (on Windows and OS X) or any terminal
emulator (on Linux) and run the command::

    docker run --rm hello-world

If you see a message saying "Hello from Docker", your Docker is set up
correctly and you can continue to :ref:`getting_magritte`.  If you get an error
message instead, follow the instructions in :ref:`troubleshooting_windows_os_x`
or :ref:`troubleshooting_linux` below.

.. _troubleshooting_windows_os_x:

Troubleshooting (Windows and OS X)
==================================

The most common causes of problems on Windows and OS X are:

1. You don't have a VirtualBox VM configured for running the Docker server.

2. The VirtualBox VM is not running.

3. Your Docker client is not configured to use the Docker server on the
   VirtualBox VM.

To fix all of these issues, follow the `getting started guide for Docker
Machine`_.

.. _troubleshooting_linux:

Troubleshooting (Linux)
=======================

Magritte expects you to be able to run Docker without ``sudo``. Make sure
you've followed `Docker's installation instructions for Linux`_, and then add
your user to the ``docker`` group::

    sudo usermod -aG docker $USER

.. note ::

    You have to log out and back in for the new group to take effect!

Check that your user is in the ``docker`` group by running the ``groups``
command. Your output will look different, but it should contain the word
``docker``::

    groups
    # user adm cdrom sudo dip plugdev lpadmin sambashare docker
    #                                                    ^^^^^^

.. _getting_magritte:

----------------
Getting Magritte
----------------

.. highlight:: sh

With Magritte, there's no installation needed. Just clone its Git repository::

    git clone https://github.com/solita/magritte.git

.. note ::

    On Windows or OS X, run this and all following commands in the Docker Quickstart Terminal.

----------------------
Creating a new project
----------------------

Create a new project by running the ``init`` script in the ``magritte``
repository you just cloned::

    magritte/init job-dsl-centos hello-magritte

This creates a new project called ``hello-magritte`` based on the
``job-dsl-centos`` project skeleton.

--------------------------------
Provisioning the virtual servers
--------------------------------

Magritte uses Docker_ containers to simulate all the pipeline's servers on your
machine. Start the virtual servers that came with the project skeleton by
moving into the project directory and running the script ``docker/start``::

    cd hello-magritte
    docker/start

Once the script completes, the servers are running but they have not yet been
provisioned, so all they do is run an SSH server. Provision them with
Ansible by running the script ``imagination/provision``::

    imagination/provision

This will take quite a while, as many packages will have to be downloaded over
the Internet. Once the scripts completes, all the virtual servers will be
configured and you'll be ready to run the deployment pipeline for a hello world
application.

--------------------
Logging into Jenkins
--------------------

Our Docker containers' exposed ports are forwarded to random ports the
VirtualBox VM running Docker (on Windows and OS X) or to localhost (on Linux).
Use the script ``docker/port`` to find the port on which we can access the
Jenkins server::

    docker/port
    # magritte-test-ansible:22/tcp -> 127.0.0.1:32983
    # magritte-test-app-build:22/tcp -> 127.0.0.1:32985
    # magritte-test-app-build:4567/tcp -> 127.0.0.1:32984
    # magritte-test-app-prod:4567/tcp -> 127.0.0.1:32986
    # magritte-test-app-prod:22/tcp -> 127.0.0.1:32987
    # magritte-test-app-qa:4567/tcp -> 127.0.0.1:32988
    # magritte-test-app-qa:22/tcp -> 127.0.0.1:32989
    # magritte-test-ci-build:22/tcp -> 127.0.0.1:32991
    # magritte-test-ci-build:8080/tcp -> 127.0.0.1:32990 <--

.. note ::

    A Docker container's ports will be forwarded to different host ports every
    time it's started. If a server doesn't seem to be listening, check the
    output of ``docker/port`` to make sure you're connecting to the correct
    port.

Jenkins is listening on the CI server's port ``8080``, and the output of
``docker/port`` tells us that in this case it's been forwarded to
``127.0.0.1:32990``. Whatever the host and port are in your case, copy them to
your browser's location bar. You should see Jenkins' login screen.

.. image:: /magritte/images/jenkins_login.png

A Jenkins user called ``user`` has been created during Jenkins provisioning.
You can find the password generated for ``user`` in the file
``imagination/build/solita_jenkins_default_password/user``::

    cat imagination/build/solita_jenkins_default_password/user
    # eAWt42:FvPMoaNy_LhlZ

Log in to Jenkins as ``user`` using this password.

-------------------------------
Running the deployment pipeline
-------------------------------

The jobs related to the deployment pipeline are grouped into a folder called
Deployment. Click on the folder to open a `Delivery Pipeline`_ view that
visualizes the deployment pipeline.

.. image:: /magritte/images/jenkins_deployment_folder.png

Create a new instance of the pipeline by clicking the button with a clock and a
green play button ("schedule a build"). Once the Build, Deploy and E2E Test
jobs complete, a black play button appears in the QA Env stage. Click on it to
deploy the built application into the QA environment.

.. image:: /magritte/images/jenkins_deploy.png

The hello world application listens on the port ``4567``, and again
``docker/port`` tells us where we can access the port ``4567`` of the QA
environment's application server::

    docker/port
    # magritte-test-ansible:22/tcp -> 127.0.0.1:32983
    # magritte-test-app-build:22/tcp -> 127.0.0.1:32985
    # magritte-test-app-build:4567/tcp -> 127.0.0.1:32984
    # magritte-test-app-prod:4567/tcp -> 127.0.0.1:32986
    # magritte-test-app-prod:22/tcp -> 127.0.0.1:32987
    # magritte-test-app-qa:4567/tcp -> 127.0.0.1:32988 <--
    # magritte-test-app-qa:22/tcp -> 127.0.0.1:32989
    # magritte-test-ci-build:22/tcp -> 127.0.0.1:32991
    # magritte-test-ci-build:8080/tcp -> 127.0.0.1:32990

Copy the host and port to your browser. You should see a hello world
application, and its build number should match the your deployment pipeline
instance.

.. image:: /magritte/images/hello_world.png

Now that you have a working deployment pipeline for a hello world, let's dig in a little deeper and see how it's put together. Continue to :doc:`anatomy`.

.. _Git: https://www.git-scm.com/
.. _Docker: https://www.docker.com/
.. _Docker's installation instructions for Linux: https://docs.docker.com/linux/step_one/
.. _getting started guide for Docker Machine: https://docs.docker.com/machine/get-started/
.. _Ansible: https://www.ansible.com/
.. _Delivery Pipeline: https://wiki.jenkins-ci.org/display/JENKINS/Delivery+Pipeline+Plugin
