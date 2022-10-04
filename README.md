# Hangman Game
This project was developed for the multimedia course of the [Multimedia Techonology Laboratory](http://hscnl.ece.ntua.gr/index.php/teaching/undergraduate/multimedia-technology) at [ECE NTUA](https://www.ece.ntua.gr/en). It's an implementation of the classic hangman game, using JavaFX.

## Overview
For each new game, the user has to guess a randomly chosen word, while making less than six wrong attempts. The words are selected by the computer, using word files called dictionaries. A dictionary can be loaded if it already exists, or be created from online sources (book descriptions on [openlibrary.org](openlibrary.org)). Once a word is chosen, the user has to find all of its missing letters, gaining or losing points for each right or wrong guess, respectively. The amount of points gained varies, depending on the character's infrequency in the selected dictionary. A game is concluded when the user successfully guesses the word or makes six failed attempts.

The application's UI was implemented using JavaFX. 
