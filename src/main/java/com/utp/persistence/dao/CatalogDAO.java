package com.utp.persistence.dao;

import com.utp.config.ConnectionPostgresSQL;
import com.utp.model.dto.CampaniaDTO;
import com.utp.model.dto.CuponDTO;
import com.utp.model.dto.ProductCatalogDTO;
import com.utp.model.entity.CategoryEntity;
import com.utp.model.entity.ProductEntity;
import com.utp.model.entity.ProductTypeEntity;
import com.utp.util.enums.ProductStatus;
import com.utp.util.mappers.CatalogMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CatalogDAO {
    public List<ProductCatalogDTO> showProductsInTheCatalog() throws SQLException {
        List<ProductCatalogDTO> list = new ArrayList<>();

        String sql = "SELECT p.id_producto, p.nombre AS nombre_producto , p.descripcion  ,c.nombre AS nombre_categoria, " +
                "p.precio, p.imagen_url, i.cantidad_disponible, tp.requiere_datos_cliente " +
                "FROM producto p " +
                "INNER JOIN categoria c ON p.id_categoria = c.id_categoria " +
                "INNER JOIN tipo_producto tp ON p.id_tipo_producto = tp.id_tipo_producto " +
                "LEFT JOIN inventario i ON p.id_producto = i.id_producto " +
                "WHERE p.estado = 'publicado'";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(CatalogMapper.toProductCatalogDTO(rs));
            }
        }
        return list;
    }

    public Optional<ProductCatalogDTO> getProductDetail(int idProduct) throws SQLException {
        ProductCatalogDTO product = null;

        String sql = "SELECT p.id_producto, p.nombre AS nombre_producto, p.descripcion  , c.nombre AS nombre_categoria, " +
                "p.precio, p.imagen_url, i.cantidad_disponible, tp.requiere_datos_cliente " +
                "FROM producto p " +
                "INNER JOIN categoria c ON p.id_categoria = c.id_categoria " +
                "INNER JOIN tipo_producto tp ON p.id_tipo_producto = tp.id_tipo_producto " +
                "LEFT JOIN inventario i ON p.id_producto = i.id_producto " +
                "WHERE p.id_producto = ? AND p.estado = 'publicado'";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idProduct);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Reutilizamos el mapper que ya tienes definido para el catálogo
                    product = CatalogMapper.toProductCatalogDTO(rs);
                }
            }
        }
        return Optional.ofNullable(product);
    }
    public boolean createProduct(ProductEntity p) {
        String sql = "INSERT INTO producto (id_categoria, id_tipo_producto, nombre, descripcion, imagen_url, precio, estado, creado_por) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p.getIdCategory());
            ps.setInt(2, p.getIdProductType());
            ps.setString(3, p.getName());
            ps.setString(4, p.getDescription());
            ps.setString(5, p.getUrlImage());
            ps.setDouble(6, p.getPrice());
            ps.setString(7, p.getProductStatus().name());
            ps.setInt(8, p.getCreatedBy());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertCampaing(String name, double percentage, int idAdmin) {
        String sql = "INSERT INTO campania_descuento (nombre, porcentaje_descuento, fecha_inicio, fecha_fin, estado, creado_por) " +
                "VALUES (?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '30 days', 'activa', ?)";
        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setDouble(2, percentage);
            ps.setInt(3, idAdmin);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean assignCouponToCustomer(int idCampania, int idClient, int idEmployeeGenerator) {
        boolean exito = false;
        String couponCode = "DESC-" + java.util.UUID.randomUUID().toString().substring(0, 4).toUpperCase();

        String sqlCupon = "INSERT INTO cupon (id_campania, codigo, creado_por) VALUES (?, ?, ?) RETURNING id_cupon";
        String sqlAsignar = "INSERT INTO cupon_asignado (id_cupon, id_cliente, asignado_por) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psCupon = conn.prepareStatement(sqlCupon);
                 PreparedStatement psAsignar = conn.prepareStatement(sqlAsignar)) {

                psCupon.setInt(1, idCampania);
                psCupon.setString(2, couponCode);
                psCupon.setInt(3, idEmployeeGenerator);

                try (ResultSet rs = psCupon.executeQuery()) {
                    if (rs.next()) {
                        int idCuponGenerado = rs.getInt(1);
                        psAsignar.setInt(1, idCuponGenerado);
                        psAsignar.setInt(2, idClient);
                        psAsignar.setInt(3, idEmployeeGenerator);
                        psAsignar.executeUpdate();
                    }
                }
                conn.commit();
                exito = true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return exito;
    }

    public List<CampaniaDTO> listarCampanias() {
        List<com.utp.model.dto.CampaniaDTO> lista = new ArrayList<>();
        String sql = "SELECT id_campania, nombre, porcentaje_descuento, estado FROM campania_descuento ORDER BY id_campania DESC";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CampaniaDTO c = new CampaniaDTO();
                c.setIdCampania(rs.getInt("id_campania"));
                c.setNombre(rs.getString("nombre"));
                c.setPorcentajeDescuento(rs.getDouble("porcentaje_descuento"));
                c.setEstado(rs.getString("estado"));
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean editarCampania(int idCampania, String nombre, double porcentaje, String estado) {
        String sql = "UPDATE campania_descuento SET nombre = ?, porcentaje_descuento = ?, estado = ? WHERE id_campania = ?";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre.trim());
            ps.setDouble(2, porcentaje);
            ps.setString(3, estado.trim().toLowerCase());
            ps.setInt(4, idCampania);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarCampania(int idCampania) {
        String sql = "UPDATE campania_descuento SET estado = 'inactiva' WHERE id_campania = ?";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCampania);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<CategoryEntity> getCategories() throws SQLException {
        List<CategoryEntity> categories = new ArrayList<>();

        String sql = "SELECT id_categoria, nombre, descripcion, estado, fecha_creacion " +
                "FROM categoria " +
                "WHERE estado = TRUE " +
                "ORDER BY nombre";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CategoryEntity category = new CategoryEntity();

                category.setIdCategory(rs.getInt("id_categoria"));
                category.setNameCategory(rs.getString("nombre"));
                category.setDescriptionCategory(rs.getString("descripcion"));
                category.setCategoryStatus(rs.getBoolean("estado"));
                category.setCreationDate(rs.getTimestamp("fecha_creacion"));

                categories.add(category);
            }
        }

        return categories;
    }


    public List<ProductTypeEntity> getProductTypes() throws SQLException {
        List<ProductTypeEntity> productTypes = new ArrayList<>();

        String sql = "SELECT id_tipo_producto, codigo, nombre, " +
                "requiere_datos_cliente, requiere_asignacion_inventario FROM tipo_producto " +
                "ORDER BY nombre";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ProductTypeEntity type = new ProductTypeEntity();

                type.setIdProductType(rs.getInt("id_tipo_producto"));
                type.setCodeProductType(rs.getString("codigo"));
                type.setNameProductType(rs.getString("nombre"));
                type.setRequiresCustomerData(
                        rs.getBoolean("requiere_datos_cliente"));
                type.setInventoryAllocationRequired(
                        rs.getBoolean("requiere_asignacion_inventario"));

                productTypes.add(type);
            }
        }

        return productTypes;
    }

    public List<ProductEntity> getAllProductsForAdmin() throws SQLException {
        List<ProductEntity> products = new ArrayList<>();

        String sql = "SELECT id_producto, id_categoria, id_tipo_producto, " +
                "nombre, descripcion, imagen_url, precio, estado, " +
                "permite_compra_multiple, cantidad_minima, cantidad_maxima, " +
                "creado_por, fecha_creacion, fecha_actualizacion " +
                "FROM producto " +
                "ORDER BY id_producto DESC";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ProductEntity product = new ProductEntity();

                product.setIdProduct(rs.getInt("id_producto"));
                product.setIdCategory(rs.getInt("id_categoria"));
                product.setIdProductType(rs.getInt("id_tipo_producto"));
                product.setName(rs.getString("nombre"));
                product.setDescription(rs.getString("descripcion"));
                product.setUrlImage(rs.getString("imagen_url"));
                product.setPrice(rs.getDouble("precio"));
                product.setProductStatus(
                        ProductStatus.valueOf(rs.getString("estado")));
                product.setAllowsMiltiplePurchases(
                        rs.getBoolean("permite_compra_multiple"));
                product.setMinimumQuantity(rs.getInt("cantidad_minima"));

                int maximumQuantity = rs.getInt("cantidad_maxima");
                product.setMacximumQuantity(
                        rs.wasNull() ? null : maximumQuantity);

                int createdBy = rs.getInt("creado_por");
                product.setCreatedBy(rs.wasNull() ? null : createdBy);

                product.setCreatedDate(rs.getTimestamp("fecha_creacion"));
                product.setUpdatedDate(rs.getTimestamp("fecha_actualizacion"));

                products.add(product);
            }
        }

        return products;
    }

    public boolean updateProduct(ProductEntity product) {
        String sql = "UPDATE producto SET " +
                "id_categoria = ?, " +
                "id_tipo_producto = ?, " +
                "nombre = ?, " +
                "descripcion = ?, " +
                "imagen_url = ?, " +
                "precio = ?, " +
                "estado = ?, " +
                "permite_compra_multiple = ?, " +
                "cantidad_minima = ?, " +
                "cantidad_maxima = ?, " +
                "fecha_actualizacion = CURRENT_TIMESTAMP " +
                "WHERE id_producto = ?";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, product.getIdCategory());
            ps.setInt(2, product.getIdProductType());
            ps.setString(3, product.getName());
            ps.setString(4, product.getDescription());
            ps.setString(5, product.getUrlImage());
            ps.setDouble(6, product.getPrice());
            ps.setString(7, product.getProductStatus().name());
            ps.setBoolean(8, product.isAllowsMiltiplePurchases());
            ps.setInt(9, product.getMinimumQuantity());

            if (product.getMacximumQuantity() != null) {
                ps.setInt(10, product.getMacximumQuantity());
            } else {
                ps.setNull(10, java.sql.Types.INTEGER);
            }

            ps.setInt(11, product.getIdProduct());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean pauseProduct(int idProduct) {
        String sql = "UPDATE producto SET " +
                "estado = 'pausado', " +
                "fecha_actualizacion = CURRENT_TIMESTAMP " +
                "WHERE id_producto = ?";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idProduct);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<CuponDTO> listarCupones() {
        List<CuponDTO> lista = new ArrayList<>();
        String sql = "SELECT c.id_cupon, c.codigo, c.estado, c.limite_usos, " +
                "cd.nombre AS nombre_campania, cd.porcentaje_descuento " +
                "FROM cupon c " +
                "INNER JOIN campania_descuento cd ON c.id_campania = cd.id_campania " +
                "ORDER BY c.id_cupon DESC";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CuponDTO c = new CuponDTO();
                c.setIdCupon(rs.getInt("id_cupon"));
                c.setCodigo(rs.getString("codigo"));
                c.setEstado(rs.getString("estado"));

                int limite = rs.getInt("limite_usos");
                c.setLimiteUsos(rs.wasNull() ? null : limite);

                c.setNombreCampania(rs.getString("nombre_campania"));
                c.setPorcentajeDescuento(rs.getDouble("porcentaje_descuento"));
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean generarCupon(int idCampania, String codigo, Integer limiteUsos, int idAdmin) {
        String sql = "INSERT INTO cupon (id_campania, codigo, limite_usos, creado_por, estado) VALUES (?, ?, ?, ?, 'activo')";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCampania);
            ps.setString(2, codigo.trim().toUpperCase());

            if (limiteUsos != null && limiteUsos > 0) {
                ps.setInt(3, limiteUsos);
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }

            ps.setInt(4, idAdmin);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editarCupon(int idCupon, String codigo, Integer limiteUsos, String estado) {
        String sql = "UPDATE cupon SET codigo = ?, limite_usos = ?, estado = ? WHERE id_cupon = ?";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigo.trim().toUpperCase());

            if (limiteUsos != null && limiteUsos > 0) {
                ps.setInt(2, limiteUsos);
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }

            ps.setString(3, estado.trim().toLowerCase());
            ps.setInt(4, idCupon);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarCupon(int idCupon) {
        String sql = "UPDATE cupon SET estado = 'inactivo' WHERE id_cupon = ?";

        try (Connection conn = ConnectionPostgresSQL.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCupon);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
