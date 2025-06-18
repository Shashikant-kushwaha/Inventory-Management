package inventorymanagement.dao;

import inventorymanagement.dbutil.DBConnection;
import inventorymanagement.pojo.ProductPojo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {
    public static boolean addProduct(ProductPojo product) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO products (product_id, product_name, quantity) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, product.getProductId());
            ps.setString(2, product.getProductName());
            ps.setInt(3, product.getQuantity());

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ProductPojo> getAllProducts() {
        List<ProductPojo> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM products")) {

            while (rs.next()) {
                list.add(new ProductPojo(
                        rs.getString("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("quantity")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateProduct(ProductPojo product) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE products SET product_name=?, quantity=? WHERE product_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, product.getProductName());
            ps.setInt(2, product.getQuantity());
            ps.setString(3, product.getProductId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProduct(String productId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM products WHERE product_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, productId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
