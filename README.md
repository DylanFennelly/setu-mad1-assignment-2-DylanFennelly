## This is a clone of the repository used for Mobile App Development 1 Assignment 2

# Dungeons & Dragons Character Manager

### A simple Android application to create and manage Dungeons & Dragons characters

---
## Functionality
The application allows for the creation, listing, updating, and deletion of characters.


### Repository branching model

---

The repository contains a number of branches, that flowed as such

main -> refactor -> main -> navigation -> main -> addCharacter -> main -> room -> main -> characterDetails -> main -> deleteCharacter -> main -> updateCharacter -> main -> uiUpdates -> main (-> characterImage)

- **refactor** - Refactoring the base starter project to better suit the intended use case
- **navigation** - Enabled multi-screen navigation using NavHost
- **addCharacter** - Added view/functionality to add a character
- **room** - Added Room DB to persist data
- **characterDetails** - Added more in-depth character details on HomeView + CharacterDetailsView for full character details
- **deleteCharacter** - Added functionality to delete a character
- **updateCharacter** - Added view/functionality to update a character
- **uiUpdates** - Made various UI improvements and changes
- **characterImage** - Functionality to add an image to a character. This branch is incomplete and not pulled into main
