-- ============================================
-- HUTECH CAFE - SQL SCRIPT ĐỂ THÊM DỮ LIỆU MẪU
-- Database: SQL Server
-- Tác giả: Trần Anh Tiến - HUTECH
-- ============================================

-- 1. THÊM DANH MỤC (CATEGORIES)
-- Xóa dữ liệu cũ nếu có
DELETE FROM order_details;
DELETE FROM orders;
DELETE FROM products;
DELETE FROM categories;

-- Reset Identity
DBCC CHECKIDENT ('categories', RESEED, 0);
DBCC CHECKIDENT ('products', RESEED, 0);

-- Thêm danh mục
INSERT INTO categories (name) VALUES 
(N'Cà Phê'),
(N'Trà & Thức Uống'),
(N'Bánh Ngọt'),
(N'Đồ Ăn Nhẹ'),
(N'Món Chính');

-- 2. THÊM SẢN PHẨM (PRODUCTS)
INSERT INTO products (name, price, description, category_id) VALUES 
-- Cà phê (category_id = 1)
(N'Espresso', 35000, N'Cà phê Espresso đậm đà, được pha từ hạt cà phê Arabica nguyên chất', 1),
(N'Cappuccino', 45000, N'Cà phê Cappuccino với lớp bọt sữa mịn màng', 1),
(N'Latte Macchiato', 50000, N'Latte với vị ngọt nhẹ, hương vị sữa hòa quyện', 1),

-- Trà & Thức Uống (category_id = 2)
(N'Trà Xanh Matcha', 40000, N'Trà xanh Matcha Nhật Bản cao cấp, giàu chất chống oxy hóa', 2),
(N'Trà Đào Cam Sả', 38000, N'Thức uống giải nhiệt mùa hè với đào, cam tươi và sả thơm', 2),

-- Bánh Ngọt (category_id = 3)
(N'Croissant Bơ', 25000, N'Bánh sừng bò Pháp giòn tan, thơm mùi bơ', 3),
(N'Tiramisu', 55000, N'Bánh Tiramisu Ý truyền thống với mascarpone và cà phê', 3),

-- Đồ Ăn Nhẹ (category_id = 4)
(N'Sandwich Gà', 42000, N'Bánh mì sandwich nhân gà nướng với rau củ tươi', 4),

-- Món Chính (category_id = 5)
(N'Bò Bít Tết', 120000, N'Bít tết bò Úc nhập khẩu, ướp gia vị đặc biệt', 5),
(N'Mì Ý Carbonara', 85000, N'Mì Ý sốt kem Carbonara với bacon giòn', 5);

-- 3. KIỂM TRA DỮ LIỆU
SELECT 
    p.id,
    p.name AS [Tên Sản Phẩm],
    FORMAT(p.price, 'N0') + 'đ' AS [Giá],
    p.description AS [Mô Tả],
    c.name AS [Danh Mục]
FROM products p
LEFT JOIN categories c ON p.category_id = c.id
ORDER BY c.id, p.id;

-- 4. THỐNG KÊ
SELECT 
    c.name AS [Danh Mục],
    COUNT(p.id) AS [Số Sản Phẩm],
    MIN(p.price) AS [Giá Thấp Nhất],
    MAX(p.price) AS [Giá Cao Nhất],
    AVG(p.price) AS [Giá Trung Bình]
FROM categories c
LEFT JOIN products p ON c.id = p.category_id
GROUP BY c.name
ORDER BY c.name;

-- ============================================
-- KẾT THÚC SCRIPT
-- ============================================
