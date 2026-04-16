app/src/main/java/com/yourname/translateapp/
│
├── di/                                     # Dependency Injection (Hilt Modules)
│   ├── DatabaseModule.kt                   # Cung cấp Room Database
│   ├── NetworkModule.kt                    # Cung cấp Retrofit (nếu dùng API AI)
│   └── RepositoryModule.kt                 # Kết nối Interface Domain và Impl ở Data
│
├── domain/                                 # LỚP LÕI (Logic nghiệp vụ thuần)
│   ├── model/                              # Các Data Class (Translation, Word, User)
│   ├── repository/                         # Các Interface (Bản thiết kế hệ thống)
│   └── usecase/                            # Các logic xử lý đơn lẻ (TranslateUseCase, ScanOcrUseCase)
│
|
|___constants                               # Các logic sử lí không đổi 

├── data/                                   # LỚP DỮ LIỆU (Triển khai thực tế)
│   ├── local/                              # SQLite / Room Database
│   │   ├── AppDatabase.kt
│   │   ├── dao/                            # Data Access Objects (SQL Queries)
│   │   └── entity/                         # Table Definitions
│   ├── remote/                             # Gọi API (Gemini, Google Cloud)
│   ├── repository_impl/                    # Hiện thực hóa các Interface từ Domain
│   └── mapper/                             # Chuyển đổi dữ liệu (DTO to Domain Model)
│
└── presentation/                           # LỚP GIAO DIỆN (MVVM)
├── navigation/                         # <--- ĐÂY LÀ ROUTER CỦA BẠN
│   ├── Screen.kt                       # Định nghĩa các Route (home, scan, history)
│   └── NavGraph.kt                     # Quản lý luồng chuyển màn hình (Giống React Router)
├── screens/                            # Các màn hình lớn (Home, Scan, Result, History)
├── components/                         # Các UI nhỏ (CustomButton, LanguagePicker)
├── viewmodel/                          # Quản lý State cho từng màn hình
└── theme/                              # Cấu hình màu sắc, kiểu chữ (Material Design 3)

bạn giúp mình tiết kế màu sắc đồng nhất giữa các màn hình sao cho phù hợp 
với 1 app học tqapj dịch quét văn bản, học từ vựng flash card, luyện tập hỏi bài 
hoặc luyện nói với al nếu có thể hãy giúp mình thiết kế sau khi sản phẩm gần 
hoàn thiện cho phép người
dùng tùy  chinh màu sắc giao diện với ạ
STRICT RULES:
DO NOT change any logic
DO NOT modify structure
DO NOT rename variables, functions, or components
DO NOT break layout
ONLY replace colors (hex, rgb, tailwind classes, css variables)