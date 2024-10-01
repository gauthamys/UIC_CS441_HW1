#!/bin/bash
# This is a bootstrap action script to install OpenBLAS on AWS EMR

# Update package lists
sudo yum update -y

# Install the required development tools
sudo yum groupinstall -y "Development Tools"

# Install OpenBLAS
sudo yum install -y openblas-devel

# Create symlinks for OpenBLAS libraries
sudo ln -sf /usr/lib64/libopenblas.so /usr/lib64/libblas.so
sudo ln -sf /usr/lib64/libopenblas.so /usr/lib64/liblapack.so

# Verify installation
echo "Verifying OpenBLAS installation..."
ldconfig -p | grep openblas
