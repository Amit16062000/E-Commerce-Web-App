
# Simple E-commerce API ✨

Welcome to the **Simple E-commerce API**, a robust RESTful API built with **Spring Boot**, **Spring Security**, and **MySQL**. This project powers an e-commerce platform with features like product listings, cart management, order creation, and role-based authentication (Customer and Admin) using **JWT (JSON Web Tokens)**. It also includes optional features like pagination and product search.

![E-commerce Store Header](https://via.placeholder.com/800x200?text=E-commerce+Store+Header)  
*<img width="1900" height="916" alt="Screenshot 2025-07-21 231801" src="https://github.com/user-attachments/assets/c1b0c91b-bcbf-4f8f-92d5-181542090faa" />
*

## Table of Contents 🎯
- [Features](#features-)
- [Technologies Used](#technologies-used-)
- [Setup Instructions](#setup-instructions-)
- [API Endpoints](#api-endpoints-)
  - [Authentication](#authentication-)
  - [Products](#products-)
  - [Cart](#cart-)
  - [Orders](#orders-)
- [Role-Based Access Control](#role-based-access-control-)
- [Additional Features](#additional-features-)
- [Database Schema](#database-schema-)
- [Screenshots](#screenshots-)
- [Running the Application](#running-the-application-)
- [Contributing](#contributing-)
- [License](#license-)

## Features 🌟
- **Product Listings**: View all products or search by ID, name, or category with pagination support.
- **Cart Management**: Add, update, remove, and clear items in the cart.
- **Order Creation**: Place orders from the cart with stock validation.
- **User Authentication**: Secure login and registration with JWT.
- **Role-Based Access**:
  - **Customer**: Browse products, manage cart, and place orders.
  - **Admin**: Full product management (CRUD operations).
- **Security**: Protected endpoints with JWT and Spring Security.
- **Optional Frontend**: Basic HTML interface for user interaction.

## Technologies Used 🛠️
- **Java 17+**
- **Spring Boot 3.x**
- **Spring Security** with JWT
- **Spring Data JPA** with Hibernate
- **MySQL** for database
- **Lombok** for concise code
- **Maven** for dependency management
- **JWT (io.jsonwebtoken)** for authentication

## Setup Instructions 🚀
1. **Prerequisites**:
   - Java 17 or higher
   - MySQL 8.0 or higher
   - Maven
   - IDE (e.g., IntelliJ IDEA, Eclipse)

2. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/ecommerce-api.git
   cd ecommerce-api
   ```

3. **Configure the Database**:
   - Create a MySQL database named `ecommerce_db`.
   - Update `application.properties` with your credentials:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db?createDatabaseIfNotExist=true
     spring.datasource.username=your-username
     spring.datasource.password=your-password
     ```

4. **Install Dependencies**:
   ```bash
   mvn clean install
   ```

5. **Run the Application**:
   ```bash
   mvn spring-boot:run
   ```
   Access it at `http://localhost:8080`.

6. **Test the API**:
   Use **Postman** or **cURL** with a `Bearer <token>` header for protected routes.

## API Endpoints 🌐

### Authentication 🔐
| Method | Endpoint            | Description                     | Required Role | Request Body                     |
|--------|---------------------|---------------------------------|---------------|----------------------------------|
| POST   | `/api/auth/register`| Register a new user            | None          | `User` (username, password, email, role) |
| POST   | `/api/auth/login`  | Login and get JWT token        | None          | `AuthRequest` (username, password) |

<img width="1728" height="897" alt="Screenshot 2025-07-21 232245" src="https://github.com/user-attachments/assets/23c90db9-68d5-4b54-8c1d-6dff7e3301d9" />


**Example Login**:
```bash
curl -X POST http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{"username": "customer1", "password": "password123"}'
```
**Response**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "username": "customer1",
  "role": "CUSTOMER"
}
```

### Products 🛒
| Method | Endpoint                     | Description                          | Required Role | Request Body/Params                 |
|--------|------------------------------|--------------------------------------|---------------|-------------------------------------|
| GET    | `/api/products`             | Get all products                    | None          | None                                |
| GET    | `/api/products/page`        | Get products with pagination        | None          | `Pageable` (page, size, sort)       |
| GET    | `/api/products/{id}`        | Get product by ID                   | None          | Path variable: `id`                 |
| GET    | `/api/products/search`      | Search by name or category          | None          | Query params: `name` or `category`  |
| POST   | `/api/products`             | Create product                      | Admin         | `ProductDto`                        |
| PUT    | `/api/products/{id}`        | Update product                      | Admin         | `ProductDto`, Path variable: `id`   |
| DELETE | `/api/products/{id}`        | Delete product                      | Admin         | Path variable: `id`                 |

**Example Search**:
```bash
curl -X GET http://localhost:8080/api/products/search?name=laptop
```

<img width="1736" height="931" alt="Screenshot 2025-07-21 232137" src="https://github.com/user-attachments/assets/a68cfbc4-a3ad-4e2a-aca2-3d26bf6a6342" />


### Cart 🛍️
| Method | Endpoint                  | Description                     | Required Role | Request Body/Params            |
|--------|---------------------------|---------------------------------|---------------|--------------------------------|
| GET    | `/api/cart`              | Get user's cart                | Customer      | None                           |
| POST   | `/api/cart/items`        | Add item to cart               | Customer      | `CartItemDto` (productId, quantity) |
| PUT    | `/api/cart/items/{itemId}`| Update item quantity           | Customer      | Path variable: `itemId`, `quantity` |
| DELETE | `/api/cart/items/{itemId}`| Remove item                    | Customer      | Path variable: `itemId`        |
| DELETE | `/api/cart`              | Clear cart                     | Customer      | None                           |

**Example Add Item**:
```bash
curl -X POST http://localhost:8080/api/cart/items \
-H "Authorization: Bearer <token>" \
-d '{"productId": 1, "quantity": 2}'
```

### Orders 📦
| Method | Endpoint           | Description                     | Required Role | Request Body/Params |
|--------|--------------------|---------------------------------|---------------|---------------------|
| GET    | `/api/orders`     | Get all user orders            | Customer      | None                |
| GET    | `/api/orders/{id}`| Get order by ID                | Customer      | Path variable: `id` |
| POST   | `/api/orders`     | Create order from cart         | Customer      | None                |

**Example Create Order**:
```bash
curl -X POST http://localhost:8080/api/orders \
-H "Authorization: Bearer <token>"
```

## Role-Based Access Control 🔒
- **Customer**: View products, manage cart, and place orders.
- **Admin**: All customer privileges + full product management.

Use a `Bearer <token>` header for authenticated requests.

## Additional Features 🎉
- **Pagination**: Via `/api/products/page` with `Pageable`.
- **Product Search**: Via `/api/products/search?name=<name>` or `/api/products/search?category=<category>`.

## Database Schema 📊
- **User**: (username, password, email, role) → 1 Cart, * Orders
- **Product**: (name, description, price, stockQuantity, category)
- **Cart**: 1 User → * CartItems, totalPrice
- **CartItem**: 1 Cart → 1 Product, quantity
- **Order**: 1 User → * OrderItems, orderDate, totalAmount, status
- **OrderItem**: 1 Order → 1 Product, quantity, price

**Schema Diagram**:
```
User 1 ----> 1 Cart
User 1 ----> * Orders
Cart 1 ----> * CartItems
Order 1 ----> * OrderItems
Product * <---- CartItem
Product * <---- OrderItem
```

## Screenshots 📸
Explore the user interface and API testing in action:

1. **Registration Page**  
   ![Register Page]()  
   *A simple registration form for new users with fields for username, password, and email.*
   <img width="1916" height="896" alt="Screenshot 2025-07-21 231912" src="https://github.com/user-attachments/assets/d30c4926-ad3d-45ff-ba74-95b23dedd705" />


3. **Products Page**  
   ![Products Page]([attachment://products_page.png](https://drive.google.com/file/d/1lPgsWkzX0BdJwWkpghC4uLn2dZkjyxpW/view?usp=drive_link))  
   *Displays available products (Laptop, Wireless Headphones, Coffee Maker, Running Shoes) with details and a search option.*

4. **Login Page**  
   ![Login Page](https://drive.google.com/file/d/1RV7jngaf3al-cIrBkZKIYQJ83agQhnFZ/view?usp=drive_link)  
   *Login form with username and password fields, showing an error for invalid credentials.*
   <img width="1907" height="917" alt="Screenshot 2025-07-21 232013" src="https://github.com/user-attachments/assets/af24d42d-0b00-4659-8144-e4906586aa0e" />


6. **API Testing with Postman**  
   - **Login Request**: ![Login Postman](attachment://login_postman.png)  
     *Testing the `/api/auth/login` endpoint to get a JWT token.*
   - **Add Product by Admin**: ![Add Product Postman]([attachment://add_product_postman.png](https://drive.google.com/file/d/1QKIrF8gpT-piNmjPLVtor7BLlhzZS0bX/view?usp=drive_link))  
     *Secured endpoint to add a product, requiring admin authentication.*
   - **Add to Cart by Customer**: ![Add to Cart Postman](attachment://add_to_cart_postman.png)  
     *Customer adding an item to the cart with a valid token.*
   - **Get Products**: ![Get Products Postman](attachment://get_products_postman.png)  
     *Fetching product details accessible to all users.*

## Running the Application 🖥️
- Ensure MySQL is running and configured.
- Run with:
  ```bash
  mvn spring-boot:run
  ```
- Test at `http://localhost:8080` using Postman or cURL.

## Contributing 🤝
1. Fork the repository.
2. Create a branch (`git checkout -b feature/your-feature`).
3. Commit changes (`git commit -am 'Add your feature'`).
4. Push and submit a Pull Request.

## License 📜
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

### Notes on Images and Usage:
- **Register Page**: Shows the registration form, aligning with the `/api/auth/register` endpoint. It demonstrates the initial user setup process.
- **Products Page**: Highlights the product listing feature, linking to `/api/products`, with search functionality for an enhanced user experience.
- **Login Page**: Illustrates the login interface, tied to `/api/auth/login`, with error handling for invalid credentials.
- **Postman Screenshots**: Provide a practical guide for API testing, covering key endpoints like login, product management, cart operations, and product retrieval, showcasing the JWT authentication flow.

This README is now visually appealing with images, structured sections, and emojis to draw attention, while providing clear instructions and context for your project. Replace the placeholder image URL with the actual hosted image links or upload them to your GitHub repository for direct access.
