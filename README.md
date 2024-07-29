# An Image Analysis Pipeline for Quantifying the Spatial Distribution of Fluorescently-Labeled Cell Markers in Stroma-Rich Tumors

![StromaCellMarkerQuant](images/fig1_workflow.png)

## Overview
This code repository provides the implementtion of a comprehensive image analysis pipeline for quantifying the spatial distributipm of fluorescently-labeled cell markers in relation to the stromal border of solid tumor environments. It is further described in Ruzette et al, 2024 (insert citation), and was successfully applied in Kozlova et al, 2024 (insert citation) to reveal the spatial distribution of NDRG1, a novel DNA repair protein, in pancreatic tumors. 


## Features
- Segmentation of nuclei in fluorescent images using pre-trained DL models
- Machine-learning based composite classification of cancer cells of interest
- Quantitative analysis of fluorescent markers across many images
- Spatial distribution using cell-stroma 2D signed distance
- Compatible with high-resolution whole-slide images 
- Visualization and statistical analysis of results in Python

## Datasets
The images used to validate this pipeline, and resulting cell measurements tables, are free to download at https://zenodo.org/records/13122087.

## Software requirements
- Python 3.7 or higher (install required packages with `pip install -r requirements.txt`)
- QuPath 0.5 or higher
- StarDist extension for QuPath

## Distribution
Our image analysis pipeline is distributed as a collection of QuPath scripts for image processing and Python notebooks for statistical analysis and visualization.

This repository is distributed under the MIT license. You are free to use, modify, and distribute the code as long as you provide proper attribution to the authors.