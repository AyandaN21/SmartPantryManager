# Smart Pantry Manager

Smart Pantry Manager is an Android application developed in Java that helps users manage the ingredients they have at home and suggests recipes based on the ingredients available in their pantry.

The application was developed as part of the Mobile App Development 700 project.

## Features

The Smart Pantry Manager includes the following features:

- Add new pantry ingredients.
- View all saved pantry ingredients.
- Edit existing ingredients.
- Delete ingredients.
- Store ingredient name, quantity, unit, and optional expiry date.
- Save pantry information using SQLite.
- Store 20 pre-loaded recipes in the database.
- Suggest recipes based on available pantry ingredients.
- Use strict recipe matching based on ingredient name, quantity, and unit.
- Handle simple ingredient name differences such as Egg and Eggs.
- Handle compatible unit conversions such as litres and millilitres.
- Display recipe images.
- View recipe ingredients, required quantities, and preparation instructions.
- Display a message when no recipes can be made.
- Save user measurement preferences in Settings.
- Navigate between the main areas of the application using the application menu.

## Technologies Used

- Java
- Android Studio
- XML
- SQLite
- RecyclerView
- SharedPreferences
- Git
- GitHub

## Database

The application uses SQLite for local data storage.

SQLite was selected because it works well with Android applications and allows information to be stored directly on the device without requiring an internet connection.

The database stores pantry ingredients and recipe information.

The main database structures include:

### Pantry Ingredients

Each pantry ingredient stores:

- Ingredient ID
- Ingredient name
- Quantity
- Unit
- Expiry date

### Recipes

Each recipe stores:

- Recipe ID
- Recipe name
- Preparation instructions
- Recipe image reference

### Recipe Ingredients

Recipe requirements are stored separately and linked to recipes.

Each recipe ingredient stores:

- Recipe ingredient ID
- Recipe ID
- Ingredient name
- Required quantity
- Unit

This structure allows the application to compare the user's pantry quantities with the quantities required by each recipe.

## Recipe Matching

The application uses strict recipe matching.

A recipe is only suggested when every required ingredient is available in the pantry in a sufficient quantity.

For example, if a recipe requires:

- 2 Eggs
- 100 ml Milk

but the pantry contains only 1 Egg, the recipe will not be suggested.

The application also handles simple differences such as:

- Egg and Eggs
- Tomato and Tomatoes
- Potato and Potatoes
- Berry and Berries
- piece and pieces
- gram, grams, and g
- kilogram, kilograms, and kg
- millilitre, millilitres, and ml
- litre, litres, and l

Compatible metric quantities can also be converted. For example, 1 litre of milk can satisfy a recipe requiring 100 ml of milk.

## Pre-loaded Recipes

The application contains 20 recipes across four categories.

### Breakfast

1. Banana Pancakes
2. Scrambled Eggs
3. French Toast
4. Cheese Omelette
5. Overnight Oats

### Lunch

1. Avocado & Egg on Toast
2. Chicken Wrap
3. Tuna Mayo Sandwich
4. Chicken Salad
5. Egg Fried Rice

### Dinner

1. Tomato & Cheese Pasta
2. Chicken & Rice
3. Spaghetti & Mince
4. Chicken Pasta
5. Pap & Mince

### Snacks

1. Cheese Toast
2. Berry Smoothie
3. Cinnamon Toast
4. Potato Wedges
5. Tomato & Cheese Toast

## Main Screens

The application contains the following main screens:

- Home
- My Pantry
- Add Ingredient
- Edit Ingredient
- Suggested Recipes
- Recipe Details
- Settings

## Input Validation

The application validates user input before saving pantry information.

Examples include:

- Ingredient name cannot be empty.
- Quantity cannot be empty.
- Quantity must contain a valid number.
- Quantity must be greater than zero.
- Unit cannot be empty.
- Expiry date is optional.

## Data Persistence

Pantry information is stored in SQLite and remains available after the application is closed and reopened.

The Settings preference is stored using SharedPreferences so that the selected measurement preference is also remembered.

## How to Run the Application

1. Install Android Studio.
2. Clone or download this repository.
3. Open the SmartPantryManager project in Android Studio.
4. Allow Gradle to finish syncing.
5. Connect an Android device with USB debugging enabled or start an Android emulator.
6. Select the device in Android Studio.
7. Click Run.
8. The Smart Pantry Manager application will open on the selected Android device.

## Testing

The application was tested on a physical Android device.

Testing included:

- Creating pantry ingredients.
- Viewing pantry ingredients.
- Editing pantry ingredients.
- Deleting pantry ingredients.
- Input validation.
- SQLite persistence.
- Recipe quantity matching.
- Ingredient plural-name matching.
- Unit conversion.
- Rejection of incompatible units.
- Recipe Details.
- Recipe images.
- Settings persistence.
- Application navigation.

## Project Status

The main application functionality has been implemented and tested successfully.

## Author

Developed for Mobile App Development 700.