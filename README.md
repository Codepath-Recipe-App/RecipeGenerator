# RecipeGenerator

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)

## Overview

### Description
This app uses the spoonacular API to generate recipes based on ingredients in your pantry.

### App Evaluation
- **Category:** Food
- **Mobile:** This app would be primarily developed for mobile but would perhaps be just as viable on a computer. Functionality wouldn't be limited to mobile devices.
- **Story:** Analyzes users food choices, and provides them with useful recipes.
- **Market:** Any individual could choose to use this app.
- **Habit:** This app could be used as often or unoften as the user wanted.
- **Scope:** First we would start with generating recipes with the provided ingredients and could potentially add the ability to connect with other users and see what recipes they are enjoying.

## Product Spec
### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* [x] User can sign up to create a new account using Parse authentication
* [x] User can log in to access previous settings
* [x] User can input ingredients
* [x] Array of ingredients are saved on server
* [x] Recipes are generated using spoonacular API
* [x] Profile pages for each user

**Optional Nice-to-have Stories**

* Favorite recipes
* Page of most popular recipes
* Generates meal plans
* Saving recipes of interest

### Video Walkthroughs

<img src='Walkthrough.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

<img src='Week2Walkthrough.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

<img src='LoginScreenWalkthrough.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

### 2. Screen Archetypes

* Login 
* Register - User signs up or logs into their account
   * Upon Download/Reopening of the application, the user is prompted to log in to gain access to their profile information. 
* Typing Screen - User types ingredients that they would like to include
   * Upon typing ingredients, recipes are generated.
* Recipes Screen
   * Allows user to select and view the recipe they desire.
* Profile Screen 
   * Allows user to upload a photo.
* Settings Screen
   * Lets people change language, and app notification settings.

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* 
* 
* 

Optional:
* 
* 

**Flow Navigation** (Screen to Screen)
* Forced Log-in -> Account creation if no log in is available
* Profile -> Text field to be modified. 
* Ingredient selection -> Recipe generation

## Digital Wireframes / Interactive Prototype
[Wireframes from figma.com](https://www.figma.com/file/mysRGKzkuw1cKg6sgv4joo/Mobile-UI-kit-Community?node-id=194%3A1561)

## Schema 

### Models
#### Post

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user post (default field) |
   | author        | Pointer to User| image author |
   | image         | File     | image of the recipe |
   | caption       | String   | image caption by author |
   | createdAt     | DateTime | date when post is created (default field) |
   | ingredients   | String   | ingredients that the recipes should contain |
   
### Networking
#### List of network requests by screen
   - Home Feed Screen
      - (Read/GET) Query all recipes where user is author
      - (Create/POST) Create a new saved recipe
      - (Delete) Delete existing recipe
      - (Create/POST) Create a new comment on a recipe
      - (Delete) Delete existing comment
   - Profile Screen
      - (Read/GET) Query logged in user object
      - (Update/PUT) Update user profile image

- [Create basic snippets for each Parse network request]

#### [OPTIONAL:] Existing API Endpoints
##### Spoonacular API
- Base URL - [https://api.spoonacular.com/recipes](https://api.spoonacular.com/recipes)

   HTTP Verb | Endpoint | Description
   ----------|----------|------------
    `GET`    | /findByIngredients | get certain number of recipes
    `GET`    | /{id}/information | return information about a recipe such as diet and allergen info
    `GET`    | /{id}/summary   | return short description of recipe
    `GET`    | /extract | return recipe data such as instructions
