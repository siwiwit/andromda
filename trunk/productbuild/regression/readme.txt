Prerequisite:
* rename the "build.properties.sample" file in this directory to "build.properties"
* then edit it to reflect your particular environment.
* Make sure you have the junit.jar version 3.8.1 or higher within your $ANT_HOME/lib directory. 
* The following must present on the same level as this directory:
-andromda
-samples
-cartridges

In order for this regression test to be useful, it must be
run AT LEAST twice. The first run of the default target will
create the initial "drafts" directory of this module (these are 
the files that will be compared against on any following runs). 
During any run after, the generated directory will be
created and compared against the "drafts" from the first run.  

Steps to see impact of modification to AndroMDA core source code or cartridge source code:
1) Run regression build file using default target
2) Make change to either cartridge or AndroMDA core source
3) Perform step 1 again
4) Watch for any failures at the end of the run, you'll know your modification(s) 
   changed the generated source if you see any.

Enjoy!

Chad Brandon
