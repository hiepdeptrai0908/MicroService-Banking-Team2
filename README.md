# <font color="green">ĐỀ BÀI:</font> Xây dựng ứng dụng Ngân hàng dựa trên kiến trúc microservice

## Kiến trúc có các service chính sau:

### 1. User Service (Auth Service):

-   Dịch vụ microservice người dùng cung cấp các chức năng quản lý người dùng.
    -   Điều này bao gồm đăng ký người dùng, đăng nhập (JWT), cập nhật thông tin người dùng, xem thông tin người dùng và truy cập tất cả các tài khoản liên quan đến người dùng.
    -   **Chú ý:** người dùng muốn truy cập các api của Các Serivce Account Service, Fund Transfer Service, Transactions Service thì cần phải xác thực thông quan cơ chế JWT trước.
    -   Tạo lớp Authentication Filter để validate token & API Gateway trước khi truy cập các api của các Service Account Service, Fund Transfer Service, Transactions Service.

### 2. Account Service:

-   Dịch vụ microservice tài khoản quản lý các API liên quan đến tài khoản.
    -   Nó cho phép người dùng chỉnh sửa chi tiết tài khoản, xem tất cả các tài khoản liên kết với hồ sơ người dùng, truy cập lịch sử giao dịch cho từng tài khoản và hỗ trợ quy trình đồng tài khoản.

### 3. Fund Transfer Service:

-   Dịch vụ microservice chuyển tiền cung cấp các chức năng liên quan đến chuyển tiền.
    -   Người dùng có thể khởi động các giao dịch chuyển tiền giữa các tài khoản khác nhau, truy cập các bản ghi chuyển tiền chi tiết và xem thông tin cụ thể của bất kỳ giao dịch chuyển tiền nào.

### 4. Transactions Service:

-   Dịch vụ giao dịch cung cấp một loạt các dịch vụ liên quan đến giao dịch.
    -   Người dùng có thể xem giao dịch dựa trên các tài khoản cụ thể hoặc ID tham chiếu giao dịch, cũng như thực hiện nạp tiền hoặc rút tiền từ tài khoản của họ.

## <font color="green">GITHUB DỰ ÁN CỦA TEAM 2</font>

Link github banking team 2: [MicroService-Banking-Team2](https://github.com/hiepdeptrai0908/MicroService-Banking-Team2)

### Bước 1: Clone dự án về máy
- **Ở tại folder muốn tải dự án về > mở Terminal > gõ dòng lệnh:**<br>
  `git clone git@github.com:hiepdeptrai0908/MicroService-Banking-Team2.git`

### Bước 2: Check out vào nhánh của bạn
1. **Đi vào thư mục vừa mới clone về:**
    - VD: `cd MicroService-Banking-Team2`

2. **Tại thư mục dự án check out vào nhánh của bạn:**
    - Để tránh bị xung đột code thì hãy pull code từ nhánh "main" về để cập nhật code mới nhất của dự án:  
      `git checkout main`  
      `git pull`

    - Sau khi đã pull code về thì checkout sang nhánh khác hoặc tạo nhánh mới để tiến hành code:  
      *VD checkout sang nhánh đã có:*  
      `git checkout dev/tên_nhánh`  
      *VD tạo nhánh mới:*  
      `git checkout -b dev/tên_nhánh_mới`

> **Lưu ý:** Mỗi khi bạn chỉnh sửa gì thì hãy checkout sang nhánh khác và đặt tên nhánh đúng với chức năng bạn sẽ làm, ví dụ: `git checkout -b dev/JWT_service`. Tuyệt đối không sửa trực tiếp vào nhánh chính là "main".

## NHIỆM VỤ:

### **Thành viên 1 (User Service)**: 
- Chịu trách nhiệm phát triển các API liên quan đến đăng ký, đăng nhập người dùng, và xác thực JWT.
- Code tại nhánh: `git checkout user-service`
- Cổng `server.port=8081`

### **Thành viên 2 (Account Service)**: 
- Xây dựng các API quản lý tài khoản, bao gồm xem và chỉnh sửa thông tin tài khoản.
- Code tại nhánh: `git checkout account-service`
- Cổng `server.port=8082`

### **Thành viên 3 (Fund Transfer Service)**: 
- Phát triển các API cho chức năng chuyển tiền.
- Code tại nhánh: `git checkout fund-transfer-service`
- Cổng `server.port=8083`

### **Thành viên 4 (Transactions Service)**: 
- Xây dựng các API liên quan đến quản lý giao dịch, bao gồm xem lịch sử giao dịch, nạp/rút tiền.
<<<<<<< HEAD
- Code tại nhánh: `git checkout transaction-service`
- Cổng `server.port=8084`
=======
- Code tại nhánh: `git checkout transaction-service`
- Cổng `server.port=8084`

### **Thành viên 5 (Cấu hình và triển khai cơ sở hạ tầng)**: 
- Cấu hình và triển khai Eureka Server và API Gateway.
- Review code. Đồng thời tích hợp các Service vào Eureka và bảo mật API Gateway với JWT.
- Cổng Eureka Server `server.port=8761`
- Cổng API Gateway `server.port=8080`

### **Service phụ của Account Service (Sequence Generator)**:
- Service phụ có nhiệm vụ: Tự động tạo ra **số tài khoản** khi tạo Account mới.
- **Account Service** sẽ gọi api chéo sang **Sequence Generator** Service để tạo account
- Code tại nhánh: `git checkout sequence-generator`
- Cổng API Gateway `server.port=8085`
