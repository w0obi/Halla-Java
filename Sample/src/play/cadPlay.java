package play;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import net.sf.dxfj.DXFDocument;
import net.sf.dxfj.DXFParser;
import net.sf.dxfj.DXFParseException;
import net.sf.dxfj.entities.Entity;
import net.sf.dxfj.entities.Line;
import net.sf.dxfj.entities.Circle;
import net.sf.dxfj.entities.Arc;

public class cadPlay {

    public static void convertDxfToXml(String inputPath, String outputPath) throws FileNotFoundException, IOException, DXFParseException {
        // DXF 파일을 파싱하여 DXFDocument 객체를 생성
        DXFDocument dxfDocument = DXFParser.parse(new FileInputStream(inputPath));

        // XML 출력을 위한 객체 생성
        FileOutputStream outputStream = new FileOutputStream(outputPath);
        XmlWriter xmlWriter = new XmlWriter(outputStream);

        // XML 문서 시작
        xmlWriter.writeStartDocument();
        xmlWriter.writeStartElement("CADData");

        // 각 DXF 엔터티를 순회하면서 XML로 변환
        for (Entity entity : dxfDocument.getEntities()) {
            // 엔터티의 유형에 따라 필요한 속성을 XML 엘리먼트에 추가
            switch (entity.getType()) {
                case Entity.TYPE_LINE:
                    Line line = (Line) entity;
                    xmlWriter.writeStartElement("Line");
                    xmlWriter.writeAttribute("id", line.getHandle());
                    xmlWriter.writeAttribute("start_point", line.getStartPoint().toString());
                    xmlWriter.writeAttribute("end_point", line.getEndPoint().toString());
                    xmlWriter.writeEndElement();
                    break;
                case Entity.TYPE_CIRCLE:
                    Circle circle = (Circle) entity;
                    xmlWriter.writeStartElement("Circle");
                    xmlWriter.writeAttribute("id", circle.getHandle());
                    xmlWriter.writeAttribute("center", circle.getCenter().toString());
                    xmlWriter.writeAttribute("radius", circle.getRadius());
                    xmlWriter.writeEndElement();
                    break;
                case Entity.TYPE_ARC:
                    Arc arc = (Arc) entity;
                    xmlWriter.writeStartElement("Arc");
                    xmlWriter.writeAttribute("id", arc.getHandle());
                    xmlWriter.writeAttribute("center", arc.getCenter().toString());
                    xmlWriter.writeAttribute("radius", arc.getRadius());
                    xmlWriter.writeAttribute("start_angle", arc.getStartAngle());
                    xmlWriter.writeAttribute("end_angle", arc.getEndAngle());
                    xmlWriter.writeEndElement();
                    break;
            }
        }

        // XML 문서 끝
        xmlWriter.writeEndElement();
        xmlWriter.writeEndDocument();
        xmlWriter.close();
    }

    public static void main(String[] args) throws FileNotFoundException, IOException, DXFParseException {
        // 입력 및 출력 파일 경로 지정
        String inputPath = "path/to/input.dxf";
        String outputPath = "path/to/output.xml";

        // DXF 파일을 XML 파일로 변환
        convertDxfToXml(inputPath, outputPath);
    }
}
