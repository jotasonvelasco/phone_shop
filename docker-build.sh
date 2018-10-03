#!/bin/bash
sbt inventoryManagement/clean docker:publishLocal
sbt orderProcessor/clean docker:publishLocal
