# Grocery Store POS System

## Overview

This is a Java-based Point of Sale (POS) system for a grocery store. The program allows a cashier to add items to a customer's bill, calculate discounts, and generate a final bill receipt. It also provides the ability to save and retrieve pending bills.

## Features

- **Add Grocery Items**: Add grocery items with details such as item code, name, price, manufacturing date, expiry date, and discount.
- **Dictionary of Grocery Items**: Store a dictionary of grocery items for quick lookup.
- **Enter Items Purchased**: Allows the cashier to enter items purchased by the customer.
- **Calculate Total Price and Discount**: Calculates the total price and the total discount applied to the purchase.
- **Generate and Print Bill**: Generates and prints a formatted bill receipt.
- **Save and Retrieve Pending Bills**: Save and load pending bills for future use.
- **Handle Invalid Item Codes**: Handles invalid item codes with custom exceptions.

## Technologies Used

- **Java**
- **Java IO (File Handling)**
- **Java Collections Framework** (HashMap, List)
- **Java Time API** (LocalDate, LocalTime)

## How to Run

1. Compile the Java program using:
   ```bash
   javac Bill.java
2. Run the program using:
   ```bash
   java Bill
3. Enter the cashier's name, branch, and customer's name when prompted.
4. Enter item codes and quantities to add items to the bill.
    - Enter 'p' to save the bill as a pending bill.
    - Enter 'r' to retrieve a previously saved pending bill.
    - Enter 'e' to finalize and print the bill.
## Example Input
  ```bash
  Enter Cashier Name: Kaja
  Enter Branch: Jaffna
  Enter Customer Name: Sara
  Enter Item Code and No of Items Customer Buys: 10 2
  Enter Item Code and No of Items Customer Buys: 12 1
  Enter Item Code and No of Items Customer Buys: e

