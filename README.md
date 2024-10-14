# <div style="color: green">ĐỀ BÀI:</div> Xây dựng ứng dụng Ngân hàng dựa trên kiến trúc microservice

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

## <div style="color: green">GITHUB DỰ ÁN CỦA TEAM 2</div>

Link github banking team 2: [MicroService-Banking-Team2](https://github.com/hiepdeptrai0908/MicroService-Banking-Team2)

### Bước 1: Clone dự án về máy
- **Ở tại folder muốn tải dự án về > mở Terminal > gõ dòng lệnh:**
  `git clone git@github.com:hiepdeptrai0908/MicroService-Banking-Team2.git`

### Bước 2: Check out vào nhánh của bạn
- **Đi vào thư mục vừa mới clone về:**
  `cd MicroService-Banking-Team2`

- **Tại thư mục dự án check out vào nhánh của bạn:**
  `Mình sẽ cập nhật nhánh của từng service sau khi đã setup xong nhé ...`