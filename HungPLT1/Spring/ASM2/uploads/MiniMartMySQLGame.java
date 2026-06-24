/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ltmavenproject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author LENOVO
 */
public class MiniMartMySQLGame extends Application {

    private ArrayList<ProductA> products = new ArrayList<>();
    private ProductA sanPhamCanChon;

    private Label lblYeuCau;
    private Label lblDiem;
    private Label lblThoiGian;
    private Label lblThongBao;

    private int diem = 0;
    private int thoiGian = 30;

    private Random random = new Random();
    private Timeline timeline;

    @Override
    public void start(Stage stage) {

        docSanPhamTuMySQL();

        lblYeuCau = new Label();
        lblYeuCau.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        lblDiem = new Label("Điểm: 0");
        lblDiem.setStyle("-fx-font-size: 18px;");

        lblThoiGian = new Label("Thời gian: 30");
        lblThoiGian.setStyle("-fx-font-size: 18px;");

        lblThongBao = new Label("");
        lblThongBao.setStyle("-fx-font-size: 18px; -fx-text-fill: red;");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);

        int col = 0;
        int row = 0;

        for (ProductA p : products) {
            Button btn = new Button(p.getTen() + "\n" + p.getGia() + "đ");
            btn.setPrefSize(140, 80);
            btn.setStyle("-fx-font-size: 15px; -fx-background-color: #f1c40f;");

            btn.setOnAction(e -> kiemTraSanPham(p));

            grid.add(btn, col, row);

            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
        }

        Button btnChoiLai = new Button("Chơi lại");
        btnChoiLai.setOnAction(e -> choiLai());

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #ecf0f1; -fx-padding: 20;");
        root.getChildren().addAll(
                lblYeuCau,
                lblDiem,
                lblThoiGian,
                lblThongBao,
                grid,
                btnChoiLai
        );

        chonSanPhamMoi();
        batDauDemNguoc();

        Scene scene = new Scene(root, 600, 550);

        stage.setTitle("Game Siêu Thị Mini Mart - MySQL");
        stage.setScene(scene);
        stage.show();
    }

    private void docSanPhamTuMySQL() {
        String sql = "SELECT id, ten, gia FROM products";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                ProductA p = new ProductA(
                        rs.getInt("id"),
                        rs.getString("ten"),
                        rs.getInt("gia")
                );

                products.add(p);
            }

        } catch (Exception e) {
            System.out.println("Lỗi đọc sản phẩm: " + e.getMessage());
        }
    }

    private void kiemTraSanPham(ProductA p) {

        if (thoiGian <= 0) {
            return;
        }

        if (p.getId() == sanPhamCanChon.getId()) {
            diem += 10;
            lblThongBao.setText("Đúng rồi! +10 điểm");
        } else {
            diem -= 5;
            lblThongBao.setText("Sai rồi! -5 điểm");
        }

        lblDiem.setText("Điểm: " + diem);
        chonSanPhamMoi();
    }

    private void chonSanPhamMoi() {
        int index = random.nextInt(products.size());
        sanPhamCanChon = products.get(index);
        lblYeuCau.setText("Hãy chọn: " + sanPhamCanChon.getTen());
    }

    private void batDauDemNguoc() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            thoiGian--;
            lblThoiGian.setText("Thời gian: " + thoiGian);

            if (thoiGian <= 0) {
                timeline.stop();
                lblThongBao.setText("Hết giờ! Điểm của bạn: " + diem);
                luuDiemVaoMySQL();
            }
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void luuDiemVaoMySQL() {
        String sql = "INSERT INTO scores(player_name, score) VALUES (?, ?)";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, "Sinh viên");
            ps.setInt(2, diem);

            ps.executeUpdate();

            System.out.println("Đã lưu điểm vào MySQL");

        } catch (Exception e) {
            System.out.println("Lỗi lưu điểm: " + e.getMessage());
        }
    }

    private void choiLai() {
        diem = 0;
        thoiGian = 30;

        lblDiem.setText("Điểm: 0");
        lblThoiGian.setText("Thời gian: 30");
        lblThongBao.setText("");

        chonSanPhamMoi();

        if (timeline != null) {
            timeline.stop();
        }

        batDauDemNguoc();
    }

    public static void main(String[] args) {
        launch();
    }
}