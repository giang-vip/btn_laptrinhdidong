app/src/main/java/com/example/app_dich_quet_van_ban/
│
├── constants/                             # Chứa các giá trị không đổi (Base URL, API Keys, định danh Database)
│   └── AppConstants.kt
│
├── di/                                    # Dependency Injection (Hilt) - Nơi khởi tạo các đối tượng dùng chung
│   └── AppModule.kt                       # Cung cấp Database, Repository cho toàn bộ App
│
├── domain/                                # LỚP LÕI (Nơi chứa logic nghiệp vụ, không phụ thuộc vào thư viện ngoài)
│   ├── model/                             # Các kiểu dữ liệu dùng cho giao diện (LangItem, TranslationResult)
│   ├── repository/                        # Các bản thiết kế (Interface) định nghĩa các tính năng (Vocabulary, Learning, Translate)
│   └── usecase/                           # (Để trống hoặc bổ sung sau) Các hành động đơn lẻ như "Dịch văn bản", "Lật thẻ"
│
├── data/                                  # LỚP DỮ LIỆU (Triển khai thực tế các thiết kế từ Domain)
│   ├── local/                             # Quản lý SQLite qua Room Database
│   │   ├── AppDatabase.kt                 # Cấu hình chính của Database
│   │   ├── dao/                           # Các câu lệnh SQL (Insert, Update, Delete, Query) cho từng bảng
│   │   └── entity/                        # Định nghĩa cấu trúc các bảng (Card, Folder, User, ScannedDoc...)
│   ├── remote/                            # Nơi gọi API bên ngoài (Dịch thuật, AI)
│   ├── repository_impl/                   # Hiện thực hóa các Repository (Lấy dữ liệu từ Local hay Remote)
│   ├── mapper/                            # Chuyển đổi qua lại giữa Entity (DB) và Model (UI)
│   └── mock/                              # Dữ liệu giả (MockData) dùng để test giao diện nhanh
│
└── presentation/                          # LỚP GIAO DIỆN (UI & Logic Giao diện)
├── navigation/                        # Quản lý luồng chuyển màn hình
│   ├── Screen.kt                      # Định nghĩa tên các Route (Id màn hình)
│   └── NavGraph.kt                    # Bản đồ kết nối các màn hình với nhau
├── screens/                           # Chứa mã nguồn UI của từng màn hình (Compose)
│   ├── Screens_Scan/                  # Nhóm màn hình Quét văn bản (Camera, Kết quả)
│   ├── Screens_Translate/             # Nhóm màn hình Dịch thuật
│   └── Screens_Vocabulary/            # Nhóm màn hình Từ vựng (Thư viện, Flashcard, Thêm từ)
├── viewmodel/                         # Cầu nối dữ liệu giữa Data và Screen (Xử lý State và Event)
├── components/                        # Các thành phần giao diện dùng chung (BottomBar, CustomButton)
└── theme/                             # Cấu hình màu sắc, phông chữ toàn app

LearningViewModel.kt
VocabularyViewModel.kt
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