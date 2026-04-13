# Banking Microservices - Team 2

## 🚀 Hướng dẫn chạy dự án sau khi clone

### Yêu cầu

- Java 17
- MySQL 8.x
- Node.js 18+ (nếu dùng banking-ui)

### Bước 1: Cấu hình Database

1. Cài MySQL, tạo database:

```sql
CREATE DATABASE IF NOT EXISTS account_service;
CREATE DATABASE IF NOT EXISTS user_service;
CREATE DATABASE IF NOT EXISTS transaction_service;
CREATE DATABASE IF NOT EXISTS fund_transfer_service;
CREATE DATABASE IF NOT EXISTS sequence_generator;
```

> Hoặc bỏ qua bước này vì app sẽ tự tạo DB nhờ `?createDatabaseIfNotExist=true`.

2. Copy file `.env`:

```bash
cp .env.example .env
```

3. Sửa `.env` cho đúng thông tin MySQL local:

```
DB_HOST=localhost
DB_PORT=3306
DB_USERNAME=root
DB_PASSWORD=your_password_here
JWT_SECRET=your_jwt_secret_here
```

### Bước 2: Cấu hình IDE (IntelliJ)

Mỗi service cần được set environment variable `DB_PASSWORD` trong Run Configuration:

**Run → Edit Configurations → Environment variables →** thêm:

```
DB_PASSWORD=your_password_here
```

> Các biến `DB_HOST`, `DB_PORT`, `DB_USERNAME` đã có default value (`localhost`, `3306`, `root`) nên không bắt buộc phải set.

### Bước 3: Khởi động các service theo thứ tự

| Thứ tự | Service               | Port |
| ------ | --------------------- | ---- |
| 1      | Eureka Server         | 8761 |
| 2      | User Service          | 8081 |
| 3      | Sequence Generator    | 8085 |
| 4      | Account Service       | 8082 |
| 5      | Transaction Service   | 8084 |
| 6      | Fund Transfer Service | 8083 |
| 7      | API Gateway           | 8080 |

### Bước 4: Test API

**Cách 1 - Postman:** Import file `Banking.postman_collection.json`

- Login sẽ tự động lưu token
- Account number và reference ID tự động fill

**Cách 2 - Banking UI:**

```bash
cd banking-ui
npm install
npm run dev
```

Mở http://localhost:3000

### Luồng test cơ bản

```
1. Register (tạo user)
2. Login (lấy token)
3. Create Account (tạo tài khoản ngân hàng)
4. Deposit (nạp tiền, cần >= 1000 để activate)
5. Update Account Status → ACTIVE
6. Withdrawal (rút tiền) / Fund Transfer (chuyển tiền)
```

### Chạy bằng Docker Compose

```bash
docker-compose up --build
```

Docker Compose tự đọc file `.env` và inject vào tất cả service.

---

## Mô tả dự án: Xây dựng ứng dụng Ngân hàng dựa trên kiến trúc Microservice

### Kiến trúc có các service chính sau:

#### 1. User Service (Auth Service):

- Cung cấp các chức năng quản lý người dùng: đăng ký, đăng nhập (JWT), cập nhật và xem thông tin người dùng.
- Người dùng cần xác thực JWT trước khi truy cập API của Account Service, Fund Transfer Service, Transactions Service.
- Tạo lớp Authentication Filter để validate token tại API Gateway.

#### 2. Account Service:

- Quản lý các API liên quan đến tài khoản: chỉnh sửa chi tiết, xem tất cả tài khoản liên kết với user, truy cập lịch sử giao dịch, đóng tài khoản.

#### 3. Fund Transfer Service:

- Cung cấp chức năng chuyển tiền giữa các tài khoản, truy cập bản ghi chuyển tiền chi tiết.

#### 4. Transactions Service:

- Xem giao dịch theo tài khoản hoặc ID tham chiếu, thực hiện nạp tiền (DEPOSIT) hoặc rút tiền (WITHDRAWAL).

#### 5. Sequence Generator:

- Service phụ của Account Service, tự động tạo số tài khoản khi tạo Account mới.

### Phân công

| Thành viên | Service                     | Nhánh                   | Port       |
| ---------- | --------------------------- | ----------------------- | ---------- |
| 1          | User Service                | `user-service`          | 8081       |
| 2          | Account Service             | `account-service`       | 8082       |
| 3          | Fund Transfer Service       | `fund-transfer-service` | 8083       |
| 4          | Transactions Service        | `transaction-service`   | 8084       |
| 5          | Eureka Server + API Gateway | `main`                  | 8761, 8080 |
| Phụ        | Sequence Generator          | `sequence-generator`    | 8085       |

### Git workflow

```bash
# Clone
git clone git@github.com:hiepdeptrai0908/MicroService-Banking-Team2.git
cd MicroService-Banking-Team2

# Cập nhật code mới nhất
git checkout main
git pull

# Tạo nhánh mới để code
git checkout -b dev/ten_chuc_nang
```

> **Lưu ý:** Không sửa trực tiếp trên nhánh `main`.
