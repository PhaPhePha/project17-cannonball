package edu.project17.cannonball

import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.control.*
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.stage.Stage

//class dieu kien giao dien
class CannonballApp : Application() {
    private val speedField = TextField()
    private val angleField = TextField()
    private val heightField = TextField()
    private val targetField = TextField()
    private val status = Label("Nhập các giá trị rồi mô phỏng chuyển động.")
    private val chart = LineChart<Number, Number>(NumberAxis(), NumberAxis())
    private val resultLabel = Label("-")

    //tao gdien chinh
    override fun start(stage: Stage) {
        val title = Label("Project 17  -  Predict the Flight of a Cannonball")
        title.style = "-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #17324d;"
        val subtitle = Label("Chuyển động ném xiên + phương pháp chia đôi")
        subtitle.style = "-fx-font-size: 13px; -fx-text-fill: #55708a;"
        val header = VBox(4.0, title, subtitle)

        //tao field nhap thong so
        val controls = VBox(10.0)
        controls.padding = Insets(18.0)
        controls.style = "-fx-background-color: #f4f8fb; -fx-background-radius: 12;"
        controls.children.add(Label("THÔNG SỐ MÔ PHỎNG").also { it.style = "-fx-font-weight: bold; -fx-text-fill: #247ba0;" })
        addInput(controls, "Vận tốc ban đầu (m/s)", speedField)
        addInput(controls, "Góc phóng (°)", angleField)
        addInput(controls, "Độ cao phóng (m)", heightField)
        addInput(controls, "Khoảng cách mục tiêu (m)", targetField)
        val simulate = Button("Mô phỏng")
        val bisect = Button("Tìm góc (phương pháp chia đôi)")
        val reset = Button("Đặt lại")
        simulate.maxWidth = Double.MAX_VALUE; bisect.maxWidth = Double.MAX_VALUE; reset.maxWidth = Double.MAX_VALUE
        simulate.style = "-fx-background-color: #247ba0; -fx-text-fill: white; -fx-font-weight: bold;"
        bisect.style = "-fx-background-color: #f28f3b; -fx-text-fill: white; -fx-font-weight: bold;"
        controls.children.addAll(simulate, bisect, reset)
        controls.children.add(Label("KẾT QUẢ").also { it.style = "-fx-font-weight: bold; -fx-text-fill: #247ba0;" })
        resultLabel.wrapTextProperty().set(true)
        controls.children.add(resultLabel)

        chart.title = "Quỹ đạo (y theo x)"
        chart.xAxis.label = "Khoảng cách theo phương ngang (m)"
        chart.yAxis.label = "Độ cao (m)"
        //cau hinh bieu do quy dao
        chart.createSymbols = false
        chart.setLegendVisible(false)
        chart.animated = false
        chart.style = "-fx-background-color: white;"
        val chartBox = VBox(chart)
        VBox.setVgrow(chart, Priority.ALWAYS)
        status.style = "-fx-text-fill: #55708a; -fx-font-size: 13px;"

        //gan evetn cho cac button
        simulate.setOnAction { handleSimulate() }
        bisect.setOnAction { handleBisection() }
        reset.setOnAction {
            speedField.clear(); angleField.clear(); heightField.clear(); targetField.clear()
            chart.data.clear(); resultLabel.text = "-"; status.text = "Nhập các giá trị rồi mô phỏng chuyển động."
        }

        //set bo cuc cho cua so
        val root = BorderPane()
        root.padding = Insets(24.0)
        root.top = header
        BorderPane.setMargin(header, Insets(0.0, 0.0, 18.0, 0.0))
        root.left = controls
        BorderPane.setMargin(controls, Insets(0.0, 20.0, 0.0, 0.0))
        root.center = chartBox
        root.bottom = status
        BorderPane.setMargin(status, Insets(16.0, 0.0, 0.0, 0.0))
        stage.scene = Scene(root, 1050.0, 680.0, Color.WHITE)
        stage.title = "Project 17 - Predict the Flight of a Cannonball"
        stage.show()
    }

    //ham them cac field nhap vao box
    private fun addInput(box: VBox, label: String, field: TextField) {
        box.children.add(Label(label))
        field.promptText = label
        box.children.add(field)
    }

    //doc va ktra thong so input
    private fun values(requireAngle: Boolean = true): Triple<Double, Double, Double> {
        val speed = InputParser.parse(speedField.text, "Vận tốc ban đầu phải là một số")
        val angle = if (requireAngle) {
            InputParser.parse(angleField.text, "Góc phóng phải là một số")
        } else {
            0.0
        }
        val height = InputParser.parse(heightField.text, "Độ cao phóng phải là một số")
        require(speed > 0) { "Vận tốc ban đầu phải lớn hơn 0." }
        if (requireAngle) {
            require(angle in 0.0..90.0) { "Góc phóng phải nằm trong khoảng từ 0° đến 90°." }
        }
        require(height >= 0) { "Độ cao phóng không được âm." }
        return Triple(speed, angle, height)
    }

    //ve duong bay va danh dau muc tieu neu co
    private fun draw(result: FlightResult, target: Double? = null) {
        chart.data.clear()
        val series = XYChart.Series<Number, Number>()
        series.name = "quỹ đạo"
        result.points.forEach { series.data.add(XYChart.Data(it.first, it.second)) }
        chart.data.add(series)
        if (target != null) {
            val marker = XYChart.Series<Number, Number>()
            marker.name = "mục tiêu"
            marker.data.add(XYChart.Data(target, 0.0))
            chart.data.add(marker)
        }
    }

    //xky chuc nang mo phong
    private fun handleSimulate() {
        try {
            val (speed, angle, height) = values()
            val r = Physics.simulate(speed, angle, height)
            draw(r)
            resultLabel.text = "Khoảng cách rơi: %.2f m\nThời gian bay: %.2f s".format(r.landingDistance, r.flightTime)
            status.text = "Mô phỏng hoàn tất."
        } catch (e: Exception) { showError(e.message ?: "Dữ liệu không hợp lệ") }
    }

    //xly chuc nang tim goc
    private fun handleBisection() {
        try {
            val (speed, _, height) = values(requireAngle = false)
            val target = InputParser.parse(targetField.text, "Khoảng cách mục tiêu phải là một số")
            require(target > 0) { "Khoảng cách mục tiêu phải lớn hơn 0." }
            val angle = Physics.solveAngle(speed, height, target)
            if (angle == null) {
                showError("Không tồn tại khoảng bao nghiệm hoặc nghiệm phù hợp với mục tiêu này, vận tốc và độ cao hiện tại.")
                return
            }
            angleField.text = "%.4f".format(angle)
            val r = Physics.simulate(speed, angle, height)
            draw(r, target)
            resultLabel.text = "Góc tìm được: %.4f°\nKhoảng cách rơi: %.4f m".format(angle, r.landingDistance)
            status.text = "Phương pháp chia đôi đã hội tụ trong khoảng bao nghiệm."
        } catch (e: Exception) { showError(e.message ?: "Dữ liệu không hợp lệ") }
    }

    //hien thi error
    private fun showError(message: String) {
        resultLabel.text = "$message"
        status.text = "Vui lòng sửa dữ liệu rồi thử lại."
    }
}
