========
Magritte
========

*This is not a pipeline -- but it can help you build one.*

.. note ::

  Magritte is past the proof of concept stage, but it's not yet in beta. We've
  only just begun to use it for production pipelines. We may still discover
  something that will force us to make major changes to the design. There are
  certainly errors and omissions that we are yet to find and fix. Documentation
  is "coming soon".

  If all that didn't put you off, you're very welcome to give it a try and let
  us know what you think on `the project's mailing list`_! When you encounter
  problems (and that's "when", not "if"), please `open a new issue`_ on the
  GitHub project page or `send a new message to the mailing list`_ describing
  the problem. Even though it's early days, we take every problem very seriously
  and will fix it as soon as we can.

Software projects are always pressed for time, and infrastructure work often
takes a back seat to product development. Not only do developers lack the time
to build their `deployment pipeline`_ the right way -- they don't have the time
to really think about what "the right way" is! That's where Magritte comes in.
It's a collection of project skeletons and utilities that take the grunt work
out of building Ansible_-based deployment pipelines, letting you focus on
solving the problems specific to your project.

If you'd like to know more about Magritte, you can read about the
:doc:`design_principles` that guide Magritte's development, or if you just want
to take it for a spin, jump straight to :doc:`quick_start`.

.. toctree::
   :maxdepth: 1

   design_principles
   quick_start

.. _deployment pipeline: http://martinfowler.com/bliki/DeploymentPipeline.html
.. _the project's mailing list: https://groups.google.com/forum/#!forum/solita-magritte
.. _open a new issue: https://github.com/solita/magritte/issues/new
.. _send a new message to the mailing list: https://groups.google.com/forum/#!newtopic/solita-magritte
.. _Ansible: https://www.ansible.com
