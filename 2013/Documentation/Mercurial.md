Introduction to Mercurial
=========================
Additional help on Mercurial can be found on the 
[Mercurial wiki](http://mercurial.selenic.com/wiki/).

What is Mercurial?
------------------
Mercurial is a **distributed version control system.** Basically, this is a fancy
way of saying that we can keep track of who made what changes to which code.

Changes from Subversion
-------------------------
*Note:* If you weren't on the team last year, don't bother reading this section.

Last year, we had a central repository where all of the changes were stored. If
we wanted to save the changes we had made, we had to connect to the Internet in
order to commit them. Committing and updating were tasks which could be fraught
with merge conflicts. TODO: continue

All history is kept in all copies of the repository. This means we won't have
any occurrences like RIT 2012, where we were frantically clustered around a laptop
trying to figure out how to get the history from the main repository.

TODO commits

TODO branches

Advantages
----------
*Note:* It's safe to skip this section.

With Mercurial, we don't need Internet access to stay in sync. This is particularly
useful at competitions. Last year, we wound up passing the code back and forth
on a flash drive, which was difficult to keep in sync. Because Mercurial is a 
*distributed* version control system, we don't need access to a central server to
use it. This means that we can export changesets and pass them around while
maintaining our history. Mercurial also offers `hg serve`, which makes a repository
accessible over the network so that other computers can push to it and pull from
it. At competitions, we could attach a laptop running `hg serve` to the wired
network, and use that to pass changes between computers. At the end of the day,
someone can simply pull the changes from the laptop, go home, and push the changes
to our Assembla repository. Thus, we can use Mercurial as normal and all of our
history is maintained, even during frantic bug-bashing at competitions.

Mercurial also offers easy branching and merging. This means that we can keep
separate branches for code that's actually been tested and code that *ought* to
work, rather than mixing them together.

TODO easy commits

Using Mercurial with NetBeans
=============================
*Note:* Wherever the instructions say to right-click on the project and chose
something from the Mercurial submenu, you can also select the project and then chose
something from the Team > Mercurial menu in the menubar. <!--TODO: why not just standardize to Team > Mercurial ? -->

Initial Setup
-------------
1. Open NetBeans and go to Team > Mercurial > Clone other...
2. Enter <https://hg.assembla.com/igknighters3173> in the Repository URL field, then
enter your Assembla username and password and click Next.
3. Click Set Default Values, then click Next again.
4. Change the clone name if desired, then click Finish.
5. TODO what about .hgignore dialog?
6. In the dialog that comes up, click 'Open Project'.
Tada!

Committing
----------
TODO explain
*Changes from Subversion:* When you commit, the changes are **not** pushed to
the repository; this step must be performed separately.
TODO don't lump unrelated changes together

Pushing
-------
Once you've made commits, you need to make them available to everyone else. The
best way to do this is to push them to our Assembla server. Just right-click on
the project and choose Mercurial > Share > Push to default.
You may be asked to enter your Assembla username and password.

*If you get an error about 'No default push path':* Right-click on the project
and choose Mercurial > Properties. Click on `default-push` and enter
<https://hg.assembla.com/igknighters3173> into the text box at the bottom of the
dialog, then click OK.
<!-- NOTE: this shouldn't happen if you clicked `Set Default Values' in step 2 of initial setup -->

Pulling
-------
To get the changes other people have made, you must pull from the repository.
To do so, right-click on the project and choose Mercurial > Share > Pull from default.
(You may need to enter your Assembla username and password.)

TODO merging

History
-------
It's possible to go back in the history very easily. Click on the project and
choose Team > Mercurial > Update... . Find the relevant revision (the filter
field can search in the changeset field or by date) and select it, then click
the Update button. If you commit changes, you will have created a new branch,
which can be worked on seperately, then merged back into the main codebase.

TODO: Merging changes

Mercurial and the IgKnighers
============================
Stuff specific to us

TODO
----
* branch layout (master, test, etc.)
* Explain our .hgignore
* Graphical merge program?
* .md files are auto-HTMLed by a hook

Links which ought to be elsewhere in this document
----------------
http://mercurial.selenic.com/wiki/ChangeSetComments


