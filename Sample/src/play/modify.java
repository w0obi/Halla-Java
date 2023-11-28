//202121087 정서림 캐드 연동 부분

//DXF파일 -> XML로 변환
import net.sf.dxfj.DXFDocument
import net.sf.dxfj.DXFParser
import net.sf.dxfj.DXFParseException
import net.sf.dxfj.entities.*
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.*
import javax.xml.stream.XMLOutputFactory
import javax.xml.stream.XMLStreamException
import javax.xml.stream.XMLStreamWriter

// DXF 파일을 XML 파일로 변환하는 함수
fun convertDxfToXml(inputPath: String, outputPath: String) {
    try {
        // DXF 파일을 파싱하여 DXFDocument 객체를 생성
        val dxfDocument: DXFDocument = DXFParser.parse(FileInputStream(inputPath))

        // XML 출력을 위한 객체 생성
        val factory: XMLOutputFactory = XMLOutputFactory.newInstance()
        val writer: XMLStreamWriter = factory.createXMLStreamWriter(FileOutputStream(outputPath), "UTF-8")

        // XML 문서 시작
        writer.writeStartDocument("UTF-8", "1.0")
        writer.writeStartElement("CADData") // 루트 엘리먼트

        // 각 DXF 엔터티를 순회하면서 XML로 변환
        for (entity in dxfDocument.entities) {
            if (entity is Line) {
                writeEntity(writer, "Line", entity)
            } else if (entity is Circle) {
                writeEntity(writer, "Circle", entity)
            } else if (entity is Arc) {
                writeEntity(writer, "Arc", entity)
            }
            // 추가적인 엔터티에 대한 처리를 여기에 추가할 수 있습니다.
        }

        // XML 문서 끝
        writer.writeEndElement()
        writer.writeEndDocument()
        writer.close()

    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: DXFParseException) {
        e.printStackTrace()
    } catch (e: XMLStreamException) {
        e.printStackTrace()
    }
}

// 각 엔터티를 XML 엘리먼트로 변환하는 함수
fun writeEntity(writer: XMLStreamWriter, entityType: String, entity: Entity) {
    writer.writeStartElement(entityType) // 엔터티의 XML 엘리먼트 시작
    writer.writeAttribute("id", entity.handle.toString()) // 엔터티의 고유 식별자

    // 엔터티의 유형에 따라 필요한 속성을 XML 엘리먼트에 추가
    when (entityType) {
        "Line" -> {
            val line = entity as Line
            writer.writeAttribute("start_point", line.startPoint.toString())
            writer.writeAttribute("end_point", line.endPoint.toString())
        }
        "Circle" -> {
            val circle = entity as Circle
            writer.writeAttribute("center", circle.center.toString())
            writer.writeAttribute("radius", circle.radius.toString())
        }
        "Arc" -> {
            val arc = entity as Arc
            writer.writeAttribute("center", arc.center.toString())
            writer.writeAttribute("radius", arc.radius.toString())
            writer.writeAttribute("start_angle", arc.startAngle.toString())
            writer.writeAttribute("end_angle", arc.endAngle.toString())
        }
        // 추가적인 엔터티 타입에 대한 처리를 여기에 추가할 수 있습니다.
    }

    writer.writeEndElement() // 엔터티의 XML 엘리먼트 끝
}

fun main() {
    val inputPath = "path/to/input.dxf"
    val outputPath = "path/to/output.xml"
    convertDxfToXml(inputPath, outputPath)
}