javapaint
=========

This project is intended to be a simple cross platform image editor.

Back in 2009, there was no simple cross platform image editor.  If you weren't on Windows, you had to use GIMP or Adobe Photoshop, even if all you wanted was circles and rectangles.  I had purchased a Mac and wanted a simple image editor like MS Paint that obeyed the K.I.S.S. principle.  As a sophomore computer science major, I decided to write my own.  

The majority of the work on this project was done in the Fall of 2009 before I knew about version control.  So the initial version here is what I ended up with.
Currently this is a functional image editor that supports PNG, JPG, and GIF formats as well as a custom format that preserves the undo/redo stack of the project.  There are 7 tools and a color selector that supports transparency.

This repository contains the Eclipse project in Java for the project. I didn't include the classpath since that will vary by system. You will have to configure that yourself: in Eclipse right click on the project and select Build Path->Configure Build Path... Then set it to use your default system Java library.
