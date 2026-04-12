#!/bin/sh

set -e

# Prefer the Python generator on Windows/MSYS because xxd is often missing there.
python make_xxd.py
