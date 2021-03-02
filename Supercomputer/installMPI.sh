#!/bin/sh

#Download all necessary dependencies and mpi4py
sudo apt-get install python-dev
sudo apt-get install python-mpi4py

#Create directory for source/demo files for mpi4py
mkdir mpi4py
cd mpi4py

#Download tarball file and extract source/demo files for mpi4py
curl -k -O https://mpi4py.googlecode.com/files/mpi4py-1.3.1.tar.gz

#MPI will already be installed at this point. This is only for having the source code, documentation, and demos for your convenience
tar xvfz mpi4py-1.3.1.tar.gz 
