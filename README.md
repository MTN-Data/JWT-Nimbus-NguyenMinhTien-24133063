# JWT với Nimbus JOSE + JWT

## Thông tin sinh viên

- Họ tên: Nguyễn Minh Tiến
- MSSV: 24133063
- Môn học: Lập trình Web
- Nội dung: Xây dựng chức năng xác thực JWT với Spring Boot 3, Spring Security 6 và Nimbus JOSE + JWT

## Mục tiêu bài thực hành

Project được xây dựng dựa trên ví dụ JWT trong bài giảng. Mục tiêu là thực hiện đầy đủ luồng đăng ký, đăng nhập, sinh JWT, xác thực Bearer Token và truy cập các API được bảo vệ.

Điểm chính của bài là thay thư viện `io.jsonwebtoken (JJWT)` trong ví dụ bằng thư viện **Nimbus JOSE + JWT**, nhưng vẫn giữ nguyên luồng xử lý của bài giảng.

## Những nội dung đã thực hiện

- Xây dựng Entity `User` và lưu dữ liệu người dùng vào SQL Server.
- Đăng ký tài khoản bằng API `/auth/signup`.
- Đăng nhập bằng email và password thông qua API `/auth/login`.
- Mã hóa password bằng `BCryptPasswordEncoder`.
- Xác thực người dùng bằng Spring Security.
- Sinh JWT sau khi đăng nhập thành công.
- Sử dụng **Nimbus JOSE + JWT** để tạo, đọc và xác minh JWT.
- JWT có thời gian hết hạn.
- Gửi JWT trong HTTP Header theo dạng:

```text
Authorization: Bearer <token>
```

- Xây dựng `JwtAuthenticationFilter` để kiểm tra token trong mỗi request.
- Lấy thông tin người dùng đang đăng nhập qua `/users/me`.
- Lấy danh sách người dùng qua `/users`.
- Chặn truy cập API khi không có JWT hoặc JWT không hợp lệ.
- Có giao diện login và profile để minh họa quá trình xác thực bằng AJAX.

## Luồng hoạt động

```text
Đăng ký tài khoản
        ↓
Lưu User vào SQL Server
        ↓
Đăng nhập bằng Email + Password
        ↓
Spring Security xác thực
        ↓
Nimbus JOSE + JWT tạo Token
        ↓
Server trả JWT cho Client
        ↓
Client gửi Bearer Token
        ↓
JwtAuthenticationFilter kiểm tra Token
        ↓
Cho phép truy cập API được bảo vệ
```

## Các API chính

| Method | Endpoint | Chức năng |
|---|---|---|
| POST | `/auth/signup` | Đăng ký tài khoản |
| POST | `/auth/login` | Đăng nhập và sinh JWT |
| GET | `/users/me` | Lấy thông tin user đang đăng nhập |
| GET | `/users` | Lấy danh sách user |

## Công nghệ sử dụng

- Java 17
- Spring Boot 3.3.5
- Spring Security 6
- Spring Data JPA
- SQL Server
- Nimbus JOSE + JWT 9.47
- Maven
- Thymeleaf
- HTML / JavaScript

## Điểm khác so với ví dụ trong bài giảng

Trong ví dụ của bài giảng, JWT được xây dựng bằng thư viện:

```text
io.jsonwebtoken - JJWT
```

Trong project này, phần tạo, đọc và xác minh JWT được thay bằng:

```text
com.nimbusds:nimbus-jose-jwt
```

Các thành phần Nimbus được sử dụng gồm:

- `SignedJWT`
- `JWTClaimsSet`
- `MACSigner`
- `MACVerifier`
- `JWSAlgorithm.HS256`

Luồng chức năng vẫn giữ theo ví dụ trong bài giảng:

```text
Signup → Login → Generate JWT → Bearer Token → Access Protected API
```

## Kết quả đạt được

Sau khi hoàn thành project:

- Người dùng có thể đăng ký tài khoản và dữ liệu được lưu vào SQL Server.
- Người dùng có thể đăng nhập bằng email và password.
- Server sinh JWT khi đăng nhập thành công.
- JWT được tạo bằng Nimbus JOSE + JWT.
- Token có thể được sử dụng để truy cập các API yêu cầu xác thực.
- Server có thể xác minh chữ ký và thời gian hết hạn của JWT.
- Request không có token hoặc token không hợp lệ sẽ không được phép truy cập tài nguyên được bảo vệ.
- Có thể kiểm tra thông tin user hiện tại và danh sách user sau khi xác thực.

## Chuẩn bị database

Tạo database SQL Server:

```sql
CREATE DATABASE jwt_nimbus CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Sau đó kiểm tra cấu hình trong file:

```text
src/main/resources/application.properties
```

Nếu username hoặc password SQL Server trên máy khác với cấu hình hiện tại thì sửa lại cho phù hợp.

## Chạy project

Project này là **Spring Boot project** nên có thể chạy trực tiếp bằng **Spring Tool Suite (STS)**, IntelliJ IDEA hoặc Eclipse có hỗ trợ Maven.

### Chạy bằng Spring Tool Suite (khuyến nghị)

1. Mở Spring Tool Suite.
2. Chọn `File → Import`.
3. Chọn `Maven → Existing Maven Projects`.
4. Chọn thư mục project.
5. Chờ Maven tải dependencies.
6. Mở file `JwtNimbusApplication.java`.
7. Chọn `Run As → Spring Boot App`.

Ứng dụng chạy tại:

```text
http://localhost:8005
```

## Test theo luồng bài giảng

### 1. Đăng ký tài khoản

```http
POST http://localhost:8005/auth/signup
```

Body:

```json
{
  "fullName": "Nguyen Minh Tien",
  "email": "tien24133063@gmail.com",
  "password": "123456"
}
```

### 2. Đăng nhập để sinh JWT

```http
POST http://localhost:8005/auth/login
```

Body:

```json
{
  "email": "tien24133063@gmail.com",
  "password": "123456"
}
```

Kết quả trả về dạng:

```json
{
  "token": "eyJ...",
  "expiresIn": 3600000
}
```

### 3. Gọi API được bảo vệ

```http
GET http://localhost:8005/users/me
```

Header:

```text
Authorization: Bearer <token>
```

Sau đó có thể test tiếp:

```http
GET http://localhost:8005/users
```

với cùng Bearer Token.

## Giao diện demo

Có thể mở:

```text
http://localhost:8005/login
```

Sau khi đăng nhập thành công, token được sử dụng để gọi API và hiển thị thông tin người dùng tại trang profile.


## Cấu hình cơ sở dữ liệu trên máy thực hành

Project đã được cấu hình theo SQL Server đang chạy trên máy:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:14330;databaseName=jwt_nimbus;encrypt=true;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=Aa@123456
```

Trước khi chạy project, tạo database một lần trong SQL Server:

```sql
CREATE DATABASE jwt_nimbus;
```

Sau đó chạy project bằng Spring Tool Suite. Hibernate với `ddl-auto=update` sẽ tự tạo/cập nhật bảng cần thiết trong database `jwt_nimbus`.
