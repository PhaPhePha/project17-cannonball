# Dự án 17 — Dự đoán quỹ đạo viên đạn

Ứng dụng máy tính viết bằng Kotlin và JavaFX. Ứng dụng mô phỏng chuyển động ném xiên, vẽ quỹ đạo bay và sử dụng phương pháp chia đôi để tìm góc phóng đạt khoảng cách mục tiêu.

## Yêu cầu

- JDK 17 trở lên
- Maven 3.9 trở lên

## Chạy ứng dụng

```bash
mvn clean javafx:run
```

Trên Windows PowerShell, chạy lệnh trên từ thư mục dự án. Dự án đã kèm Maven cục bộ trong thư mục `.tools`, nên có thể chạy dễ hơn bằng:

```powershell
.\run.ps1
```

Lệnh mặc định thực hiện `clean`, `compile` rồi mới chạy JavaFX. Nếu chỉ muốn
biên dịch và chạy kiểm tra, dùng:

Để chỉ biên dịch và kiểm tra:

```powershell
.\run.ps1 test
```

Nếu PowerShell chặn script, chạy một lần:

```powershell
Set-ExecutionPolicy -Scope CurrentUser RemoteSigned
```

## Mô hình toán học

Ứng dụng sử dụng `x(t) = v cos(θ)t` và `y(t) = h + v sin(θ)t − ½gt²`, với `g = 9.81 m/s²`. Thời gian bay là nghiệm dương của phương trình `y(t) = 0`. Từ đó, ứng dụng tính khoảng cách rơi và vẽ các điểm `(x(t), y(t))`.

Để tìm mục tiêu, đặt `f(θ) = khoảng cách rơi(θ) − mục tiêu`. Phương pháp chia đôi liên tục chia đôi một khoảng mà hai đầu có dấu trái ngược nhau. Ứng dụng sẽ tìm khoảng cách lớn nhất có thể đạt được, loại các mục tiêu không khả thi, sau đó tìm khoảng bao nghiệm phù hợp và dừng khi khoảng góc hoặc sai số hàm đủ nhỏ.

## Chức năng

- **Mô phỏng** kiểm tra vận tốc, góc và độ cao, sau đó vẽ quỹ đạo.
- **Tìm góc (phương pháp chia đôi)** kiểm tra mục tiêu, tìm góc, vẽ quỹ đạo và đánh dấu mục tiêu.
- **Đặt lại** khôi phục các giá trị mẫu.
- Dữ liệu không hợp lệ và mục tiêu không thể đạt được sẽ được thông báo trong khu vực kết quả.

Ví dụ mặc định: vận tốc 30 m/s, góc 45°, độ cao 2 m và mục tiêu 70 m.

## Kiểm thử

Các kiểm thử đơn vị nằm trong `src/test/kotlin` và tập trung vào công thức
vật lý, điểm đầu/cuối quỹ đạo, nghiệm Bisection và trường hợp mục tiêu gần
góc 90°. Chạy bằng:

```powershell
.\run.ps1 test
```
