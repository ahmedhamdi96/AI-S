# AI-S
*Artificial Intelligence-Search*

## Project Overview
*GUC, Winter 2018, CSEN901*

The purpose of this (Game of Thrones-themed) project was to implement search to help Jon Snow save Westeros. The field where the white walkers are frozen in place can be thought of as an m x n grid of cells where m and n are greater than or equal to 4. A grid cell is either free or contains one of the following: a white walker, Jon Snow, the dragonstone, or an obstacle. Jon Snow can move in the four directions as long as the cell in the direction of movement does not contain an obstacle or a living white walker. To obtain the dragonglass by which the white walkers can be killed, Jon has to go to the cell where the dragonstone lies to pick up a fixed number of pieces of dragonglass that he can carry. In order to kill a white walker, Jon has to be in an adjacent cell. An adjacent cell is a cell that lies one step to the north, south, east, or west. With a single move using the same piece of dragonglass, Jon can kill all adjacent white walkers. If Jon steps out of a cell where he used a piece of dragonglass to kill adjacent walkers, that piece of dragonglass becomes unusable. Once a white walker is killed, Jon can move through the cell where the walker was. If Jon runs out of dragonglass before all the white walkers are killed, he has to go back to the dragonstone to pick up more pieces of dragonglass. Using search, a plan that Jon can follow to kill all the white walkers was formulated. An optimal plan is one where Jon uses the least number of pieces of dragonglass to kill all the white walkers. The following search algorithms were implemented and each was used to help Jon:

* Breadth-first search.
* Depth-first search.
* Iterative deepening search.
* Uniform-cost search.
* Greedy search with two heuristics.
* A* search with two admissible heuristics.

## Project Partners
*in alphabetical order*

* [Ahmed Zaki](https://github.com/Zakyyy)
* [Ebraheem Mohamed](https://github.com/Ebraheem1)
