=================
Design principles
=================

This page describes the key ideas that guide Magritte's development. It will
hopefully make it clear why Magritte was created and why you might want to use
it. If you just want to see Magritte in action, feel free to skip ahead to the
:doc:`quickstart_guide`.

.. contents::
   :backlinks: none
   :local:

---------------------------
Every pipeline is different
---------------------------

There's such a variety of technologies, operating environments, release cycles,
development and QA processes, divisions of labor, legal requirements, and so
on, that building a deployment pipeline often can't be outsourced to people who
don't understand the work and the context it's done in. An off-the-shelf,
one-size-fits-all deployment pipeline can never cover all this variety, as such
a pipeline would drown in parameters (and the complexity created by their
interaction) and it would still never be flexible enough.

Magritte is not a framework where you plug in the parameters of your project. It
gives you a minimal pipeline to start with, but after that, for better or worse,
it's all yours. You're always free to change anything and everything to whatever
works best for you and your project.

----------------------------
... but not *that* different
----------------------------

The fact that each pipeline has unique features that we can't anticipate doesn't
mean that *everything* about it is unique. Our goal is to provide sensible
defaults that work well for *most* projects. (Conversely, we leave out
everything that only a minority of projects is likely to use.)

Some narrowly focused tasks are always performed the same way if they're
performed at all, so their details don't have to be visible to our users. We
push their complexity and maintenance burden away from our project skeletons and
into tools and libraries. Of course, whenever you're not happy with our tool
choice, you're free to swap it for another one.

----------------------------------------------
Nothing should escape configuration management
----------------------------------------------

For every change that's made to the pipeline or any of its servers, you should
be able to tell exactly when the change was made, by whom, what was changed, and
why. You should always be able to roll back every change, even if it means
provisioning new servers from a previous version of the configuration.

Configuration management is so ingrained in Magritte that Ansible_ is the only
dependency you can't easily replace. The only (allowed) way to make changes to
your pipeline is to commit a new configuration to your version control system,
from where it's applied to the servers by your CI tool or `Ansible Tower`_.

-------------------------------------
Every change should be tested locally
-------------------------------------

Every change should be tested locally *before* it's commited to version
control.  First, it keeps the pipeline safe from configuration errors. Second,
it keeps the version control history clear, with each commit doing one thing
only and doing it completely. Third, it shortens the feedback loop during
pipeline development and frees you to try things without fear of breaking the
pipeline.

Magritte allows any developer to simulate the full pipeline with all its
servers on their own development machine. Unlike the real pipeline, the
simulated pipeline takes its configuration from the local filesystem instead of
version control to make pre-commit testing possible.

---------------------------
Our tools should be helpful
---------------------------

It's not enough for our tools (and their documentation) to be useful. They must
be helpful, doing their best to minimize the amount of work the user has to do
and clear any confusion the user might have. We consider any failure to do so a
defect in the tool or its documentation.

One example of this principle in practice is that whether you use Windows, OS
X, or Linux, the only thing you need installed in order to develop, test and
provision a Magritte-based pipeline is Docker_. Even the installation of
Ansible_ and its roles is taken care of without polluting your development
machine.

-------------------------------------------------
Duplication is cheaper than the wrong abstraction
-------------------------------------------------

This principle follows directly from the first *(Every pipeline is different)*
and fifth *(Our tools should be helpful)* but it's important enough, and
perhaps surprising enough, to be mentioned explicitly. As a software developer,
you might be shocked to find out that from the idea of project skeletons to
duplication within them, Magritte is based on copying and pasting code. We hope
to convince you that it's not the result of carelessness but of careful
consideration.

Whenever we have duplication in our project skeletons, we *could* capture the
details into a new abstraction and create multiple instances of it, but by doing
that, we'd force all the instances to evolve in lockstep. Let's suppose we've
done that and you, our user, decide to change just some of these instances. What
are your options? As Sandi Metz describes in `The Wrong Abstraction`_, you can
either keep introducing parameters to the abstraction as the different kinds of
instances diverge, making the system harder and harder to understand, or you can
dismantle the wrong abstraction to make way for new abstractions that are more
useful in your context.

By leaving the duplication in the project skeletons (and paying for the
maintenance burden it brings), we leave you free to take any part of your
pipeline in any direction you want and introduce the abstractions that make
sense in *your* context.

.. _Ansible: https://www.ansible.com
.. _Ansible Tower: https://www.ansible.com/tower
.. _Docker: https://www.docker.com
.. _The Wrong Abstraction: http://www.sandimetz.com/blog/2016/1/20/the-wrong-abstraction
