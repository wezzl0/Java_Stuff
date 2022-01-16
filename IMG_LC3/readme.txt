Hello!

This .jar file takes a given image that is 128x124 (height = 124, width = 128) and generates an .obj file that you can load into the simulator. ( The penn state lc3 simulator )

The generated .obj file contains a simple program to load and store color codes into the simulator's "image" field, starting at address x3000. The program will loop within itself, incrementing through its array, until halting at x300D.

The program contains an array starting after x300D, containing the color codes and then the locations for each non-black pixel. Depending on the size of the array, it can end at x300F for only one pixel, or xAC0D for a full color image. (Program requires at least one pixel to be non-black)

Two completed .obj files and a compatable image are given as an example.

RUNNING:

All given images must fit size requirements

If ran from command line, you are able to provide a path to an image as an argument. If no argument is given, or if it is double-clicked, then the program will try to use any file named "IMG.png" in the same directory.

OUTPUT:

Upon completion, an .obj file with the name "drawImage_N.obj" will be created in the same directory, where N is a random number between 0 and 999. This .obj file can be loaded and ran in the simulator.