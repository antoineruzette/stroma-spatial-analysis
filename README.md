# An Image Analysis Pipeline for Quantifying the Spatial Distribution of Fluorescently-Labeled Cell Markers in Stroma-Rich Tumors

![StromaCellMarkerQuant](images/fig1_workflow.png)

## Overview
This code repository provides the implementtion of an image analysis pipeline for quantifying the spatial distributipm of fluorescently-labeled cell markers in relation to the stromal border of solid tumor environments. The pipeline is designed to process high-resolution whole-slide images of fluorescently-labeled tissue sections and consists of: (1) a deep learning-based nuclei segmentation step using StarDist, (2) a machine learning-based composite classification step and (3) a parameter sensitivity analysis and visualization step. The pipeline is implemented in QuPath (Groovy) and Python.


## Features
- Segmentation of nuclei in fluorescent images using pre-trained DL models
- Machine-learning based composite classification of cancer cells of interest
- Quantitative analysis of fluorescent markers across many images
- Spatial distribution using cell-stroma 2D signed distance
- Compatible with high-resolution whole-slide images 
- Visualization, sensitivity and statistical analysis of results in Python


## Software requirements
- Python 3.7 or higher (install required packages with `pip install -r requirements.txt`)
- QuPath 0.5 or higher
- StarDist extension for QuPath
- The pipeline was developed and tested on MacOS Ventura 10.5. 


## Datasets
[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.13122087.svg)](https://doi.org/10.5281/zenodo.13122087)

Resulting cell measurements tables, are free to download at https://doi.org/10.5281/zenodo.13122087. Download the tables and run the Jupyter notebooks to reproduce the results.


## Running the pipeline on your own data
To run the pipeline on your own data, follow the steps below:

1. Load your images into QuPath and create a project.
2. Run the scripts in the `qupath_scripts` folder in the following order:
    1. `stardist_cell_detection.groovy`
    2. `remove_impossible_nuclei.groovy`
    3. `cell_classification.groovy` if using threshold-based classification, otherwise follow ![instructions](https://qupath.readthedocs.io/en/stable/docs/tutorials/cell_classification.html#train-a-cell-classifier-based-on-annotations) from QuPath to train a machine learning classifier .
    4. `stroma_annotation.groovy` (make sure to adapt the tresholds to your images)
    5. `measurements_export.groovy` 
    
3. Run cell-by-cell the Jupyter notebooks in the `Python` folder to analyze the exported measurement tables.


## Install dependencies
To install the required Python packages in your environment, run the following commands in your terminal:

```bash
conda create -n stroma-spatial-analysis python=3.7
conda activate stroma-spatial-analysis
pip install -r requirements.txt
```

It should not take more than a few minutes to install all the required packages.


## Distribution
Our image analysis pipeline is distributed as a collection of Groovy scripts for image processing in QuPath and Python Jupyter notebooks for statistical analysis and visualization.

This repository is distributed under the MIT license. You are free to use, modify, and distribute the code as long as you provide proper attribution to the authors.